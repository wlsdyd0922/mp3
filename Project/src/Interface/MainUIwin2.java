//package Interface;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.GridLayout;
//import java.awt.Toolkit;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
//import javax.swing.BorderFactory;
//import javax.swing.DefaultListModel;
//import javax.swing.JButton;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JList;
//import javax.swing.JMenu;
//import javax.swing.JMenuBar;
//import javax.swing.JMenuItem;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.ListSelectionModel;
//import javax.swing.border.Border;
//import javax.swing.filechooser.FileFilter;
//
//import javafx.stage.FileChooser;
//import javazoom.jl.player.Player;
//
//class MainUIwin2 extends JFrame implements ActionListener{
//	final static int LOGIN = 0; // 로그인 요청
//	final static int JOIN = 1; // 회원 가입
//	final static int LIST = 2; // 개인 리스트 요청
//	final static int MUSIC = 3; // 음악 파일 다운 요청
//	final static int DROP = 4; // 탈퇴
//	final static int LOGOUT = 5; // 로그아웃
//	final static int TOTAL_LIST = 6; // 서버 전체 음악 리스트
//	final static int MUSIC_ADD = 7; // 음악 추가
//	final static int MUSIC_DEL = 8; // 음악 삭제
//
//	private PlayThread t;
//	private Client cl = null;
//	private Player p;
//
//	private JFileChooser chooser = new JFileChooser();
//	
//	private JPanel bg1 = new JPanel(null);
//	private JPanel bg = new JPanel(new GridLayout(4, 1));
//	private JPanel lyricline = new JPanel(new BorderLayout());
//	private JPanel buttonline = new JPanel(null);
//	private JPanel titleLine = new JPanel(null);
//	private JPanel scrollLine = new JPanel(new BorderLayout());
//
//	protected static JList<String> musicList = new JList<>(new DefaultListModel<>());
//	protected static int x;
//	protected static int y;
//	// protected static boolean logInflag = false;
//
//	private JScrollPane scroll = new JScrollPane();
//	protected static JLabel la1 = new JLabel("mp3파일 이름 출력", JLabel.LEFT);
//	private JLabel la2 = new JLabel("진행시간", JLabel.CENTER);
//	private JLabel la3 = new JLabel("가사", JLabel.CENTER);
//	protected static JLabel la4 = new JLabel("비트레이트", JLabel.LEFT);
//	protected static JLabel la5 = new JLabel("주파수", JLabel.LEFT);
//
//	private String[] str = new String[] { "◀◀", "▶", "▶▶", "반복", "Random", "All", "■" };
//	private JButton[] bt = new JButton[7];
//	protected static JButton bt1 = new JButton("로그인");
//	protected static JButton bt2 = new JButton("회원가입");
//	protected static JButton bt3 = new JButton("서버음악검색");
//
//	private JMenuBar bar = new JMenuBar();
//	private JMenu menu = new JMenu("File");
//	private JMenu menu2 = new JMenu("Option");
//	private JMenuItem open = new JMenuItem("Open");
//
//	private LoginDialog login = new LoginDialog(this);
//	private SignUpDialog signup = new SignUpDialog(this);
//
//
//	private void event() {
//		WindowListener win = new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent arg0) {
//				System.exit(0);
//			}
//		};
//		addWindowListener(win);
//
//		KeyAdapter listdelete = new KeyAdapter() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
//					DefaultListModel<String> model = new DefaultListModel<>();
//					for (int i = 0; i < musicList.getModel().getSize(); i++) {
//						model.addElement(MainUIwin.musicList.getModel().getElementAt(i));
//					}
//
//					for (int i = 0; i < musicList.getSelectedValuesList().size(); i++) {
//						String str = musicList.getSelectedValuesList().get(i);
//						model.removeElement(str);
//					}
//					musicList.setModel(model);
//				}
//			}
//		};
//		musicList.addKeyListener(listdelete);
//
//		KeyAdapter playmusicenter = new KeyAdapter() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//					if (musicList.getSelectedValue() != null) {
//						if (t == null) {
//							t = new PlayThread();
//							t.setDaemon(true);
//							t.start();
//						} else {
//							t.kill();
//							t = new PlayThread();
//							t.setDaemon(true);
//							t.start();
//							System.out.println(t.getState());
//						}
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//			}
//		};
//		musicList.addKeyListener(playmusicenter);
//
//		MouseAdapter playMusicMou = new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (e.getClickCount() == 2) {
//					if (musicList.getSelectedValue() != null) {
//						if (t == null) {
//							t = new PlayThread();
//							t.setDaemon(true);
//							t.start();
//							
//						} else {
//							t.kill();
//							t = new PlayThread();
//							t.setDaemon(true);
//							t.start();
//							System.out.println(t.getState());
//						}
//						try {
//							Thread.sleep(1000);
//						} catch (InterruptedException e1) {
//							e1.printStackTrace();
//						}
//					}
//				}
//			}
//		};
//		musicList.addMouseListener(playMusicMou);
//
//		ActionListener act = new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (e.getSource() instanceof JButton) {
//					switch (e.getActionCommand()) {
//					case "로그인":
//						login.setVisible(true);
//						break;
//					case "회원가입":
//						signup.setVisible(true);
//						break;
//					case "목록저장":
//						cl = new Client();
//						cl.clientMusicListSave(MUSIC_ADD);
//						break;
//					case "로그아웃":
//						bt1.setText("로그인");
//						bt2.setText("회원가입");
//						cl = new Client();
//						cl.logOut(LOGOUT);
//						String[] str = new String[] {};
//						Search.allList.setListData(str);
//						break;
//					
//					case "서버음악검색":
//						Client.search.setVisible(true);
//						break;
//					case "▶":
//						if (musicList.getSelectedValue() != null) {
//							if (t == null) {
//								t = new PlayThread();
//								t.setDaemon(true);
//								t.start();
//								System.out.println(t.getState());
//							} else {
//								t.kill();
//								t = new PlayThread();
//								t.setDaemon(true);
//								t.start();
//								System.out.println(t.getState());
//							}
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e1) {
//								e1.printStackTrace();
//							}
//						}
//						System.out.println("스레드 개수 : " + Thread.activeCount());
//						break;
//					case "■":
//						t.kill();
//						
//						break;
//						
//					}
//				}
//			}
//		};
//		bt1.addActionListener(act);
//		bt2.addActionListener(act);
//		open.addActionListener(act);
//		bt3.addActionListener(act);
//		bt[1].addActionListener(act);
//		bt[6].addActionListener(act);
//	}
//
//	
//	public void actionPerformed(ActionEvent evt) {
//	    Object source = evt.getSource();
//	    if (source == open) {
//	      JFileChooser chooser = new JFileChooser();
//	      chooser.setCurrentDirectory(new File("."));
//	      chooser.setMultiSelectionEnabled(true);
//	      chooser.setFileFilter(new FileFilter() { 
//	    	  
//	        public boolean accept(File f) {
//	          return f.getName().toLowerCase().endsWith(".mp3")|| f.isDirectory();
//	        }
//	        public String getDescription() {
//	          return "mp3 Files";
//	        }
//	        
//	      });
//	      
//	      chooser.setFileFilter(new FileFilter() { 
//	    	  
//		        public boolean accept(File f) {
//		          return f.getName().toLowerCase().endsWith(".wav")|| f.isDirectory();
//		        }
//		        public String getDescription() {
//		          return "wav Files";
//		        }
//		        
//		      });
//	      
//	      chooser.setFileFilter(new FileFilter() { 
//	    	  
//		        public boolean accept(File f) {
//		          return f.getName().toLowerCase().endsWith(".mpeg")|| f.isDirectory();
//		        }
//		        public String getDescription() {
//		          return "mpeg Files";
//		        }
//		        
//		      });
//	      
//	      chooser.setFileFilter(new FileFilter() { 
//	    	  
//		        public boolean accept(File f) {
//		          return f.getName().toLowerCase().endsWith(".wma")|| f.isDirectory();
//		        }
//		        public String getDescription() {
//		          return "wma Files";
//		        }
//		        
//		      });
//	      
//	      
//	      
//	      //열기-파일열기해서 경로를 받아서 Player 메소드에 집어넣어 재생
//	      int r = chooser.showOpenDialog(bg);
//	      CustomPlayer player = new CustomPlayer();
//	      Scanner s = new Scanner(System.in);
//	      
//	      if (r == JFileChooser.APPROVE_OPTION) {
//	        String musicname = chooser.getSelectedFile().getPath();
//	        player.setPath(musicname);
//	        player.play(-1);
//	        System.out.println("Play Start");
//	        
//	        
//	        String order1=s.nextLine();
//	       
//	        
//				if (order1.equals("pause")) {
//					player.pause();
//					String order2 = s.nextLine();
//					if (order2.equals("resume")) {
//						player.resume();
//					}
//				}
//	        
//	        
//	        
//	        
//	      }
//	      
//	    } 
//	  }
//	
//	private void design() {
//		setContentPane(bg1);
//		bg1.add(bg, BorderLayout.CENTER);
//		bg.setBounds(0, 0, 600, 635);
//
//		scroll.setViewportView(musicList);
//		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//		musicList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//		bg1.add(scrollLine);
//
//		scrollLine.add(scroll, BorderLayout.CENTER);
//		scrollLine.add(bt3, BorderLayout.SOUTH);
//
//		bg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "MP3플레이어"));
//
//		buttonline.setBorder(BorderFactory.createLineBorder(Color.black, 3));
//		titleLine.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
//
//		bg.setBackground(Color.WHITE);
//		buttonline.setBackground(Color.white);
//		titleLine.setBackground(Color.white);
//
//		bg.add(titleLine);
//		titleLine.add(la1);
//		titleLine.add(la4);
//		titleLine.add(la5);
//		titleLine.add(bt1);
//		titleLine.add(bt2);
//		
//		
//		bg.add(la2);
//		bg.add(buttonline);
//
//		for (int i = 0; i < bt.length; i++) {
//			bt[i] = new JButton(str[i]);
//			buttonline.add(bt[i]);
//			bt[i].setBackground(Color.WHITE);
//		}
//		bt1.setBackground(Color.white);
//		bt2.setBackground(Color.white);
//
//		scrollLine.setBounds(600, 0, 283, 635);
//		bt[0].setBounds(10, 10, 80, 40);
//		bt[1].setBounds(110, 10, 80, 40);
//		bt[2].setBounds(210, 10, 80, 40);
//		bt[3].setBounds(355, 10, 60, 40);
//		bt[4].setBounds(420, 10, 90, 40);
//		bt[5].setBounds(515, 10, 60, 40);
//		bt[6].setBounds(110, 49, 80, 40);
//		la1.setBounds(10, 10, 400, 20);
//		la4.setBounds(10, 40, 400, 20);
//		la5.setBounds(10, 60, 400, 20);
//		bt1.setBounds(490, 10, 90, 40);
//		bt2.setBounds(490, 60, 90, 40);
//		bt3.setEnabled(false);
//		bg.add(la3);
//		la3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "가사"));
//		la3.setBackground(Color.WHITE);
//	}
//
//	private void menu() {
//		setJMenuBar(bar);
//		bar.add(menu);
//		bar.add(menu2);
//		menu.add(open);
//	}
//
//	public MainUIwin() {
//		design();
//		event();
//		menu();
//		setTitle("Playing");
//		setSize(900, 700);
//		setLocation(300, 100);
//		setResizable(false);
//		setVisible(true);
//		open.addActionListener(this);
//	}
//}
