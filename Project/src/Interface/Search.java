package Interface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Search extends JFrame{
	private JPanel bg = new JPanel(new BorderLayout());
	private JPanel top = new JPanel(null);
	private JList<String> allList = new JList<>();
	private JScrollPane scroll = new JScrollPane();
	private String[] servermusic = new String[] {"서버노래1","서버노래2","서버노래3"};
	private JTextField text = new JTextField();
	private JButton bt = new JButton("검색");
	
	private void event() {
		
		text.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					musicSearch();
				}
			}

		});
		bt.addActionListener(e->{
			musicSearch();
		});
		WindowListener win = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				
			}
		};
		
		KeyAdapter listadd = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					System.out.println("추가");
				}
			}
		};
		allList.addKeyListener(listadd);
		
		MouseAdapter listaddmou = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					System.out.println("추가");
				}
			}
		};
		allList.addMouseListener(listaddmou);
		
		
		
		
		
		
	}


	private void musicSearch() {
		
		
		
		
		
		
		
		
	}


	private void design() {
		setContentPane(bg);
		bg.setBackground(Color.WHITE);
		bg.add(top);
		scroll.setViewportView(allList);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		top.add(scroll);
		
		scroll.setBounds(5,45,275,510);
		top.add(text,FlowLayout.LEFT);
		text.setBorder(BorderFactory.createLineBorder(Color.gray));
		text.setBounds(5,5,200,30);
		bt.setBounds(210,5,70,30);
		top.add(bt);
		allList.setListData(servermusic);
	}
	
	public Search() {
		design();
		event();
		setTitle("노래 검색");
		setSize(300,600);
		setLocation(1200, 100);
		setVisible(false);
	}
}


