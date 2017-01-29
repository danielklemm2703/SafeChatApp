package verify;

public class Verify {

    public static void verify(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException("could not verify expression");
        }
    }
}
