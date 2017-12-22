package Interface;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;
import javax.swing.border.Border;

public class Lyric extends JFrame {
	private JPanel bg = new JPanel(new BorderLayout());
	private JLabel la = new JLabel("가사",JLabel.CENTER);
	
	
	
	private void event() {
		WindowListener win = new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				MainUIwin.lyricflag = false;
			}
		};
		addWindowListener(win);
	}
	
	private void menu() {
		
	}

	private void design() {
		Border lyrics = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,2), "가사");
		setContentPane(bg);
		bg.add(la);
		bg.setBorder(lyrics);
		bg.setBackground(Color.WHITE);
	}

	public Lyric() {
		design();
		event();
		menu();
		setTitle("가사");
		setSize(600, 200);
		setLocation(300, 500);
		setAlwaysOnTop(true);
		setVisible(true);
	}
}

