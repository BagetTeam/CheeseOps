package ca.mcgill.ecse.cheecsemanager.fxml.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event requesting that a toast notification be shown near the root.
 *
 * @author Ming Li Liu
 * */
public class ToastEvent extends Event {
  public static final EventType<ToastEvent> TOAST_NOTIFICATION =
      new EventType<>(Event.ANY, "TOAST_NOTIFICATION");

  private final String message;
  private final ToastType type;

  /** Supported visual states for toast notifications. */
  public enum ToastType { INFO, SUCCESS, WARNING, ERROR }

  /**
   * Builds a toast event with the provided message and severity.
   * @param message text to display
   * @param type toast style to render
   */
  public ToastEvent(String message, ToastType type) {
    super(TOAST_NOTIFICATION);
    this.message = message;
    this.type = type;
  }

  /** @return human-readable message */
  public String getMessage() { return message; }

  /** @return the toast variant to display */
  public ToastType getType() { return type; }
}
