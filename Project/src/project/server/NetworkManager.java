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
			System.out.println("Ŭ���̾�Ʈ ��� �б� ����");
		}
	}
}
