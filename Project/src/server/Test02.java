package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Test02 {
	static boolean flag = true;
	final static int LOGIN = 0;
	final static int JOIN = 1;						//ȸ�� ����
	public static void main(String[] args) throws Exception {
		String ip = "192.168.0.171";
		int port = 20000;
		Scanner s = new Scanner(System.in);

		InetAddress inet = InetAddress.getByName(ip);

		Socket socket = new Socket(inet, port);

		PrintWriter out = new PrintWriter(
				new BufferedWriter(
						new OutputStreamWriter(
								socket.getOutputStream())));

		int state = JOIN;
		out.write(state);
		out.flush();
		
		System.out.println("ID �Է�");
		String id = s.next();
		out.println(id);
		out.flush();
		
		System.out.println("PW �Է�");
		String pw = s.next();
		out.println(pw);
		out.flush();
		
		System.out.println("email �Է�");
		String email = s.next();
		out.println(email);
		out.flush();

		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(
						socket.getInputStream()));) 
		{
				String textFS = in.readLine();
					System.out.println("server : " + textFS);
			
		} catch (Exception e) {
			System.err.println("�ޱ� ����");
		}
		
		socket.close();
		System.out.println("connection end");
	}
}
