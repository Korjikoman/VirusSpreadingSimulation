package charts;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingWorker;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.markers.None;

import main.SPanel;

/**
 * Creates a real-time chart using SwingWorker
 */
public class Charts {

	int infectedNum;
    int healthyNum;
    int immuneNum;
    
    SPanel sp;
    
    public Charts(SPanel spanel) {
    	this.sp = spanel;
    }
	
	
	  MySwingWorker mySwingWorker;
	  SwingWrapper<XYChart> sw;
	  XYChart chart;
	  
	  List<Integer> infected = new ArrayList<>();
	    List<Integer> healthy = new ArrayList<>();
	    List<Integer> recovered = new ArrayList<>();
	    List<Integer> time = new ArrayList<>();

	    double t = 0;
  public void go() {

    // Create Chart
    chart = QuickChart.getChart("Number of infected objects", "Time", "Infected", "randomWalk", new double[] { 0 }, new double[] { 0 });
    chart.getStyler().setLegendVisible(false);
    chart.getStyler().setXAxisTicksVisible(false);
    
    chart.addSeries("Больные", new double[] {0}, new double[] {0}).setMarker(new None());
    chart.addSeries("Здоровые", new double[] {0}, new double[] {0}).setMarker(new None());
    chart.addSeries("Выздоровевшие", new double[] {0}, new double[] {0}).setMarker(new None());

    // Show it
    sw = new SwingWrapper<XYChart>(chart);
    sw.displayChart();

    mySwingWorker = new MySwingWorker();
    mySwingWorker.execute();
  }

  private class MySwingWorker extends SwingWorker<Boolean, double[]> {

    LinkedList<Double> fifo = new LinkedList<Double>();

    public MySwingWorker() {

      fifo.add(0.0);
    }

    @Override
    protected Boolean doInBackground() throws Exception {

      while (!isCancelled()) {
    	infectedNum = sp.infectedNum;
    	healthyNum = sp.healthyNum;
    	immuneNum = sp.immuneNum;
    	
    	time.add((int)t++);
        infected.add(infectedNum);
        healthy.add(healthyNum);
        recovered.add(immuneNum);
    	   
        chart.updateXYSeries("Больные", time, infected, null);
        chart.updateXYSeries("Здоровые", time, healthy, null);
        chart.updateXYSeries("Выздоровевшие", time, recovered, null);
        sw.repaintChart();

        try {
            Thread.sleep(500); 
        } catch (InterruptedException e) {
            System.out.println("Остановлено.");
            return false;
        }

      }

      return true;
    }

//    @Override
//    protected void process(List<double[]> chunks) {
//
//      System.out.println("number of chunks: " + chunks.size());
//
//      double[] mostRecentDataSet = chunks.get(chunks.size() - 1);
//
//      chart.updateXYSeries("randomWalk", null, mostRecentDataSet, null);
//      sw.repaintChart();
//
//      long start = System.currentTimeMillis();
//      long duration = System.currentTimeMillis() - start;
//      try {
//        Thread.sleep(40 - duration); // 40 ms ==> 25fps
//        // Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
//      } catch (InterruptedException e) {
//      }
//
//    }
  }
}