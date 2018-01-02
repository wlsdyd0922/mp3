package server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicManager /*extends Thread */{ 
	//private static List<String> list;
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
            System.out.println("member 叼泛配府 积己");
        }
        else 
        	return false;
        
        return true;
	}
	
	public boolean createMusicList(String id)
	{
		List<String> list = new ArrayList<>();
		//list.add(" ");
		File musicList = new File("members",id+".db");
//		if(!musicList.exists())
//		{
//			try {
//				musicList.createNewFile();
//				System.out.println(id+".db 积己");
//			} catch (IOException e) {
//				e.printStackTrace();
//				return false;
//			}
//			return true;
//		}
//		else
//			return false;
		try (ObjectOutputStream obj = new ObjectOutputStream(
				new BufferedOutputStream(
						new FileOutputStream(musicList)));) {
			obj.writeObject(list);
		} catch (Exception e) {
			System.out.println(id + "musicList create error");
			return false;
		}
		System.out.println(id + ".db 积己");
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> loadServerList()
	{
		System.out.println("load Server List");
		List<String> serverList = new ArrayList<>();
		File dir = new File("musics");		

		File[] flist = dir.listFiles();
		for (File f : flist)
			serverList.add(f.getName());
		
		return serverList; 
	}
	
	public List<MusicInfo> loadServerListUsingClass()
	{
		List<MusicInfo> mList = new ArrayList<>();
		System.out.println("mp3 沥焊 府胶飘 积己");
		File dir = new File("musics");
		Player p;
		File[] flist = dir.listFiles();
		for (File f : flist) {
			try {
				MP3File mp3 = (MP3File) AudioFileIO.read(f);
				String fileName = mp3.getBaseFilename(f);
				Tag tag = mp3.getTagOrCreateDefault();
				int len = mp3.getAudioHeader().getTrackLength();
				int bitRate = Integer.parseInt(mp3.getAudioHeader().getBitRate());
				String artist = tag.getFirst(FieldKey.ARTIST);
				if(artist == null)artist="阂疙";
				String genre = tag.getFirst(FieldKey.GENRE);
				if(genre == null)genre="阂疙";
//				System.out.println("Song Name : " + title);
//				System.out.println("Artist : " + artist);
//				System.out.println("Genre : " + genre);
				mList.add(new MusicInfo(fileName,artist,genre,len,bitRate));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mList;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> readMusicList(String id) {
		System.out.println(id + " read Music List");
		File musicList = new File("members",id+".db");
		try (ObjectInputStream obj = new ObjectInputStream(
														  new BufferedInputStream(
														   new FileInputStream(musicList)));)
		{
			List<String> list;
			list = (List<String>) obj.readObject();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
		List<String> list = readMusicList(id);
		if(!list.remove(music))
		{
			System.err.println("music delete failed");
			return false;
		}
		return updateMusicList(id,list);
	}
	
	public File findMusic(String music)
	{
		File target = new File("musics",music);
		return target;
	} 
	
	public boolean updateMusicList(String id,List<String> list)
	{
		//List<String> list = readMusicList(id);
		File musicList = new File("members",id+".db");
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
	
	public boolean addToMusicList(String id, List<String> music)
	{
		List<String> list = readMusicList(id);
		for(String s : music)
		if(!list.add(s))
		{
			System.err.println("music add failed");
			return false;
		}
		return updateMusicList(id,list);
	}
	
	public boolean deleteMusicList(String id)
	{
		File musicList = new File("members",id+".db");
		if(musicList.delete())
			return true;
		else
			return false;
	}
}
