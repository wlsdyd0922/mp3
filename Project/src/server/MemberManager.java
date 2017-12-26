package server;

import java.io.*;
import java.util.*;

public class MemberManager {
	//private static List<Member> clientList = new ArrayList<>();
	private Map<String,Member> clientList = new HashMap<>();
	private static MusicManager musicManager;
	//private static File memberDB;

	public void InitList()
	{
		File memberDB = new File("members", "member.db");
		//clientList = new ArrayList<>();
		//clientList = new HashMap<>();
		updateMemberList();
	}
	
	@SuppressWarnings("unchecked")
	public MemberManager() 
	{
		musicManager = new MusicManager();
		musicManager.createDirectory();
		
		File memberDB = new File("members", "member.db");
		//if(memberDB.length() == 0)
		InitList();
		if(!memberDB.exists())
		{
			System.out.println("member db°¡ ¾øÀ½");
			return;
		}
		
		try (ObjectInputStream obj = new ObjectInputStream(
															new BufferedInputStream(
																new FileInputStream(memberDB)));)
		{
			clientList = (Map<String, Member>) obj.readObject();
		} 
		catch (Exception e) 
		{
			System.out.println("memberDB read failed");
		}
	}

	public boolean updateMemberList()
	{
		File memberDB = new File("members", "member.db");
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
		if (clientList.get(id).getPassword() == pw)
				return true;
		return false;
	}

	public boolean memberAccept(String id, String pw, String email)
	{
		musicManager.createDirectory();
		if(clientList.containsKey(id))
			return false;
		//Member newMember = new Member(id, pw);
		musicManager.createMusicList(id);
		clientList.put(id, new Member(id,pw,email));//(newMember);
		updateMemberList();
		return true;
	}
	
	public boolean memberDrop(String id)
	{
		if(clientList.remove(id) != null)
		{
			musicManager.deleteMusicList(id);
			return true;
		}
		else
			return false;
	}

	public void memberDisplay() {
		File memberDB = new File("members", "member.db");
		try (ObjectInputStream obj = new ObjectInputStream(
															new BufferedInputStream(
															new FileInputStream(memberDB)));) {
			clientList = (Map<String, Member>) obj.readObject();
			
			System.out.println(clientList.toString());
		} catch (Exception e) {
			System.out.println("memberDB read failed");
		}
	}
}
