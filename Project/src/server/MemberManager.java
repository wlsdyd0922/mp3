package server;

import java.io.*;
import java.util.*;

public class MemberManager {
	private Map<String,Member> clientList = new HashMap<>();
	private MusicManager musicManager;
	File memberDB;

	public void InitList()
	{
		memberDB = new File("members", "member.db");
		if(!memberDB.exists())
		{
			updateMemberList();
			InitList();
		}
		readMemberList();
	}
	
	@SuppressWarnings("unchecked")
	public MemberManager() 
	{
		musicManager = new MusicManager();
		musicManager.createDirectory();
		
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
		if(clientList.containsKey(id))
			return clientList.get(id).getPassword().equals(pw);
		else
			return false;
	}

	public boolean memberAccept(String id, String pw, String email)
	{
		readMemberList();
		if(clientList.containsKey(id))
			return false;
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
		try (ObjectInputStream obj = new ObjectInputStream(
															new BufferedInputStream(
															new FileInputStream(memberDB)));) {
			clientList = (Map<String, Member>) obj.readObject();
			
			System.out.println("--회원 목록--");
			Iterator<String> it = clientList.keySet().iterator();
			while(it.hasNext()) 
			{
				String n = it.next();
				System.out.println("id : "+n);
			}
		} catch (Exception e) {
			System.out.println("memberDB read failed");
		}
	}
}