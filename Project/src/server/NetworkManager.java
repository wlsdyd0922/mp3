package server;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class NetworkManager extends Thread{
	private boolean flag = true;
	
	private Map<String,ArrayList<String>> ipList = new HashMap<>();
	
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
					System.out.println(socket.toString() + " ���� ��û");
					id = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " id : " + id);
					pw = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " pw : " + pw);
					email = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " email : " + email);

					boolean joinResult = memM.memberAccept(id, pw, email);
					out.writeObject(joinResult);
					out.flush();

					System.out.println(socket.getInetAddress() + " : ȸ������ ��� " + joinResult);
					memM.memberDisplay();
					kill();
					break;

				case LOGIN:
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress() + " �α��� �õ�");

					id = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " : id = " + id);
					pw = (String)in.readObject();
					System.out.println(socket.getInetAddress() + " : pw = " + pw);

					boolean loginResult = memM.login(id, pw);
					out.writeObject(loginResult);
					out.flush();
					System.out.println(socket.getInetAddress() + " �α��� ��� :  " + loginResult);
					break;

				case LIST:
					id = (String)in.readObject();
//					if(!status)
//					{
//						System.out.println(socket.getInetAddress() + " : �� �α��� ���·� ���� �õ�");
//						out.writeObject(LOGIN_CONFIRM);
//						out.writeObject("�α��� �ʿ�");
//						out.flush();
//					}
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress());
					System.out.println(id + " ���� ����Ʈ ��û");
					listSender(id);
					kill();
					break;

				case TOTAL_LIST:
					id = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress());
					System.out.println(id + " ��ü ����Ʈ ��û");
					listSender("server");
					break;

				case MUSIC:
					id = (String)in.readObject();
					System.out.println(f.format(today));
					musicTitle = (String)in.readObject();
					System.out.println(socket.getInetAddress());
					System.out.println(id + " ���� ���� ��û " + musicTitle);
					musicSender(id, musicTitle);
					break;

				case LOGOUT:
					id = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress());
					System.out.println(id + " �α׾ƿ�");
					break;

				case DROP:
					id = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress());
					System.out.println(id + " Ż��");
					boolean dropResult = memM.memberDrop(id);
					if (dropResult)
					{
						out.writeObject(dropResult);
						out.flush();
						kill();
					}
					else
					{
						out.writeObject("�ٽ� �α���");
						out.flush();
						kill();
					}
					break;
					
				case MUSIC_ADD :
					id = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(socket.getInetAddress() +" " + id + " : ���� �߰� ��û");
					@SuppressWarnings("unchecked") 
					List<String> addmusic = (List<String>) in.readObject();
					System.out.println(id + " : " + addmusic);
					System.out.println(socket.getInetAddress() + " " + addmusic + "����Ʈ�� �߰�");
					boolean addResult =musM.addToMusicList(id, addmusic);
					out.writeObject(addResult);
					out.flush();
					break;
					
				case MUSIC_DEL:
					id = (String)in.readObject();
					System.out.println(socket.getInetAddress() +" " + id + " : ���� ���� ��û");
					String delmusic = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(id + " " + delmusic + "����");
					boolean delResult =musM.deleteMusic(id, delmusic);
					out.writeObject(delResult);
					out.flush();
					kill();
					break;
					
				default:
					id = (String)in.readObject();
					System.out.println(f.format(today));
					System.out.println(id + ": �߸��� ��û");
					out.writeObject("�߸��� ����");
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

	public void ipSave(String id) 
	{
		if(ipList.get(id) == null)
		ipList.put(id, new ArrayList<String>());
		ipList.get(id).add(socket.getInetAddress().toString());
	}
}