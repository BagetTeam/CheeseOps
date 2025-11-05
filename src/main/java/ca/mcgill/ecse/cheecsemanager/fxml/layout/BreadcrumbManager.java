package ca.mcgill.ecse.cheecsemanager.fxml.layout;

import ca.mcgill.ecse.cheecsemanager.fxml.state.NavigationState;
import ca.mcgill.ecse.cheecsemanager.fxml.util.PageSwitchEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Pane;
import org.controlsfx.control.BreadCrumbBar;

import java.util.ArrayList;

public class BreadcrumbManager {
    private final BreadCrumbBar<String> breadCrumbBar;
    private final Pane parentContainer;
    private final ArrayList<NavigationState<?>> collapsedStack = new ArrayList<>();
    private final ArrayList<NavigationState<?>> navigationStack = new ArrayList<>();

    private final TreeItem<String> ellipsisItem = new TreeItem<>("..."); // Non-clickable item
    private final int THRESHOLD_WIDTH = 50;
    private double lastChangedWidth = 0;

    /**
     * Initialize Breadcrumbar
     *
     * @param breadCrumbBar   Breadcrumbar
     * @param parentContainer Container containing breadcrumb
     */
    public BreadcrumbManager(BreadCrumbBar<String> breadCrumbBar, Pane parentContainer) {
        this.breadCrumbBar = breadCrumbBar;
        this.parentContainer = parentContainer;

        this.breadCrumbBar.setOnCrumbAction(event -> {
            int depth = getCrumbDepth(event.getSelectedCrumb());
            if (depth > 0 && !collapsedStack.isEmpty()) {
                depth += collapsedStack.size() - 1; // To exclude ellipse depth
            }
            ///  1. Calculate Last item and Selected Item depth
            int rawNavigationDepth = navigationStack.size();
            int rawSelectionDepth = depth + 1; // Handle the new item that will be added as part of this event trigger

            /// 2. Update navigation stack and remove items after selected item
            event.getSelectedCrumb().getChildren().clear();
            NavigationState<?> state = navigationStack.get(depth);
            navigationStack.subList(depth, navigationStack.size()).clear();

            /// 3. Launch the selected Page
            this.parentContainer.fireEvent(new PageSwitchEvent(state));

            /// 4. Expand breadcrumb if required
            expandIfRequired(rawSelectionDepth, rawNavigationDepth);
        });

        // Handle Ellipse breadcrum styling
        this.breadCrumbBar.setCrumbFactory(crumb -> {
            BreadCrumbBar.BreadCrumbButton breadCrumbButton = new BreadCrumbBar.BreadCrumbButton(crumb.getValue());
            breadCrumbButton.setDisable(crumb.getValue().equals("..."));
            return breadCrumbButton;
        });
        this.breadCrumbBar.widthProperty().addListener((obs, oldVal, newVal) -> collapseItems(newVal.doubleValue()));
        this.parentContainer.widthProperty().addListener((obs, oldVal, newVal) -> expandItems(newVal.doubleValue()));
    }

    /**
     * Helper Methods
     *
     * @param navItem Add item to the navStack
     */
    public void addNavItem(final NavigationState<?> navItem) {
        navigationStack.add(navItem);
    }

    /**
     * Remove last item and returns Second last item
     *
     * @return navigationState
     */
    public NavigationState<?> removeLastAndGetSecondLast() {
        // Remove the last element
        navigationStack.remove(navigationStack.size() - 1);
        // Return the new last element (formerly second-to-last)
        return navigationStack.get(navigationStack.size() - 1);
    }

    /**
     * Force rerenders the Breadcrumb UI
     */
    public void forceUpdateUi(TreeItem<String> last) {
        this.breadCrumbBar.setSelectedCrumb(null); // Tem workaround for UI to update
        this.breadCrumbBar.setSelectedCrumb(last);
    }

    /**
     * Finds the first item in the Tree Structure of breadcrumb
     */
    public TreeItem<String> getRootTreeItem(TreeItem<String> node) {
        TreeItem<String> temp = node;
        while (temp.getParent() != null) {
            temp = temp.getParent();
        }
        return temp;
    }

    /**
     * Calculate depth of the selected breadcrumb
     */
    protected int getCrumbDepth(TreeItem<String> item) {
        int depth = 0;
        while (item.getParent() != null) {
            depth++;
            item = item.getParent();
        }
        return depth;
    }

