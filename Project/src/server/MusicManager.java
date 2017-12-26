package server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MusicManager /*extends Thread */{
	private static List<String> list;
	private static File musicList;
	MusicManager() 
	{
	}
	
	public boolean createDirectory()
	{
        String path = "musics";
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
            System.out.println("music 叼泛配府 积己");
        }
        else
        	return false;
        
        path = "members";
        file = new File(path);
        if(!file.exists()){
            file.mkdirs();
            System.out.println("music 叼泛配府 积己");
        }
        else return false;
        
        return true;
	}
	
	public boolean createMusicList(String id)
	{
		musicList = new File("members",id+".db");
		if(musicList.exists())
			return true;
		else
			return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> loadServerList()
	{
		List serverList = new ArrayList<>();
		File dir = new File("musics");		

		File[] flist = dir.listFiles();
		for (File f : flist)
			//System.out.println(f.getAbsolutePath());
			serverList.add(f.getName());
		
		return serverList;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> readMusicList(String id) {
		File musicList = new File("members",id+".db");
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

//	public boolean addMusic(String id,String music)
//	{
//		list = readMusicList(id);
//		if(!list.add(music))
//		{
//			System.err.println("music add failed");
//		}
//		return updateMusicList(id);
//	}
	
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
		musicList = new File("members",id+".db");
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
		musicList = new File("members",id+".db");
		if(musicList.delete())
			return true;
		else
			return false;
	}
}
