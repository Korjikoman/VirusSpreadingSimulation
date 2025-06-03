import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.*;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Object extends JComponent {
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed = 3;
    private int angle;

    public void paintComponent(Graphics g) {
        Graphics2D gg = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        move();

        gg.setColor(Color.BLACK);
        gg.fillRect(x, y, width, height);

    }

    private void move() {
        x += (int) speed * Math.sin((double) angle);
        y += (int) speed * Math.cos((double) angle);

    }

    private int random_number(int start, int end) {
        Random random_num = new Random();
        return random_num.nextInt(181);
    }

    Object(int x_pos, int y_pos, int obj_width, int obj_height) {
        x = x_pos;
        y = y_pos;
        width = obj_width;
        height = obj_height;

        angle = random_number(0, 181);

        Thread animationThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    repaint();
                    try {
                        Thread.sleep(10);
                    } catch (Exception ex) {
                    }
                }
            }
        });
        animationThread.start();
    }
}
