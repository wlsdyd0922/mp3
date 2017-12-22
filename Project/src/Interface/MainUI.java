package Interface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

class MainUIwin extends JFrame {
	private JPanel bg = new JPanel(new GridLayout(3, 1));
	private JPanel buttonline = new JPanel(new BorderLayout());
	private JPanel buttonlineC = new JPanel(new FlowLayout(5,5,5));
	private JLabel la1 = new JLabel("mp3파일 이름 출력", JLabel.CENTER);
	private JLabel la2 = new JLabel("진행시간", JLabel.CENTER);
	private JButton bt = new JButton("List");
	private JButton bt4 = new JButton("가사");
	private JButton bt1 = new JButton("Prev");
	private JButton bt2 = new JButton("Play");
	private JButton bt3 = new JButton("Next");
	private JButton bt5 = new JButton("한곡 반복");
	private JButton bt6 = new JButton("전체 반복");
	private JButton bt7 = new JButton("Random");

	protected static int x;
	protected static int y;
	private JFrame playList = null;
	private JFrame lyric = null;

	private void event() {
		ActionListener listBt = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				playList.setLocation(getX() + 600, getY());
				playList.setVisible(true);
			}
		};

		bt.addActionListener(listBt);

		ActionListener lyricbt = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lyric.setLocation(getX(), getY() + 400);
				lyric.setVisible(true);
			}
		};
		bt4.addActionListener(lyricbt);

		ComponentListener cl = new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent arg0) {
				if (playList != null) {
					playList.setLocation(getX() + 600, getY());
				}
				if (lyric != null) {
					lyric.setLocation(getX(), getY() + 400);
				}
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
		setContentPane(bg);
		bg.setBorder(mainBorder);
		bg.setBackground(Color.WHITE);
		buttonline.setBackground(Color.white);
		bg.add(la1);
		bg.add(la2);
		bg.add(buttonline);
		buttonline.add(buttonlineC,BorderLayout.SOUTH);
		
		buttonlineC.add(bt1);
		buttonlineC.add(bt2);
		buttonlineC.add(bt3);
		buttonlineC.add(bt5);
		buttonlineC.add(bt6);
		buttonlineC.add(bt7);
		
		buttonlineC.add(bt);
		buttonlineC.add(bt4);
		
	}

	private void menu() {
	}

	public MainUIwin() {
		design();
		event();
		menu();
		setTitle("Playing");
		setSize(615, 408);
		setLocation(300, 100);
		setAlwaysOnTop(true);
		setVisible(true);
		setResizable(false);
		playList = new Playlist();
		lyric = new Lyric();
		allClose();
	}
}

public class MainUI {
	public static void main(String[] args) {
		JFrame mainUi = new MainUIwin();
	}
}
