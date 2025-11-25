import os
import xml.etree.ElementTree as ET
from pathlib import Path
import re

# SVG namespace
SVG_NS = "{http://www.w3.org/2000/svg}"

special_icons = ["logo", "CheeseWheel"]


def parse_transform(transform_str: str) -> str:
    """Parse SVG transform attribute and return JavaFX transform string"""
    if not transform_str:
        return ""

    transforms: list[str] = []

    # Parse translate
    for match in re.finditer(r"translate\(([^)]+)\)", transform_str):
        values = match.group(1).replace(",", " ").split()
        x = values[0]
        y = values[1] if len(values) > 1 else "0"
        transforms.append(f'<Translate x="{x}" y="{y}" />')

    # Parse rotate
    for match in re.finditer(r"rotate\(([^)]+)\)", transform_str):
        values = match.group(1).replace(",", " ").split()
        angle = values[0]
        pivot_x = values[1] if len(values) > 1 else "0"
        pivot_y = values[2] if len(values) > 2 else "0"
        transforms.append(
            f'<Rotate angle="{angle}" pivotX="{pivot_x}" pivotY="{pivot_y}" />'
        )

    # Parse scale
    for match in re.finditer(r"scale\(([^)]+)\)", transform_str):
        values = match.group(1).replace(",", " ").split()
        x = values[0]
        y = values[1] if len(values) > 1 else x
        transforms.append(f'<Scale x="{x}" y="{y}" />')

    if transforms:
        return f' transforms="[{", ".join(transforms)}]"'
    return ""


def parse_style(style_str: str) -> dict[str, str]:
    """Parse inline CSS style and return dictionary"""
    if not style_str:
        return {}
    styles: dict[str, str] = {}
    for declaration in style_str.split(";"):
        if ":" in declaration:
            prop, value = declaration.split(":", 1)
            styles[prop.strip()] = value.strip()
    return styles


def get_attribute(element: ET.Element, name: str, default: str | None = None):
    """Get attribute from element, handling namespaces"""
    # Try without namespace first
    value = element.get(name)
    if value is not None:
        return value
    # Try with namespace
    return element.get(f"{SVG_NS}{name}", default)


def escape_xml(value: str) -> str:
    """Escape XML special characters"""
    if not value:
        return value
    return (
        value.replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace('"', "&quot;")
    )


def convert_to_pascal_case(name: str) -> str:
    """Convert a string to PascalCase"""
    words = name.split("-")
    return words[0] + "".join(word.title() for word in words[1:])


def convert_path(element: ET.Element, svg_name: str) -> str:
    """Convert SVG path to JavaFX SVGPath"""
    fxml = "<SVGPath" + (' styleClass="icon"' if svg_name not in special_icons else "")

    attributes = [
        "d",
        "fill",
        "stroke",
        "stroke-width",
        "opacity",
        "fill-rule",
        "clip-rule",
        "fill-opacity",
    ]

    for attr in attributes:
        if (attr == "fill" or attr == "stroke") and svg_name not in special_icons:
            continue

        value = get_attribute(element, attr)

        if value:
            if attr == "d":
                attr = "content"

            if attr == "fill-rule":
                value = "EVEN_ODD" if value == "evenodd" else "NON_ZERO"

            if attr == "clip-rule":
                value = "EVEN_ODD" if value == "evenodd" else "NON_ZERO"
            fxml += f' {convert_to_pascal_case(attr)}="{value}"'

    transform = get_attribute(element, "transform", "")
    if transform:
        transform_attr = parse_transform(transform)

        fxml += f" {transform_attr}"

    fxml += " />"

    return fxml


def convert_circle(element: ET.Element, svg_name: str):
    """Convert SVG circle to JavaFX Circle"""
    fxml = "<Circle" + (' styleClass="icon"' if svg_name not in special_icons else "")

    attributes = [
        "cx",
        "cy",
        "r",
        "fill",
        "stroke",
        "stroke-width",
        "opacity",
    ]

    for attr in attributes:
        if (attr == "fill" or attr == "stroke") and svg_name not in special_icons:
            continue

        value = get_attribute(element, attr)
        if value:
            if attr == "d":
                attr = "content"
            if attr == "cx":
                attr = "centerX"
            if attr == "cy":
                attr = "centerY"
            if attr == "r":
                attr = "radius"
            fxml += f' {convert_to_pascal_case(attr)}="{value}"'

    transform = get_attribute(element, "transform", "")
    if transform:
        transform_attr = parse_transform(transform)

        fxml += f" {transform_attr}"

    fxml += " />"

    return fxml


