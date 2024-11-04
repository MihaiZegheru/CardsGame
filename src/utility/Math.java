package utility;

public final class Math {
    /**
     * Clamps int val between min and max.
     *
     * @param val
     * @param min
     * @param max
     * @return int
     */
    public static int clamp(final int val, final int min, final int max) {
        return val < min ? min : java.lang.Math.min(val, max);
    }

    private Math() { }
}
