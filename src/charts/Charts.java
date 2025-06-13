package charts;

// Импорт стандартных библиотек для работы с UI и событиями
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

import org.knowm.xchart.*; // Импорт классов библиотеки XChart
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.None;

import main.SPanel;

/**
 * Класс для отображения и сохранения графиков, связанных с эпидемиологической симуляцией.
 */
public class Charts {

	// Переменные для хранения чисел объектов в различных состояниях
	int infectedNum;
	int healthyNum;
	int immuneNum;
	int deadNum;

	SPanel sp; // Панель с данными от симуляции

	int numCharts = 4;

	// SwingWorker для асинхронного обновления графиков
	MySwingWorker mySwingWorker;

	// Обёртки для отображения графиков
	SwingWrapper<XYChart> sw1, sw2, sw3, sw4;

	// Графики по категориям
	XYChart healthyChart;
	XYChart infectedChart;
	XYChart immuneChart;
	XYChart deathChart;

	// Панели с графиками
	XChartPanel hP, infP, immP, dP;

	// Списки для хранения временных значений и данных графиков
	List<Integer> infected = new ArrayList<>();
	List<Integer> healthy = new ArrayList<>();
	List<Integer> recovered = new ArrayList<>();
	List<Integer> dead = new ArrayList<>();
	List<Integer> time = new ArrayList<>();

	double t = 0;

	// Имена сохраняемых графиков
	private String areaChartName = "area_chart";
	private String quickChartName = "quick_chart";
	private String histogramName = "histogram";

	// UI компоненты
	JFrame charsFrame;
	JPanel contentPanel;

	/**
	 * Конструктор принимает ссылку на панель симуляции
	 */
	public Charts(SPanel spanel) {
		this.sp = spanel;
	}

