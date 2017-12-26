package Interface;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	private InetAddress inet;
	private Socket socket;
	private ObjectOutputStream out;

	public Client() {
		try {
			inet = InetAddress.getByName("localhost");
			socket = new Socket(inet, 20000);
			out = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logInManager(int login, String id, String pw) {
		try {
			out.writeInt(login);
			out.flush();
			out.writeObject(id);
			out.flush();
			out.writeObject(pw);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void signUpManager(int join, String id, String pw,String email) {
		try {
			out.writeInt(join);
			out.flush();
			out.writeObject(id);
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
}
