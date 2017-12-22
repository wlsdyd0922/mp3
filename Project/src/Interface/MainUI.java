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
	private JLabel la1 = new JLabel("mp3플레이 정보",JLabel.CENTER);
	private JLabel la2 = new JLabel("가사",JLabel.CENTER);
	private JLabel la3 = new JLabel("mp3플레이 리스트",JLabel.CENTER);
	private JList list = new JList();
	private String[] str = new String[] {"노래1","노래2","노래3","노래4"};
	
	private JScrollPane scroll = new JScrollPane();
	
	
	private void event() {
		System.out.println("12312321323");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		ListSelectionListener sel = new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				String str1 = (String) list.getSelectedValue();
				System.out.println(str1);
			}
		};
		list.addListSelectionListener(sel);
	}

	private void design() {
		Border mainBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,2), "MP3플레이어");
		Border playInfo = BorderFactory.createLineBorder(Color.black,2);
		Border lyrics = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,2), "가사");
		Border playList = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,2), "mp3 Play List");
		scroll.setViewportView(list);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		list.setListData(str); // 이름넣음
		setContentPane(bg);
		bg.setBorder(mainBorder);
		la1.setBorder(playInfo);
		la2.setBorder(lyrics);
		la3.setBorder(playList);
		//grid1.setBorder(bd);
		bg.add(grid);
		grid.add(grid1);
		grid.add(scroll);
		grid1.add(la1);
		grid1.add(la2);

		int a = list.getSelectedIndex();
		System.out.println(a);
	}

	private void menu() {

	}

	public Window01() {
		design();
		event();
		menu();
		setTitle("Swing예제 01");
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
		JFrame f1 = new Window01();
		JFrame f2 = new Window01();
	}
}
