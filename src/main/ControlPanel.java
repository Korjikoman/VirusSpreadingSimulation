package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.function.BiFunction;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import charts.Charts;

public class ControlPanel extends JFrame implements ActionListener {
	
	// SPanel SETTINGS
    private SPanel sp;
    private JFrame frame;
    private static String TITLE = "Some title";
    public static String chartsPath = System.getProperty("user.dir") + "\\res\\charts\\";
    
    
    // BUTTONS
    private JButton btnStart;
    private JButton btnSettings;
    private JButton btnStop;
    private JButton btnResume;
    private JButton btnCharts;
    private JButton btnSaved;
    private JButton btnApply;
    private JButton btnReset;
    
    // 
    int chartsPressed = 0;
    
    // SETTINGS DATA /////////////////////////////////////////
    private JFrame settingsFrame;
    
    // sliders
    JSlider slCount;
    JSlider slImmune;
    JSlider slInfected;
    JSlider slSpeed;
    JSlider slInfectionChance;
    JSlider slReinfectionChance;
    JSlider slMortality;
    JSlider slVaccineEffectiveness;
    JSlider slSimulationTime;
    
    
    // checkbox 
    JCheckBox cbBorders;

    JCheckBox cbFullIsolation;
    
    //////////////////
    
    int objectsNum;
    double objectsVelocity;
    int mapNum = 1;
  

    public ControlPanel() {
    
        // Frame settings
        setTitle("Панель управления");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 500);
        setLayout(null);
        setLocationRelativeTo(null);

        // Initialize buttons
        btnStart = createButton("Начать симуляцию", 50, 20, 200, 50);
        btnSettings = createButton("Настройки", 50, 90, 200, 50);
        btnStop = createButton("Стоп", 50, 160, 200, 50);
        btnResume = createButton("Возобновить", 50, 160, 200, 50);
        btnResume.setVisible(false);
        btnResume.setEnabled(false);
        btnCharts = createButton("График-момент.", 50, 230, 200, 50);
        btnSaved = createButton("Сохр. графики", 50, 300, 200, 50);
        btnReset = createButton("Сброс", 50, 370, 200, 50);
      

        setVisible(true);
        

