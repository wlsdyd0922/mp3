package project.server;

import java.io.*;
import java.util.*;

public class MemberManager {
	private static List<Member> clientList;
	private static MusicManager musicManager;
	private static File memberDB;

	@SuppressWarnings("unchecked")
	public MemberManager() 
	{
		memberDB = new File("members", "member.db");
		try (ObjectInputStream obj = new ObjectInputStream(
															new BufferedInputStream(
															new FileInputStream(memberDB)));)
		{
			clientList = (List<Member>) obj.readObject();
		} 
		catch (Exception e) 
		{
			System.out.println("memberDB read failed");
		}
		musicManager = new MusicManager();
	}

	public boolean updateMemberList(String id)
	{
		try(ObjectOutputStream obj = new ObjectOutputStream(
				new BufferedOutputStream(
				new FileOutputStream(memberDB)));)
		{
			obj.writeObject(clientList);
		}
		catch (Exception e) {
			System.out.println("memberDB update failed");
			return false;
		}
		return true;
	}

	public boolean login(String id, String pw) 
	{
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
		Member newMember = new Member(id, pw);
		musicManager.createMusicList(id);
		clientList.add(newMember);
		return true;
	}
}
