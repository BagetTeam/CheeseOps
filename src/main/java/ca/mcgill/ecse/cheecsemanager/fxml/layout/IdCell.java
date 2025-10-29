package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IdCell<S, C> extends TableCell<S, C> {
    private final Hyperlink link = new Hyperlink();
    private final Function<S, String> textExtractor;
    private final Consumer<S> redirectAction;
    private final String title;

    public IdCell(Function<S, String> textExtractor, String title, Consumer<S> redirectAction) {
        this.textExtractor = textExtractor;
        this.title = title;
        this.redirectAction = redirectAction;
    }

    @Override
    protected void updateItem(C item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            String fullText = textExtractor.apply(getTableRow().getItem());
            if (redirectAction == null) {
                setGraphic(new Text(fullText));
                return;
            }
            link.setText(fullText);
            link.setOnAction(event -> {
                    boolean truncated = isTextTruncated(fullText, link, link.getFont());
                    System.out.println("Clicked on ID of " + title);
                    if (truncated) {
                        showPopupWindow(fullText);
                    } else {
                        redirectAction.accept(getTableRow().getItem());
                    }
            });
            setGraphic(link);
        }
    }

    public void showPopupWindow(String fullText) {
        // Split the fullText into individual items
        List<String> ticketIds = Arrays.stream(fullText.split(",\\s*"))
                .collect(Collectors.toList());

        ListView<String> listView = new ListView<>(FXCollections.observableArrayList(ticketIds));
        listView.setPrefHeight(300); // Set preferred height
        listView.setPrefWidth(400);  // Set preferred width

        Button closeButton = new Button("Redirect to " + this.title);
        closeButton.setOnAction(e -> {
            ((Stage) closeButton.getScene().getWindow()).close();
            redirectAction.accept(getTableRow().getItem());
        });
        HBox buttonBox = new HBox(closeButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(0, 0, 0, 0)); // Top padding to separate from ListView

        VBox box = new VBox(10, listView, buttonBox);
        box.setPadding(new Insets(10));

        Scene scene = new Scene(box);
        Stage stage = new Stage();
        stage.setTitle(this.title);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    // Accurate check using Text helper
    private boolean isTextTruncated(String text, Region region, Font font) {
        Text helper = new Text(text);
        helper.setFont(font);
        helper.setWrappingWidth(0); // single-line
        helper.setLineSpacing(0);
        return helper.getLayoutBounds().getWidth() > region.getWidth();
    }
}

