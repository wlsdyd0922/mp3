package Interface;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javazoom.jl.player.Player;

public class MainUIwin extends JFrame {
	final static int LOGIN = 0; // 로그인 요청
	final static int JOIN = 1; // 회원 가입
	final static int LIST = 2; // 개인 리스트 요청
	final static int MUSIC = 3; // 음악 파일 다운 요청
	final static int DROP = 4; // 탈퇴
	final static int LOGOUT = 5; // 로그아웃
	final static int TOTAL_LIST = 6; // 서버 전체 음악 리스트
	final static int MUSIC_ADD = 7; // 음악 추가
	final static int MUSIC_DEL = 8; // 음악 삭제
	final static int LYRIC_CALL = 9;
	final static int LYRIC_ADD = 1024735; // 가사 추가

	private PlayThread t;
	private Client cl = null;
	private Player p;
	private int skip;
	private boolean allFLag;
	private boolean infFLag;
	private boolean ranFlag;
	private int sel = 0;

	private JFileChooser chooser = new JFileChooser();

	private JPanel bg1 = new JPanel(null);
	private JPanel buttonline = new JPanel(null);
	private JPanel titleLine = new JPanel(null);
	private JPanel bg = new JPanel(new GridLayout(4, 1));
	private JPanel lyricline = new JPanel(new BorderLayout());
	private JPanel scrollLine = new JPanel(new BorderLayout());
	private JPanel bg3 = new JPanel(new BorderLayout());

	protected static JList<String> musicList = new JList<>(new DefaultListModel<>());
	protected static int x;
	protected static int y;
	// protected static boolean logInflag = false;

	private JScrollPane scroll = new JScrollPane();
	protected static JLabel la1 = new JLabel("mp3파일 이름 출력", JLabel.LEFT);
	private JLabel la2 = new JLabel("진행시간", JLabel.CENTER);
	private JLabel la3 = new JLabel("가사", JLabel.CENTER);
	private JTextArea tp = new JTextArea();
	private JScrollPane scroll1 = new JScrollPane();
	protected static JLabel la4 = new JLabel("비트레이트", JLabel.LEFT);
	protected static JLabel la5 = new JLabel("주파수", JLabel.LEFT);

	private String[] str = new String[] { "◀◀", "▶", "▶▶", "■", "∥" };
	private String[] str1 = new String[] { "로그인", "회원가입", "서버음악검색", "All", "Random", "반복" };
	private JButton[] bt = new JButton[5];
	protected static JButton[] bts = new JButton[6];

	private JMenuBar bar = new JMenuBar();
	private JMenu menu = new JMenu("File");
	private JMenu menu2 = new JMenu("Option");
	private JMenuItem open = new JMenuItem("Open");

	private LoginDialog login = new LoginDialog(this);
	private SignUpDialog signup = new SignUpDialog(this);

