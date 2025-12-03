package ca.mcgill.ecse.cheecsemanager.fxml.events;

import javafx.event.Event;
import javafx.event.EventType;

/** Event signaling that any open popup should be dismissed. */
public class HidePopupEvent extends Event {
  public static final EventType<HidePopupEvent> HIDE_POPUP =
      new EventType<>(Event.ANY, "HIDE_POPUP");

  /** Creates a new hide request targeting the currently visible popup. */
  public HidePopupEvent() { super(HIDE_POPUP); }
}
