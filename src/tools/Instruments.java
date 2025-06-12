package tools;
import java.util.Random;

public class Instruments {
    public static double random_number(double start, double end) {
        Random random_num = new Random();
        return random_num.nextDouble(start, end);
    }

}
