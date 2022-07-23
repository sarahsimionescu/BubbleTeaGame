package bubbleteagame;

import java.util.Random;

/**
 * The MathTool class conveniently stores mathematical functions used by several
 * objects.
 *
 * @author Sarah Simionescu
 * @version 1
 */
public class MathTool {

    /**
     * Maps a position from one range into another range.
     *
     * @param x The initial position.
     * @param imin The minimum of the initial range.
     * @param imax The minimum of the initial range.
     * @param fmin The minimum of the new range.
     * @param fmax The maximum of the new range.
     * @return Returns the position of x within the new range.
     */
    public double map(double x, double imin, double imax, double fmin, double fmax) {
        if (imax - imin == 0) {
            return fmin + (fmax - fmin) / 2;
        } else {
            return (x - imin) / (imax - imin) * (fmax - fmin) + fmin;
        }
    }

    /**
     * Generates a random number in between the given range.
     *
     * @param min The minimum number for the range.
     * @param max The maximum number for the range.
     *
     * @return A random number in between the given range.
     */
    public double random(int min, int max) {
        return (Math.random() * (max - min + 1) + min);
    }
    
    /**
     * Selects a value according to a bell curve of probability.
     *
     * @param pivot The center value for the bell curve.
     * @param variance The wideness of the bell curve.
     * @param min The minimum value that can be returned.
     * @param max The maximum value that can be returned.
     *
     * @return A selected value according to a bell curve of probability.
     */ 
    int gaussian(double pivot, double variance, double min, double max) {
        double r = pivot + new Random().nextGaussian() * variance;
        if (r < min)
        {
            r =min;
        }else if (r > max)
        {
            r = max;
        }
        return (int) r;
    }
    
}
