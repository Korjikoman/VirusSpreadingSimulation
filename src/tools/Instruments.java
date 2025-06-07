package tools;
import java.util.Random;

public class Instruments {
    public static int random_number(int start, int end) {
        Random random_num = new Random();
        return random_num.nextInt(start, end);
    }

}
