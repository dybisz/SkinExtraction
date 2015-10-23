package util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * Since information about saturation of each pixel is encoded
 * into integer value, we need elegant method to wrap some ugly
 * bit operations.
 * <p></p>
 * Created by dybisz on 12/10/2015.
 */
public abstract class PixelOperations {
    /**
     * @param val Value of pixel.
     * @return Value of red chanel of val.
     */
    public static int acquireR(int val) {
        return ((val >> 16) & 0xff);
    }

    /**
     * @param val Value of pixel.
     * @return Value of green channel of val.
     */
    public static int acquireG(int val) {
        return ((val >> 8) & 0xff);
    }

    /**
     * @param val Value of pixel.
     * @return Value of blue channel of val.
     */
    public static int acquireB(int val) {
        return ((val) & 0xff);
    }

    /**
     * @param r Red channel saturation.
     * @param g Green channel saturation.
     * @param b Blue channel saturation.
     * @return Cr saturation in CrCgCb color space.
     */
    public static int acquireCr(int r, int g, int b) {
        return (int) (128 + ((double) (112 * r) +
                (-93.768 * (double) g) +
                (-18.214 * (double) b)) / ((double) 256));
    }

    /**
     * @param r Red channel saturation.
     * @param g Green channel saturation.
     * @param b Blue channel saturation.
     * @return Cg saturation in CrCgCb color space.
     */
    public static int acquireCg(int r, int g, int b) {
        return (int) (128 + ((double) (-81.085 * (double) r) +
                (112 * (double) g) +
                (-30.915 * (double) b)) / ((double) 256));
    }

    /**
     * @param r Red channel saturation.
     * @param g Green channel saturation.
     * @param b Blue channel saturation.
     * @return Cb saturation in CrCgCb color space.
     */
    public static int acquireCb(int r, int g, int b) {
        return (int) (128 + ((double) (-37.945 * (double) r) +
                (-74.494 * (double) g) +
                (112 * (double) b)) / ((double) 256));
    }

}
