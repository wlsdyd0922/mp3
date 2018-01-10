package server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class NetworkManager extends Thread{
	private boolean flag = true;
	private int otp;
	private Map<String,Integer> ipList;
	private Random rand;
	
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
	final static int LYRIC_CALL = 9;		//가사 요청
	final static int LYRIC_ADD = 10;			//가사 추가
	
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
		flag = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NetworkManager(Socket socket,Map<String,Integer> list)
	{
		port = 20000;
		this.socket = socket;
		System.out.println("networkManager : " + socket.toString());
		this.ipList = list;
		memM = new MemberManager();
		musM = new MusicManager();
		try {
			out = new ObjectOutputStream(
					socket.getOutputStream());
			
			in = new ObjectInputStream(
					new BufferedInputStream(
							this.socket.getInputStream()));
			
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
				state = (int)in.readObject();
				switch (state) 
				{
				case JOIN:
					joiner();
					break;

				case LOGIN:
					loginer();
					break;

				case LIST:
					userList();
					break;

				case TOTAL_LIST:
					totalList();
					break;

				case MUSIC:
					musicFile();
					break;

				case LOGOUT:
					logout();
					break;

				case DROP:
					drop();
					break;
					
				case MUSIC_ADD :
					musicAdd();
					break;
					
				case MUSIC_DEL:
					musicDel();
					break;
					
				case  LYRIC_CALL :
					lyricCall();
					break;
					
				case LYRIC_ADD:
					lyricAdd();
					break;
					
				default:
					illegalAcception();
					break;
				}
			}
		}
		catch (Exception e) 
		{
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
	
	public void joiner()
	{
		try {
		System.out.println(f.format(today));
		System.out.println(socket.toString() + " 가입 요청");
		String id = (String)in.readObject();
		System.out.println(socket.getInetAddress() + " id : " + id);
		String pw = (String)in.readObject();
		System.out.println(socket.getInetAddress() + " pw : " + pw);
		String email = (String)in.readObject();
		System.out.println(socket.getInetAddress() + " email : " + email);

		int joinResult = memM.memberAccept(id, pw, email);
		out.writeObject(joinResult);
		out.flush();

		System.out.println(socket.getInetAddress() + " : 회원가입 결과 " + joinResult);
		memM.memberDisplay();
		kill();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("join method error");
		}
	}
	
	public void loginer()
	{
		try {
		System.out.println(f.format(today));
		System.out.println(socket.getInetAddress() + " 로그인 시도");

		String id = (String)in.readObject();
		System.out.println(socket.getInetAddress() + " : id = " + id);
		String pw = (String)in.readObject();
		System.out.println(socket.getInetAddress() + " : pw = " + pw);
		int OTP = (Integer)in.readObject();
		boolean loginResult = memM.login(id, pw);
		if(!loginCheck(id,OTP))
			loginResult = false;
		out.writeObject(loginResult);
		out.flush();
		System.out.println(socket.getInetAddress() + " 로그인 결과 :  " + loginResult);
		if(loginResult)
			ipSave(id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("login method error");
		}
	}
	
	public void userList()
	{
		try {
		String id = (String)in.readObject();
		System.out.println(f.format(today));
		System.out.println(socket.getInetAddress());
		System.out.println(id + " 개인 리스트 요청");
		listSender(id);
		kill();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("user list method error");
		}
	}
	
	public void totalList()
	{
		try {
		String id = (String)in.readObject();
		System.out.println(f.format(today));
		System.out.println(socket.getInetAddress());
		System.out.println(id + " 전체 리스트 요청");
		listSender("server");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("total list method error");
		}
	}
	
	public void musicFile()
	{
		try {
		String id = (String)in.readObject();
		System.out.println(f.format(today));
		String musicTitle = (String)in.readObject();
		System.out.println(socket.getInetAddress());
		System.out.println(id + " 음악 파일 요청 " + musicTitle);
		musicSender(id, musicTitle);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("musicFile method error");
		}
	}
	
	private void logout()
	{
		try {
		String id = (String)in.readObject();
		System.out.println(f.format(today));
		System.out.println(socket.getInetAddress());
		System.out.println(id + " 로그아웃");
		ipList.remove(id); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("logout method error");
		}
	}
	
	private void drop()
	{
		try {
			String id = (String) in.readObject();
			System.out.println(f.format(today));
			System.out.println(socket.getInetAddress());
			System.out.println(id + " 탈퇴");
			boolean dropResult = memM.memberDrop(id);
			if (dropResult) {
				out.writeObject(dropResult);
				out.flush();
				kill();
			} else {
				out.writeObject("다시 로그인");
				out.flush();
				kill();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("drop method error");
		}
	}
	
	private void musicAdd()
	{
		try {
			String id = (String) in.readObject();
			System.out.println(f.format(today));
			System.out.println(socket.getInetAddress() + " " + id + " : 음악 추가 신청");
			@SuppressWarnings("unchecked")
			List<String> addmusic = (List<String>) in.readObject();
			System.out.println(id + " : " + addmusic);
			System.out.println(socket.getInetAddress() + " " + addmusic + "리스트에 추가");
			boolean addResult = musM.addToMusicList(id, addmusic);
			out.writeObject(addResult);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("musicAdd method error");
		}
	}
	
	private void musicDel()
	{
		try {
			String id = (String) in.readObject();
			System.out.println(socket.getInetAddress() + " " + id + " : 음악 삭제 신청");
			String delmusic = (String) in.readObject();
			System.out.println(f.format(today));
			System.out.println(id + " " + delmusic + "삭제");
			boolean delResult = musM.deleteMusic(id, delmusic);
			out.writeObject(delResult);
			out.flush();
			kill();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("musicDel method error");
		}
	}
	
	private void lyricCall()
	{
		try {
		System.out.println(f.format(today));
		String id = (String)in.readObject();
		System.out.println(id + " : 가사 요청");
		String music = (String)in.readObject();
		String lyric = musM.loadLyric(music);
		out.writeObject(lyric);
		out.flush();
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("lyricCall method error");
		}
	}
	
	private void lyricAdd()
	{
		try {
		System.out.println(f.format(today));
		String id = (String)in.readObject();
		System.out.println(id + " : 가사 추가 시도");
		String title = (String)in.readObject();
		System.out.println(id + " : 목표 -> " + title);
		String recvLyric = (String)in.readObject();
		boolean lyricResult = musM.saveLyric(title, recvLyric);
		out.writeObject(lyricResult);
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("lyricAdd method error");
		}
	}
	
	private void illegalAcception()
	{
		try {
		String id = (String)in.readObject();
		System.out.println(f.format(today));
		System.out.println(id + ": 잘못된 요청");
		out.writeObject("잘못된 접근");
		out.flush();
		kill();
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("iA method error");
		}
	}
	
	public boolean ipSave(String id) 
	{
//		if(ipList.get(id) == null)
//		ipList.put(id, new ArrayList<String>());
//		ipList.get(id).add(socket.getInetAddress().toString());
//		try(ObjectOutputStream obj = new ObjectOutputStream(
//				new BufferedOutputStream(
//					new FileOutputStream(
//							new File("memberDB",id+"ip.list"))));)
//		{
//			obj.writeObject(ipList);
//		} catch (Exception e) {
//			System.out.println("memberDB update failed");
//		}
		boolean putResult=false;
		if(ipList.put(id,rand.nextInt(999999)+1) != null)
			putResult=true;
		return putResult;
	}
	
	public boolean loginCheck(String id,int OTP)
	{
		return (ipList.get(id) == OTP);
	}
	
//	public boolean ipLoad(String id)
//	{
//		try (ObjectInputStream obj = new ObjectInputStream(
//				new BufferedInputStream(
//						new FileInputStream(
//								new File("memberDB",id+"ip.list"))));) {
//			ipList = (Map<String, ArrayList<String>>) obj.readObject();
//
//			Iterator<String> it = ipList.keySet().iterator();
//			while (it.hasNext()) {
//				String n = it.next();
//				if(n.equals(socket.getInetAddress()))
//					return false;
//			}
//		} catch (Exception e) {
//			System.out.println("memberDB read failed");
//		}
//		return true;
//		}
}