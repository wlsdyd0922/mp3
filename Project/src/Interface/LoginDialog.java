package Interface;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class LoginDialog extends JDialog {

	private JLabel nameLabel = new JLabel("Name : ");
	private JLabel passwordLabel = new JLabel("Password : ");

	private JTextField nameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();

	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");

	private List<String> clList = new ArrayList<>();

	public LoginDialog(JFrame owner) {
		setupUI();
		setUpListeners();
		setSize(400, 150);
		setLocationRelativeTo(owner);
		setVisible(false);
	}

	public void setupUI() {

		this.setTitle("Login");

		JPanel topPanel = new JPanel(new GridBagLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(4, 4, 4, 4);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		topPanel.add(nameLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		topPanel.add(nameField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		topPanel.add(passwordLabel, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		topPanel.add(passwordField, gbc);

		this.add(topPanel);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void setUpListeners() {
		WindowListener win = new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				setVisible(false);
				nameField.setText("");
				passwordField.setText("");
				MainUIwin.bt1.setText("로그인");
				super.windowClosing(arg0);
			}
		};
		addWindowListener(win);

		passwordField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					loginPass();
					nameField.setText("");
					passwordField.setText("");

//					Client cl = new Client();
//					clList = cl.clientMusicList(MainUIwin.LIST);
//					DefaultListModel<String> model = new DefaultListModel<>();
//					for (int i = 0; i < clList.size(); i++) {
//						model.addElement(clList.get(i));
//					}
//					MainUIwin.musicList.setModel(model);

				}
			}
		});

		okButton.addActionListener(e -> {
			loginPass();
			nameField.setText("");
			passwordField.setText("");
			
		});
		cancelButton.addActionListener(e -> {
			LoginDialog.this.setVisible(false);
			nameField.setText("");
			passwordField.setText("");
			MainUIwin.bt1.setText("로그인");
		});
	}

	private void loginPass() {
		String id = nameField.getText();
		String pw = "";

		char[] pw1 = passwordField.getPassword();
		for (int i = 0; i < pw1.length; i++) {
			pw += pw1[i];
		}
		Client cl = new Client();
		cl.logInManager(MainUIwin.LOGIN, id, pw);
		LoginDialog.this.setVisible(false);
	}
}