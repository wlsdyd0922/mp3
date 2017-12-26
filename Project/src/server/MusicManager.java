package server;

import java.io.*;
import java.util.List;

public class MusicManager /*extends Thread */{
	private static List<String> list;
	public static File musicList;

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
	
	@SuppressWarnings("unchecked")
	public List<String> readMusicList(String id) {
		try (ObjectInputStream obj = new ObjectInputStream(
														  new BufferedInputStream(
														   new FileInputStream(musicList)));)
		{
			list = (List<String>) obj.readObject();
		} 
		catch (Exception e)
		{
			System.err.println("music list update failed");
			return null;
		}
		return list;
	}
	
	public boolean addMusic(String id,String music)
	{
		list = readMusicList(id);
		if(!list.add(music))
		{
			System.err.println("music add failed");
		}
		return updateMusicList(id);
	}
	
	public boolean deleteMusic(String id, String music)
	{
		list = readMusicList(id);
		if(!list.remove(music))
		{
			System.err.println("music delete failed");
			return false;
		}
		return updateMusicList(id);
	}
	
	public File findMusic(String music)
	{
		File target = new File("musics",music);
		return target;
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
}
