package server;

import java.net.ServerSocket;
import java.net.Socket;

public class server {
	public static void main(String[] args) {
		try(ServerSocket server = new ServerSocket(20000)){
			System.out.println("���� �⵿");
//			while(true)
//			{
				Socket s = server.accept();
				NetworkManager nm = new NetworkManager(s);
				System.out.println("��Ʈ��ũ �Ŵ��� ����");
//			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
