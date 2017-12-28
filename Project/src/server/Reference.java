package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reference {
	static boolean flag = true;
	final static int LOGIN = 0; // 로그인 요청
	final static int JOIN = 1; // 회원 가입
	final static int LIST = 2; // 개인 리스트 요청
	final static int MUSIC = 3; // 음악 파일 다운 요청
	final static int DROP = 4; // 탈퇴
	final static int LOGOUT = 5; // 로그아웃
	final static int TOTAL_LIST = 6; // 서버 전체 음악 리스트
	final static int MUSIC_ADD = 7; // 음악 추가
	final static int MUSIC_DEL = 8; // 음악 삭제

	public static void musicReceive(int port, String music) {
		////////////////////// udp 파일전송////////////////////////
		byte[] buffer = new byte[1024];
		long fileSize;
		long totalReadBytes = 0;

		try {
			int nReadSize = 0;
			System.out.println("Waitng.....");
			DatagramSocket ds = new DatagramSocket(port);
			FileOutputStream fos = null;
			fos = new FileOutputStream(music);
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			ds.receive(dp);
			String str = new String(dp.getData()).trim();

			if (str.equals("start")) {
				System.out.println(str);
				dp = new DatagramPacket(buffer, buffer.length);
				ds.receive(dp);
				str = new String(dp.getData()).trim();
				fileSize = Long.parseLong(str);
				while (true) {
					ds.receive(dp);
					str = new String(dp.getData()).trim();
					nReadSize = dp.getLength();
					fos.write(dp.getData(), 0, nReadSize);
					Thread.sleep(1);
					totalReadBytes += nReadSize;
					System.out.println("In progress: " + totalReadBytes + "/" + fileSize + " Byte(s) ("
							+ (totalReadBytes * 100 / fileSize) + " %)");
					if (totalReadBytes >= fileSize)
						break;
				}

				System.out.println("File transfer completed");
				fos.close();
				ds.close();
			} else {
				System.out.println("Start Error");
				fos.close();
				ds.close();
			}
		} catch (Exception e) {
		}
		System.out.println("Process Close");
	}
	static Socket socket;
	public static void main(String[] args) throws Exception {
		String ip = "192.168.0.171";
		int port = 20000;
		System.out.println(ip);
		Scanner s = new Scanner(System.in);

		InetAddress inet = InetAddress.getByName(ip);
		socket = new Socket(inet, port);
		
		BufferedReader bin = new BufferedReader(
				new InputStreamReader(
						socket.getInputStream()));
		
		System.out.println(socket);
		PrintWriter out = new PrintWriter(
				new BufferedWriter(
						new OutputStreamWriter(
								socket.getOutputStream())));

		ObjectInputStream oin = new ObjectInputStream(
				new BufferedInputStream(
						socket.getInputStream()));
		
		System.out.println("기동");
		while (true) 
		{
			System.out.println("0 로그인\n"
					+ "1 회원 가입\n"
					+ "2 내 리스트 보기\n" 
					+ "3 음악 파일 받기\n" 
					+ "4 탈퇴\n" 
					+ "5 로그아웃\n" 
					+ "6 서버 음악 리스트\n"
					+ "7 음악 추가\n" 
					+ "8 음악 삭제\n");

			int state = s.nextInt();
			out.println(state);
			out.flush();

			System.out.println("ID 입력");
			String id = s.next();
			out.println(id);
			out.flush();

			switch (state) {
			case LOGIN:
				System.out.println("PW 입력");
				String pw = s.next();
				out.println(pw);
				out.flush();
				try {String textFS = bin.readLine();
					System.out.println("server : " + textFS);

				} catch (Exception e) {
					System.err.println("받기 실패");
				}
				break;

			case JOIN:
				System.out.println("PW 입력");
				pw = s.next();
				out.println(pw);
				out.flush();

				System.out.println("email 입력");
				String email = s.next();
				out.println(email);
				out.flush();

				try {
					String textFS = bin.readLine();
					System.out.println("server : " + textFS);

				} catch (Exception e) {
					System.err.println("받기 실패");
				}
				break;

			case LIST:
				List<String> list = (ArrayList<String>) oin.readObject();
				System.out.println("server : " + list.toString());
				break;

			case MUSIC:
				System.out.println("파일명 입력");
				String music = s.next();
				out.println(music);
				out.flush();
				musicReceive(port, music);
				break;

			case DROP: // 탈퇴
				try {
					String textFS = bin.readLine();
					System.out.println("server : " + textFS);

				} catch (Exception e) {
					System.err.println("받기 실패");
				}
				break;

			case LOGOUT:
				try 
				{
					String textFS = bin.readLine();
					System.out.println("server : " + textFS);

				} catch (Exception e) {
					System.err.println("받기 실패");
				}
				break;

			case TOTAL_LIST: // 서버 전체 음악 리스트
				
				List<String> tlist = (ArrayList<String>) oin.readObject();
				System.out.println("server : " + tlist.toString());
				break;

			case MUSIC_ADD: // 음악 추가
				System.out.println("곡 제목 입력");
				String song = s.next();
				out.println(song);
				out.flush();

				try {
					String textFS = bin.readLine();
					System.out.println("server : " + textFS);

				} catch (Exception e) {
					System.err.println("받기 실패");
				}
				break;

			case MUSIC_DEL: // 음악 삭제
				System.out.println("곡 제목 입력");
				String dsong = s.next();
				out.println(dsong);
				out.flush();
				try {
					String textFS = bin.readLine();
					System.out.println("server : " + textFS);

				} catch (Exception e) {
					System.err.println("받기 실패");
				}
				break;

			default:
				System.out.println("다시 입력");
				break;
			}

			//////////////// 일반 데이터 수신//////////////////////////////
			// try (BufferedReader bin = new BufferedReader(
			// new InputStreamReader(
			// socket.getInputStream()));)
			// {
			// String textFS = bin.readLine();
			// System.out.println("server : " + textFS);
			//
			// } catch (Exception e) {
			// System.err.println("받기 실패");
			// }

			///////////////////////////////////////////////////////////////////////
		}
		// socket.close();
		// System.out.println("connection end");
	}
}
