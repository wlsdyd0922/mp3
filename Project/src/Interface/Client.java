package Interface;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	private InetAddress inet;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	protected static String id;
	protected static Search search = null;
	protected static boolean logInflag = false;
	private List<String> list = new ArrayList<>();
	private int port = 20000;
	private long size = 0;
	private String dir = "C:\\mp3tmp\\";

	public Client() {
		try {
			inet = InetAddress.getByName("192.168.0.171");
			socket = new Socket(inet, port);
			out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			
		} catch (IOException e) {
			logInflag = false;
			e.printStackTrace();
		}
	}

	public void logInManager(int login, String id, String pw) {
		try {
			out.writeObject(login);
			out.flush();
			out.writeObject(id);
			out.flush();
			this.id = id;
			out.writeObject(pw);
			out.flush();
			logInflag = (boolean) in.readObject();
			if (logInflag) {
				search = new Search();
				MainUIwin.bt3.setEnabled(true);
				MainUIwin.bt1.setText("로그아웃");
				MainUIwin.bt2.setText("목록저장");
				MainUIwin.bt4.setEnabled(true);
				MainUIwin.bt5.setEnabled(true);
				MainUIwin.bt6.setEnabled(true);
				clientMusicList(MainUIwin.LIST);
			} else {
				MainUIwin.bt3.setEnabled(false);
			}
			out.close();
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void logOut(int logout) {
		try  {
			out.writeObject(logout);
			out.writeObject(id);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logInflag = false;
		String[] str = new String[] { "" };
		MainUIwin.musicList.setListData(str);
		MainUIwin.bt3.setEnabled(false);
		search.setVisible(false);
	}

	public void signUpManager(int join, String id, String pw, String email) {
		try  {
			out.writeObject(join);
			out.flush();
			out.writeObject(id);
			out.flush();
			out.writeObject(pw);
			out.flush();
			out.writeObject(email);
			out.flush();
			boolean a = (boolean) in.readObject();
			if (a) {
				System.out.println("회원가입 완료");
			} else {
				System.out.println("id 중복");
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> serverMusicList(int total_list) {
		try  {
			out.writeObject(total_list);
			out.flush();
			out.writeObject(id);
			out.flush();
			List<String> list = (ArrayList<String>) in.readObject();
			System.out.println(list);
			out.close();
			in.close();
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	public void musicAdd(int music_add) {
		try  {
			out.writeObject(music_add);
			out.flush();
			out.writeObject(id);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean clientMusicListSave(int cllistSave) {
		try	{
			out.writeObject(cllistSave);
			out.flush();
			out.writeObject(id);
			out.flush();
			for (int i = 0; i < MainUIwin.musicList.getModel().getSize(); i++) {
				list.add(MainUIwin.musicList.getModel().getElementAt(i));
			}
			out.writeObject(list);
			out.flush();
			System.out.println(list.toString());
			boolean a = (boolean) in.readObject();
			out.close();
			in.close();
			return a;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String> clientMusicList(int cllist) {
		try  {
			out.writeObject(cllist);
			out.flush();
			out.writeObject(id);
			out.flush();
			List<String> list = (ArrayList<String>) in.readObject();
			if (!(list.size() == 0)) {
				String[] str = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					str[i] = list.get(i);
				}
				MainUIwin.musicList.setListData(str);
			}
			out.close();
			in.close();
			return list;
		} catch (Exception e) {
			return null;
		}
	}
	public long getFileSize() {
		return this.size;
	}

	public String play(int play,String music) {
		try {
			out.writeObject(play);
			out.flush();
			out.writeObject(id);
			out.flush();
			out.writeObject(music);
			out.flush();
			size = musicReceive(port, music);
			System.out.println("실행준비 완료");
			out.close();
			in.close();
			music = dir+music;
			return music;
			//끝나면 지움
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public String getDir(){
		return this.dir;
	}

	public long musicReceive(int port, String music) {
		byte[] buffer = new byte[65000];
		long fileSize = 0;
		long totalReadBytes = 0;

		try {
			File file = new File(dir);
			if(!file.mkdir()) {
				System.out.println("파일 생성 안댐");
			}
			DatagramSocket ds = new DatagramSocket(20000); 
			FileOutputStream fos = new FileOutputStream(new File(dir,music));
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			int nReadSize = 0;
			System.out.println("Waitng.....");
			ds.receive(dp);
			String str = new String(dp.getData()).trim();
			if (str.equals("start")) {
				System.out.println(str);
				ds.receive(new DatagramPacket(buffer, buffer.length));
				fileSize = Long.parseLong(new String(dp.getData()).trim());
				while (true) {
					ds.receive(dp);
					str = new String(dp.getData()).trim();
					nReadSize = dp.getLength();
					fos.write(dp.getData(), 0, nReadSize);
					Thread.sleep(1);
					totalReadBytes += nReadSize;
					if (totalReadBytes >= fileSize)
						break;
				}
				ds.close();
				fos.close();
				System.out.println("File transfer completed");
			} else {
				System.out.println("Start Error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Process Close");
		return fileSize;
	}
}