def convert_rect(element: ET.Element, svg_name: str):
    """Convert SVG rect to JavaFX Rectangle"""
    fxml = "<Rectangle" + (
        ' styleClass="icon"' if svg_name not in special_icons else ""
    )

    attributes = [
        "x",
        "y",
        "width",
        "height",
        "fill",
        "stroke",
        "stroke-width",
        "opacity",
        "rx",
        "ry",
        "arc-width",
        "arc-height",
    ]
    for attr in attributes:
        if (attr == "fill" or attr == "stroke") and svg_name not in special_icons:
            continue
        value = get_attribute(element, attr)

        if attr == "rx":
            attr = "arcWidth"
        elif attr == "ry":
            attr = "arcHeight"
        elif attr == "d":
            attr = "content"

        if value:
            fxml += f' {convert_to_pascal_case(attr)}="{value}"'

    transform = get_attribute(element, "transform", "")
    if transform:
        transform_attr = parse_transform(transform)

        fxml += f" {transform_attr}"

    fxml += " />"

    return fxml


def process_element(element: ET.Element, svg_name: str, level: int = 1) -> str:
    """Recursively process SVG elements and return FXML string"""
    indent = "  " * level

    tag = element.tag
    if tag.startswith(SVG_NS):
        tag = tag[len(SVG_NS) :]

    if tag == "path":
        return indent + convert_path(element, svg_name) + "\n"
    elif tag == "circle":
        return indent + convert_circle(element, svg_name) + "\n"
    elif tag == "rect":
        return indent + convert_rect(element, svg_name) + "\n"
    elif tag == "g":
        # Handle group
        transform = get_attribute(element, "transform", "")
        transform_attr = parse_transform(transform)

        group_fxml = f'{indent}<Group styleClass="icon"{transform_attr}>\n'
        for child in element:
            group_fxml += process_element(child, svg_name, level + 1)
        group_fxml += f"{indent}</Group>\n"
        return group_fxml
    elif tag == "mask":
        # Handle mask - JavaFX requires manual implementation
        mask_fxml = (
            f"{indent}<!-- Mask element: implement using Blend effect or clip -->\n"
        )
        for child in element:
            mask_fxml += process_element(child, svg_name, level + 1)
        return mask_fxml
    elif tag in ["defs", "title", "desc"]:
        # Skip these elements
        return f"{indent}<!-- Skipped {tag} element -->\n"
    else:
        # Unknown element
        return f"{indent}<!-- Unsupported element: {tag} -->\n"


def svg_to_fxml(svg_file: Path, output_dir: Path):
    """Convert a single SVG file to FXML"""
    try:
        # Parse SVG
        tree = ET.parse(svg_file)
        root = tree.getroot()

        # Get viewBox for reference
        viewBox = get_attribute(root, "viewBox", "0 0 24 24")

        # Create FXML content
        fxml_content = '<?xml version="1.0" encoding="UTF-8"?>\n\n'
        fxml_content += "<?import javafx.scene.*?>\n"
        fxml_content += "<?import javafx.scene.shape.*?>\n"
        fxml_content += "<?import javafx.scene.effect.*?>\n\n"
        fxml_content += "<?import javafx.scene.layout.*?>\n\n"
        fxml_content += f"<!-- Original SVG: {svg_file.name} -->\n"
        fxml_content += f"<!-- ViewBox: {viewBox} -->\n"
        if viewBox:
            height = viewBox.split(" ")[3]
            width = viewBox.split(" ")[2]
            fxml_content += f'<StackPane xmlns:fx="http://javafx.com/fxml" prefHeight="{height}" prefWidth="{width}">\n'
        fxml_content += '<Group xmlns:fx="http://javafx.com/fxml">\n'

        # Process all child elements
        for element in root:
            fxml_content += process_element(element, svg_file.name.replace(".svg", ""))

        fxml_content += "</Group>\n"
        fxml_content += "</StackPane>\n"

        # Write to file
        output_file = output_dir / f"{svg_file.stem}.fxml"
        with open(output_file, "w", encoding="utf-8") as f:
            _ = f.write(fxml_content)

        print(f"✓ Converted {svg_file.name} -> {output_file.name}")

    except Exception as e:
        print(f"✗ Error converting {svg_file.name}: {str(e)}")


def main():
    # Configuration - change these paths as needed
    svg_folder = Path(
        "./src/main/resources/ca/mcgill/ecse/cheecsemanager/view/icons"
    )  # Folder containing SVG files
    output_folder = Path(
        "./src/main/resources/ca/mcgill/ecse/cheecsemanager/view/icons/fxml"
    )  # Output folder for FXML files

    # Create output folder if it doesn't exist
    output_folder.mkdir(exist_ok=True)

    # Check if input folder exists
    if not svg_folder.exists():
        print(f"Error: Input folder '{svg_folder}' does not exist!")
        print("Please create the folder and place your SVG files in it.")
        return

    # Find all SVG files
    svg_files = list(svg_folder.glob("*.svg"))

    if not svg_files:
        print(f"No SVG files found in '{svg_folder}'")
        return

    print(f"Found {len(svg_files)} SVG file(s) to convert\n")

    # Convert each file
    for svg_file in svg_files:
        if svg_file.name.replace(".svg", "") in special_icons:
            continue

        svg_to_fxml(svg_file, output_folder)

    print(f"\n✓ Conversion complete! FXML files saved to '{output_folder}'")


if __name__ == "__main__":
    main()
