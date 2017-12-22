package project.server;

import java.io.*;
import java.net.*;
import java.util.List;

public class MusicManager /*extends Thread */{
	static List<String> list;
	Socket socket = null;
	File file = null;
	FileOutputStream fos = null;
	InputStream is = null;

	public static File musicList;// = new File("files", "member.db");

	MusicManager() 
	{
	}

	public boolean createMusicList(String id)
	{
		musicList = new File("musics",id+".db");
		if(musicList.exists())
			return true;
		else
			return false;
	}
	
	public File findMusicList(String id)
	{
		musicList = new File("musics",id+".db");
		if(musicList.exists())
			return musicList;
		else
			return null;
	}
	
	public boolean updateMusicList(String id)
	{
		musicList = new File("musics",id+".db");
		try(ObjectOutputStream obj = new ObjectOutputStream(
															new BufferedOutputStream(
																new FileOutputStream(musicList)));)
		{
			obj.writeObject(list);
		}
		catch (Exception e) 
		{
			System.err.println("music list update failed");
			return false;
		}
		return true;
	}
	
	public boolean deleteMusicList(String id)
	{
		musicList = new File("musics",id+".db");
		if(musicList.delete())
			return true;
		else
			return false;
	}
	
//	public static void seekMusic(String fileName) {
//		for (String m : list)
//			if (m.equals(fileName)) {
//				try
//				{
//					ServerSocket ss = new ServerSocket(8000);
//					Socket socket = ss.accept();
//					OutputStream os = socket.getOutputStream();
//
//					File file = new File("musics", fileName + ".mp3");
//					FileInputStream fis = new FileInputStream(file);
//					int readCount = 0;
//					byte[] buffer = new byte[4096];
//
//					while ((readCount = fis.read(buffer)) > 0)
//					{
//						os.write(buffer, 0, readCount);
//					}
//					os.close();
//					fis.close();
//				}
//				catch (IOException e) 
//				{
//					e.printStackTrace();
//				}
//				
//
//				return;
//			}
//	}
}
