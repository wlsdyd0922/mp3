package Interface;

import java.io.*;
import java.net.*;

public class Client {
	private InetAddress inet;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	public Client() {
		try {
			inet  = InetAddress.getByName("localhost");
			socket = new Socket(inet, 20000);
			out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean logInManager(int login, String id, String pw) {
		out.println(login);
		out.flush();
		out.println(id);
		out.flush();
		out.println(pw);
		out.flush();
		out.close();
		return login();
	}
	private boolean login() {
		try {
			String log= in.readLine();
			return Boolean.parseBoolean(log);
		} catch (IOException e) {
			return false;
		}
	}
	
	public void signUpManager(int join, String id, String pw,String email) {
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
}
