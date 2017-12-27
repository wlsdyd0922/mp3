package server;

import java.io.*;
import java.util.*;

public class MemberManager {
	private Map<String,Member> clientList = new HashMap<>();
	private static MusicManager musicManager;

	public void InitList()
	{
		File memberDB = new File("members", "member.db");
		readMemberList();
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
			System.out.println("member db가 없음");
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
	
	public boolean readMemberList()
	{
		File memberDB = new File("members", "member.db");
		try(ObjectInputStream in = new ObjectInputStream(
				new BufferedInputStream(
						new FileInputStream(memberDB)));)
		{
			Map<String,Member> list = (Map<String,Member>)in.readObject();
					return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
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
		readMemberList();
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
			
			Iterator<String> it = clientList.keySet().iterator();
			while(it.hasNext()) 
			{
				String n = it.next();
				System.out.println("회원 id : "+n);
			}
		} catch (Exception e) {
			System.out.println("memberDB read failed");
		}
	}
}