	/**
	 * Метод запуска построения и отображения графиков
	 */
	public void go() {

		// Создаём графики с начальными значениями
		healthyChart = QuickChart.getChart("Number of healthy objects", "Time", "Healthy", "Healthy", new double[] {0}, new double[] {0});
		healthyChart.getStyler().setLegendVisible(false);
		healthyChart.getStyler().setXAxisTicksVisible(false);
		healthyChart.getSeriesMap().get("Healthy").setLineColor(Color.ORANGE);

		infectedChart = QuickChart.getChart("Number of infected objects", "Time", "Infected", "Infected", new double[] {0}, new double[] {0});
		infectedChart.getStyler().setLegendVisible(false);
		infectedChart.getStyler().setXAxisTicksVisible(false);
		infectedChart.getSeriesMap().get("Infected").setLineColor(Color.GREEN);

		immuneChart = QuickChart.getChart("Number of immune objects", "Time", "Immune", "Immune", new double[] {0}, new double[] {0});
		immuneChart.getStyler().setLegendVisible(false);
		immuneChart.getStyler().setXAxisTicksVisible(false);
		immuneChart.getSeriesMap().get("Immune").setLineColor(Color.BLUE);

		deathChart = QuickChart.getChart("Number of dead objects", "Time", "Dead", "Dead", new double[] {0}, new double[] {0});
		deathChart.getStyler().setLegendVisible(false);
		deathChart.getStyler().setXAxisTicksVisible(false);
		deathChart.getSeriesMap().get("Dead").setLineColor(Color.BLACK);

		// Создаём окно с сеткой 2x2
		charsFrame = new JFrame("Графики");
		charsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		charsFrame.setSize(750, 750);
		charsFrame.setLayout(new GridLayout(2, 2));

		// Создаём панели для графиков
		hP = new XChartPanel<>(healthyChart);
		infP = new XChartPanel<>(infectedChart);
		immP = new XChartPanel<>(immuneChart);
		dP = new XChartPanel<>(deathChart);

		// Добавляем панели в окно
		charsFrame.add(hP);
		charsFrame.add(infP);
		charsFrame.add(immP);
		charsFrame.add(dP);

		charsFrame.setVisible(true);

		// Запускаем асинхронное обновление данных
		mySwingWorker = new MySwingWorker();
		mySwingWorker.execute();

		// Остановка обновления при закрытии окна
		charsFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (mySwingWorker != null && !mySwingWorker.isDone()) {
					mySwingWorker.cancel(true);
				}
			}
		});
	}

	/**
	 * Создаёт график с заливкой (Area Chart)
	 */
	public XYChart areaChart(List<Double> healthyY, List<Double> infectedY, List<Double> immuneY, List<Double> deadY, List<Double> time2) {

		XYChart chart = new XYChartBuilder().width(800).height(600).title(getClass().getSimpleName()).xAxisTitle("X").yAxisTitle("Y").build();
		chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
		chart.getStyler().setAxisTitlesVisible(false);
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

		chart.addSeries("Healthy", time2, healthyY).setLineColor(Color.ORANGE).setFillColor(new Color(255, 165, 0, 100));
		chart.addSeries("Infected", time2, infectedY).setLineColor(Color.RED).setFillColor(new Color(255, 0, 0, 100));
		chart.addSeries("Immune", time2, immuneY).setLineColor(Color.BLUE).setFillColor(new Color(0, 0, 255, 100));
		chart.addSeries("Dead", time2, deadY).setLineColor(Color.BLACK).setFillColor(new Color(0, 0, 0, 100));

		return chart;
	}

	/**
	 * Создаёт обычный линейный график
	 */
	public XYChart quickChart(List<Double> healthyY, List<Double> infectedY, List<Double> immuneY, List<Double> deadY, List<Double> time2) {
		XYChart chart = QuickChart.getChart("Simulation Progress", "Time", "Population", "Healthy", time2, healthyY);

		chart.getSeriesMap().get("Healthy").setLineColor(Color.ORANGE);
		chart.addSeries("Infected", time2, infectedY).setMarker(new None()).setLineColor(Color.RED);
		chart.addSeries("Immune", time2, immuneY).setMarker(new None()).setLineColor(Color.BLUE);
		chart.addSeries("Dead", time2, deadY).setMarker(new None()).setLineColor(Color.BLACK);

		chart.getStyler().setAxisTitlesVisible(false);
		chart.getStyler().setLegendVisible(true);

		return chart;
	}

	/**
	 * Гистограмма (столбчатая диаграмма) по состояниям
	 */
	public CategoryChart histogram(List<Double> healthyY, List<Double> infectedY, List<Double> immuneY, List<Double> deadY, List<Double> time2) {
		CategoryChart chart = new CategoryChartBuilder()
				.width(800)
				.height(600)
				.title("Epidemic Progress")
				.xAxisTitle("Time")
				.yAxisTitle("Number of people")
				.build();

		// Преобразуем метки времени в строки
		List<String> timeLabels = time2.stream().map(t -> String.format("%.1f", t)).collect(Collectors.toList());

		chart.addSeries("Infected", timeLabels, infectedY);
		chart.addSeries("Immune", timeLabels, immuneY);
		chart.addSeries("Healthy", timeLabels, healthyY);
		chart.addSeries("Dead", timeLabels, deadY);

		return chart;
	}

	/**
	 * Метод для сохранения графиков в PNG-файлы
	 */
	public void saveDataInCharts(List<double[]> dataPerSecond, String PathToSave) {
		List<Double> healthyY = new ArrayList<>();
		List<Double> infectedY = new ArrayList<>();
		List<Double> immuneY = new ArrayList<>();
		List<Double> deadY = new ArrayList<>();
		List<Double> time = new ArrayList<>();

		// Инициализация начального состояния
		time.add(0.0);
		infectedY.add(1.0);
		immuneY.add(0.0);
		healthyY.add((double) sp.OBJECTS_NUM);
		deadY.add(0.0);

		int i = 1;
		for (double[] state : dataPerSecond) {
			healthyY.add(state[0]);
			infectedY.add(state[1]);
			immuneY.add(state[2]);
			deadY.add(state[3]);
			time.add((double) i++);
		}

		// Построение и сохранение графиков
		XYChart areaChart = areaChart(healthyY, infectedY, immuneY, deadY, time);
		XYChart quickChart = quickChart(healthyY, infectedY, immuneY, deadY, time);
		CategoryChart histogram = histogram(healthyY, infectedY, immuneY, deadY, time);

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

	/**
	 * Внутренний класс SwingWorker для фона обновления графиков в реальном времени
	 */
	private class MySwingWorker extends SwingWorker<Boolean, double[]> {

		@Override
		protected Boolean doInBackground() throws Exception {
			while (!isCancelled()) {
				// Получаем данные из симуляции
				infectedNum = sp.infectedNum;
				healthyNum = sp.healthyNum;
				immuneNum = sp.immuneNum;
				deadNum = sp.deadNum;

				// Добавляем данные во временные ряды
				time.add((int) t++);
				infected.add(infectedNum);
				healthy.add(healthyNum);
				recovered.add(immuneNum);
				dead.add(deadNum);

				// Обновляем графики
				healthyChart.updateXYSeries("Healthy", time, healthy, null);
				infectedChart.updateXYSeries("Infected", time, infected, null);
				immuneChart.updateXYSeries("Immune", time, recovered, null);
				deathChart.updateXYSeries("Dead", time, dead, null);

				// Перерисовываем панели
				hP.repaint();
				infP.repaint();
				immP.repaint();
				dP.repaint();

				// Пауза между обновлениями
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
