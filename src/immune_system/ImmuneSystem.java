package immune_system;

import entity.SimulationObject;
import tools.Instruments;

public class ImmuneSystem {
	
	 // вероятность повторно заразиться
    public double K_IMMUNE_MAX = 10; // усиление имунитета максимальное
    public double kImmune; // усиление иммунитета 
    public int sickTimes; // сколько раз переболел
    
    SimulationObject object;
    
    double reinfectionNum;
	
    public ImmuneSystem(SimulationObject obj) {
    	kImmune = Instruments.random_number(0, K_IMMUNE_MAX);
    	sickTimes = 0;
    	
    	object = obj;
    }
    
    public boolean reInfect() {
		reinfectionNum = Instruments.random_number(0.01, 100); // рандомное число, отражающее вероятность повторного заражения
		
		double reinfection = object.panel.REINFECTION_PROBABILITY - sickTimes * kImmune;
		System.out.println("REINFECTION = " + reinfection + "/ " + kImmune+ " / "+ object.panel.REINFECTION_PROBABILITY);
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