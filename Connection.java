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

//������ ������� �� input/output�� ����
// - �����Ϸ��� Socket�̶�� ������ �ʿ�
public class Connection extends Thread{
	//�ܺε����� : Connection ��ü�� �����ϴ� ����� static ���·� ����
	private static List<Connection> list = new ArrayList<>();
	public static void add(Connection c) {
		list.add(c);
	}
	public static void remove(Connection c) {
		list.remove(c);
	}
	//��� ����ڿ��� �޼����� �����ϴ� ���
	public static void broadcast(String text) {
		System.out.println("�� �� "+list.size()+"���� ����ڿ��� broadcast �õ�");
		for(Connection c : list) {
			c.send(text);
		}
	}
	
	
	//���ε�����
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
		remove(this);//���� ������
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
				send(str);//�����̿��Ը� ȸ��
//				System.out.println(socket+" : "+str);
//				broadcast(str);//��ü���� ȸ��
			}
		}catch(Exception e) {
//			e.printStackTrace();
			kill();
		}
	}
}









