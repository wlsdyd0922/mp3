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
	
	final static int LOGIN_CONFIRM = -1;//Ŭ���̾�Ʈ �α��� ��Ȯ�� ��û��
	final static int LOGIN = 0;					//�α��� ��û
	final static int JOIN = 1;						//ȸ�� ����
	final static int LIST = 2;						//���� ����Ʈ ��û
	final static int MUSIC = 3;					//���� ���� �ٿ� ��û
	final static int DROP = 4;					//Ż��
	final static int LOGOUT = 5;				//�α׾ƿ�
	final static int TOTAL_LIST = 6;		//���� ��ü ���� ����Ʈ
	final static int MUSIC_ADD = 7;		//���� �߰�
	final static int MUSIC_DEL = 8;			//���� ����
	final static int LYRIC_CALL = 9;		//���� ��û
	final static int LYRIC_ADD = 10;			//���� �߰�
	
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
			System.out.println(socket.getInetAddress() + " : "+id +" ���� ����Ʈ ���� : " + music);
			
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
			System.out.println(socket.getInetAddress() + " : "+id + "���� ���� �Ϸ�");
			fis.close();
			ds.close();
			return true;
		} 
		catch (Exception e) 
		{
			System.out.println(socket.getInetAddress() + " : "+id + "���� ���� ����");
			return false;
		}
	}
	
	public void joiner()
	{
		try {
		System.out.println(f.format(today));
		System.out.println(socket.toString() + " ���� ��û");
		String id = (String)in.readObject();
		System.out.println(socket.getInetAddress() + " id : " + id);
		String pw = (String)in.readObject();
		System.out.println(socket.getInetAddress() + " pw : " + pw);
		String email = (String)in.readObject();
		System.out.println(socket.getInetAddress() + " email : " + email);

		int joinResult = memM.memberAccept(id, pw, email);
		out.writeObject(joinResult);
		out.flush();

		System.out.println(socket.getInetAddress() + " : ȸ������ ��� " + joinResult);
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
		System.out.println(socket.getInetAddress() + " �α��� �õ�");

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
		System.out.println(socket.getInetAddress() + " �α��� ��� :  " + loginResult);
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
		System.out.println(id + " ���� ����Ʈ ��û");
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
		System.out.println(id + " ��ü ����Ʈ ��û");
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
		System.out.println(id + " ���� ���� ��û " + musicTitle);
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
		System.out.println(id + " �α׾ƿ�");
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
			System.out.println(id + " Ż��");
			boolean dropResult = memM.memberDrop(id);
			if (dropResult) {
				out.writeObject(dropResult);
				out.flush();
				kill();
			} else {
				out.writeObject("�ٽ� �α���");
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
			System.out.println(socket.getInetAddress() + " " + id + " : ���� �߰� ��û");
			@SuppressWarnings("unchecked")
			List<String> addmusic = (List<String>) in.readObject();
			System.out.println(id + " : " + addmusic);
			System.out.println(socket.getInetAddress() + " " + addmusic + "����Ʈ�� �߰�");
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
			System.out.println(socket.getInetAddress() + " " + id + " : ���� ���� ��û");
			String delmusic = (String) in.readObject();
			System.out.println(f.format(today));
			System.out.println(id + " " + delmusic + "����");
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
		System.out.println(id + " : ���� ��û");
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
		System.out.println(id + " : ���� �߰� �õ�");
		String title = (String)in.readObject();
		System.out.println(id + " : ��ǥ -> " + title);
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
		System.out.println(id + ": �߸��� ��û");
		out.writeObject("�߸��� ����");
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