package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

public class NetworkManager {
	private boolean flag = true;
	private ServerSocket server;
	private Socket socket;
	//private DatagramSocket ds;
	
	public NetworkManager()
	{
		try 
		{
			server = new ServerSocket(20000);
			socket = server.accept();
			//ds = new DatagramSocket(20000);
		}
		catch (IOException e)
		{
			System.err.println("server socket create error");
		}
	}
	
	public void receiver()
	{
		
	}
	
	public boolean listSender(List<String> musics) 
	{
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
	
	public boolean musicReceiver() {
		try {
			DatagramSocket socket = new DatagramSocket(20000);

			File file = null;
			DataOutputStream dos = null;

			DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
			socket.receive(dp);
			String str = new String(dp.getData()).trim();
			file = new File(str);
			dos = new DataOutputStream(
						new BufferedOutputStream(
							new FileOutputStream(file)));

			dos.write(str.getBytes(), 0, str.getBytes().length);

			dos.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

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
//			System.out.println("mp3���� ���� ���� : " + totalSize + "byte");
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
	}
	
	public boolean musicSender()
	{
		Scanner in = new Scanner(System.in);
		InetAddress inet;
		DatagramSocket ds;
		int port = 20000;
		try 
		{
			inet = InetAddress.getByName("192.168.0.132");
			try 
			{
				ds = new DatagramSocket();

				String str = "UDP �׽�Ʈ";
				byte[] b = str.getBytes();
				DatagramPacket dp = new DatagramPacket(b, b.length, inet, port);
				System.out.println("������ �غ� �Ϸ�");

				try 
				{
					ds.send(dp);
				}
				catch (IOException e) 
				{
					System.err.println("send error");
					return false;
				}
				System.out.println("���� �Ϸ�");

				ds.close();
				System.out.println("���� ���� ȸ�� �Ϸ�");
			} 
			catch (SocketException e1) 
			{
				System.err.println("packet error");
				return false;
			}
			System.out.println("���� �غ� �Ϸ�");
			return true;
		} 
		catch (UnknownHostException e) 
		{
			System.err.println("Inet error");
			return false;
		}
	}
}
