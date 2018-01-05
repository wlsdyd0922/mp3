package Interface;

import java.awt.*;
import java.awt.event.*;
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

	private PlayThread t;
	private Client cl = null;
	private Player p;
	private int skip;
	private boolean allFLag;
	private boolean infFLag;
	

	private JFileChooser chooser = new JFileChooser();

	private JPanel bg1 = new JPanel(null);
	private JPanel bg = new JPanel(new GridLayout(4, 1));
	private JPanel lyricline = new JPanel(new BorderLayout());
	private JPanel buttonline = new JPanel(null);
	private JPanel titleLine = new JPanel(null);
	private JPanel scrollLine = new JPanel(new BorderLayout());

	protected static JList<String> musicList = new JList<>(new DefaultListModel<>());
	protected static int x;
	protected static int y;
	// protected static boolean logInflag = false;

	private JScrollPane scroll = new JScrollPane();
	protected static JLabel la1 = new JLabel("mp3파일 이름 출력", JLabel.LEFT);
	private JLabel la2 = new JLabel("진행시간", JLabel.CENTER);
	private JLabel la3 = new JLabel("가사", JLabel.CENTER);
	protected static JLabel la4 = new JLabel("비트레이트", JLabel.LEFT);
	protected static JLabel la5 = new JLabel("주파수", JLabel.LEFT);

	private String[] str = new String[] { "◀◀", "▶", "▶▶", "All", "Random", "반복", "■", "∥" };
	private JButton[] bt = new JButton[8];
	protected static JButton bt1 = new JButton("로그인");
	protected static JButton bt2 = new JButton("회원가입");
	protected static JButton bt3 = new JButton("서버음악검색");

	private JMenuBar bar = new JMenuBar();
	private JMenu menu = new JMenu("File");
	private JMenu menu2 = new JMenu("Option");
	private JMenuItem open = new JMenuItem("Open");

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
						if (t == null) {
							if (skip != 0) {
								t = new PlayThread(skip);
							} else {
								t = new PlayThread();
							}
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
							t.setAllFlag(allFLag);
							t.setInfFlag(infFLag);
							t.setDaemon(true);
							t.start();
							System.out.println(t.getState());
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
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
						if (t == null) {
							if (skip != 0) {
								t = new PlayThread(skip);
							} else {
								t = new PlayThread();
							}
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
							t.setAllFlag(allFLag);
							t.setInfFlag(infFLag);
							t.setDaemon(true);
							t.start();
							System.out.println(t.getState());
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
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
						bt1.setText("로그인");
						bt2.setText("회원가입");
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
						if (musicList.getSelectedValue() != null) {
							if (t == null) {
								if (skip != 0) {
									t = new PlayThread(skip);
								} else {
									t = new PlayThread();
								}
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
					case "■":
						t.kill();
						break;
					case "∥":
						skip = t.stopper();
						break;
					case "All":
						t.setAllFlag(true);
						allFLag = t.getAllFlag();
						bt[3].setText("All X");
						break;
					case "All X":
						t.setAllFlag(false);
						allFLag = t.getAllFlag();
						bt[3].setText("All");
						break;
					case "◀◀":
						/////////////////////////////////////////////
						if (musicList.getSelectedValue() != null) {
							if (t == null) {
								if (skip != 0) {
									t = new PlayThread(skip);
								} else {
									t = new PlayThread();
								}
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
						//////////////////////////////////////////////
						break;
					case "▶▶":
						/////////////////////////////////////////////
						if (musicList.getSelectedValue() != null) {
							if (t == null) {
								if (skip != 0) {
									t = new PlayThread(skip);
								} else {
									t = new PlayThread();
								}
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
						//////////////////////////////////////////////
						break;
					case"반복":
						t.setInfFlag(true);
						infFLag = t.getInfFlag();
						bt[5].setText("반복X");
						break;
					case"반복X":
						t.setInfFlag(false);
						infFLag = t.getInfFlag();
						bt[5].setText("반복");
						break;
					}
				}
			}
		};
		bt1.addActionListener(act);
		bt2.addActionListener(act);
		open.addActionListener(act);
		bt3.addActionListener(act);
		bt[1].addActionListener(act);
		bt[2].addActionListener(act);
		bt[3].addActionListener(act);
		bt[5].addActionListener(act);
		bt[6].addActionListener(act);
		bt[7].addActionListener(act);
	}

	private void design() {
		setContentPane(bg1);
		bg1.add(bg, BorderLayout.CENTER);
		bg.setBounds(0, 0, 600, 635);

		scroll.setViewportView(musicList);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		musicList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		bg1.add(scrollLine);

		scrollLine.add(scroll, BorderLayout.CENTER);
		scrollLine.add(bt3, BorderLayout.SOUTH);

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

		scrollLine.setBounds(600, 0, 283, 635);
		bt[0].setBounds(10, 10, 80, 40);
		bt[1].setBounds(110, 10, 80, 40);
		bt[2].setBounds(210, 10, 80, 40);
		bt[3].setBounds(355, 10, 60, 40);
		bt[4].setBounds(420, 10, 90, 40);
		bt[5].setBounds(515, 10, 60, 40);
		bt[6].setBounds(110, 49, 80, 40);
		bt[7].setBounds(210, 49, 80, 40);
		la1.setBounds(10, 10, 400, 20);
		la4.setBounds(10, 40, 400, 20);
		la5.setBounds(10, 60, 400, 20);
		bt1.setBounds(490, 10, 90, 40);
		bt2.setBounds(490, 60, 90, 40);
		bt3.setEnabled(false);
		bg.add(la3);
		la3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "가사"));
		la3.setBackground(Color.WHITE);
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
}