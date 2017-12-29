package Interface;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Client {
	private InetAddress inet;
	private Socket socket;
	private PrintWriter out;
	private ObjectInputStream in;
	protected static String id;
	protected static JFrame search = null;
	protected static boolean logInflag = false;

	public Client() {
		try {
			inet = InetAddress.getByName("192.168.0.171");
			socket = new Socket(inet, 20000);
			out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (IOException e) {
			logInflag = false;
			e.printStackTrace();
		}
	}

	public void logInManager(int login, String id, String pw) {
		out.println(login);
		out.flush();
		out.println(id);
		this.id = id;
		out.flush();
		out.println(pw);
		out.flush();
		login();
	}

	private void login() {
		try {
			logInflag = (boolean) in.readObject();
			if (logInflag) {
				search = new Search();
				MainUIwin.bt3.setEnabled(true);
			} else {
				MainUIwin.bt3.setEnabled(false);
			}
		} catch (Exception e) {
			System.out.println("???");
		}
	}

	public void signUpManager(int join, String id, String pw, String email) {
		out.println(join);
		out.flush();
		out.println(id);
		out.flush();
		out.println(pw);
		out.flush();
		out.println(email);
		out.flush();
		out.close();
	}

	public List<String> serverMusicList(int total_list) {
		out.println(total_list);
		out.flush();
		out.println(Client.id);
		out.flush();
		try {
		List<String> list = (ArrayList<String>) in.readObject();
		return list;
		}catch (Exception e) {
			return null;
		}
	}
	
	public void musicAdd(int music_add) {
		out.println(music_add);
		out.flush();
		out.println(Client.id);
		out.flush();
	}
	
	public List<String> clientMusicList(int cllist) {
		out.println(cllist);
		out.flush();
		out.println(Client.id);
		out.flush();
		try {
		List<String> list = (ArrayList<String>) in.readObject();
		return list;
		}catch (Exception e) {
			return null;
		}
	}
}
