package by.andrey_zhukov;

public class AlgorithmTime {
    private final long time;
    private boolean stopped = false;

    AlgorithmTime() {
        this.time = System.currentTimeMillis();
    }

    public boolean isTimeToStop(long anotherTime) {
        stopped = getCurrentTime() >= anotherTime * 10;
        return stopped;
    }

    @Override
    public String toString() {
        long timePassed = getCurrentTime();
        return String.format("%d min. %d sec. %d ms.\"",
                timePassed / 60000, (timePassed / 1000) % 60, timePassed % 1000);
    }

    public long getCurrentTime() {
        return System.currentTimeMillis() - time;
    }

    public static String parseTime(long timeInMills) {
        return String.format("%d min. %d sec. %d ms.\"",
                timeInMills / 60000, (timeInMills / 1000) % 60, timeInMills % 1000);
    }

    public boolean isStopped() {
        return stopped;
    }
}
