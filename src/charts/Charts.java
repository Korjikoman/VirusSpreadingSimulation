package charts;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingWorker;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
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
    
    
    private String areaChartName = "area_chart";
    private String quickChartName = "quick_chart";
    
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
    chart = QuickChart.getChart("Number of infected objects", "Time", "Infected", "Healthy", new double[] { 0 }, new double[] { 0 });
    chart.getStyler().setLegendVisible(true);
    chart.getStyler().setXAxisTicksVisible(false);

    chart.addSeries("Infected", new double[] {0}, new double[] {0}).setMarker(new None());

    chart.addSeries("Immune", new double[] {0}, new double[] {0}).setMarker(new None());

    // Show it
    sw = new SwingWrapper<XYChart>(chart);
   
    sw.displayChart();
    
    mySwingWorker = new MySwingWorker();
    mySwingWorker.execute();
    
  }
  
  public XYChart areaChart (double[] healthyY, double[] infectedY, double[] immuneY, double[] time) {
	  
	// Create Chart
	    XYChart chart = new XYChartBuilder().width(800).height(600).title(getClass().getSimpleName()).xAxisTitle("X").yAxisTitle("Y").build();
	 
	    // Customize Chart
	    chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
	    chart.getStyler().setAxisTitlesVisible(false);
	    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
	    
	    // Series
	    chart.addSeries("Healthy", time, healthyY).setLineColor(Color.ORANGE).setFillColor(new Color(255, 165, 0, 100));
	    chart.addSeries("Infected", time, infectedY).setLineColor(Color.RED).setFillColor(new Color(255, 0, 0, 100));
	    chart.addSeries("Immune", time, immuneY).setLineColor(Color.BLUE).setFillColor(new Color(0, 0, 255, 100));
	  
	  
	  return chart;
			  
			 
  }
  
  public XYChart quickChart(double[] healthyY, double[] infectedY, double[] immuneY, double[] time) {
	    
	    
	    XYChart chart = QuickChart.getChart(
	        "Simulation Progress",       
	        "Time",                      
	        "Population",               
	        "Healthy",                   
	        time,                        
	        healthyY
	        
	    );
	    
	    
	    chart.getSeriesMap().get("Healthy").setLineColor(Color.ORANGE);
	    chart.addSeries("Infected", time, infectedY).setMarker(new None()).setLineColor(Color.RED);
	    chart.addSeries("Immune", time, immuneY).setMarker(new None()).setLineColor(Color.BLUE);
	    chart.getStyler().setAxisTitlesVisible(false);

	    chart.getStyler().setLegendVisible(true);
	    //chart.getStyler().setChartTitleVisible(true);
	    
	    return chart;
	}
  
  
  public void saveDataInCharts(List<int[]> dataPerSecond, String PathToSave) {
	  
	  double[] healthyY = new double[22];
	  
	  double[] infectedY = new double[22];
	  
	  double[] immuneY = new double[22];
	  
	  double[] time = new double[22];
	  time[0] = 0;
	  immuneY[0] = 0;
	  infectedY[0] = 1;
	  healthyY[0] = sp.OBJECTS_NUM;
	  
	  int healthy;
	  int infected;
	  int immune;
	  
	  
	  int i = 1;
	  for (int[] state : dataPerSecond) {
		  
		  healthy = state[0];
		  infected = state[1];
		  immune = state[2];
		  
		  healthyY[i] = healthy;
		  infectedY[i] = infected;
		  immuneY[i] = immune;
		  
		  
		  time[i] = i;
		  System.out.println("time[" + i+"] = " + time[i]);
		  
		  i++;
		  
	  }
	 
	  // CREATING CHARTS
	  XYChart areaChart = areaChart(healthyY, infectedY, immuneY,time);
	  XYChart quickChart = quickChart(healthyY, infectedY, immuneY, time);

	  
	 // Save to file
	    try {
	        BitmapEncoder.saveBitmap(areaChart, PathToSave + areaChartName, BitmapEncoder.BitmapFormat.PNG);
	        BitmapEncoder.saveBitmap(quickChart, PathToSave + quickChartName, BitmapEncoder.BitmapFormat.PNG);
	        System.out.println("Charts saved to " + PathToSave);
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
    	   
        chart.updateXYSeries("Infected", time, infected, null);
        chart.updateXYSeries("Healthy", time, healthy, null);
        chart.updateXYSeries("Immune", time, recovered, null);
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