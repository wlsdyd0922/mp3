package server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class NetworkManager extends Thread{
	private boolean flag = true;
	
	private Map<String,ArrayList<String>> ipList = new HashMap<>();
	
	final static int LOGIN_CONFIRM = -1;//클라이언트 로그인 재확인 요청값
	final static int LOGIN = 0;					//로그인 요청
	final static int JOIN = 1;						//회원 가입
	final static int LIST = 2;						//개인 리스트 요청
	final static int MUSIC = 3;					//음악 파일 다운 요청
	final static int DROP = 4;					//탈퇴
	final static int LOGOUT = 5;				//로그아웃
	final static int TOTAL_LIST = 6;		//서버 전체 음악 리스트
	final static int MUSIC_ADD = 7;		//음악 추가
	final static int MUSIC_DEL = 8;			//음악 삭제
	
	private Date today = new Date();
	private SimpleDateFormat f = new SimpleDateFormat("MM-dd HH:mm:ss");
	
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out; 
	private int port;
	
	private MemberManager memM;
	private MusicManager musM;
	
	public void kill()
	{
//		status = false;
		flag = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NetworkManager(Socket socket)
	{
		port = 20000;
		this.socket = socket;
		System.out.println("networkManager : " + socket.toString());
		memM = new MemberManager();
		musM = new MusicManager();
		
		try {
			out = new ObjectOutputStream(
					socket.getOutputStream());
			//System.out.println(out);
			
			in = new ObjectInputStream(
					new BufferedInputStream(
							this.socket.getInputStream()));
			//System.out.println(in);
			
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
				System.out.println(f.format(today));
				System.out.println(socket.toString());
				int state;
				String id = null;
				String pw = null;
				String email = null;
				String musicTitle = null;
				state = (int)in.readObject();
				switch (state) 
				{
				case JOIN:
					System.out.println(f.format(today));
					System.out.println(socket.toString() + " 가입 요청");
					id = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " id : " + id);
					pw = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " pw : " + pw);
					email = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " email : " + email);

					boolean joinResult = memM.memberAccept(id, pw, email);
					out.writeObject(joinResult);
					out.flush();

					System.out.println(socket.getInetAddress() + " : 회원가입 결과 " + joinResult);
					memM.memberDisplay();
					kill();
					break;

				case LOGIN:
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress() + " 로그인 시도");

					id = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " : id = " + id);
					pw = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " : pw = " + pw);

					boolean loginResult = memM.login(id, pw);
					out.writeObject(loginResult);
					out.flush();
					System.out.println(socket.getInetAddress() + " 로그인 결과 :  " + loginResult);
					break;

				case LIST:
					id = (String)in.readObject();
//					if(!status)
//					{
//						System.out.println(socket.getInetAddress() + " : 비 로그인 상태로 접속 시도");
//						out.writeObject(LOGIN_CONFIRM);
//						out.writeObject("로그인 필요");
//						out.flush();
//					}
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress());
					System.out.println(id + " 개인 리스트 요청");
					listSender(id);
					kill();
					break;

				case TOTAL_LIST:
					id = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress());
					System.out.println(id + " 전체 리스트 요청");
					listSender("server");
					break;

				case MUSIC:
					id = (String)in.readObject();
					System.out.println(f.format(today));
					musicTitle = (String)in.readObject();
					System.out.println(socket.getInetAddress());
					System.out.println(id + " 음악 파일 요청 " + musicTitle);
					musicSender(id, musicTitle);
					break;

				case LOGOUT:
					id = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress());
					System.out.println(id + " 로그아웃");
					break;

				case DROP:
					id = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress());
					System.out.println(id + " 탈퇴");
					boolean dropResult = memM.memberDrop(id);
					if (dropResult)
					{
						out.writeObject(dropResult);
						out.flush();
						kill();
					}
					else
					{
						out.writeObject("다시 로그인");
						out.flush();
						kill();
					}
					break;
					
				case MUSIC_ADD :
					id = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress() +" " + id + " : 음악 추가 신청");
					@SuppressWarnings("unchecked") 
					List<String> addmusic = (List<String>) in.readObject();
					System.out.println(id + " : " + addmusic);
					System.out.println(socket.getInetAddress() + " " + addmusic + "리스트에 추가");
					boolean addResult =musM.addToMusicList(id, addmusic);
					out.writeObject(addResult);
					out.flush();
					break;
					
				case MUSIC_DEL:
					id = (String)in.readObject();
					System.out.println(socket.getInetAddress() +" " + id + " : 음악 삭제 신청");
					String delmusic = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(id + " " + delmusic + "삭제");
					boolean delResult =musM.deleteMusic(id, delmusic);
					out.writeObject(delResult);
					out.flush();
					kill();
					break;
					
				default:
					id = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(id + ": 잘못된 요청");
					out.writeObject("잘못된 접근");
					out.flush();
					kill();
					break;
				}
			}
		}
		catch (Exception e) 
		{
//			try {
//				socket.close();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
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
		
		try
		{
			System.out.println(socket.getInetAddress() + " : "+id +" 서버 리스트 전송 : " + music);
			
			out.writeObject(music);
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
		File file = new File("musics",musicTitle);
		DatagramSocket ds = null;
		InetAddress inet = socket.getInetAddress();
		
		if (!file.exists())
		{
			System.out.println("File not exist");
			return false;
		}
		long fileSize = file.length();
		long totalReadBytes = 0;

		try {
			ds = new DatagramSocket();
			String str = "start";
			DatagramPacket dp = new DatagramPacket(str.getBytes(), str.getBytes().length, inet, port);
			ds.send(dp);
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[65000];

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
				Thread.sleep(1);
//				System.out.println("In progress: " + totalReadBytes + "/" + fileSize + " Byte(s) ("
//						+ (totalReadBytes * 100 / fileSize) + " %)");
			}
			str = "end";
			dp = new DatagramPacket(str.getBytes(), str.getBytes().length, inet, port);
			ds.send(dp);
			System.out.println(socket.getInetAddress() + " : "+id + "음악 전송 완료");
			fis.close();
			ds.close();
			return true;
		} 
		catch (Exception e) 
		{
			System.out.println(socket.getInetAddress() + " : "+id + "음악 전송 실패");
			return false;
		}
	}

	public void ipSave(String id) 
	{
		if(ipList.get(id) == null)
		ipList.put(id, new ArrayList<String>());
		ipList.get(id).add(socket.getInetAddress().toString());
	}
}