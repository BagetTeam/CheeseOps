package ca.mcgill.ecse.cheecsemanager.fxml.components.Animation;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.WritableValue;
import javafx.util.Duration;

/**
 * Reusable animation manager for smooth property animations with custom easing.
 * Can animate any WritableValue (properties, transforms, etc.).
 */
public class AnimationManager {

  /**
   * Creates and plays an animation with the specified parameters.
   *
   * @param property The property to animate (e.g., node.translateXProperty())
   * @param startValue Starting value
   * @param endValue Ending value
   * @param duration Animation duration
   * @param interpolator Easing function to use
   * @param onFinished Optional callback when animation completes
   * @param <T> The type of value being animated
   * @return The Timeline instance (can be used to pause, stop, etc.)
   */
  public static <T> Timeline animate(WritableValue<T> property, T startValue,
                                     T endValue, Duration duration,
                                     javafx.animation.Interpolator interpolator,
                                     Runnable onFinished) {

    Timeline timeline = new Timeline();

    // Set initial value
    property.setValue(startValue);

    // Create keyframe with custom interpolator
    KeyValue keyValue = new KeyValue(property, endValue, interpolator);
    KeyFrame keyFrame = new KeyFrame(duration, keyValue);

    timeline.getKeyFrames().add(keyFrame);

    if (onFinished != null) {
      timeline.setOnFinished(e -> onFinished.run());
    }

    timeline.play();
    return timeline;
  }

  /**
   * Overload without completion callback
   */
  public static <T>
      Timeline animate(WritableValue<T> property, T startValue, T endValue,
                       Duration duration,
                       javafx.animation.Interpolator interpolator) {
    return animate(property, startValue, endValue, duration, interpolator,
                   null);
  }

  /**
   * Fluent builder for complex animations
   */
  public static <T> AnimationBuilder<T> builder() {
    return new AnimationBuilder<T>();
  }

  /**
   * Convenience builder for numeric animations (handles DoubleProperty,
   * IntegerProperty, etc.)
   */
  public static NumericAnimationBuilder numericBuilder() {
    return new NumericAnimationBuilder();
  }

  public static MultiAnimationBuilder multiBuilder() {
    return new MultiAnimationBuilder();
  }

  /**
   * Builder class for configuring animations fluently
   */
  public static class AnimationBuilder<T> {
    private WritableValue<T> target;
    private T startValue;
    private T endValue;
    private Duration duration;
    private javafx.animation.Interpolator interpolator = Interpolator.EASE_BOTH;
    private Runnable onFinished;

    public AnimationBuilder<T> target(WritableValue<T> target) {
      this.target = target;
      return this;
    }

    public AnimationBuilder<T> from(T startValue) {
      this.startValue = startValue;
      return this;
    }

    public AnimationBuilder<T> to(T endValue) {
      this.endValue = endValue;
      return this;
    }

    public AnimationBuilder<T> duration(Duration duration) {
      this.duration = duration;
      return this;
    }

    public AnimationBuilder<T> durationMillis(double millis) {
      this.duration = Duration.millis(millis);
      return this;
    }

    public AnimationBuilder<T>
    easing(javafx.animation.Interpolator interpolator) {
      this.interpolator = interpolator;
      return this;
    }

    public AnimationBuilder<T> onFinished(Runnable callback) {
      this.onFinished = callback;
      return this;
    }

    public Timeline play() {
      if (target == null || startValue == null || endValue == null ||
          duration == null) {
        throw new IllegalStateException(
            "Animation requires target, startValue, endValue, and duration");
      }

      Timeline timeline = new Timeline();
      target.setValue(startValue);

      KeyValue keyValue = new KeyValue(target, endValue, interpolator);
      KeyFrame keyFrame = new KeyFrame(duration, keyValue);

      timeline.getKeyFrames().add(keyFrame);

      if (onFinished != null) {
        timeline.setOnFinished(e -> onFinished.run());
      }

      timeline.play();
      return timeline;
    }
  }

  public static class NumericAnimationBuilder {
    private WritableValue<Number> target;
    private double startValue;
    private double endValue;
    private Duration duration;
    private javafx.animation.Interpolator interpolator =
        javafx.animation.Interpolator.EASE_BOTH;
    private Runnable onFinished;

