package ca.mcgill.ecse.cheecsemanager.fxml.components;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import java.io.IOException;
import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

/**
 * Simple wrapper that loads reusable SVG-based icons from FXML snippets.
 *
 * @author Ming Li liu
 * */
public class Icon extends StackPane {
  private String icon;
  /** @return identifier of the loaded icon */
  public String getIcon() { return icon; }
  /** Sets the icon identifier without reloading resources. */
  public void setIcon(String icon) { this.icon = icon; }

  /** Builds an empty icon container. */
  public Icon() { super(); }

  /** Loads the referenced icon FXML and injects it into the stack pane. */
  public Icon(@NamedArg("icon") String icon) {
    this();
    var resource = CheECSEManagerApplication.getResource("view/icons/fxml/" +
                                                         icon + ".fxml");

    if (resource == null) {
      throw new RuntimeException("Icon not found");
    }

    FXMLLoader loader = new FXMLLoader(resource);

    try {
      StackPane node = loader.load();
      setChildren(node);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /** Copies the child nodes and preferred sizing from the loaded template. */
  private void setChildren(StackPane node) {
    var children = node.getChildren();
    this.getChildren().setAll(children);
    this.setPrefHeight(node.getPrefHeight());
    this.setPrefWidth(node.getPrefWidth());
  }
}
