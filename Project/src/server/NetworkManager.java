package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class NetworkManager extends Thread{
	private boolean flag = true;
	private boolean status = false;
	private String uId="";
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
	
	private Socket socket;
	//private BufferedReader in;
	private ObjectInputStream in;
	private ObjectOutputStream out; 
	private int port;
	
	private MemberManager memM;
	private MusicManager musM;
	
	public void kill()
	{
		status = false;
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
//			in = new BufferedReader(
//					new InputStreamReader(
//							this.socket.getInputStream()));
			
			out = new ObjectOutputStream(
					socket.getOutputStream());
			System.out.println(out);
			
			in = new ObjectInputStream(
					new BufferedInputStream(
							this.socket.getInputStream()));
			System.out.println(in);
			
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
				System.out.println(socket.toString());
				int state;
				//String id = null;
				String pw = null;
				String email = null;
				String musicTitle = null;
				state = (int)in.readObject();
				switch (state) 
				{
				case JOIN:
					System.out.println(socket.toString() + " 가입 요청");
					uId = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " id : " + uId);
					pw = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " pw : " + pw);
					email = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " email : " + email);

					boolean joinResult = memM.memberAccept(uId, pw, email);
					out.writeObject(joinResult);
					out.flush();

					System.out.println(socket.getInetAddress() + " : 회원가입 결과 " + joinResult);
					memM.memberDisplay();
					socket.close();
					break;

				case LOGIN:
					System.out.println(socket.getInetAddress() + " 로그인 시도");

					uId = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " : id " + uId);
					pw = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " : pw " + pw);

					boolean loginResult = memM.login(uId, pw);
					out.writeObject(loginResult);
					out.flush();
					status = loginResult;
					//uId = id;
					System.out.println(socket.getInetAddress() + " 로그인 결과 :  " + loginResult);
					break;

				case LIST:
					//id = (String)in.readObject();
//					if(!status)
//					{
//						System.out.println(socket.getInetAddress() + " : 비 로그인 상태로 접속 시도");
//						out.writeObject(LOGIN_CONFIRM);
//						out.writeObject("로그인 필요");
//						out.flush();
//					}
					System.out.println(uId + " 개인 리스트 요청");
					listSender(uId);
					break;

				case TOTAL_LIST:
					//id = (String)in.readObject();
//					if(!status)
//					{
//						System.out.println(socket.getInetAddress() + " : 비 로그인 상태로 접속 시도");
//						out.writeObject(LOGIN_CONFIRM);
//						out.writeObject("로그인 필요");
//						out.flush();
//					}
					System.out.println(uId + " 전체 리스트 요청");
					listSender("server");
					break;

				case MUSIC:
					//id = (String)in.readObject();
//					if(!status)
//					{
//						System.out.println(socket.getInetAddress() + " : 비 로그인 상태로 접속 시도");
//						out.writeObject(LOGIN_CONFIRM);
//						out.writeObject("로그인 필요");
//						out.flush();
//					}
					System.out.println(uId + " 음악 파일 요청");
					musicTitle = (String)in.readObject();
					musicSender(uId, musicTitle);
					break;

				// case UPLOAD:
				// id = in.readLine();
				// System.out.println(id + " 음악 파일 업로드");
				// musicTitle = in.readLine();
				// musicReceiver(id, musicTitle);
				// break;

				case LOGOUT:
					//id = (String)in.readObject();
//					if(!status)
//					{
//						System.out.println(socket.getInetAddress() + " : 비 로그인 상태로 접속 시도");
//						out.writeObject(LOGIN_CONFIRM);
//						out.writeObject("로그인 필요");
//						out.flush();
//					}
					System.out.println(uId + " 로그아웃");
					out.writeObject(true);
					out.flush();
					kill();
					break;

				case DROP:
					//id = (String)in.readObject();
//					if(!status)
//					{
//						System.out.println(socket.getInetAddress() + " : 비 로그인 상태로 접속 시도");
//						out.writeObject(LOGIN_CONFIRM);
//						out.writeObject("로그인 필요");
//						out.flush();
//					}
					System.out.println(uId + " 탈퇴");
					boolean dropResult = memM.memberDrop(uId);
					if (dropResult)
					{
						//out.println(dropResult);
						out.writeObject(dropResult);
						out.flush();
						kill();
					}
					else
					{
						//out.println("다시 로그인");
						out.writeObject("다시 로그인");
						out.flush();
						kill();
					}
					break;
					
				case MUSIC_ADD :
					//id = (String)in.readObject();
//					if(!status)
//					{
//						System.out.println(socket.getInetAddress() + " : 비 로그인 상태로 접속 시도");
//						out.writeObject(LOGIN_CONFIRM);
//						out.writeObject("로그인 필요");
//						out.flush();
//					}
					System.out.println(socket.getInetAddress() +" " + uId + " : 음악 추가 신청");
//					String addmusic = in.readLine();
					@SuppressWarnings("unchecked") 
					List<String> addmusic = (List<String>) in.readObject();
					System.out.println(uId + " : " + addmusic);
					System.out.println(socket.getInetAddress() + " " + addmusic + "리스트에 추가");
					boolean addResult =musM.addToMusicList(uId, addmusic);
					//out.println(addResult);
					out.writeObject(addResult);
					out.flush();
					status = true;
					break;
					
				case MUSIC_DEL:
					//id = (String)in.readObject();
//					if(!status)
//					{
//						System.out.println(socket.getInetAddress() + " : 비 로그인 상태로 접속 시도");
//						out.writeObject(LOGIN_CONFIRM);
//						out.writeObject("로그인 필요");
//						out.flush();
//					}
					System.out.println(socket.getInetAddress() +" " + uId + " : 음악 삭제 신청");
					String delmusic = (String)in.readObject();
					System.out.println(uId + " " + delmusic + "삭제");
					boolean delResult =musM.deleteMusic(uId, delmusic);
					//out.println(delResult);
					out.writeObject(delResult);
					out.flush();
					break;
					
				default:
					//id = (String)in.readObject();
					System.out.println(uId + ": 잘못된 요청");
//					if(!status)
//					{
//						System.out.println(socket.getInetAddress() + " : 비 로그인 상태로 접속 시도");
//						out.writeObject(LOGIN_CONFIRM);
//						out.writeObject("로그인 필요");
//						out.flush();
//					}
					out.writeObject("잘못된 접근");
					out.flush();
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
		
		if (music.isEmpty())
		music.add("추가된 음악이 없습니다");
		
		try
		{
			System.out.println("서버 리스트 전송 : " + music);
			
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
				Thread.sleep(1);
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

	public void ipSave(String id) 
	{
		if(ipList.get(id) == null)
		ipList.put(id, new ArrayList<String>());
		ipList.get(id).add(socket.getInetAddress().toString());
	}
}