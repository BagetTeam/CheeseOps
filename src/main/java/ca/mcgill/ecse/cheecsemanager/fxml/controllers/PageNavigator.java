package ca.mcgill.ecse.cheecsemanager.fxml.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.util.Stack;

/**
 * Controller that handles navigating between pages
 * @author Ewen Gueguen
 */
public class PageNavigator {
    private static PageNavigator instance;
    private StackPane contentArea;
    private Stack<Pane> navigationStack = new Stack<>();
    
    private PageNavigator() {}
    
    public static PageNavigator getInstance() {
        if (instance == null) {
            instance = new PageNavigator();
        }
        return instance;
    }
    
    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }
    
    public void navigateTo(String fxmlPath) {
        navigateTo(fxmlPath, null);
    }
    
    public void navigateTo(String fxmlPath, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane newPage = loader.load();
            
            // If controller has a setData method, call it
            if (data != null) {
                Object controller = loader.getController();
                if (controller instanceof DataReceiver) {
                    ((DataReceiver) controller).setData(data);
                }
            }
            
            // Save current page to stack if it exists
            if (!contentArea.getChildren().isEmpty()) {
                Pane currentPage = (Pane) contentArea.getChildren().get(0);
                navigationStack.push(currentPage);
            }
            
            // Display new page
            contentArea.getChildren().clear();
            contentArea.getChildren().add(newPage);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goBack() {
        if (!navigationStack.isEmpty()) {
            Pane previousPage = navigationStack.pop();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(previousPage);
            
            // If the page supports refresh, call it
            Object userData = previousPage.getUserData();
            if (userData instanceof PageRefreshable) {
                ((PageRefreshable) userData).onPageAppear();
            }
        }
    }
    
    public boolean canGoBack() {
        return !navigationStack.isEmpty();
    }
    
    public void clearHistory() {
        navigationStack.clear();
    }
    
    // Interface for controllers that can receive data
    public interface DataReceiver {
        void setData(Object data);
    }
    
    // Interface for pages that need to refresh when they become visible
    public interface PageRefreshable {
        void onPageAppear();
    }
}