	private void event() {
		WindowListener win = new WindowAdapter() {
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
					playing();
				}
			}
		};
		musicList.addKeyListener(playmusicenter);

		MouseAdapter playMusicMou = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					playing();
				}
			}
		};
		musicList.addMouseListener(playMusicMou);
		ActionListener act = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof JButton) {
					switch (e.getActionCommand()) {
					case "로그인":
						login.setVisible(true);
						break;
					case "회원가입":
						signup.setVisible(true);
						break;
					case "목록저장":
						cl = new Client();
						cl.clientMusicListSave(MUSIC_ADD);
						break;
					case "로그아웃":
						bts[0].setText("로그인");
						bts[1].setText("회원가입");
						if (t != null) {
							t.kill();
						}
						cl = new Client();
						cl.logOut(LOGOUT);
						String[] str = new String[] {};
						Search.allList.setListData(str);
						break;
					case "Open":
						chooser.setMultiSelectionEnabled(true);
						chooser.setFileFilter(
								new javax.swing.filechooser.FileNameExtensionFilter("mp3 File (*.mp3)", ".mp3"));
						chooser.setFileFilter(
								new javax.swing.filechooser.FileNameExtensionFilter("mpeg File (*.mpeg)", ".mpeg"));
						chooser.setFileFilter(
								new javax.swing.filechooser.FileNameExtensionFilter("wav File (*.wav)", ".wav"));
						chooser.setFileFilter(
								new javax.swing.filechooser.FileNameExtensionFilter("wma File (*.wma)", ".wma"));
						chooser.showOpenDialog(bg);
						break;
					case "서버음악검색":
						Client.search.setVisible(true);
						break;
					case "▶":
						playing();
						break;
					case "■":
						if (t != null) {
							t.kill();
							skip = 0;
						}
						break;
					case "∥":
						if (t != null) {
							skip = t.stopper();
							sel = t.getSelect();
						}
						break;
					case "All":
						if (t != null) {
							t.setAllFlag(true);
							allFLag = t.getAllFlag();
							bts[3].setText("All X");
						}
						break;
					case "All X":
						if (t != null) {
							t.setAllFlag(false);
							allFLag = t.getAllFlag();
							bts[3].setText("All");
						}
						break;
					case "◀◀":
						if (musicList.getSelectedValue() != null) {
							if (t == null) {
								int a = musicList.getSelectedIndex() - 1;
								if (a < 0) {
									musicList.setSelectedIndex(musicList.getLastVisibleIndex());
								} else {
									musicList.setSelectedIndex(a);
								}
								if (skip != 0) {
									t = new PlayThread(skip);
								} else {
									t = new PlayThread();
								}
								t.setRanFlag(ranFlag);
								t.setAllFlag(allFLag);
								t.setInfFlag(infFLag);
								t.setDaemon(true);
								t.start();
							} else {
								t.kill();
								musicList.setSelectedIndex(t.getSelectPrev());
								if (skip != 0) {
									t = new PlayThread(skip);
								} else {
									t = new PlayThread();
								}
								t.setRanFlag(ranFlag);
								t.setAllFlag(allFLag);
								t.setInfFlag(infFLag);
								t.setDaemon(true);
								t.start();
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
						break;
					case "▶▶":
						if (musicList.getSelectedValue() != null) {
							if (t == null) {
								int a = musicList.getSelectedIndex() + 1;
								if (a > musicList.getLastVisibleIndex()) {
									musicList.setSelectedIndex(0);
								} else {
									musicList.setSelectedIndex(a);
								}
								if (skip != 0) {
									t = new PlayThread(skip);
								} else {
									t = new PlayThread();
								}
								t.setRanFlag(ranFlag);
								t.setAllFlag(allFLag);
								t.setInfFlag(infFLag);
								t.setDaemon(true);
								t.start();
							} else {
								t.kill();
								musicList.setSelectedIndex(t.getSelectNext());
								if (skip != 0) {
									t = new PlayThread(skip);
								} else {
									t = new PlayThread();
								}
								t.setRanFlag(ranFlag);
								t.setAllFlag(allFLag);
								t.setInfFlag(infFLag);
								t.setDaemon(true);
								t.start();
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
						break;
					case "반복":
						if (t != null) {
							t.setInfFlag(true);
							infFLag = t.getInfFlag();
							bts[5].setText("반복X");
						}
						break;
					case "반복X":
						if (t != null) {
							t.setInfFlag(false);
							infFLag = t.getInfFlag();
							bts[5].setText("반복");
						}
						break;
					case "Random":
						if (t != null) {
							t.setRanFlag(true);
							ranFlag = t.getRanFlag();
							bts[4].setText("Random X");
							bts[5].setEnabled(false);
						}
						break;
					case "Random X":
						if (t != null) {
							t.setRanFlag(false);
							ranFlag = t.getRanFlag();
							bts[4].setText("Random");
							bts[5].setEnabled(true);
						}
						break;
					}
				}
			}
		};
		open.addActionListener(act);
		for (int i = 0; i < bts.length; i++) {
			bts[i].addActionListener(act);
		}
		for (int i = 0; i < bt.length; i++) {
			bt[i].addActionListener(act);
		}
	}

	private void design() {
		for (int i = 0; i < bt.length; i++) {
			bt[i] = new JButton(str[i]);
			buttonline.add(bt[i]);
			bt[i].setBackground(Color.WHITE);
		}
		for (int i = 0; i < bts.length; i++) {
			bts[i] = new JButton(str1[i]);
		}

		setContentPane(bg1);
		bg1.add(bg, BorderLayout.CENTER);
		bg.setBounds(0, 0, 600, 635);

		scroll.setViewportView(musicList);
		scroll1.setViewportView(tp);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		musicList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		bg1.add(scrollLine);

		scrollLine.add(scroll, BorderLayout.CENTER);
		scrollLine.add(bts[2], BorderLayout.SOUTH);

		bg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "MP3플레이어"));

		buttonline.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		titleLine.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

		bg.setBackground(Color.WHITE);
		buttonline.setBackground(Color.white);
		titleLine.setBackground(Color.white);

		bg.add(titleLine);
		titleLine.add(la1);
		titleLine.add(la4);
		titleLine.add(la5);
		titleLine.add(bts[0]);
		titleLine.add(bts[1]);

		bg.add(la2);
		bg.add(buttonline);

		buttonline.add(bts[3]);
		buttonline.add(bts[4]);
		buttonline.add(bts[5]);
		bts[0].setBackground(Color.white);
		bts[1].setBackground(Color.white);
		bts[3].setBackground(Color.white);
		bts[4].setBackground(Color.white);
		bts[5].setBackground(Color.white);

		bts[3].setEnabled(false);
		bts[4].setEnabled(false);
		bts[5].setEnabled(false);

		scrollLine.setBounds(600, 0, 283, 635);
		bt[0].setBounds(10, 10, 80, 40);
		bt[1].setBounds(110, 10, 80, 40);
		bt[2].setBounds(210, 10, 80, 40);
		bt[3].setBounds(110, 49, 80, 40);
		bt[4].setBounds(210, 49, 80, 40);

		bts[0].setBounds(490, 10, 90, 40);
		bts[1].setBounds(490, 60, 90, 40);
		bts[3].setBounds(330, 10, 60, 40);
		bts[4].setBounds(400, 10, 100, 40);
		bts[5].setBounds(510, 10, 70, 40);

		la1.setBounds(10, 10, 400, 20);
		la4.setBounds(10, 40, 400, 20);
		la5.setBounds(10, 60, 400, 20);

		bts[2].setEnabled(false);

		bg.add(bg3);
		bg3.add(scroll1);
		bg3.setBackground(Color.WHITE);
		bg3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "가사"));
		tp.setBackground(Color.WHITE);
		tp.setFont(new Font("굴림", Font.PLAIN, 20));
		tp.setFocusable(false);
		tp.setText("asdasd\nas\nsddssda\nsds\nsdsd\nasdfds\nsdfdf");
	}

	private void menu() {
		setJMenuBar(bar);
		bar.add(menu);
		bar.add(menu2);
		menu.add(open);
	}

	public MainUIwin() {
		design();
		event();
		menu();
		setTitle("Playing");
		setSize(900, 700);
		setLocation(300, 100);
		setResizable(false);
		setVisible(true);
	}

	public void playing() {
		if (sel != musicList.getSelectedIndex()) {
			skip = 0;
		}
		if (musicList.getSelectedValue() != null) {
			if (t == null) {
				if (skip != 0) {
					t = new PlayThread(skip);
				} else {
					t = new PlayThread();
				}
				t.setRanFlag(ranFlag);
				t.setAllFlag(allFLag);
				t.setInfFlag(infFLag);
				t.setDaemon(true);
				t.start();
			} else {
				t.kill();
				if (skip != 0) {
					t = new PlayThread(skip);
				} else {
					t = new PlayThread();
				}
				t.setRanFlag(ranFlag);
				t.setAllFlag(allFLag);
				t.setInfFlag(infFLag);
				t.setDaemon(true);
				t.start();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}