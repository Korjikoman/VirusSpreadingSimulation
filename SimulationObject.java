import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimulationObject {
    private float x;
    private float y;
    private int width;
    private int height;
    private double speed = 1;
    private int angle;
   
    public double getSpeed() {
        return speed;
    }

    public void move() {

        int leftBound = 0;
        int rightBound = 768;
        int topBound = 0;
        int bottomBound = 576;
        System.out.println("--> " + x + ';' + y + " angle = " + angle);

        // Объект не должен выходить за пределы экрана
        if (x <= leftBound) {
            angle = Instruments.random_number(0, 180);
            System.out.println("Объект вышел за левую границу --> " + x + ';' + y);

        } else if (x + width >= rightBound) {

            angle = Instruments.random_number(180, 360);
            System.out.println("Объект вышел за ПРАВУЮ границу --> " + x + ';' + y);
        } else if (y <= topBound) {

            angle = Instruments.random_number(-90, 90);
            System.out.println("Объект вышел за ВЕРХ границу --> " + x + ';' + y);
        } else if (y + height >= bottomBound) {

            angle = Instruments.random_number(90, 270);
            System.out.println("Объект вышел за НИЗ границу --> " + x + ';' + y);
        }

        x += speed * Math.sin((double) Math.toRadians(angle));
        y += speed * Math.cos((double) Math.toRadians(angle));

    }

    SimulationObject(float x_pos, float y_pos, int obj_width, int obj_height) {
        x = x_pos;
        y = y_pos;
        width = obj_width;
        height = obj_height;

        angle = Instruments.random_number(0, 360);
        System.out.println("!!!!x = " + x + " y = " + y + " \n");
    }

    public void paintObj(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);

        Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
        g2.fill(rect);

        //g2.dispose();
    }
}
