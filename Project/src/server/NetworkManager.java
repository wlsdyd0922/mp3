package server;

import java.io.*;
import java.net.*;
import java.util.*;


public class NetworkManager extends Thread{
	private boolean flag = true;
	
	final static int LOGIN = 0;					//로그인 요청
	final static int JOIN = 1;						//회원 가입
	final static int LIST = 2;						//로그인 성공 후 리스트 요청
	final static int MUSIC = 3;					//개인 리스트 요청
	final static int DROP = 4;					//탈퇴
	final static int LOGOUT = 5;				//로그아웃
	final static int TOTAL_LIST = 6;		//서버 전체 음악 리스트
//	final static int UPLOAD = 6;				//음악 업로드
//	final static int MUSIC_DEL = 7;			//음악 삭제
	
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
					System.out.println(socket.toString() + " 가입 요청");
					id = in.readLine();
					System.out.println(socket.getInetAddress() + " id : " + id);
					pw = in.readLine();
					System.out.println(socket.getInetAddress() + " pw : " + pw);
					email = in.readLine();
					System.out.println(socket.getInetAddress() + " email : " + email);

					boolean joinResult = memM.memberAccept(id, pw, email);
					out.println(joinResult);
					out.flush();

					System.out.println(socket.getInetAddress() + " : 회원가입 결과 " + joinResult);
					memM.memberDisplay();
					socket.close();
					break;

				case LOGIN:
					System.out.println(socket.getInetAddress() + " 로그인 시도");

					id = in.readLine();
					System.out.println(socket.getInetAddress() + " : id " + id);
					pw = in.readLine();
					System.out.println(socket.getInetAddress() + " : pw " + pw);

					boolean loginResult = memM.login(id, pw);
					out.println(loginResult);
					out.flush();
					System.out.println(socket.getInetAddress() + " 로그인 결과 :  " + loginResult);
					//listSender(id);
					//socket.close();
					break;

				case LIST:
					id = in.readLine();
					System.out.println(id + " 개인 리스트 요청");
					listSender(id);
					break;

				case TOTAL_LIST:
					id = in.readLine();
					System.out.println(id + " 전체 리스트 요청");
					listSender("server");
					break;

				case MUSIC:
					id = in.readLine();
					System.out.println(id + " 음악 파일 요청");
					musicTitle = in.readLine();
					musicSender(id, musicTitle);
					break;

				// case UPLOAD:
				// id = in.readLine();
				// System.out.println(id + " 음악 파일 업로드");
				// musicTitle = in.readLine();
				// musicReceiver(id, musicTitle);
				// break;

				case LOGOUT:
					id = in.readLine();
					System.out.println(id + " 로그아웃");
					socket.close();
					break;

				case DROP:
					id = in.readLine();
					System.out.println(id + " 탈퇴");
					boolean dropResult = memM.memberDrop(id);
					if (dropResult)
						socket.close();
					break;

				default:
					id = in.readLine();
					System.out.println(id + ": 잘못된 요청");
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
			System.out.println("서버 리스트 전송 : " + music);
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