package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reference {
	static boolean flag = true;
	final static int LOGIN = 0;					//�α��� ��û
	final static int JOIN = 1;						//ȸ�� ����
	final static int LIST = 2;						//���� ����Ʈ ��û
	final static int MUSIC = 3;					//���� ���� �ٿ� ��û
	final static int DROP = 4;					//Ż��
	final static int LOGOUT = 5;				//�α׾ƿ�
	final static int TOTAL_LIST = 6;		//���� ��ü ���� ����Ʈ
	final static int MUSIC_ADD = 7;		//���� �߰�
	final static int MUSIC_DEL = 8;			//���� ����
	public static void main(String[] args) throws Exception {
		String ip = "192.168.0.171";
		int port = 20000;
		Scanner s = new Scanner(System.in);

		InetAddress inet = InetAddress.getByName(ip);

		Socket socket = new Socket(inet, port);

		PrintWriter out = new PrintWriter(
				new BufferedWriter(
						new OutputStreamWriter(
								socket.getOutputStream())));

		int state = JOIN;
		out.println(state);
		out.flush();
		System.out.println("ID �Է�");
		String id = s.next();
		out.println(id);
		out.flush();
//		System.out.println("���ϸ� �Է�");
//		String music = s.next();
//		out.println(music);
//		out.flush();
		
		System.out.println("PW �Է�");
		String pw = s.next();
		out.println(pw);
		out.flush();
		
		System.out.println("email �Է�");
		String email = s.next();
		out.println(email);
		out.flush();
		
		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(
						socket.getInputStream()));) 
		{
				String textFS = in.readLine();
					System.out.println("server : " + textFS);

		} catch (Exception e) {
			System.err.println("�ޱ� ����");
		}

//		ObjectInputStream in = new ObjectInputStream(
//												new BufferedInputStream(
//													socket.getInputStream()));
//		List<String> list = (ArrayList<String>) in.readObject();
//		System.out.println("server : " + list.toString());
		
		
//////////////////////udp////////////////////////
		/*		
		byte[] buffer = new byte[1024];
		long fileSize;
        long totalReadBytes = 0;

        try {
            int nReadSize = 0;
            System.out.println("Waitng.....");
              
            DatagramSocket ds = new DatagramSocket(port);
            FileOutputStream fos = null;       
            fos = new FileOutputStream(music);
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
                    Thread.sleep(1);
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
            }
            else{
                System.out.println("Start Error");
                fos.close();
                ds.close();
            }
        } catch (Exception e) {}
        System.out.println("Process Close");
		*/
		socket.close();
		System.out.println("connection end");
	}
}
