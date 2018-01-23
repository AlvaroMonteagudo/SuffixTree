import java.util.concurrent.TimeUnit;

public class Timer {

    private long starts;

    public static Timer start() {
        return new Timer();
    }

    private Timer() {
        reset();
    }

    public void reset() {
        starts = System.nanoTime();
    }

    public long time() {
        long end = System.nanoTime();
        return end - starts;
    }

    public long time(TimeUnit unit) {
        return unit.convert(time(), TimeUnit.NANOSECONDS);
    }

    public long convertTo(TimeUnit unit, long value) { return unit.convert(value, TimeUnit.NANOSECONDS); }

}
