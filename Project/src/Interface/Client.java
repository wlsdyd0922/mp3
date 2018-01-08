package Interface;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JOptionPane;

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
	private MainUIwin mainUIwin;
	private long size = 0;
	private String dir = "C:\\mp3tmp\\";

	public Client() {
		try {
			File target = new File("Project\\data", "IP.txt");
			int bufSize = (int) target.length();
			byte[] buffer = new byte[bufSize];
			FileInputStream ipIn = new FileInputStream(target);
			int n = ipIn.read(buffer);
			ipIn.close();

			String ip = new String(buffer);
			inet = InetAddress.getByName(ip);

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
			Client.id = id;
			out.writeObject(pw);
			out.flush();
			logInflag = (boolean) in.readObject();
			if (logInflag) {
				search = new Search();
				MainUIwin.bts[2].setEnabled(true);
				MainUIwin.bts[0].setText("로그아웃");
				MainUIwin.bts[1].setText("목록저장");
				MainUIwin.bts[3].setEnabled(true);
				MainUIwin.bts[4].setEnabled(true);
				MainUIwin.bts[5].setEnabled(true);
				clientMusicList(MainUIwin.LIST);
			} else {
				JOptionPane.showMessageDialog(mainUIwin, "입력정보 확인하세요", "Login 실패", JOptionPane.WARNING_MESSAGE);
				MainUIwin.bts[2].setEnabled(false);
			}
			out.close();
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void logOut(int logout) {
		try {
			out.writeObject(logout);
			out.writeObject(id);
			out.close();
			JOptionPane.showMessageDialog(mainUIwin, "Logout 완료");
		} catch (IOException e) {
			e.printStackTrace();
		}
		logInflag = false;
		String[] str = new String[] { "" };
		MainUIwin.musicList.setListData(str);
		MainUIwin.bts[2].setEnabled(false);
		search.setVisible(false);
	}

	public void signUpManager(int join, String id, String pw, String email) {
		try {
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
				JOptionPane.showMessageDialog(mainUIwin, "회원가입 완료");
			} else {
				JOptionPane.showMessageDialog(mainUIwin, "ID 4~10자의 영문숫자, ID 중복");
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> serverMusicList(int total_list) {
		try {
			out.writeObject(total_list);
			out.flush();
			out.writeObject(id);
			out.flush();
			List<String> list = (ArrayList<String>) in.readObject();
			out.close();
			in.close();
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	public void musicAdd(int music_add) {
		try {
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
		try {
			out.writeObject(cllistSave);
			out.flush();
			out.writeObject(id);
			out.flush();
			for (int i = 0; i < MainUIwin.musicList.getModel().getSize(); i++) {
				list.add(MainUIwin.musicList.getModel().getElementAt(i));
			}
			out.writeObject(list);
			out.flush();
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
		try {
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
				MainUIwin.musicList.removeAll();
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

	public String play(int play, String music) {
		try {
			out.writeObject(play);
			out.flush();
			out.writeObject(id);
			out.flush();
			out.writeObject(music);
			out.flush();
			size = musicReceive(port, music);
			out.close();
			in.close();
			music = dir + music;
			return music;
			// 끝나면 지움
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void lyricAdd(String lyric) {
		try {
			out.writeObject(MainUIwin.musicList.getSelectedValue());
			out.flush();
			out.writeObject(lyric);
			out.flush();
			boolean result = (boolean) in.readObject();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDir() {
		return this.dir;
	}

	public long musicReceive(int port, String music) {
		byte[] buffer = new byte[65000];
		long fileSize = 0;
		long totalReadBytes = 0;

		try {
			File file = new File(dir);
			file.deleteOnExit();
			if (!file.mkdir()) {
				if (!file.exists()) {
					JOptionPane.showMessageDialog(mainUIwin, "파일 생성 실패", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
			}
			DatagramSocket ds = new DatagramSocket(20000);
			FileOutputStream fos = new FileOutputStream(new File(dir, music));
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			int nReadSize = 0;
			ds.receive(dp);
			String str = new String(dp.getData()).trim();
			if (str.equals("start")) {
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

				// System.out.println("File transfer completed");
			} else {
				JOptionPane.showMessageDialog(mainUIwin, "실행 중 오류가 발생하였습니다.", "Start Error",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("Process Close");
		return fileSize;
	}
}