        restart(0);
        
    }
    
    private void restart(int mode) {
    	if (mode == 1) {
    		frame.getContentPane().removeAll();
    		frame.revalidate();
    		sp.stopSimulation();
    		sp.removeAll();
    		sp.revalidate();
    		sp.repaint();
    	}
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setTitle(TITLE);
        
        
        // ADDING SIMULATION PANEL
        sp = new SPanel();    	
    }
    
    private void createWorld() {
        frame.add(sp);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        sp.startThread();

    }

    private JButton createButton(String text, int x, int y, int w, int h) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setForeground(Color.BLACK);
        btn.setBackground(new Color(200, 220, 255)); // мягкий синий
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(btn);
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSettings) {
            openSettingsWindow();
        }
        else if (e.getSource() == btnStart) {
        
        	// CREATING NEW WORLD ///////////////////////////
            
        	createWorld();
            
            ////////////////////////////////////////////////////
            
        	btnSettings.setEnabled(false);
        	btnStart.setEnabled(false);
        	
        }
        else if (e.getSource()  == btnStop) {
        	
        	// CHANGE STOP TO RESUME
        	btnStop.setEnabled(false);
        	btnStop.setVisible(false);
        	btnResume.setEnabled(true);
        	btnResume.setVisible(true);
        	///////////////////////////
        	try {
        		sp.stopSimulation();	
        	}
        	catch (NullPointerException ex) {
        		System.out.println("START THE SIMULATION!");
        	}
        	
        	System.out.println("STOP");
        }
        else if (e.getSource()  == btnResume) {
        	
        	// CHANGE RESUME TO STOP
        	btnResume.setEnabled(false);
        	btnResume.setVisible(false);
        	btnStop.setEnabled(true);
        	btnStop.setVisible(true);
        	///////////////////////
        	try {
        		sp.runSimulation();
        	}
        	catch (NullPointerException ex) {
        		System.out.println("START THE SIMULATION!");
        	}
        	System.out.println("RESUME");
        	
        	
        }
        else if (e.getSource() == btnReset) {
        	try {
        	restart(1);
        	btnSettings.setEnabled(true);
        	btnStart.setEnabled(true);
        	}catch(NullPointerException ex) { 
        		System.out.println("START THE SIMULATION!");
        	}
    
        	
        }
        else if (e.getSource() == btnCharts) {
        	
            sp.charts = new Charts(sp);
            sp.charts.go();

        	
        }
        else if (e.getSource() == btnSaved) {
        	Desktop d = null;
            File file = new File(chartsPath);
            
            if (Desktop.isDesktopSupported()) {
            	d = Desktop.getDesktop();
            	
            }
            try {
            	d.open(file);
            }
            catch(IOException ex) {
            	System.out.println(ex);
            }
        }
        
        
        
        else if (e.getSource() == btnApply) {
            System.out.println("CHANGES APPLIED");
            
            recreateSPanel();
            
            System.out.println("WORLD RECREATED");
            
            settingsFrame.dispose();
        }
        
        
    }

    // SETTINGS WINDOW
    private void openSettingsWindow() {
        settingsFrame = new JFrame("Настройки");
        settingsFrame.setSize(450, 600);
        settingsFrame.setLocationRelativeTo(this);
        settingsFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2, true));

        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font titleFont = new Font("Arial", Font.BOLD, 16);

        int[] y = {20};
        int labelWidth = 220;
        int sliderWidth = 180;

        // Utility to create a labeled slider
        BiFunction<String, int[], JSlider> createSlider = (text, range) -> {
            JLabel label = new JLabel(text);
            label.setFont(labelFont);
            label.setBounds(20, y[0], labelWidth, 25);
            panel.add(label);

            JSlider slider = new JSlider(range[0], range[1], range[2]);
            slider.setBounds(240, y[0], sliderWidth, 40);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.setMajorTickSpacing((range[1] - range[0]) / 2);
            slider.setMinorTickSpacing((range[1] - range[0]) / 4);
            slider.setBackground(panel.getBackground());
            slider.setFocusable(false);
            panel.add(slider);

            y[0] += 50;
            return slider;
        };

        // SLIDERS
        slCount = createSlider.apply("Здоровых людей", new int[]{1, 100, 10});
        slImmune = createSlider.apply("Людей с иммунитетом", new int[]{1, 100, 5});
        slInfected = createSlider.apply("Больных людей", new int[]{1, 100, 2});
        slSpeed = createSlider.apply("Скорость", new int[]{0, 10, 3});
        slInfectionChance = createSlider.apply("Вероятность заражения (%)", new int[]{0, 100, 50});
        slReinfectionChance = createSlider.apply("Вероятность повторного заражения (%)", new int[]{0, 100, 10});
        slMortality = createSlider.apply("Смертность (%)", new int[]{0, 100, 5});
        slVaccineEffectiveness = createSlider.apply("Эффективность вакцины (%)", new int[]{0, 100, 70});
        slSimulationTime = createSlider.apply("Длительность симуляции, с", new int[]{5, 40, 10});
        // CHECKBOX
        cbBorders = new JCheckBox("Частичная изоляция");
        cbBorders.setBounds(20, y[0], 180, 25);
        cbBorders.setBackground(panel.getBackground());
        cbBorders.setFont(labelFont);
        panel.add(cbBorders);

        cbFullIsolation = new JCheckBox("Полная изоляция");
        cbFullIsolation.setBounds(220, y[0], 180, 25);
        cbFullIsolation.setBackground(panel.getBackground());
        cbFullIsolation.setFont(labelFont);
        panel.add(cbFullIsolation);

        y[0] += 50;

        // APPLY BUTTON
        btnApply = new JButton("ОК");
        btnApply.setBounds(100, y[0], 250, 35);
        btnApply.setFont(titleFont);
        btnApply.setBackground(new Color(100, 150, 255));
        btnApply.setForeground(Color.WHITE);
        btnApply.setFocusPainted(false);
        btnApply.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2, true));
        btnApply.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnApply.addActionListener(this);
        panel.add(btnApply);

        panel.setBounds(10, 10, 420, y[0] + 60);
        settingsFrame.setContentPane(panel);
        settingsFrame.setVisible(true);
    }

    
    // RECREATING PANEL WITH SIMULATION
    private void recreateSPanel() {
 
    	sp.OBJECTS_NUM = (int)slCount.getValue();
        sp.objectsVelocity = (double) slSpeed.getValue();
        sp.simulationTIME = ((int) slSimulationTime.getValue() - 1) * 1000;
        sp.VACCINE_EFFICIENCY = (double)slVaccineEffectiveness.getValue() ;
        sp.IMMUNE_NUM = slImmune.getValue();
        sp.INFECTED_NUM = slInfected.getValue();
        sp.INFECTED_PROBABILITY = (double) slInfectionChance.getValue() ; 
        sp.REINFECTION_PROBABILITY = (double) slReinfectionChance.getValue();
        sp.MORALITY = (double) slMortality.getValue();
        
        if (cbBorders.isSelected() && !cbFullIsolation.isSelected()) {
        	sp.mapFilePath = "/maps/map2.txt";
        	
        }
        else if(!cbBorders.isSelected() && cbFullIsolation.isSelected()) {
        	sp.mapFilePath = "/maps/map3.txt";
        }

    }
}
