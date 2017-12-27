package Interface;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	private InetAddress inet;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	public Client() {
		try {
			inet = InetAddress.getByName("localhost");
			socket = new Socket(inet, 20000);
			out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logInManager(int login, String id, String pw) {
		out.write(login);
		out.flush();
		out.write(id);
		out.flush();
		out.write(pw);
		out.flush();
		out.close();
	}
	
	public void signUpManager(int join, String id, String pw,String email) {
		out.write(join);
		out.flush();
		out.write(id);
		out.write(pw);
		out.flush();
		out.write(email);
		out.flush();
		out.close();
	}
}
