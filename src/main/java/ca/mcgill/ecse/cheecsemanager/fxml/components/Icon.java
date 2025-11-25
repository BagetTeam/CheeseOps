package ca.mcgill.ecse.cheecsemanager.fxml.components;

import ca.mcgill.ecse.cheecsemanager.application.CheECSEManagerApplication;
import java.io.IOException;
import java.util.HashMap;
import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;

public class Icon extends Group {
  static HashMap<String, Group> iconCache = new HashMap<>();

  private String icon;
  public String getIcon() { return icon; }
  public void setIcon(String icon) { this.icon = icon; }

  public Icon() { super(); }

  public Icon(@NamedArg("icon") String icon) {
    this();

    if (iconCache.containsKey(icon)) {
      this.getChildren().setAll(iconCache.get(icon).getChildren());
      return;
    }

    var resource = CheECSEManagerApplication.getResource("view/icons/fxml/" +
                                                         icon + ".fxml");

    if (resource == null) {
      throw new RuntimeException("Icon not found");
    }

    FXMLLoader loader = new FXMLLoader(resource);

    try {
      Group node = loader.load();
      var children = node.getChildren();
      this.getChildren().setAll(children);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
