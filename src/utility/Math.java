package utility;

public class Math {
    public static <T extends Comparable<T>> T Clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        else if (val.compareTo(max) > 0) return max;
        else return val;
    }

    public static <T> void Swap(T a, T b) {
        T aux = a;
        a = b;
        b = aux;
    }
}
