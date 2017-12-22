package project.server;

import java.io.*;
import java.util.*;

public class MemberManager {
	private static List<Member> clientList;
	private static MusicManager musicManager;
	private static final File memberDB = new File("members", "member.db");
//	private File musicList;
//	

	public MemberManager()
	{
		clientList = new ArrayList<>();
		musicManager = new MusicManager();
	}
	
	public boolean updateMemberList()
	{
		return true;
	}
	
	public boolean login(String id, String pw) {
		for (Member m : clientList)
			if (m.getId().equals(id) && m.getPassword().equals(pw))
				return true;
		return false;
	}

	public boolean memberAccept(String id, String pw)
	{
		for (Member m : clientList)
			if (m.getId().equals(id))
				return false;
		Member newMember = new Member(id,pw);
		musicManager.createMusicList(id);
		return true;
	}
	
//	public List<Member> loadMusicList(Member m) {
//		try (ObjectInputStream obj = new ObjectInputStream(
//															new BufferedInputStream(
//																new FileInputStream(db)));) {
//			musicList = new File("musics",m.getId()+".db");
//			//list = (List<Member>) obj.readObject();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
