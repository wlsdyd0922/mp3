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
		remove(this);//나를 지워라
	}
	
	final static int LOGIN = 0;
	final static int LIST = 1;
	final static int MUSIC = 2;
	final static int UPLOAD = 3;
	
	private ServerSocket server;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	private int port;
	
	private MemberManager memM;
	private MusicManager musM;
	//private DatagramSocket ds;
	
	public NetworkManager(Socket socket)
	{
		port = 20000;
		this.socket = socket;
		this.setDaemon(true);
		this.start();
		
		memM = new MemberManager();
		try 
		{
			server = new ServerSocket(20000);
			socket = server.accept();

			in = new BufferedReader(
						new InputStreamReader(
							socket.getInputStream()));

			out = new PrintWriter(
						new BufferedWriter(
							new OutputStreamWriter(
									socket.getOutputStream())));
			
			// ds = new DatagramSocket(20000);
		}
		catch (IOException e)
		{
			System.err.println("server socket create error");
		}
	}
	
	public void run() {
		while (flag) {
			int state;
			String id = null;
			String musicTitle = null;
			try {
				state = in.read();
				switch (state) {
				case LOGIN:
					id = in.readLine();
					System.out.println("client : " + id);
					String pw = in.readLine();
					System.out.println("client : " + pw);

					String result = "로그인 실패";
					if (memM.login(id, pw)) {
						result = "로그인 성공";
						System.out.println(id + " : 로그인 성공");
					}
					out.println(result);
					out.flush();
					break;

				case LIST:
					id = in.readLine();
					System.out.println(id + " 리스트 요청");
					listSender(id);
					break;

				case MUSIC:
					id = in.readLine();
					System.out.println(id + " 음악 파일 요청");
					musicTitle = in.readLine();
					musicSender(id, musicTitle);
					break;

				case UPLOAD:
					id = in.readLine();
					System.out.println(id + " 음악 파일 업로드");
					musicTitle = in.readLine();
					musicReceiver(musicTitle);
					break;

				default:
					id = in.readLine();
					System.out.println(id + ": 잘못된 요청");
					break;
				}
			} catch (IOException e) {
				kill();
			}
			
			try {
				socket.close();
				server.close();
			} catch (IOException e) {
				System.out.println("socket/server close error");
				e.printStackTrace();
			}
		}
	}
	
	public boolean listSender(String id) 
	{
		List<String> musics = musM.readMusicList(id);
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
	
	public boolean musicReceiver(String music) {
		String filename = music;
		 
        long fileSize;
        long totalReadBytes = 0;
         
        byte[] buffer = new byte[1024];
        try {
            int nReadSize = 0;
            System.out.println("전송 대기");
              
            DatagramSocket ds = new DatagramSocket(port);
            FileOutputStream fos = null;       
            fos = new FileOutputStream(filename);
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            ds.receive(dp);
            String str = new String(dp.getData()).trim();
             
            if (str.equals("start")){
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
                    totalReadBytes+=nReadSize;
                    System.out.println("In progress: " + totalReadBytes + "/"
                            + fileSize + " Byte(s) ("
                            + (totalReadBytes * 100 / fileSize) + " %)");
                    if(totalReadBytes>=fileSize)
                        break;
                }
                System.out.println("File transfer completed");
                fos.close();
                ds.close();
                return true;
            }
            else{
                System.out.println("Start Error");
                fos.close();
                ds.close();
                return false;
            }
        } 
        catch (Exception e)
        {
        	System.out.println("music receive error");
        }
        System.out.println("Process Close");
        return false;
    }
		
//		try  
//		{
//			DatagramSocket socket = new DatagramSocket(20000);
//
//			DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
//			socket.receive(dp);
//			String str = new String(dp.getData()).trim();
//			File file = new File(str);
//			DataOutputStream dos = new DataOutputStream(
//													new BufferedOutputStream(
//													new FileOutputStream(file)));
//
//			dos.write(str.getBytes(), 0, str.getBytes().length);
//
//			dos.close();
//			return true;
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			return false;
//		}

//		try(DataInputStream in = new DataInputStream(
//													new BufferedInputStream(
//													  socket.getInputStream()));)
//		{
//			String name = in.readUTF();
//			
//			File target = new File("musics", name);
//			FileOutputStream out = new FileOutputStream(target);
//			
//			Long totalSize = in.readLong();
//			System.out.println("mp3파일 전송 받음 : " + totalSize + "byte");
//			
//			byte[] buffer = new byte[1024];
//			while(true)
//			{
//				int size = in.read(buffer);
//				if(size==-1)break;
//				out.write(buffer,0,size);
//			}
//		} 
//		catch (IOException e)
//		{
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
	
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
