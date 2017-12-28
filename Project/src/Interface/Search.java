package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Search extends JFrame {
	private JPanel bg = new JPanel(new BorderLayout());
	private JPanel top = new JPanel(null);
	private JList<String> allList = new JList<>(new DefaultListModel<>());
	private JScrollPane scroll = new JScrollPane();
	private String[] serverMusic = new String[] { "서버노래1", "서버노래2", "서버노래3" };

	private JTextField text = new JTextField();
	private JButton bt = new JButton("검색");
	private List list;

	private void event() {
		list = new List();

		text.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					musicSearch();
				}
			}

		});
		bt.addActionListener(e -> {
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
					if (allList.getSelectedValuesList() != null) {
						DefaultListModel<String> model = new DefaultListModel<>();
						for (int i = 0; i < MainUIwin.musicList.getModel().getSize(); i++) {
							model.addElement(MainUIwin.musicList.getModel().getElementAt(i));
						}
						for (int i = 0; i < allList.getSelectedValuesList().size(); i++) {
							String str = allList.getSelectedValuesList().get(i);
							model.addElement(str);
						}
						MainUIwin.musicList.setModel(model);
					}
				}
			}
		};
		allList.addKeyListener(listadd);

		MouseAdapter listaddmou = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					DefaultListModel<String> model = new DefaultListModel<>();
					for (int i = 0; i < MainUIwin.musicList.getModel().getSize(); i++) {
						model.addElement(MainUIwin.musicList.getModel().getElementAt(i));
					}
					model.addElement(allList.getSelectedValue());
					MainUIwin.musicList.setModel(model);
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

		scroll.setBounds(5, 45, 275, 510);
		top.add(text, FlowLayout.LEFT);
		text.setBorder(BorderFactory.createLineBorder(Color.gray));
		text.setBounds(5, 5, 200, 30);
		bt.setBounds(210, 5, 70, 30);
		top.add(bt);
		allList.setListData(serverMusic);
		allList.setSelectedIndex(0);
	}

	public Search() {
		design();
		event();
		setTitle("노래 검색");
		setSize(300, 600);
		setLocation(1200, 100);
		setVisible(false);
	}
}
