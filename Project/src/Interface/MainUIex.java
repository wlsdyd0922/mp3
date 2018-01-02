package ������;


import java.awt.*;
import java.awt.event.*;
import java.io.File;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;


import javafx.stage.FileChooser;

class MainUIwin extends JFrame implements ActionListener{

	final static int LOGIN = 0; // �α��� ��û
	final static int JOIN = 1; // ȸ�� ����
	final static int LIST = 2; // ���� ����Ʈ ��û
	final static int MUSIC = 3; // ���� ���� �ٿ� ��û
	final static int DROP = 4; // Ż��
	final static int LOGOUT = 5; // �α׾ƿ�
	final static int TOTAL_LIST = 6; // ���� ��ü ���� ����Ʈ
	final static int MUSIC_ADD = 7; // ���� �߰�
	final static int MUSIC_DEL = 8; // ���� ����

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
	private JLabel la1 = new JLabel("mp3���� �̸� ���", JLabel.LEFT);
	private JLabel la2 = new JLabel("����ð�", JLabel.CENTER);
	private JLabel la3 = new JLabel("����", JLabel.CENTER);

	private String[] str = new String[] { "����", "����", "����", "�ݺ�", "Random", "All", };
	private JButton[] bt = new JButton[6];
	private JButton bt1 = new JButton("�α���");
	private JButton bt2 = new JButton("ȸ������");
	protected static JButton bt3 = new JButton("�������ǰ˻�");

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
						System.out.println(musicList.getSelectedValue() + "����");
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
						System.out.println(musicList.getSelectedValue() + "����");
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

		bt3.addActionListener(e -> {
			Client.search.setVisible(true);
		});

	}

	
	public void actionPerformed(ActionEvent evt) {
	    Object source = evt.getSource();
	    if (source == open) {
	      JFileChooser chooser = new JFileChooser();
	      chooser.setCurrentDirectory(new File("."));
	      chooser.setMultiSelectionEnabled(true);
	      chooser.setFileFilter(new FileFilter() {
	    	  
	        public boolean accept(File f) {
	          return f.getName().toLowerCase().endsWith(".mp3")|| f.isDirectory();
	        }
	        

	        public String getDescription() {
	          return "mp3 Files";
	        }
	       
	      });
	      int r = chooser.showOpenDialog(bg);
	      
	      if (r == JFileChooser.APPROVE_OPTION) {
	        String mp3name = chooser.getSelectedFile().getPath();
	        System.out.println(mp3name);
	      }
	      
	    } 
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

		bg.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "MP3�÷��̾�"));

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
		bt3.setEnabled(false);
		bg.add(la3);
		Border lyrics = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "����");
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
		design();
		event();
		menu();
		setTitle("Playing");
		setSize(1100, 700);
		setLocation(300, 100);
		setResizable(false);
		setVisible(true);
		open.addActionListener(this);
	}
}

public class MainUIex {
	public static void main(String[] args) {
		JFrame mainui = new MainUIwin();
		mainui.show();
	}
}