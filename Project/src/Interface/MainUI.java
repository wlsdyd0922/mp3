package Interface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class Window01 extends JFrame {
	private JPanel bg = new JPanel(new BorderLayout());
	private JPanel grid = new JPanel(new GridLayout(1, 2));
	private JPanel grid1 = new JPanel(new GridLayout(2, 1));
	
	private void event() {
		
	}

	private void design() {
		setContentPane(bg);
		bg.add(grid);
		grid.add(grid1);
		grid.add
	}

	private void menu() {

	}

	public Window01() {
		design();
		event();
		menu();
		setTitle("Swing¿¹Á¦ 01");
		setSize(800, 600);
		setLocationByPlatform(true);
		setAlwaysOnTop(true);
		setResizable(false);
		setVisible(true);
	}
}

public class MainUI {
	public static void main(String[] args) {
		JFrame f = new Window01();
	}
}
