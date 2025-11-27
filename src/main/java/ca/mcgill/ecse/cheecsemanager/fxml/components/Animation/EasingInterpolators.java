package ca.mcgill.ecse.cheecsemanager.fxml.components.Animation;

import javafx.animation.Interpolator;

/**
 * Collection of custom easing interpolators for more organic animations.
 * Based on Robert Penner's easing equations.
 */
public class EasingInterpolators {

  /**
   * Quadratic easing - accelerating from zero velocity
   */
  public static class QuadraticIn extends Interpolator {
    @Override
    protected double curve(double t) {
      return t * t;
    }
  }

  /**
   * Quadratic easing - decelerating to zero velocity
   */
  public static class QuadraticOut extends Interpolator {
    @Override
    protected double curve(double t) {
      return 1 - (1 - t) * (1 - t);
    }
  }

  /**
   * Cubic easing - accelerating from zero velocity
   */
  public static class CubicIn extends Interpolator {
    @Override
    protected double curve(double t) {
      return t * t * t;
    }
  }

  /**
   * Cubic easing - decelerating to zero velocity
   */
  public static class CubicOut extends Interpolator {
    @Override
    protected double curve(double t) {
      double f = (1 - t);
      return 1 - f * f * f;
    }
  }

  /**
   * Elastic easing - snapping back like a rubber band
   */
  public static class ElasticOut extends Interpolator {
    @Override
    protected double curve(double t) {
      if (t == 0 || t == 1)
        return t;

      double p = 0.3;
      double s = p / 4;
      return Math.pow(2, -10 * t) * Math.sin((t - s) * (2 * Math.PI) / p) + 1;
    }
  }

  /**
   * Bounce easing - bouncing back like a ball
   */
  public static class BounceOut extends Interpolator {
    @Override
    protected double curve(double t) {
      if (t < 1 / 2.75) {
        return 7.5625 * t * t;
      } else if (t < 2 / 2.75) {
        t -= 1.5 / 2.75;
        return 7.5625 * t * t + 0.75;
      } else if (t < 2.5 / 2.75) {
        t -= 2.25 / 2.75;
        return 7.5625 * t * t + 0.9375;
      } else {
        t -= 2.625 / 2.75;
        return 7.5625 * t * t + 0.984375;
      }
    }
  }

  /**
   * Back easing - overshooting and then correcting
   */
  public static class BackOut extends Interpolator {
    private static final double s = 1.70158;

    @Override
    protected double curve(double t) {
      t -= 1;
      return t * t * ((s + 1) * t + s) + 1;
    }
  }

  // Convenience instances
  public static final Interpolator QUADRATIC_IN = new QuadraticIn();
  public static final Interpolator QUADRATIC_OUT = new QuadraticOut();
  public static final Interpolator CUBIC_IN = new CubicIn();
  public static final Interpolator CUBIC_OUT = new CubicOut();
  public static final Interpolator ELASTIC_OUT = new ElasticOut();
  public static final Interpolator BOUNCE_OUT = new BounceOut();
  public static final Interpolator BACK_OUT = new BackOut();
}
