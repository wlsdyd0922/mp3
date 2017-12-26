package server;

import java.io.*;
import java.util.*;

public class MemberManager {
	//private static List<Member> clientList = new ArrayList<>();
	private Map<String,String> clientList = new HashMap<>();
	private static MusicManager musicManager;
	private static File memberDB;

	public void InitList()
	{
		//clientList = new ArrayList<>();
		//clientList = new HashMap<>();
		updateMemberList();
	}
	
	@SuppressWarnings("unchecked")
	public MemberManager() 
	{
		memberDB = new File("members", "member.db");
		if(memberDB.length() == 0)
			InitList();
		
		try (ObjectInputStream obj = new ObjectInputStream(
															new BufferedInputStream(
															new FileInputStream(memberDB)));)
		{
			clientList = (Map<String, String>) obj.readObject();
		} 
		catch (Exception e) 
		{
			System.out.println("memberDB read failed");
		}
	}

	public boolean updateMemberList()
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
		//for (Member m : clientList)
		if (clientList.get(id) == pw)
				return true;
		return false;
	}

	public boolean memberAccept(String id, String pw)
	{
		if(clientList.containsKey(id))
			return false;
		//Member newMember = new Member(id, pw);
		musicManager.createMusicList(id);
		clientList.put(id, pw);//(newMember);
		updateMemberList();
		return true;
	}
	
	public boolean memberDrop(String id)
	{
		if(clientList.remove(id) != null)
			return true;
		else
			return false;
	}
}
