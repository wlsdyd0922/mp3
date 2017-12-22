package Interface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

class MainUIwin extends JFrame {
	private JPanel bg = new JPanel(new GridLayout(3, 1));
	private JPanel buttonline = new JPanel(new FlowLayout());
	private JLabel la1 = new JLabel("mp3파일 이름 출력", JLabel.CENTER);
	private JLabel la2 = new JLabel("진행시간", JLabel.CENTER);
	private JLabel la3 = new JLabel("버튼모음", JLabel.CENTER);
	private JButton bt = new JButton("음악 목록");
	private JButton bt4 = new JButton("가사");
	private JButton bt1 = new JButton("prev");
	private JButton bt2 = new JButton("play");
	private JButton bt3 = new JButton("next");
	protected static boolean listflag = true;
	protected static boolean lyricflag = true;
	protected static int x;
	protected static int y;
	private JFrame f1 =null;
	private JFrame f2 = null;
	

	private void event() {
		ActionListener listClose = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listflag == false) {
					JFrame f1 = new Playlist();
					listflag = true;
				}
			}
		};
		bt.addActionListener(listClose);
		ActionListener lyricClose = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (lyricflag == false) {
					JFrame f2 = new Lyric();
					lyricflag = true;
				}
			}
		};
		bt4.addActionListener(lyricClose);

		ComponentListener cl = new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent arg0) {
				System.out.println("x = "+getX()+" y= " +getY());
				f1.setLocation(getX()+600, getY());
				f2.setLocation(getX(), getY()+400);
			}
		};
		addComponentListener(cl);

	}

	private void allClose() {
		WindowListener win = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		addWindowListener(win);
	}

	private void design() {
		Border mainBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "MP3플레이어");
		Border playInfo = BorderFactory.createLineBorder(Color.black, 2);
		Border lyrics = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "가사");
		Border playList = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2),
				"mp3 Play List");
		setContentPane(bg);
		bg.setBorder(mainBorder);
		bg.setBackground(Color.WHITE);
		bg.add(la1);
		bg.add(la2);
		bg.add(buttonline);
		buttonline.add(bt1);
		buttonline.add(bt2);
		buttonline.add(bt3);
		buttonline.add(bt);
		buttonline.add(bt4);
	}

	private void menu() {

	}

	public MainUIwin() {
		design();
		event();
		menu();
		setTitle("Playing");
		setSize(614, 408);
		setLocation(300, 100);
		setAlwaysOnTop(true);
		setVisible(true);
		f1 = new Playlist();
		f2 = new Lyric();
		
		allClose();
	}
}

public class MainUI {
	public static void main(String[] args) {
		JFrame f = new MainUIwin();

	}
}
