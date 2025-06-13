package immune_system;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import entity.SimulationObject;
import tools.Instruments;

public class ImmuneSystem {
	
	 // вероятность повторно заразиться
    public double K_IMMUNE_MAX = 10; // усиление имунитета максимальное
    public double kImmune; // усиление иммунитета 
    public int sickTimes; // сколько раз переболел
    
    SimulationObject object;
    
    double reinfectionNum;
	
    // таймер, в течение которого объект не может повторно заразиться 
    public Timer untouchableTimer;
    boolean stop_timer = false;
    int untouchable = 0;
    long can_reinfect = 2;
    
    public ImmuneSystem(SimulationObject obj) {
    	kImmune = Instruments.random_number(0, K_IMMUNE_MAX);
    	sickTimes = 0;
    	
    	object = obj;
    	
    	
    	
    	untouchableTimer = new Timer();
    	untouchableTimer.schedule(new TimerTask() {
  		  @Override
  		  public void run() {
  			untouchable++;
  			System.out.println("UNTOUCHABLE ================== " + untouchable);
  			if (untouchable >= can_reinfect) {
  				untouchableTimer.cancel();
  			}
  		  }}
  		,0, 2000);	
    }   
    public boolean reInfect() {
    	
    	// если он еще не может заражаться, то сброс
    	if (untouchable < can_reinfect) {
    		return false;
    	}
    	
		reinfectionNum = Instruments.random_number(0.01, 100); // рандомное число, отражающее вероятность повторного заражения
		
		double reinfection = object.panel.REINFECTION_PROBABILITY - sickTimes * kImmune;
		System.out.println("REINFECTION = " + untouchable + "/ " + can_reinfect+ " / "+ object.panel.REINFECTION_PROBABILITY);
		if (reinfectionNum <= reinfection) {
			return true;
		}
		
		return false;
	}
    
    // объект повторно заболел
    public void sick() {
    	sickTimes++;
    }

}