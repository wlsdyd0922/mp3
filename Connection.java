package api.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//연결을 기반으로 한 input/output을 구현
// - 생성하려면 Socket이라는 정보가 필요
public class Connection extends Thread{
	//외부데이터 : Connection 전체를 관리하는 기능을 static 형태로 보관
	private static List<Connection> list = new ArrayList<>();
	public static void add(Connection c) {
		list.add(c);
	}
	public static void remove(Connection c) {
		list.remove(c);
	}
	//모든 사용자에게 메세지를 전송하는 기능
	public static void broadcast(String text) {
		System.out.println("→ 총 "+list.size()+"명의 사용자에게 broadcast 시도");
		for(Connection c : list) {
			c.send(text);
		}
	}
	
	
	//내부데이터
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	
	private boolean flag = true;
	public void kill() {
		try {
			flag = false;
			in.close();
			out.close();
			socket.close();
		}catch(Exception e) {	}
		remove(this);//나를 지워라
	}
	
	public Connection(Socket socket) throws IOException{
		this.socket = socket;
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.setDaemon(true);
		this.start();
	}
	
	public void send(String text) {
		try {
			out.println(text);
			out.flush();
		}catch(Exception e) {
			kill();
		}
	}
	
	public void run() {
		try {
			while(flag) {
				String str = in.readLine();
				if(str == null) throw new Exception();
				send(str);//보낸이에게만 회신
//				System.out.println(socket+" : "+str);
//				broadcast(str);//전체에게 회신
			}
		}catch(Exception e) {
//			e.printStackTrace();
			kill();
		}
	}
}









