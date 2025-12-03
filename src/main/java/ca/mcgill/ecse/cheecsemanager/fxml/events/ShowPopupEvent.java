package ca.mcgill.ecse.cheecsemanager.fxml.events;

import javafx.event.Event;
import javafx.event.EventType;

/** Event requesting that the main controller display a popup. */
public class ShowPopupEvent extends Event {
  public static final EventType<ShowPopupEvent> SHOW_POPUP =
      new EventType<>(Event.ANY, "SHOW_POPUP");

  private final String fxml;
  private final String title;

  /**
   * Builds an event that loads popup content without a custom title.
   * @param fxml relative FXML path for the popup body
   */
  public ShowPopupEvent(String fxml) {
    super(SHOW_POPUP);
    this.fxml = fxml;
    this.title = null;
  }

  /**
   * Builds an event that loads popup content and shows a title string.
   * @param fxml relative FXML path for the popup body
   * @param title popup title displayed in the header
   */
  public ShowPopupEvent(String fxml, String title) {
    super(SHOW_POPUP);
    this.fxml = fxml;
    this.title = title;
  }

  /** @return optional popup title */
  public String getTitle() { return title; }
  /** @return popup content path */
  public String getContent() { return fxml; }
}
