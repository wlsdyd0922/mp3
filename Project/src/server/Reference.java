package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reference {
	static boolean flag = true;
	final static int LOGIN = 0;					//로그인 요청
	final static int JOIN = 1;						//회원 가입
	final static int LIST = 2;						//로그인 성공 후 리스트 요청
	final static int MUSIC = 3;					//개인 리스트 요청
	final static int DROP = 4;					//탈퇴
	final static int LOGOUT = 5;				//로그아웃
	final static int TOTAL_LIST = 6;		//서버 전체 음악 리스트
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

		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(
						socket.getInputStream()));) 
		{
//			while(true)
//			{
				int state = LOGIN;
				out.println(state);
				out.flush();
				System.out.println("ID 입력");
				String id = s.next();
				out.println(id);
				out.flush();
				System.out.println("PW 입력");
				String pw = s.next();
				out.println(pw);
				out.flush();
//				System.out.println("email 입력");
//				String email = s.next();
//				out.println(email);
//				out.flush();
				String textFS = in.readLine();
					System.out.println("server : " + textFS);
//			}
		} catch (Exception e) {
			System.err.println("받기 실패");
		}

//		ObjectInputStream in = new ObjectInputStream(
//												new BufferedInputStream(
//													socket.getInputStream()));
//		List<String> list = (ArrayList<String>) in.readObject();
//		System.out.println("server : " + list.toString());
		
		socket.close();
		System.out.println("connection end");
	}
}
