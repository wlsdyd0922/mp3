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
			System.out.println("member db�� ����");
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
		//memberDB = new File("members", "member.db");
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
		//memberDB = new File("members", "member.db");
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
		if (clientList.get(id).getPassword().equals(pw))
				return true;
		return false;
	}

	public boolean memberAccept(String id, String pw, String email)
	{
		readMemberList();
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
		//memberDB = new File("members", "member.db");
		try (ObjectInputStream obj = new ObjectInputStream(
															new BufferedInputStream(
															new FileInputStream(memberDB)));) {
			clientList = (Map<String, Member>) obj.readObject();
			
			System.out.println("--ȸ�� ���--");
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