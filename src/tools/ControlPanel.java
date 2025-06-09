package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import charts.Charts;
import main.MainScreen;
import main.SPanel;

public class ControlPanel extends JFrame implements ActionListener {
	
	// SPanel SETTINGS
    private SPanel sp;
    private JFrame frame;
    private static String TITLE = "Some title";
	
    // BUTTONS
    private JButton btnStart;
    private JButton btnSettings;
    private JButton btnStop;
    private JButton btnCharts;
    private JButton btnReset;
    private JButton btnApply;
    
    // SETTINGS DATA
    private JFrame settingsFrame;
    JSpinner spCount; // OBJECTS NUMBER
    JSpinner spSpeed; // OBJECTS SPEED
    JCheckBox cbBorders; // BORDERS
    
    int objectsNum;
    double objectsVelocity;
    int mapNum = 1;
  

    public ControlPanel() {
    
        // Frame settings
        setTitle("Панель управления");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        // Initialize buttons
        btnStart = createButton("Начать симуляцию", 50, 20, 200, 50);
        btnSettings = createButton("Настройки", 50, 90, 200, 50);
        btnStop = createButton("Стоп", 50, 160, 200, 50);
        btnCharts = createButton("График-момент.", 50, 230, 200, 50);
        btnReset = createButton("Сброс", 50, 300, 200, 50);

      

        setVisible(true);
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setTitle(TITLE);
        
        
        // ADDING SIMULATION PANEL
        sp = new SPanel();
       
        
    }

    private JButton createButton(String text, int x, int y, int w, int h) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
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
            
            
            
            frame.add(sp);
            frame.pack();

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
           ////////////////////////////////////////////////////
        	
        	sp.startThread();
        	btnSettings.setEnabled(false);
        }
        else if (e.getSource()  == btnStop) {
        	sp.stopSimulation();
        }
        else if (e.getSource() == btnCharts) {
        	// CREATING CHARTS
        	if (!sp.startCharts) {
                sp.startCharts = true; 
                new Thread(() -> {
                    sp.charts = new Charts(sp);
                    sp.charts.go();
                }).start(); 
            }
        }
        else if (e.getSource() == btnReset) {
            // DO SMTH
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
        settingsFrame.setSize(400, 300);
        settingsFrame.setLayout(null);
        settingsFrame.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 400, 300);

        // NUMBER OF OBJECTS
        JLabel lblCount = new JLabel("Количество объектов:");
        lblCount.setBounds(20, 20, 150, 25);
        spCount = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        spCount.setBounds(180, 20, 60, 25);
        panel.add(lblCount);
        panel.add(spCount);
        
        

        // OBJECTS' VELOCITY
        JLabel lblSpeed = new JLabel("Скорость:");
        lblSpeed.setBounds(20, 60, 150, 25);
        spSpeed = new JSpinner(new SpinnerNumberModel(3.0, 0.0, 7.0, 0.1));
        spSpeed.setBounds(180, 60, 60, 25);
        panel.add(lblSpeed);
        panel.add(spSpeed);
        
       
        
        // BORDERS
        cbBorders = new JCheckBox("Частичная изоляция");
        cbBorders.setBounds(20, 100, 200, 25);
        panel.add(cbBorders);
        
        

        
        settingsFrame.add(panel);
        settingsFrame.setVisible(true);
        
        // APPLY CHANGES
        btnApply = new JButton("Применить изменения");
        btnApply.setBounds(20, 140, 200, 25);
        btnApply.setFont(new Font("Arial", Font.BOLD, 16));
        btnApply.setFocusable(false);
        btnApply.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        panel.add(btnApply);
        btnApply.addActionListener(this);
    }
    
    // RECREATING PANEL WITH SIMULATION
    private void recreateSPanel() {
 
    	sp.OBJECTS_NUM = (int)spCount.getValue();
        sp.objectsVelocity = (double) spSpeed.getValue();
        if (cbBorders.isSelected()) {
        	sp.mapFilePath = "/maps/map2.txt";
        	
        }


    }
}
