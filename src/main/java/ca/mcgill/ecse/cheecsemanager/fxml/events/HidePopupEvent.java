package ca.mcgill.ecse.cheecsemanager.fxml.events;

import javafx.event.Event;
import javafx.event.EventType;

public class HidePopupEvent extends Event {
  public static final EventType<HidePopupEvent> HIDE_POPUP =
      new EventType<>(Event.ANY, "HIDE_POPUP");

  public HidePopupEvent() { super(HIDE_POPUP); }
}
