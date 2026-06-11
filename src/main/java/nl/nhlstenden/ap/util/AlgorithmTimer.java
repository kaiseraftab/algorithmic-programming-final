package nl.nhlstenden.ap.util;

public class AlgorithmTimer {

    private long startTime;
    private long endTime;

    public void start() {
        this.startTime = System.nanoTime();
    }

    public void stop() {
        this.endTime = System.nanoTime();
    }

    public String getElapsedFormatted() {
        double seconds = (endTime - startTime) / 1_000_000_000.0;
        return String.format("%.1fs", seconds);
    }

    /**
     * Tenth-of-a-second value required by the assignment, plus the exact
     * millisecond count in brackets. Most operations finish well under 0.1s,
     * so the bracketed value is what makes the timing differences visible.
     */
    public String getElapsedDetailed() {
        double seconds = (endTime - startTime) / 1_000_000_000.0;
        double millis = (endTime - startTime) / 1_000_000.0;
        return String.format("%.1fs (%.1f ms)", seconds, millis);
    }

    public long getElapsedNanos() {
        return endTime - startTime;
    }

    public double getElapsedSeconds() {
        return (endTime - startTime) / 1_000_000_000.0;
    }
}
