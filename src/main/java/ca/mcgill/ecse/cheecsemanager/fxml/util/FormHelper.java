package ca.mcgill.ecse.cheecsemanager.fxml.util;

import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.util.Duration;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Helper Methods to manipulate the Form values / layout
 */
public class FormHelper {

    public static String formatTextMessage(Exception e) {
        // 1. Check for the first specific exception type
        if (e instanceof NumberFormatException) {
            return "Invalid input number";
        }

        // 2. Check for the second specific exception type
        // This requires that 'InvalidInputException' is a class that exists and extends Exception.
        else if (e instanceof InvalidInputException) {
            InvalidInputException ie = (InvalidInputException) e;
            return ie.getMessage();
        }

        // 3. The final 'else' acts as the default case (for all other Exception types)
        else {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }

    public static void triggerAfterDelay(Runnable action, int delay) {
        PauseTransition pause = new PauseTransition(Duration.seconds(delay));
        pause.setOnFinished(event -> {
            // Trigger the event after input seconds
            action.run();
        });
        pause.play();
    }


    public static boolean isEmpty(Object obj) {// Must handle null separately, as old switch on null throws NullPointerException
        if (obj == null) {
            return true;
        }

        // Use 'instanceof' for type checking and explicit casting
        if (obj instanceof String) {
            String s = (String) obj;
            return s.trim().isEmpty() || s.equals("null");
        }

        else if (obj instanceof Integer) {
            Integer i = (Integer) obj;
            // The logic (i.doubleValue() == 0 || i.doubleValue() == -1) is maintained
            return i.doubleValue() == 0 || i.doubleValue() == -1;
        }

        else if (obj instanceof List) {
            List<?> l = (List<?>) obj;
            return l.isEmpty();
        }

        // This is the 'default' case for all other objects
        else {
            return false;
        }
    }

    public static <T> String truncateText(List<T> items) {
        if (items.isEmpty()) return ""; // Handle empty list case

        List<String> currItems = items.stream().map(String::valueOf).sorted().toList();
        int size = currItems.size();
        System.out.println(currItems);
        return switch (size) {
            case 1 -> currItems.get(0);
            case 2, 3 -> String.join(", ", currItems);
            default -> "%s, %s...%s".formatted(currItems.get(0), currItems.get(1), currItems.get(currItems.size() - 1));
        };
    }
    
    public static <T> int getIndexOfItem(List<T> objects, T item) {
        return IntStream.range(0, objects.size())
                .filter(i -> FormHelper.areAllAttributesEqual(objects.get(i), item))
                .findFirst()
                .orElse(-1);
    }

    public static boolean areAllAttributesEqual(Object obj1, Object obj2) {
        if (obj1 == obj2) return true;
        if (obj1 == null || obj2 == null || obj1.getClass() != obj2.getClass()) return false;

        Class<?> clazz = obj1.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // Allows access to private fields
            try {
                Object value1 = field.get(obj1);
                Object value2 = field.get(obj2);
                if (!Objects.equals(value1, value2)) {
                    return false; // Attributes are different
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }
        return true; // All attributes match
    }

    public static String convertCamelCaseToWords(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();

        // Capitalize the first letter
        result.append(Character.toUpperCase(chars[0]));

        for (int i = 1; i < chars.length; i++) {
            char current = chars[i];
            if (Character.isUpperCase(current)) {
                result.append(' ');
                result.append(current);
            } else {
                result.append(current);
            }
        }

        return result.toString();
    }

    /**
     * @param node Node to be hidden
     * Hides a node from the scene graph
     */
    public static void hideNode(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    /**
     * @param node Node to be unhidden
     * Unhides a node from the scene graph
     */
    public static void unhideNode(Node node) {
        node.setVisible(true);
        node.setManaged(true);
    }

    /**
     * @param node Node to be disabled
     * Disables a node from the scene graph
     */
    public static void disableNode(Node node) {
        node.setDisable(true);
    }
}
