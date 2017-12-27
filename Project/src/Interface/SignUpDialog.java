package Interface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SignUpDialog extends JDialog {

	JLabel nameLabel = new JLabel("Name : ");
	JLabel passwordLabel = new JLabel("Password : ");
	JLabel emailLabel = new JLabel("email : ");

	JTextField nameField = new JTextField();
	JPasswordField passwordField = new JPasswordField();
	JTextField emailField = new JTextField();

	JButton okButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");

	public SignUpDialog(JFrame owner) {
		setupUI();
		setUpListeners();
		setSize(400, 215);
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

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		topPanel.add(emailLabel, gbc);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 1;
		topPanel.add(emailField, gbc);

		this.add(topPanel);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	private void setUpListeners() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		passwordField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					SignUp();
				}
			}
		});
		WindowListener win = new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				nameField.setText("");
				passwordField.setText("");
				emailField.setText("");
				setVisible(false);
				super.windowClosing(arg0);
			}
		};
		addWindowListener(win);
		okButton.addActionListener(e -> {
			SignUp();
			nameField.setText("");
			passwordField.setText("");
			emailField.setText("");
		});
		cancelButton.addActionListener(e -> {
			SignUpDialog.this.setVisible(false);
			nameField.setText("");
			passwordField.setText("");
			emailField.setText("");
		});
	}
 
	private void SignUp() {
		String id = nameField.getText();
		String pw = "";
		String email = emailField.getText();
		char[] pw1 = passwordField.getPassword();
		for (int i = 0; i < pw1.length; i++) {
			pw += pw1[i];
		}
		Client cl = new Client();
		cl.signUpManager(MainUIwin.JOIN, id, pw, email);
		System.out.println("id : " + id + "/ pw : " + pw + "/ email : " + email);
		System.out.println("회원가입 완료");
		SignUpDialog.this.setVisible(false);
	}
}