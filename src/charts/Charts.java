package charts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingWorker;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;
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
  
  public void saveDataInCharts(List<int[]> dataPerSecond, String PathToSave) {
	  
	  double[] dataHY = new double[21];
	  
	  double[] dataInfY = new double[21];
	  
	  double[] dataImY = new double[21];
	  
	  double[] time = new double[21];
	  time[0] =0;
	  
	  int healthy;
	  int infected;
	  int immune;
	  
	  
	  int i = 0;
	  for (int[] state : dataPerSecond) {
		  
		  healthy = state[0];
		  infected = state[1];
		  immune = state[2];
		  
		  dataHY[i] = healthy;
		  dataInfY[i] = infected;
		  dataImY[i] = immune;
		  
		  
		  time[i] = i;
		  System.out.println("time[" + i+"] = " + time[i]);
		  
		  i++;
		  
	  }
	 
	  // Create Chart
	    XYChart chart = new XYChartBuilder().width(800).height(600).title(getClass().getSimpleName()).xAxisTitle("X").yAxisTitle("Y").build();
	 
	    // Customize Chart
	    chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
	    chart.getStyler().setAxisTitlesVisible(false);
	    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
	 
	    // Series
	    chart.addSeries("Healthy", time, dataHY);
	    chart.addSeries("Infected", time, dataInfY);
	    chart.addSeries("Immune", time, dataImY);
	 // Save to file
	    try {
	        BitmapEncoder.saveBitmap(chart, PathToSave, BitmapEncoder.BitmapFormat.PNG);
	        System.out.println("Chart saved to " + PathToSave);
	    } catch (IOException e) {
	        System.err.println("Failed to save chart to file.");
	        e.printStackTrace();
	    }
	    
	    
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


  }
}