package ca.mcgill.ecse.cheecsemanager.fxml.components;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import java.io.IOException;
import java.util.HashMap;
import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class Icon extends StackPane {
  static HashMap<String, StackPane> iconCache = new HashMap<>();

  private String icon;
  public String getIcon() { return icon; }
  public void setIcon(String icon) { this.icon = icon; }

  public Icon() { super(); }

  public Icon(@NamedArg("icon") String icon) {
    this();

    if (iconCache.containsKey(icon)) {
      this.getChildren().setAll(iconCache.get(icon).getChildren());
      this.setPrefHeight(iconCache.get(icon).getPrefHeight());
      this.setPrefWidth(iconCache.get(icon).getPrefWidth());

      return;
    }

    var resource = CheECSEManagerApplication.getResource("view/icons/fxml/" +
                                                         icon + ".fxml");

    if (resource == null) {
      throw new RuntimeException("Icon not found");
    }

    FXMLLoader loader = new FXMLLoader(resource);

    try {
      StackPane node = loader.load();
      var children = node.getChildren();
      this.getChildren().setAll(children);
      this.setPrefHeight(node.getPrefHeight());
      this.setPrefWidth(node.getPrefWidth());
      iconCache.put(icon, node);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
