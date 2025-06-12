package charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
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
    int deadNum;
    
    SPanel sp;
    
    int numCharts = 4;
    
    MySwingWorker mySwingWorker;
	SwingWrapper<XYChart> sw1;
	SwingWrapper<XYChart> sw2;
	SwingWrapper<XYChart> sw3;
	SwingWrapper<XYChart> sw4;
	
	XYChart healthyChart;
	XYChart infectedChart;
	XYChart immuneChart;
	XYChart deathChart;
	
	XChartPanel hP;
	XChartPanel infP;
	XChartPanel immP;
	XChartPanel dP;
	
	List<Integer> infected = new ArrayList<>();
	List<Integer> healthy = new ArrayList<>();
	List<Integer> recovered = new ArrayList<>();
	List<Integer> dead = new ArrayList<>();
	List<Integer> time = new ArrayList<>();

	double t = 0;
    private String areaChartName = "area_chart";
    private String quickChartName = "quick_chart";
    private String histogramName = "histogram";
    
    JFrame charsFrame;
    JPanel contentPanel; 
    
    public Charts(SPanel spanel) {
    	this.sp = spanel;
    	
    }
	
    
	public void go() {
	
	    // Create a matrix chart 
		List<XYChart> charts = new ArrayList<XYChart>();
		
		healthyChart = QuickChart.getChart("Number of healthy objects", "Healthy", "Infected", "Healthy", new double[] { 0 }, new double[] { 0 });
		healthyChart.getStyler().setLegendVisible(false);
		healthyChart.getStyler().setXAxisTicksVisible(false);	
		healthyChart.getSeriesMap().get("Healthy").setLineColor(Color.ORANGE);
		
		
	    infectedChart = QuickChart.getChart("Number of infected objects", "Infected", "Time", "Infected", new double[] { 0 }, new double[] { 0 });
	    infectedChart.getStyler().setLegendVisible(false);
	    infectedChart.getStyler().setXAxisTicksVisible(false);	
	    infectedChart.getSeriesMap().get("Infected").setLineColor(Color.GREEN);
	    
	    immuneChart = QuickChart.getChart("Number of immune objects", "Immune", "Time", "Immune", new double[] { 0 }, new double[] { 0 });
	    immuneChart.getStyler().setLegendVisible(false);
	    immuneChart.getStyler().setXAxisTicksVisible(false);	
	    immuneChart.getSeriesMap().get("Immune").setLineColor(Color.BLUE);
	
	    deathChart = QuickChart.getChart("Number of dead objects", "Dead", "Time", "Dead", new double[] { 0 }, new double[] { 0 });
	    deathChart.getStyler().setLegendVisible(false);
	    deathChart.getStyler().setXAxisTicksVisible(false);	
	    deathChart.getSeriesMap().get("Dead").setLineColor(Color.BLACK);
	    
	    
	    
	    charsFrame = new JFrame("Графики");
	    charsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    charsFrame.setSize(750, 750);
	    charsFrame.setLayout(new GridLayout(2, 2)); // 2 ROWS 2 COLS

	    
	    // CREATE PANELS 
	    hP = new XChartPanel<>(healthyChart); // Healthy Panel
	    infP = new XChartPanel<>(infectedChart); // Infected Panel
	    immP = new XChartPanel<>(immuneChart); // Immune Panel
	    dP = new XChartPanel<>(deathChart);  // Dead Panel


	    // ADDING PANELS
	    charsFrame.add(hP);
	    charsFrame.add(infP);
	    charsFrame.add(immP);
	    charsFrame.add(dP);

	    charsFrame.setVisible(true);

	    
	    
	    mySwingWorker = new MySwingWorker();
	    mySwingWorker.execute();
	    charsFrame.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	            if (mySwingWorker != null && !mySwingWorker.isDone()) {
	                mySwingWorker.cancel(true);  
	                
	            }
	        }
	    });
	    
    
  }
  
  	public XYChart areaChart (List<Double> healthyY, List<Double> infectedY, List<Double> immuneY, List<Double> deadY, List<Double> time2) {
	  
	// Create Chart
	    XYChart chart = new XYChartBuilder().width(800).height(600).title(getClass().getSimpleName()).xAxisTitle("X").yAxisTitle("Y").build();
	 
	    // Customize Chart
	    chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
	    chart.getStyler().setAxisTitlesVisible(false);
	    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
	    
	    // Series
	    System.out.println("LENGTH OF TIME" + time2.size() + "  LENGTH OF HEALTHY " + healthyY.size() + " LENGTH OF INFECTED " + infectedY.size() + " LENGTH OF IMMUNE " + immuneY.size());
	    
	    chart.addSeries("Healthy", time2, healthyY).setLineColor(Color.ORANGE).setFillColor(new Color(255, 165, 0, 100));
	    chart.addSeries("Infected", time2, infectedY).setLineColor(Color.RED).setFillColor(new Color(255, 0, 0, 100));
	    chart.addSeries("Immune", time2, immuneY).setLineColor(Color.BLUE).setFillColor(new Color(0, 0, 255, 100));
	    chart.addSeries("Dead", time2, deadY).setLineColor(Color.BLACK).setFillColor(new Color(0, 0, 0, 100));
	    
	  return chart;
			  
			 
  }
  
  	public XYChart quickChart(List<Double> healthyY, List<Double> infectedY, List<Double> immuneY, List<Double> deadY, List<Double> time2) {
	    
	    
	    XYChart chart = QuickChart.getChart(
	        "Simulation Progress",       
	        "Time",                      
	        "Population",               
	        "Healthy",                   
	        time2,                        
	        healthyY
	        
	    );
	    
	    
	    chart.getSeriesMap().get("Healthy").setLineColor(Color.ORANGE);
	    chart.addSeries("Infected", time2, infectedY).setMarker(new None()).setLineColor(Color.RED);
	    chart.addSeries("Immune", time2, immuneY).setMarker(new None()).setLineColor(Color.BLUE);
	    chart.addSeries("Dead", time2, deadY).setMarker(new None()).setLineColor(Color.BLACK);
	    
	    chart.getStyler().setAxisTitlesVisible(false);

	    chart.getStyler().setLegendVisible(true);

	    
	    return chart;
  }
  
  	public CategoryChart histogram(List<Double> healthyY, List<Double> infectedY, List<Double> immuneY,List<Double> deadY, List<Double> time2) {
  		CategoryChart chart = new CategoryChartBuilder()
  		        .width(800)
  		        .height(600)
  		        .title("Epidemic Progress")
  		        .xAxisTitle("Time")
  		        .yAxisTitle("Number of people")
  		        .build();

  		List<String> timeLabels = time2.stream()
  		    .map(t -> String.format("%.1f", t))
  		    .collect(Collectors.toList());

  		chart.addSeries("Infected", timeLabels, infectedY);
  		chart.addSeries("Immune", timeLabels, immuneY);
  		chart.addSeries("Healthy", timeLabels, healthyY);
  		chart.addSeries("Dead", timeLabels, deadY);
  		return chart;
  	}
  	

  	public void saveDataInCharts(List<double[]> dataPerSecond, String PathToSave) {
	  
	  List<Double> healthyY = new ArrayList<Double>();
	  
	  List<Double> infectedY = new ArrayList<Double>();
	  
	  List<Double> immuneY = new ArrayList<Double>();
	  
	  List<Double> deadY = new ArrayList<Double>();
	  
	  List<Double> time = new ArrayList<Double>();
	  
	  time.add(0.0d);
	  infectedY.add(1.0d);
	  immuneY.add(0.0d);
	  healthyY.add((double) sp.OBJECTS_NUM);
	  deadY.add(0.0d);
	  
	  double healthy;
	  double infected;
	  double immune;
	  double dead;
	  
	  
	  int i = 1;
	  for (double[] state : dataPerSecond) {
		  
		  healthy = state[0];
		  infected = state[1];
		  immune = state[2];
		  dead = state[3];
		  
		  healthyY.add(healthy);
		  infectedY.add(infected);
		  immuneY.add(immune);
		  deadY.add(dead);
		  
		  time.add((double) i);

		  
		  i++;
		  
	  }
	 
	  // CREATING CHARTS
	  XYChart areaChart = areaChart(healthyY, infectedY, immuneY, deadY, time);
	  XYChart quickChart = quickChart(healthyY, infectedY, immuneY, deadY, time);
	  CategoryChart histogram = histogram(healthyY, infectedY, immuneY, deadY, time);

	  
	 // Save to file
	    try {
	        BitmapEncoder.saveBitmap(areaChart, PathToSave + areaChartName, BitmapEncoder.BitmapFormat.PNG);
	        BitmapEncoder.saveBitmap(quickChart, PathToSave + quickChartName, BitmapEncoder.BitmapFormat.PNG);
	        BitmapEncoder.saveBitmap(histogram, PathToSave + histogramName, BitmapEncoder.BitmapFormat.PNG);
	        System.out.println("Charts saved to " + PathToSave);
	    } catch (IOException e) {
	        System.err.println("Failed to save chart to file.");
	        e.printStackTrace();
	    }
	    
	    
  }
  
  	private class MySwingWorker extends SwingWorker<Boolean, double[]> {
	
	    @Override
	    protected Boolean doInBackground() throws Exception {
	
	      while (!isCancelled()) {
	    	infectedNum = sp.infectedNum;
	    	healthyNum = sp.healthyNum;
	    	immuneNum = sp.immuneNum;
	    	deadNum = sp.deadNum;
	    	
	    	time.add((int)t++);
	        infected.add(infectedNum);
	        healthy.add(healthyNum);
	        recovered.add(immuneNum);
	        dead.add(deadNum);
	    	
	        
	        
	        healthyChart.updateXYSeries("Healthy", time, healthy, null);
	        infectedChart.updateXYSeries("Infected", time, infected, null);
	        immuneChart.updateXYSeries("Immune", time, recovered, null);
	        deathChart.updateXYSeries("Dead", time, dead, null);
	        
	        System.out.println("UPDATED");
	        // Перерисовываем интерфейс
	        hP.repaint();
	        infP.repaint();
	        immP.repaint();
	        dP.repaint();
            
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