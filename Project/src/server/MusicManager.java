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

public class MusicManager{ 
	MusicManager() 
	{
	}
	
	public boolean createDirectory()
	{
        String path = "musics";
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
            System.out.println("music 디렉토리 생성");
        }
        else
        	return false;
        
        path = "members";
        file = new File(path);
        if(!file.exists()){
            file.mkdirs();
            System.out.println("member 디렉토리 생성");
        }
        else 
        	return false;
        
        return true;
	}
	
	public boolean createMusicList(String id)
	{
		List<String> list = new ArrayList<>();
		File musicList = new File("members",id+".db");
		try (ObjectOutputStream obj = new ObjectOutputStream(
				new BufferedOutputStream(
						new FileOutputStream(musicList)));) {
			obj.writeObject(list);
		} catch (Exception e) {
			System.out.println(id + "musicList create error");
			return false;
		}
		System.out.println(id + ".db 생성");
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
		System.out.println("mp3 정보 리스트 생성");
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
				if(artist == null)artist="불명";
				String genre = tag.getFirst(FieldKey.GENRE);
				if(genre == null)genre="불명";
			
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
		return updateMusicList(id,music);
	}
	
	public boolean deleteMusicList(String id)
	{
		File musicList = new File("members",id+".db");
		if(musicList.delete())
			return true;
		else
			return false;
	}
	public boolean saveLyric(String music,String lyrics)
	{
		File musicLyric = new File("musics",music+".lr");
		try(ObjectOutputStream obj = new ObjectOutputStream(
															new BufferedOutputStream(
															  new FileOutputStream(musicLyric)));)
		{
			obj.writeObject(lyrics);
		}
		catch (Exception e) 
		{
			System.err.println("music list update failed");
			return false;
		}
		return true;
	}
	public String loadLyric(String music) 
	{
		File musicLyric = new File("musics",music+".lr");
		try (ObjectInputStream obj = new ObjectInputStream(
														  new BufferedInputStream(
														   new FileInputStream(musicLyric)));)
		{
			String list;
			list = (String) obj.readObject();
			return list;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
