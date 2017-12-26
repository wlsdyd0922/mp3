package server;

import java.net.ServerSocket;
import java.net.Socket;

public class server {
	public static void main(String[] args) {
		try(ServerSocket server = new ServerSocket(20000)){
			Socket s = server.accept();
			NetworkManager nm = new NetworkManager(s);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
