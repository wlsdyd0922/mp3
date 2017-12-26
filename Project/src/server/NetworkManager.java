package server;

import java.io.*;
import java.net.*;
import java.util.*;


public class NetworkManager extends Thread{
	private boolean flag = true;
	
	private static List<NetworkManager> list = new ArrayList<>();
	public static void add(NetworkManager c) {
		list.add(c);
	}
	public static void remove(NetworkManager c) {
		list.remove(c);
	}
	
	public void kill() {
		try {
			flag = false;
			in.close();
			out.close();
			socket.close();
		}catch(Exception e) {	}
		remove(this);
	}
	
	final static int LOGIN = 0;					//로그인 요청
	final static int JOIN = 1;						//회원 가입
	final static int LIST = 2;						//로그인 성공 후 리스트 요청
	final static int MUSIC = 3;					//개인 리스트 요청
	final static int DROP = 4;					//탈퇴
	final static int LOGOUT = 5;				//로그아웃
	final static int TOTAL_LIST = 6;		//서버 전체 음악 리스트
//	final static int UPLOAD = 6;				//음악 업로드
//	final static int MUSIC_DEL = 7;			//음악 삭제
	
	private ServerSocket server; 
	private Socket socket;
	private BufferedReader in;
	//private ObjectInputStream in; 
	private PrintWriter out;
	private int port;
	
	private MemberManager memM;
	private MusicManager musM;
	//private DatagramSocket ds;
	
	public NetworkManager(Socket socket)
	{
		port = 20000;
		this.socket = socket;
		System.out.println("networkManager : " + socket.toString());
		memM = new MemberManager();
//			try {
//				server = new ServerSocket(port);
//				socket = server.accept();
//			} catch (IOException e) {
//				System.out.println("server socket create error");
//			}

			try {
			in = new BufferedReader(
					new InputStreamReader(
							this.socket.getInputStream()));

			out = new PrintWriter(
						new BufferedWriter(
							new OutputStreamWriter(
									this.socket.getOutputStream())));
//
//				in = new ObjectInputStream(
//						new BufferedInputStream(
//								this.socket.getInputStream()));
//
//				out = new PrintWriter(
//							new BufferedWriter(
//								new OutputStreamWriter(
//										this.socket.getOutputStream())));
			this.setDaemon(true);
			this.start();
			} catch (IOException e) {
				e.getStackTrace();
				System.out.println("in/out stream error");
			}
	}
	
	public void run() {
		try {
		while (flag) {
			int state;
			String id = null;
			String pw = null;
			String email = null;
			String musicTitle = null;
				state = in.read();
				switch (state) {
				case JOIN : 
					System.out.println(socket.toString() + " 가입 요청");
					//id = (String)in.readObject();
					id = in.readLine();
					System.out.println("client id : " + id);
					//pw = (String)in.readObject();
					pw = in.readLine();
					System.out.println("client pw : " + pw);
					//email = (String)in.readObject();
					email = in.readLine();
					System.out.println("client email : " + email);
					boolean joinResult = memM.memberAccept(id, pw, email);
					out.print(joinResult);
					out.flush();
					memM.memberDisplay();
					kill();
					break;
					
				case LOGIN:
					//id = (String)in.readObject();
					id = in.readLine();
					System.out.println("client : " + id);
					//pw = (String)in.readObject();
					pw = in.readLine();
					System.out.println("client : " + pw);

					boolean loginResult  = memM.login(id, pw);
					
					out.print(loginResult);
					out.flush();
					break;

				case LIST:
					//id = (String)in.readObject();
					id = in.readLine();
					System.out.println(id + " 개인 리스트 요청");
					listSender(id);
					break;
					
				case TOTAL_LIST:
					//id = (String)in.readObject();
					id = in.readLine();
					System.out.println(id + " 전체 리스트 요청");
					listSender(null);
					break;

				case MUSIC:
					//id = (String)in.readObject();
					id = in.readLine();
					System.out.println(id + " 음악 파일 요청");
					//musicTitle = (String)in.readObject();
					musicTitle = in.readLine();
					musicSender(id, musicTitle);
					break;

//				case UPLOAD:
//					id = in.readLine();
//					System.out.println(id + " 음악 파일 업로드");
//					musicTitle = in.readLine();
//					musicReceiver(id, musicTitle);
//					break;

				case LOGOUT : 
					//id = (String)in.readObject();
					id = in.readLine();
					System.out.println(id + " 로그아웃");
					kill();
					break;
					
				case DROP:
					//id = (String)in.readObject();
					id = in.readLine();
					System.out.println(id + " 탈퇴");
					boolean dropResult = memM.memberDrop(id);
					if(dropResult)
						kill();
					break;
					
				default:
					//id = (String)in.readObject();
					id = in.readLine();
					System.out.println(id + ": 잘못된 요청");
					break;
				}
		}
		}
			catch (Exception e) 
			{
				kill();
			}
			
//			try
//			{
//				socket.close();
//				server.close();
//			} 
//			catch (IOException e)
//			{
//				System.out.println("socket/server close error");
//				e.printStackTrace();
//			}
		}
	
	
	public boolean listSender(String id) 
	{
		List<String> musics = null;
		if(id != null)
			musics = musM.readMusicList(id);
		else
			musics = musM.loadServerList();
		try (ObjectOutputStream out = new ObjectOutputStream(
															socket.getOutputStream());) 
		{
			out.writeObject(musics);
			out.flush();
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
		File file = new File(musicTitle);
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
			System.out.println("음악 전송 완료");
			fis.close();
			ds.close();
			return true;
		} 
		catch (Exception e) 
		{
			System.out.println("음악 전송 실패");
			return false;
		}
	}
}