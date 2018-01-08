package Interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.*;

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
	protected static JList<String> allList = new JList<>(new DefaultListModel<>());
	private JScrollPane scroll = new JScrollPane();
	private JTextField text = new JTextField();
	private JButton bt = new JButton("검색");
	private List<String> list = new ArrayList<>();
	private DefaultListModel<String> modelserver = new DefaultListModel<>();
	private DefaultListModel<String> searchmodel = new DefaultListModel<>();
	private boolean sear = true;

	private void search() {
		musicSearch();
		bt.setText("취소");
		sear = false;
	}

	private void cancel() {
		searchmodel.removeAllElements();
		allList.setModel(modelserver);
		bt.setText("검색");
		sear = true;
	}

	private void event() {
		text.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (sear) {
						search();
					} else {
						cancel();
					}
				}
			}
		});

		bt.addActionListener(e -> {
			if (e.getActionCommand() == "검색") {
				search();
			} else if (e.getActionCommand() == "취소") {
				cancel();
			}
		});

		KeyAdapter listadd = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					DefaultListModel<String> model = new DefaultListModel<>();
					for (int i = 0; i < MainUIwin.musicList.getModel().getSize(); i++) {
						model.addElement(MainUIwin.musicList.getModel().getElementAt(i));
					}
					for (int i = 0; i < allList.getSelectedValuesList().size(); i++) {
						String str = allList.getSelectedValuesList().get(i);
						if (!model.contains(str)) {
							model.addElement(str);
							System.out.println(str);
						}
						MainUIwin.musicList.removeAll();
						MainUIwin.musicList.setModel(model);
					}
				}
			}
		};
		allList.addKeyListener(listadd);

		MouseAdapter listaddmou = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				DefaultListModel<String> model = new DefaultListModel<>();
				if (e.getClickCount() == 2) {
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
		for (int i = 0; i < allList.getModel().getSize(); i++) {
			if (allList.getModel().getElementAt(i).toLowerCase().contains(text.getText().toLowerCase())) {
				searchmodel.addElement(allList.getModel().getElementAt(i));
			}
		}
		allList.setModel(searchmodel);
	}

	private void design() {
		Client cl = new Client();
		list = cl.serverMusicList(MainUIwin.TOTAL_LIST);
		for (int i = 0; i < list.size(); i++) {
			modelserver.addElement(list.get(i));
		}
		allList.setModel(modelserver);

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
		// allList.setListData(serverMusic);
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
