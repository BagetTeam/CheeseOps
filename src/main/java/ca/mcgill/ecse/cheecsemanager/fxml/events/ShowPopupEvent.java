package ca.mcgill.ecse.cheecsemanager.fxml.events;

import javafx.event.Event;
import javafx.event.EventType;

public class ShowPopupEvent extends Event {
  public static final EventType<ShowPopupEvent> SHOW_POPUP =
      new EventType<>(Event.ANY, "SHOW_POPUP");

  private final String content;

  public ShowPopupEvent(String content) {
    super(SHOW_POPUP);
    this.content = content;
  }

  public String getContent() { return content; }
}
