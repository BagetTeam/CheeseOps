package ca.mcgill.ecse.cheecsemanager.fxml.util;

import javafx.event.Event;
import javafx.event.EventType;

public class TabSwitchEvent extends Event {
    public static final EventType<TabSwitchEvent> SWITCH_TAB =
            new EventType<>(Event.ANY, "SWITCH_TAB");

    private final String tabName;

    public TabSwitchEvent(String tabName) {
        super(SWITCH_TAB);
        this.tabName = tabName;
    }

    public String getTabName() {
        return tabName;
    }
}
