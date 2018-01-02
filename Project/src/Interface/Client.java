package Interface;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Client {
	private InetAddress inet;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	protected static String id;
	protected static Search search = null;
	protected static boolean logInflag = false;
	private List<String> list = new ArrayList();

	public Client() {
		try {
			inet = InetAddress.getByName("192.168.0.171");
			socket = new Socket(inet, 20000);
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (IOException e) {
			logInflag = false;
			e.printStackTrace();
		}
	}

	public void logInManager(int login, String id, String pw) {
		try {
			out.writeObject(login);
			out.flush();
			out.writeObject(id);
			out.flush();
			out.writeObject(pw);
			out.flush();
			login();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void login() {
		try {
			logInflag = (boolean) in.readObject();
			if (logInflag) {
				search = new Search();
				MainUIwin.bt3.setEnabled(true);
				MainUIwin.bt1.setText("로그아웃");
				MainUIwin.bt2.setText("목록저장");
			} else {
				MainUIwin.bt3.setEnabled(false);
			}
		} catch (Exception e) {
			System.out.println("???");
		}
	}

	public void signUpManager(int join, String id, String pw, String email) {
		try {
			out.writeObject(join);
			out.flush();
			out.writeObject(pw);
			out.flush();
			out.writeObject(email);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> serverMusicList(int total_list) {
		try {
			out.writeObject(total_list);
			out.flush();
			List<String> list = (ArrayList<String>) in.readObject();
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	public void musicAdd(int music_add) {
		try {
			out.writeObject(music_add);
			out.flush();
			out.writeObject(123);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean clientMusicListSave(int cllist) {
		try {
			out.writeObject(cllist);
			out.flush();
			for (int i = 0; i < MainUIwin.musicList.getModel().getSize(); i++) {
				list.add(MainUIwin.musicList.getModel().getElementAt(0));
			}
			out.writeObject(list);
			boolean a = (boolean) in.readObject();
			return a;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void clientMusicList() {

	}
}
