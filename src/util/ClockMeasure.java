package util;

/**
 * Created by dybisz on 13.12.15.
 */
public class ClockMeasure {
    private long timeStart;

    public void clockStart() {
        timeStart = System.currentTimeMillis();
    }

    public double clockEnd() {
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - timeStart;
        return tDelta / 1000.0;
    }
}
