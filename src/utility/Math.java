package utility;

public class Math {
    public static int Clamp(int val, int min, int max) {
        if (val < min) return min;
        else if (val > max) return max;
        else return val;
    }

    public static <T> void Swap(T a, T b) {
        T aux = a;
        a = b;
        b = aux;
    }
}
