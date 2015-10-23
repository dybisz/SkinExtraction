package util;

import util.PixelOperations;

/**
 * Class is crucial for synchronizing vast parts of the application.
 * <p></p>
 * Each pixel of original image is stored in appropriate r,g,b
 * channels and automatically transformed into Cr, Cg, Cb space.
 * <p></p>
 * In addition, it holds two box-objects, which represents pixel
 * on 3D plots.
 * <p></p>
 * Created by dybisz on 12/10/2015.
 */
public class Pixel {
    private int x;
    private int y;
    private int r;
    private int g;
    private int b;
    private int Cr;
    private int Cg;
    private int Cb;

    /**
     * @param r Red channel saturation.
     * @param g Green channel saturation.
     * @param b Blue channel saturation.
     * @param x Pixel position in an image; x axis.
     * @param y Pixel position in an image; y axis.
     */
    public Pixel(int r, int g, int b, int x, int y) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.g = g;
        this.b = b;
        Cr = PixelOperations.acquireCr(r, g, b);
        Cg = PixelOperations.acquireCg(r, g, b);
        Cb = PixelOperations.acquireCb(r, g, b);
    }

    public int getCr() {
        return Cr;
    }

    public int getCg() {
        return Cg;
    }

    public int getCb() {
        return Cb;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }
}
