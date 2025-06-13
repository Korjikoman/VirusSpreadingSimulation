package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import charts.Charts;
import entity.SimulationObject;
import tile.TileManager;
import tools.Instruments;

public class SPanel extends JPanel implements Runnable {
	
	// SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 15;
    public final int screenWidth = tileSize * maxScreenCol; // 960 px
    public final int screenHeight = tileSize * maxScreenRow; // 720 px

    // STARTING SIMULATION
    private volatile boolean running = false;  
    
    // MAP
    public String mapFilePath = "/maps/map.txt";
    
    // OBJECTS & PARAMETRES ///////////////////////////////////
    
    public int OBJECTS_NUM = 50;
    public int INFECTED_NUM = 1;
    public int IMMUNE_NUM = 10;
    
    public double INFECTED_PROBABILITY = 20; // %
    public double REINFECTION_PROBABILITY = 50; // %
    public double MORALITY = 100; //  %
    public double VACCINE_EFFICIENCY =80; // 80% 
    
    ////////////////////////////////////////////////////////////
    
    
    ArrayList<SimulationObject> objects = new ArrayList<>();
    public double objectsVelocity = 2;
    
    // FPS
    int FPS = 60;
    int recoverDelayMs = 3000; 
    Thread thread;
    
    // TILES
    public TileManager tileM = new TileManager(this);
    
    
    // COLLISION 
    public CollisionChecker cChecker = new CollisionChecker(this);
    
    
    
    
    public int infectedNum;
    public int healthyNum;
    public int immuneNum;
    public int deadNum;
    
    // SIMULATION TIMER
    public Timer simulationTimer;
    public int simulationTIME = 10000;
    int elapsedTime=0;
    
    // CHARTS
    public Charts charts;
    public boolean startCharts = false;
    
    
    // DATA FOR CHARTS
    List<double[]> dataPerSecond = new ArrayList<>();
    
    // 
    
    public SPanel() {
    	
    	
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        
        
    	charts = new Charts(this);
    	

    }
        
     /*
      *  Инициализация мира: карта + объекты
      */
    public void initializeWORLD() {
    	tileM.loadMap(mapFilePath);
    	
    	// INITIALIZE IMMUNE OBJECT
    	for (int i = 0; i < IMMUNE_NUM; i++) {
    		double x_pos = Instruments.random_number(0, screenWidth - tileSize);
    		double y_pos = Instruments.random_number(0, screenHeight - tileSize);
            addObject(0, false, true, objectsVelocity);
            infectedNum ++;
    	}
    	// INITIALIZE INFECTED OBJECT
    	for (int i = 0; i < INFECTED_NUM; i++) {
    		double x_pos = Instruments.random_number(0, screenWidth - tileSize);
    		double y_pos = Instruments.random_number(0, screenHeight - tileSize);
        addObject(0, true, false, objectsVelocity);
        infectedNum ++;
    	}
        
        // INITIALIZE HEALTHY OBJECTS
        for (int i = 1; i < OBJECTS_NUM - IMMUNE_NUM - INFECTED_NUM; i++) {
        	double x_posit = Instruments.random_number(0, screenWidth - tileSize);
        	double y_posit = Instruments.random_number(0, screenHeight - tileSize);
            addObject(i, false, false, objectsVelocity);
            healthyNum++;
        }
        
        // SIMULATION TIMER
        
    	simulationTimer = new Timer(1000, new ActionListener() {
    				@Override
    				public void actionPerformed(ActionEvent e) {
    					dataPerSecond.add(new double[] {healthyNum, infectedNum, immuneNum, deadNum});
    					
    					elapsedTime += 1000;
    					
    					
    				}
    	    	});
    	
    }
    
    /*
     * Добавление объекта в симуляцию с проверкой на столкновение с тайлами
     */
    public void addObject(int number, boolean is_infected, boolean is_immune, double objVelocity) {
    	double x_pos, y_pos;
        SimulationObject object;
        // OBJECTS MUST NOT CREATE ON COLLISION ZONE --------------------- (!!! NOT EFFECTIVE ALGORITHM !!!)
        do {
            x_pos = Instruments.random_number(0, screenWidth - tileSize);
            y_pos = Instruments.random_number(0, screenHeight - tileSize);
            object = new SimulationObject(this, (int)x_pos, (int)y_pos, tileSize, tileSize, number, is_infected, is_immune,  objVelocity);
        } while (cChecker.checkTile(object));
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        
        objects.add(object);
    }
    
    public ArrayList<SimulationObject> getObjects() {
    	return objects;
    }
    
    /*
     * Запуск симуляции
     */
    public void startThread() {
    	
    	// INIT OBJECTS AND MAP
    	initializeWORLD();
    	///////////////////////////////
    	
    	if (!running) {
    		
            running = true;
            simulationTimer.start();
            thread = new Thread(this);
            thread.start();
            
        }
    	

    }
    
    /*
     * Остановка симуляции
     */
    public void stopSimulation() {
        running = false;
        
    }
    
    /*
     * Возобновление симуляции
     */
    public void runSimulation() {
        running = true;
        simulationTimer.start();
        thread = new Thread(this);
        thread.start();
        
    }
    
    
    /*
     * Переопределения метода, который и будет двигать и отрисовывать объекты
     */
    @Override
    public void run() {
    	
        double drawInterval = 1000000000 / FPS; // 1 sec / 60 FPS
        double nextDrawTime = System.nanoTime() + drawInterval;
        
        while (running && elapsedTime <= simulationTIME) {
        	//System.out.println("ELAPSED_TIME = " + elapsedTime);
            update();
            repaint();
            
            try {
                long sleepMs = (long)((nextDrawTime - System.nanoTime()) / 1_000_000);
                if (sleepMs < 0) sleepMs = 0;
                Thread.sleep(sleepMs);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        
        for (SimulationObject obj : objects) {
        	if (obj.infected) {
        		obj.stopRecover();
        		
        		//System.out.println("TIMER STOPPED");
        	}
        }
        simulationTimer.stop();
        // SAVE CHARTS
        if (elapsedTime >= simulationTIME) {
        	System.out.println("SAVING CAUSE ELAPSED TIME == " + elapsedTime);
        	charts.saveDataInCharts(dataPerSecond, ControlPanel.chartsPath);
        }
        
        
        
        // После выхода из while поток завершится, thread можно занулить:
        thread = null;
    }
        

    /*
     * Обновление состояния всех объектов симуляции
     */
    public void update() {
    	int infected = 0;
        int healthy = 0;
        int immune = 0;
        int dead = 0;
        
        
        for (SimulationObject obj : objects) {
            obj.move();
           
            if (obj.infected) {
           		obj.startRecover();
           		infected += 1;
            }
            else if (obj.immune) {
        		immune += 1;
        		obj.stopRecover();
        	}
            else if (obj.healthy){
            	healthy += 1;
            }
            else if (obj.dead) {
            	dead += 1;
            	 System.out.println(" DEAD ");
            }
       
        }
        System.out.println("HEALTHY "+ healthy + " INFECTED " + infected + " IMMUNE " + immune + " DEAD " + dead);
        infectedNum = infected;
        healthyNum = healthy;
        immuneNum = immune;
        deadNum = dead;
    }

    /*
     * Метод отрисовки (вызов отрисовки карты и объектов)
     */
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
