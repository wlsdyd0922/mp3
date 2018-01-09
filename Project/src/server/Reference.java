package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	final static int LOGIN = 0; // �α��� ��û
	final static int JOIN = 1; // ȸ�� ����
	final static int LIST = 2; // ���� ����Ʈ ��û
	final static int MUSIC = 3; // ���� ���� �ٿ� ��û
	final static int DROP = 4; // Ż��
	final static int LOGOUT = 5; // �α׾ƿ�
	final static int TOTAL_LIST = 6; // ���� ��ü ���� ����Ʈ
	final static int MUSIC_ADD = 7; // ���� �߰�
	final static int MUSIC_DEL = 8; // ���� ����
	final static int LYRIC_CALL = 9;		//���� ��û
	final static int LYRIC_ADD = 10;			//���� �߰�
	
	public static void musicReceive(int port, String music) {
		////////////////////// udp ��������////////////////////////
		byte[] buffer = new byte[65000];
		long fileSize;
		long totalReadBytes = 0;

		try {
			int nReadSize = 0;
			System.out.println("Waitng.....");
			DatagramSocket ds = new DatagramSocket(port);
			FileOutputStream fos = null;
			fos = new FileOutputStream(new File("E:\\GNS3",music));
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
		File target = new File("data","IP.txt");
		int size = (int) target.length();
		byte[] buffer = new byte[size];
		FileInputStream ipIn = new FileInputStream(target);
		int n = ipIn.read(buffer);
		ipIn.close();
		
//		boolean flag = true;
//		Thread th1 = new Thread()
//				{
//					public void run()
//					{
//						while(flag)
//						{
//							int tick = total / playTime;
//							skip+=tick;
//						}
//					}
//				};
//				th1.setDaemon(true);
//				th1.start();
		
		String ip = new String(buffer);
		int port = 20000;
		System.out.println(ip);
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);

		InetAddress inet = InetAddress.getByName(ip);
		socket = new Socket(inet, port);
		
		ObjectInputStream in = new ObjectInputStream(
				new BufferedInputStream(
						socket.getInputStream()));
		
		System.out.println(in);
		
		ObjectOutputStream out = new ObjectOutputStream(
				socket.getOutputStream());

		System.out.println(out);
		
		System.out.println("�⵿");
		while (true) 
		{
			System.out.println("0 �α���\n"
					+ "1 ȸ�� ����\n"
					+ "2 �� ����Ʈ ����\n" 
					+ "3 ���� ���� �ޱ�\n" 
					+ "4 Ż��\n" 
					+ "5 �α׾ƿ�\n" 
					+ "6 ���� ���� ����Ʈ\n"
					+ "7 ���� �߰�\n" 
					+ "8 ���� ����\n"
					+ "1024735 ���� �߰�");

			int state = s.nextInt();
			out.writeObject(state);
			out.flush();

			System.out.println("ID �Է�");
			String id = s.next();
			out.writeObject(id);
			out.flush();

			switch (state) {
			case LOGIN:
				System.out.println("PW �Է�");
				String pw = s.next();
				out.writeObject(pw);
				out.flush();
				try {
					Boolean textFS = (Boolean) in.readObject();
					System.out.println(textFS);

				} catch (Exception e) {
					System.err.println("�ޱ� ����");
				}
				break;

			case JOIN:
				System.out.println("PW �Է�");
				pw = s.next();
				out.writeObject(pw);
				out.flush();

				System.out.println("email �Է�");
				String email = s.next();
				out.writeObject(email);
				out.flush();

				try {
					Boolean textFS = (Boolean) in.readObject();
					System.out.println("server : " + textFS);

				} catch (Exception e) {
					System.err.println("�ޱ� ����");
				}
				break;

			case LIST:
				@SuppressWarnings("unchecked") List<String> list = (ArrayList<String>) in.readObject();
				System.out.println("server : " + list.toString());
				break;

			case MUSIC:
				System.out.println("���ϸ� �Է�");
				String music = s.next();
				out.writeObject(music);
				out.flush();
				musicReceive(port, music);
				break;

			case DROP: // Ż��
				try {
					Boolean textFS = (Boolean) in.readObject();
					System.out.println("server : " + textFS);

				} catch (Exception e) {
					System.err.println("�ޱ� ����");
				}
				break;

			case LOGOUT:
				try 
				{
					Boolean textFS = (Boolean) in.readObject();
					System.out.println("server : " + textFS);

				} catch (Exception e) {
					System.err.println("�ޱ� ����");
				}
				break;

			case TOTAL_LIST: // ���� ��ü ���� ����Ʈ
				
				@SuppressWarnings("unchecked")
				List<String> tlist = (ArrayList<String>) in.readObject();
				System.out.println("server : " + tlist.toString());
				break;

			case MUSIC_ADD: // ���� �߰�, ����Ʈȭ �Ǿ����Ƿ� ����
				System.out.println("�� ���� �Է�");
				String song = s.next();
				out.writeObject(song);
				out.flush();

				try {
					Boolean textFS = (Boolean) in.readObject();
					System.out.println("server : " + textFS);

				} catch (Exception e) {
					System.err.println("�ޱ� ����");
				}
				break;

			case MUSIC_DEL: // ���� ����
				System.out.println("�� ���� �Է�");
				String dsong = s.next();
				out.writeObject(dsong);
				out.flush();
				try {
					Boolean textFS = (Boolean) in.readObject();
					System.out.println("server : " + textFS);

				} catch (Exception e) {
					System.err.println("�ޱ� ����");
				}
				break;

			case LYRIC_ADD:
				System.out.println("�� ���� �Է�");
				String title = s.next();
				out.writeObject(title);
				out.flush();
				System.out.println("���� �Է�");
				String lyric = s.next();
				out.writeObject(lyric);
				out.flush();
				boolean result = (Boolean)in.readObject();
				System.out.println(result);
				break;
				
			default:
				System.out.println("�ٽ� �Է�");
				break;
			}
		}
	}
}