    /**
     * Add a new Item before an item in breadcrumb
     *
     * @param selectedItem Item before which new item is added
     * @param newItemValue String item to be added
     */
    private void insertNewItemBefore(TreeItem<String> selectedItem, String newItemValue) {
        TreeItem<String> parent = selectedItem.getParent();
        if (parent != null) {
            int index = parent.getChildren().indexOf(selectedItem);
            parent.getChildren().remove(selectedItem); // Remove the selected item from its parent

            TreeItem<String> newItem = new TreeItem<>(newItemValue);
            parent.getChildren().add(index, newItem); // Insert new item at the same position
            newItem.getChildren().add(selectedItem); // Add the original item as a child of the new item
        }
    }

    /**
     * When New item is added in breadcrumb, it checks if theere is enough space.
     * If not, then one item is collapsed and replaced with ellipse
     *
     * @param breadcrumbWidth
     */
    private void collapseItems(double breadcrumbWidth) {
        double containerWidth = this.parentContainer.getWidth();
        boolean isOverflowing = breadcrumbWidth + THRESHOLD_WIDTH > containerWidth;
        if (!isOverflowing) return;
        if (getCrumbDepth(this.breadCrumbBar.getSelectedCrumb()) < 3) return;
        TreeItem<String> last = this.breadCrumbBar.getSelectedCrumb();
        TreeItem<String> first = getRootTreeItem(last);

        TreeItem<String> second = first.getChildren().get(0);
        // If ellipse already there then remove ellipse and update the second item to non ellipse second item
        if (second.equals(ellipsisItem)) {
            first.getChildren().remove(second);
            second = second.getChildren().get(0);
        }
        // Remove second item
        collapsedStack.add(navigationStack.get(1 + collapsedStack.size()));
        System.out.println("Collapsing " + collapsedStack.size() + " items");
        // Append 3rd item to ellipse
        TreeItem<String> third = second.getChildren().get(0);
        ellipsisItem.getChildren().add(0, third);
        first.getChildren().clear();
        first.getChildren().add(ellipsisItem);
        this.forceUpdateUi(last);
        lastChangedWidth = parentContainer.getWidth();
    }


    /**
     * When width of the Window is increased, it checks if there is available space
     * If yes, then one item is expanded
     *
     * @param newWidth
     */
    private void expandItems(double newWidth) {
        if (Math.abs(newWidth - lastChangedWidth) < THRESHOLD_WIDTH) return;
        if (collapsedStack.isEmpty()) return;
        // Expand 1 item and
        TreeItem<String> last = this.breadCrumbBar.getSelectedCrumb();
        TreeItem<String> first = getRootTreeItem(last);

        if (collapsedStack.size() == 1) {
            TreeItem<String> itemAfterEllipse = ellipsisItem.getChildren().get(0);
            TreeItem<String> collapsedItem = new TreeItem<>(collapsedStack.remove(0).getTitle());
            collapsedItem.getChildren().add(itemAfterEllipse);
            first.getChildren().remove(ellipsisItem);
            first.getChildren().add(collapsedItem);
        } else {
            var firstChild = ellipsisItem.getChildren().get(0);
            NavigationState<?> lastState = collapsedStack.remove(collapsedStack.size() - 1);
            insertNewItemBefore(firstChild, lastState.getTitle());
        }
        forceUpdateUi(last);
        // update lastChangedWidth
        lastChangedWidth = newWidth;
    }


    /**
     * When a breadcrumb item is clicked, then the number of items after selected item are collapsed (Already done before calling this method)
     * At the same time, same number of items  (if any) before selected item is expanded
     *
     * @param clickedDepth         Depth of the selected item
     * @param totalNavigationDepth Total length of Navigation Stack
     */
    private void expandIfRequired(int clickedDepth, int totalNavigationDepth) {
        if (collapsedStack.isEmpty()) return;
        System.out.println("Clicked depth: " + clickedDepth);
        System.out.println("Last item depth: " + totalNavigationDepth);
        int numOfItemsToExpand = totalNavigationDepth - clickedDepth;
        TreeItem<String> last = this.breadCrumbBar.getSelectedCrumb();
        TreeItem<String> secondItem = ellipsisItem;
        if (numOfItemsToExpand >= collapsedStack.size()) {
            System.out.println("Remove Eclipse");
            secondItem = ellipsisItem.getParent();
            secondItem.getChildren().add(ellipsisItem.getChildren().get(0));
            secondItem.getChildren().remove(ellipsisItem);
        }
        System.out.println(collapsedStack.size());
        System.out.println("Expand " + numOfItemsToExpand + " items with ellipse");
        int finalItems = Math.min(numOfItemsToExpand, collapsedStack.size());
        for (int i = 0; i < finalItems; i++) {
            insertNewItemBefore(
                secondItem.getChildren().get(0),                      // get(0) → get(0)
                collapsedStack.remove(collapsedStack.size() - 1).getTitle()  // removeLast() → remove(size-1)
        );
        }
        forceUpdateUi(last);
    }

}
