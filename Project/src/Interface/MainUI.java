package Interface;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;

import javafx.stage.FileChooser;

class MainUIwin extends JFrame {
	final static int LOGIN = 0; // 로그인 요청
	final static int JOIN = 1; // 회원 가입
	final static int LIST = 2; // 로그인 성공 후 리스트 요청
	final static int MUSIC = 3; // 개인 리스트 요청
	final static int DROP = 4; // 탈퇴
	final static int LOGOUT = 5; // 로그아웃
	final static int TOTAL_LIST = 6;

	private JFileChooser chooser = new JFileChooser();
	private JPanel bg1 = new JPanel(null);
	private JPanel bg = new JPanel(new GridLayout(4, 1));
	private JPanel lyricline = new JPanel(new BorderLayout());
	private JPanel buttonline = new JPanel(null);
	private JPanel titleLine = new JPanel(null);
	private JPanel scrollLine = new JPanel(new BorderLayout());

	protected static JList<String> musicList = new JList<>(new DefaultListModel<>());
	private JScrollPane scroll = new JScrollPane();
	private String[] str1 = new String[] { "노래1", "노래2", "노래3", "노래4" };

	private JLabel la1 = new JLabel("mp3파일 이름 출력", JLabel.LEFT);
	private JLabel la2 = new JLabel("진행시간", JLabel.CENTER);
	private JLabel la3 = new JLabel("가사", JLabel.CENTER);

	private String[] str = new String[] { "◀◀", "▶■", "▶▶", "반복", "Random", "All", };
	private JButton[] bt = new JButton[6];
	private JButton bt1 = new JButton("로그인");
	private JButton bt2 = new JButton("회원가입");
	private JButton bt3 = new JButton("서버음악검색");

	private JMenuBar bar = new JMenuBar();
	private JMenu menu = new JMenu("File");
	private JMenu menu2 = new JMenu("Option");
	private JMenuItem open = new JMenuItem("Open");

	protected static int x;
	protected static int y;
	private JFrame search = null;
	private LoginDialog login = new LoginDialog(this);
	private SignUpDialog signup = new SignUpDialog(this);

	private void event() {
		WindowListener win = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
		};
		addWindowListener(win);

		KeyAdapter listdelete = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					DefaultListModel<String> model = new DefaultListModel<>();
					for (int i = 0; i < musicList.getModel().getSize(); i++) {
						model.addElement(MainUIwin.musicList.getModel().getElementAt(i));
					}

					for (int i = 0; i < musicList.getSelectedValuesList().size(); i++) {
						String str = musicList.getSelectedValuesList().get(i);
						model.removeElement(str);
					}
					musicList.setModel(model);

				}
			}
		};
		musicList.addKeyListener(listdelete);

		KeyAdapter playmusicenter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (musicList.getSelectedValue() != null) {
						System.out.println(musicList.getSelectedValue() + "실행");
					}
				}
			}
		};
		musicList.addKeyListener(playmusicenter);

		MouseAdapter playMusicMou = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (musicList.getSelectedValue() != null) {
						System.out.println(musicList.getSelectedValue() + "실행");
					}
				}
			}
		};
		musicList.addMouseListener(playMusicMou);

		bt1.addActionListener(e -> {
			login.setVisible(true);
		});

		bt2.addActionListener(e -> {
			signup.setVisible(true);
		});

		open.addActionListener(e -> {
			chooser.setMultiSelectionEnabled(true);
			chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("mp3 File (*.mp3)", ".mp3"));
			chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("mpeg File (*.mpeg)", ".mpeg"));
			chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("wav File (*.wav)", ".wav"));
			chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("wma File (*.wma)", ".wma"));
			chooser.showOpenDialog(bg);
		});

		bt3.addActionListener(e -> {
			search.setVisible(true);
		});
	}

	private void design() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) d.getWidth() / 5 * 2 - 40;
		int y = (int) d.getHeight() / 5 * 3 - 4;
		int xp = (int) d.getWidth() / 5 - 30;
		setContentPane(bg1);
		bg1.add(bg, BorderLayout.CENTER);
		bg.setBounds(0, 0, x, y);

		scroll.setViewportView(musicList);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		musicList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		bg1.add(scrollLine);
		scrollLine.setBounds(x + 1, 0, xp - 7, y);
		scrollLine.add(scroll, BorderLayout.CENTER);
		scrollLine.add(bt3, BorderLayout.SOUTH);
		musicList.setListData(str1);

		bg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "MP3플레이어"));

		buttonline.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		titleLine.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

		bg.setBackground(Color.WHITE);
		buttonline.setBackground(Color.white);
		titleLine.setBackground(Color.white);

		bg.add(titleLine);
		titleLine.add(la1);
		titleLine.add(bt1);
		titleLine.add(bt2);

		bg.add(la2);
		bg.add(buttonline);

		for (int i = 0; i < bt.length; i++) {
			bt[i] = new JButton(str[i]);
			buttonline.add(bt[i]);
			bt[i].setBackground(Color.WHITE);
		}
		bt1.setBackground(Color.white);
		bt2.setBackground(Color.white);
		bt[0].setBounds(10, 10, 80, 40);
		bt[1].setBounds(110, 10, 80, 40);
		bt[2].setBounds(210, 10, 80, 40);
		bt[3].setBounds(355, 10, 60, 40);
		bt[4].setBounds(420, 10, 90, 40);
		bt[5].setBounds(515, 10, 60, 40);
		la1.setBounds(10, 10, 400, 20);
		bt1.setBounds(495, 10, 80, 40);
		bt2.setBounds(490, 60, 90, 40);

		bg.add(la3);
		Border lyrics = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "가사");
		la3.setBorder(lyrics);
		la3.setBackground(Color.WHITE);
	}

	private void menu() {
		setJMenuBar(bar);
		bar.add(menu);
		bar.add(menu2);
		menu.add(open);
	}

	public MainUIwin() {
		search = new Search();
		design();
		event();
		menu();
		setTitle("Playing");
		setSize(900, 600);
		setLocation(300, 100);
		setResizable(false);
		setVisible(true);
	}

	private void add() {
		musicList.setListData(str1);
	}

	private void delete() {
		musicList.getSelectedValuesList();
	}
}

public class MainUI {
	public static void main(String[] args) {
		JFrame mainui = new MainUIwin();
	}
}