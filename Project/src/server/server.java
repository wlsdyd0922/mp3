package server;

import java.net.*;
import java.util.*;

public class server {
	private static List<String> ipList = new ArrayList<>();
	public static void main(String[] args) {
		try(ServerSocket server = new ServerSocket(20000)){
			System.out.println("서버 기동"); 
			while(true)
			{
				Socket s = server.accept();
				new NetworkManager(s,ipList);
				System.out.println("Client Accessed");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		} 
	}
}