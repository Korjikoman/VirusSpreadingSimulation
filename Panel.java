import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable {

    /**
	 * 
	 */

	// SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 px
    final int screenHeight = tileSize * maxScreenRow; // 576 px

    // FPS
    int FPS = 60;

    Thread thread;

    // OBJECTS
    final static int OBJECTS_NUM = 3;
    ArrayList<SimulationObject> objects = new ArrayList<>();

    public Panel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);

        // INITIALIZE OBJECTS
        for (int i = 0; i < OBJECTS_NUM; i++) {
            float x_pos = Instruments.random_number(0, screenWidth - tileSize);
            float y_pos = Instruments.random_number(0, screenHeight - tileSize);
            addObject(x_pos, y_pos);
        }
    }

    public void addObject(float x_pos, float y_pos) {
    	SimulationObject object = new SimulationObject(x_pos, y_pos, tileSize, tileSize);
        objects.add(object);

    }

    public void startThread() {
        thread = new Thread(this);
        thread.start();

    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS; // 1 sec / 60 FPS
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (thread != null) {

            // UPDATE: information --> object position
            update();
            // DRAW: draw the screen with updated information
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void update() {
        for (SimulationObject obj : objects) {
            obj.move();
        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        for (SimulationObject obj : objects) {
            obj.paintObj(g);

        }
      
    }

}
