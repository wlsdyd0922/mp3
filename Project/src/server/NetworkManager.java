package server;

import java.io.*;
import java.net.*;
import java.util.*;


public class NetworkManager extends Thread{
	private boolean flag = true;
	
	final static int LOGIN = 0;					//�α��� ��û
	final static int JOIN = 1;						//ȸ�� ����
	final static int LIST = 2;						//�α��� ���� �� ����Ʈ ��û
	final static int MUSIC = 3;					//���� ����Ʈ ��û
	final static int DROP = 4;					//Ż��
	final static int LOGOUT = 5;				//�α׾ƿ�
	final static int TOTAL_LIST = 6;		//���� ��ü ���� ����Ʈ
//	final static int UPLOAD = 6;				//���� ���ε�
//	final static int MUSIC_DEL = 7;			//���� ����
	
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private int port;
	
	private MemberManager memM;
	private MusicManager musM;
	
	public NetworkManager(Socket socket)
	{
		port = 20000;
		this.socket = socket;
		System.out.println("networkManager : " + socket.toString());
		memM = new MemberManager();
		musM = new MusicManager();

			try {
			in = new BufferedReader(
					new InputStreamReader(
							this.socket.getInputStream()));

			out = new PrintWriter(
						new BufferedWriter(
							new OutputStreamWriter(
									this.socket.getOutputStream())));

			this.setDaemon(true);
			this.start();
			} catch (IOException e) {
				System.err.println("in/out stream error");
			}
	}
	
	public void run() 
	{
		try {
			while (flag) {
				int state;
				String id = null;
				String pw = null;
				String email = null;
				String musicTitle = null;
				state = Integer.parseInt(in.readLine());
				switch (state) {
				case JOIN:
					System.out.println(socket.toString() + " ���� ��û");
					id = in.readLine();
					System.out.println(socket.getInetAddress() + " id : " + id);
					pw = in.readLine();
					System.out.println(socket.getInetAddress() + " pw : " + pw);
					email = in.readLine();
					System.out.println(socket.getInetAddress() + " email : " + email);

					boolean joinResult = memM.memberAccept(id, pw, email);
					out.println(joinResult);
					out.flush();

					System.out.println(socket.getInetAddress() + " : ȸ������ ��� " + joinResult);
					memM.memberDisplay();
					socket.close();
					break;

				case LOGIN:
					System.out.println(socket.getInetAddress() + " �α��� �õ�");

					id = in.readLine();
					System.out.println(socket.getInetAddress() + " : id " + id);
					pw = in.readLine();
					System.out.println(socket.getInetAddress() + " : pw " + pw);

					boolean loginResult = memM.login(id, pw);
					out.println(loginResult);
					out.flush();
					System.out.println(socket.getInetAddress() + " �α��� ��� :  " + loginResult);
					//listSender(id);
					//socket.close();
					break;

				case LIST:
					id = in.readLine();
					System.out.println(id + " ���� ����Ʈ ��û");
					listSender(id);
					break;

				case TOTAL_LIST:
					id = in.readLine();
					System.out.println(id + " ��ü ����Ʈ ��û");
					listSender("server");
					break;

				case MUSIC:
					id = in.readLine();
					System.out.println(id + " ���� ���� ��û");
					musicTitle = in.readLine();
					musicSender(id, musicTitle);
					break;

				// case UPLOAD:
				// id = in.readLine();
				// System.out.println(id + " ���� ���� ���ε�");
				// musicTitle = in.readLine();
				// musicReceiver(id, musicTitle);
				// break;

				case LOGOUT:
					id = in.readLine();
					System.out.println(id + " �α׾ƿ�");
					socket.close();
					break;

				case DROP:
					id = in.readLine();
					System.out.println(id + " Ż��");
					boolean dropResult = memM.memberDrop(id);
					if (dropResult)
						socket.close();
					break;

				default:
					id = in.readLine();
					System.out.println(id + ": �߸��� ��û");
					break;
				}
			}
		}
		catch (Exception e) 
		{
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	public boolean listSender(String id) 
	{
		System.out.println("list Sender id : " + id);
		List<String> music = null;
		
		if(!id.equals("server"))
			music = musM.readMusicList(id);
		else
			music = musM.loadServerList();
		
		try (ObjectOutputStream out = new ObjectOutputStream(
															socket.getOutputStream());) 
		{
			System.out.println("���� ����Ʈ ���� : " + music);
			out.writeObject(music);
			out.close();
		} 
		catch (Exception e) 
		{
			System.err.println("stream output error");
			return false;
		}
		return true;
	} 
	
	public boolean musicSender(String id,String musicTitle)
	{
		File file = new File("musics",musicTitle);
		DatagramSocket ds = null;
		InetAddress inet = null;
		
		if (!file.exists()) {
			System.out.println("File not exist");
			return false;
		}
		long fileSize = file.length();
		long totalReadBytes = 0;

		try {
			inet = InetAddress.getByName("192.168.0.132");
			ds = new DatagramSocket();
			String str = "start";
			DatagramPacket dp = new DatagramPacket(str.getBytes(), str.getBytes().length, inet, port);
			ds.send(dp);
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];

			str = String.valueOf(fileSize);
			dp = new DatagramPacket(str.getBytes(), str.getBytes().length, inet, port);
			ds.send(dp);

			while (true) {
				int readBytes = fis.read(buffer, 0, buffer.length);
				if (readBytes == -1)
					break;
				dp = new DatagramPacket(buffer, readBytes, inet, port);
				ds.send(dp);
				totalReadBytes += readBytes;
				System.out.println("In progress: " + totalReadBytes + "/" + fileSize + " Byte(s) ("
						+ (totalReadBytes * 100 / fileSize) + " %)");

			}
			str = "end";
			dp = new DatagramPacket(str.getBytes(), str.getBytes().length, inet, port);
			ds.send(dp);
			System.out.println("���� ���� �Ϸ�");
			fis.close();
			ds.close();
			return true;
		} 
		catch (Exception e) 
		{
			System.out.println("���� ���� ����");
			return false;
		}
	}
}