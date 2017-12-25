package Interface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

import javafx.stage.FileChooser;

class MainUIwin extends JFrame {
	private JFileChooser chooser = new JFileChooser();
	private JPanel bg = new JPanel(new GridLayout(3, 1));
	private JPanel buttonline = new JPanel(null);
	private JLabel la1 = new JLabel("mp3파일 이름 출력", JLabel.CENTER);
	private JLabel la2 = new JLabel("진행시간", JLabel.CENTER);
	private String[] str = new String[] { "◀◀", "▶■", "▶▶", "반복", "Random", "All", "가사", "≡" };
	private JButton[] bt = new JButton[8];
	private JMenuBar bar = new JMenuBar();
	private JMenu menu = new JMenu("File");
	private JMenuItem open = new JMenuItem("Open");
	
	protected static int x;
	protected static int y;
	private JFrame playList = null;
	private JFrame lyric = null;

	private void event() {
		ActionListener fileOpen = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chooser.setMultiSelectionEnabled(true);
				chooser.showOpenDialog(bg);
			}
		};
		open.addActionListener(fileOpen);
		
		
		ActionListener listBt = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				playList.setLocation(getX() + 600, getY());
				playList.setVisible(true);
			}
		};

		bt[7].addActionListener(listBt);

		ActionListener lyricbt = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lyric.setLocation(getX(), getY() + 400);
				lyric.setVisible(true);
			}
		};
		bt[6].addActionListener(lyricbt);

		ComponentListener cl = new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent arg0) {
				if (playList != null) {
					playList.setLocation(getX() + 600, getY());
				}
				if (lyric != null) {
					lyric.setLocation(getX() - 5, getY() + 400);
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
		for (int i = 0; i < bt.length; i++) {
			bt[i] = new JButton(str[i]);
			buttonline.add(bt[i]);
			bt[i].setBackground(Color.WHITE);
		}
		
		bt[0].setBounds(10, 10, 80, 40);
		bt[1].setBounds(110, 10, 80, 40);
		bt[2].setBounds(210, 10, 80, 40);
		bt[3].setBounds(355, 10, 60, 40);
		bt[4].setBounds(420, 10, 90, 40);
		bt[5].setBounds(515, 10, 60, 40);
		bt[6].setBounds(10, 60, 60, 40);
		bt[7].setBounds(515, 60, 60, 40);
		bt[7].setFont(new Font("굴림", Font.PLAIN, 30));
		Border mainBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "MP3플레이어");
		Border btBorder = BorderFactory.createLineBorder(Color.black, 3);
		setContentPane(bg);
		bg.setBorder(mainBorder);
		bg.setBackground(Color.WHITE);
		buttonline.setBackground(Color.white);
		buttonline.setBorder(btBorder);
		bg.add(la1);
		bg.add(la2);
		bg.add(buttonline);
	}

	private void menu() {
		setJMenuBar(bar);
		bar.add(menu);
		menu.add(open);
	}

	public MainUIwin() {
		design();
		event();
		menu();
		setTitle("Playing");
		setSize(610, 405);
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
