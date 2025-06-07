package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.swing.JPanel;

import entity.SimulationObject;
import tile.TileManager;
import tools.Instruments;

public class SPanel extends JPanel implements Runnable {
	
	// SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 px
    public final int screenHeight = tileSize * maxScreenRow; // 576 px

    // FPS
    int FPS = 60;
    int recoverDelayMs = 3000; 
    Thread thread;
    
    // TILES
    TileManager tileM = new TileManager(this);
    
    
    // COLLISION 
    public CollisionChecker cChecker = new CollisionChecker(this);
    
    
    // OBJECTS
    final static int OBJECTS_NUM = 20;
    ArrayList<SimulationObject> objects = new ArrayList<>();

    public SPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        
        // INITIALIZE INFECTED OBJECT
        float x_pos = Instruments.random_number(0, screenWidth - tileSize);
        float y_pos = Instruments.random_number(0, screenHeight - tileSize);
        addObject(x_pos, y_pos, 0, true);
        
        
        // INITIALIZE OBJECTS
        for (int i = 1; i < OBJECTS_NUM; i++) {
            float x_posit = Instruments.random_number(0, screenWidth - tileSize);
            float y_posit = Instruments.random_number(0, screenHeight - tileSize);
            addObject(x_posit, y_posit, i, false);
        }
    }

    public void addObject(float x_pos, float y_pos, int number, boolean is_infected) {
    	SimulationObject object = new SimulationObject(this, x_pos, y_pos, tileSize, tileSize, number, is_infected);
        objects.add(object);

    }
    
    public ArrayList<SimulationObject> getObjects() {
    	return objects;
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
            if (obj.infected) {
            	obj.recover(recoverDelayMs);
            }
        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; 
        // draw tilemap
        tileM.draw(g2);
        
        // draw objects
        for (SimulationObject obj : objects) {
            obj.paintObj(g2);

        }
        
      
    }
    
    
}
