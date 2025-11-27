package ca.mcgill.ecse.cheecsemanager.fxml.events;

import javafx.event.Event;
import javafx.event.EventType;

public class ToastEvent extends Event {
  public static final EventType<ToastEvent> TOAST_NOTIFICATION =
      new EventType<>(Event.ANY, "TOAST_NOTIFICATION");

  private final String message;
  private final ToastType type;

  public enum ToastType { INFO, SUCCESS, WARNING, ERROR }

  public ToastEvent(String message, ToastType type) {
    super(TOAST_NOTIFICATION);
    this.message = message;
    this.type = type;
  }

  public String getMessage() { return message; }

  public ToastType getType() { return type; }
}
