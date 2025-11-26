package ca.mcgill.ecse.cheecsemanager.fxml.events;

import javafx.event.Event;
import javafx.event.EventType;

public class ShowPopupEvent extends Event {
  public static final EventType<ShowPopupEvent> SHOW_POPUP =
      new EventType<>(Event.ANY, "SHOW_POPUP");

  private final String fxml;
  private final String title;

  public ShowPopupEvent(String fxml) {
    super(SHOW_POPUP);
    this.fxml = fxml;
    this.title = null;
  }

  public ShowPopupEvent(String fxml, String title) {
    super(SHOW_POPUP);
    this.fxml = fxml;
    this.title = title;
  }

  public String getTitle() { return title; }
  public String getContent() { return fxml; }
}
