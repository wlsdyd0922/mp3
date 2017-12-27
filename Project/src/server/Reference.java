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
	final static int LOGIN = 0;					//로그인 요청
	final static int JOIN = 1;						//회원 가입
	final static int LIST = 2;						//개인 리스트 요청
	final static int MUSIC = 3;					//음악 파일 다운 요청
	final static int DROP = 4;					//탈퇴
	final static int LOGOUT = 5;				//로그아웃
	final static int TOTAL_LIST = 6;		//서버 전체 음악 리스트
	final static int MUSIC_ADD = 7;		//음악 추가
	final static int MUSIC_DEL = 8;			//음악 삭제
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
		System.out.println("ID 입력");
		String id = s.next();
		out.println(id);
		out.flush();
//		System.out.println("파일명 입력");
//		String music = s.next();
//		out.println(music);
//		out.flush();
		
		System.out.println("PW 입력");
		String pw = s.next();
		out.println(pw);
		out.flush();
		
		System.out.println("email 입력");
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
			System.err.println("받기 실패");
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