    public NumericAnimationBuilder target(WritableValue<Number> target) {
      this.target = target;
      return this;
    }

    public NumericAnimationBuilder from(double startValue) {
      this.startValue = startValue;
      return this;
    }

    public NumericAnimationBuilder to(double endValue) {
      this.endValue = endValue;
      return this;
    }

    public NumericAnimationBuilder duration(Duration duration) {
      this.duration = duration;
      return this;
    }

    public NumericAnimationBuilder durationMillis(double millis) {
      this.duration = Duration.millis(millis);
      return this;
    }

    public NumericAnimationBuilder
    easing(javafx.animation.Interpolator interpolator) {
      this.interpolator = interpolator;
      return this;
    }

    public NumericAnimationBuilder onFinished(Runnable callback) {
      this.onFinished = callback;
      return this;
    }

    public Timeline play() {
      if (target == null || duration == null) {
        throw new IllegalStateException(
            "Animation requires target, startValue, endValue, and duration");
      }

      return AnimationManager.animate(target, startValue, endValue, duration,
                                      interpolator, onFinished);
    }
  }

  /**
   * Builder for animating multiple properties simultaneously
   */
  public static class MultiAnimationBuilder {
    private final List<AnimationTarget<?>> targets = new ArrayList<>();
    private Duration duration;
    private javafx.animation.Interpolator interpolator =
        javafx.animation.Interpolator.EASE_BOTH;
    private Runnable onFinished;

    /**
     * Add a property to animate
     * @param property The property to animate
     * @param startValue Starting value
     * @param endValue Ending value
     * @param <T> The type of the property
     * @return This builder for method chaining
     */
    public <T> MultiAnimationBuilder addTarget(WritableValue<T> property,
                                               T startValue, T endValue) {
      targets.add(new AnimationTarget<>(property, startValue, endValue));
      return this;
    }

    /**
     * Convenience method for adding numeric properties
     */
    public MultiAnimationBuilder
    addNumericTarget(WritableValue<Number> property, double startValue,
                     double endValue) {
      targets.add(new AnimationTarget<>(property, startValue, endValue));
      return this;
    }

    public MultiAnimationBuilder duration(Duration duration) {
      this.duration = duration;
      return this;
    }

    public MultiAnimationBuilder durationMillis(double millis) {
      this.duration = Duration.millis(millis);
      return this;
    }

    public MultiAnimationBuilder
    easing(javafx.animation.Interpolator interpolator) {
      this.interpolator = interpolator;
      return this;
    }

    public MultiAnimationBuilder onFinished(Runnable callback) {
      this.onFinished = callback;
      return this;
    }

    public Timeline play() {
      if (targets.isEmpty() || duration == null) {
        throw new IllegalStateException(
            "Multi-animation requires at least one target and a duration");
      }

      Timeline timeline = new Timeline();
      List<KeyValue> keyValues = new ArrayList<>();

      // Create KeyValue for each target
      for (AnimationTarget<?> target : targets) {
        addKeyValue(target, keyValues, interpolator);
      }

      KeyFrame keyFrame =
          new KeyFrame(duration, keyValues.toArray(new KeyValue[0]));
      timeline.getKeyFrames().add(keyFrame);

      if (onFinished != null) {
        timeline.setOnFinished(e -> onFinished.run());
      }

      timeline.play();
      return timeline;
    }

    @SuppressWarnings("unchecked")
    private static <T> void
    addKeyValue(AnimationTarget<?> target, List<KeyValue> keyValues,
                javafx.animation.Interpolator interpolator) {
      AnimationTarget<T> typedTarget = (AnimationTarget<T>)target;
      typedTarget.property.setValue(typedTarget.startValue);
      KeyValue keyValue = new KeyValue(typedTarget.property,
                                       typedTarget.endValue, interpolator);
      keyValues.add(keyValue);
    }
  }

  /**
   * Internal holder for animation targets
   */
  private static class AnimationTarget<T> {
    final WritableValue<T> property;
    final T startValue;
    final T endValue;

    AnimationTarget(WritableValue<T> property, T startValue, T endValue) {
      this.property = property;
      this.startValue = startValue;
      this.endValue = endValue;
    }
  }
}
