package tools;
import java.util.Random;

/*
 * Класс инструментов
 */
public class Instruments {
	
	// Метод для генерации случайного числа с плавающей точкой в диапазоне [start, end)
    public static double random_number(double start, double end) {
        Random random_num = new Random();
        return random_num.nextDouble(start, end);
    }

}
