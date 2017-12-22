package project.server;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class NetworkManager {
	private ObjectInputStream obj;
	
	public NetworkManager()
	{
		try {
			obj = new ObjectInputStream(
						new BufferedInputStream(
							new FileInputStream(memberDB)));
		} catch (Exception e) {
			System.out.println("클라이언트 목록 읽기 실패");
		}
	}
}
