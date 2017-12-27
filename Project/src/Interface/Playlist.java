package Interface;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;

//플레이 리스트
public class Playlist extends JFrame{
	private JPanel bg = new JPanel(new BorderLayout());
	private JList<String> musicList = new JList<>(); 
	private JScrollPane scroll = new JScrollPane();
	private String[] str = new String[] {"노래1","노래2","노래3","노래4"};
	private JButton bt = new JButton("검색");
	private JFrame search= new Search();
	
	private void menu() {
	}

	private void event() {
		WindowListener win = new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
			}
		};
		addWindowListener(win);
		bt.addActionListener(e->{
			search.setVisible(true);
		});
	}

	private void design() {
		Border playList = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,2), "mp3 Play List");
		scroll.setViewportView(musicList);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setContentPane(bg);
		musicList.setListData(str);
		bg.add(scroll,BorderLayout.CENTER);
		bg.add(bt,BorderLayout.SOUTH);
	}
	
	public Playlist() {
		design();
		event();
		setTitle("Play List");
		setSize(300, 600);
		setLocation(900, 100);
		setVisible(true);
	}
}