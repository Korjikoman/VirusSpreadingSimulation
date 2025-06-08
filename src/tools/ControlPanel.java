package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import main.SPanel;

public class ControlPanel extends JFrame implements ActionListener{
	SPanel sp;
	int startButtonX = 100;
	int startButtonY = 100;
	int startButtonWidth = 100;
	int startButtonHeight = 50;
	
	// DECLARE BUTTONS
	JButton button;
	
	
	public ControlPanel() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setSize(300, 600);
		this.setVisible(true);
		
		button = new JButton();
		button.setBounds(50, 50, 150,50);
		this.add(button);
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button ) {
			
		}
		
	}
	
	
}
