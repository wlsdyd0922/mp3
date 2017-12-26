package server;

import java.net.ServerSocket;
import java.net.Socket;

public class server {
	public static void main(String[] args) {
		try(ServerSocket server = new ServerSocket(20000)){
			System.out.println("서버 기동");
//			while(true)
//			{
				Socket s = server.accept();
				NetworkManager nm = new NetworkManager(s);
				System.out.println("네트워크 매니저 생성");
//			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
