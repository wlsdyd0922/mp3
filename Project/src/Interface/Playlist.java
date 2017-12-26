package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

//플레이 리스트
public class Playlist extends JFrame{
	private JPanel bg = new JPanel(new BorderLayout());
	private JList<String> musicList = new JList<>(); 
	private JScrollPane scroll = new JScrollPane();
	private String[] str = new String[] {"노래1","노래2","노래3","노래4"};
	
	private void menu() {
	}

	private void event() {
		WindowListener win = new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
			}
		};
		addWindowListener(win);
	}

	private void design() {
		Border playList = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,2), "mp3 Play List");
		scroll.setViewportView(musicList);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setContentPane(bg);
		musicList.setListData(str);
		bg.add(scroll);
	}
	
	public Playlist() {
		design();
		event();
		menu();
		setTitle("Play List");
		setSize(300, 600);
		setLocation(900, 100);
		setAlwaysOnTop(true);
		setVisible(true);
	}
}