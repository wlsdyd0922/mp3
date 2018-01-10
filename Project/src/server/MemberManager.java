package server;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class MemberManager {
	private Map<String,Member> clientList = new HashMap<>();
	private MusicManager musicManager;
	File memberDB;

	final static int SUCCES = 0;
	final static int DUPLICATE = 1;
	final static int IDFORMAT = 2;
	final static int PWFORMAT = 3;
	final static int EMAILFORMAT = 4;
	final static int ADMIN = 5;
	final static int IDLENGTH = 6;
	
	final static int IDPWERROR = 1;
	final static int DUPLICATION = 2;
	final static int TIMEOUT = 3;

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

	public int login(String id, String pw)  
	{
		if(clientList.containsKey(id))
			if(clientList.get(id).getPassword().equals(pw))
				return SUCCES;
		return IDPWERROR;
	}

	public int memberAccept(String id, String pw, String email)
	{
		if (id.length() <= 3 || id.length() > 10)
		{
			System.out.println(id + " 길이가 안맞음");
			return IDLENGTH;
		}
		
		if (id.contains("Admin")) 
		{
			System.out.println("사용 불가 닉네임");
			return -1;
		}
		
		boolean flag = false;
		for(int i=0;i<id.length();i++)
		{
			char ch = id.charAt(i);
			if(!(ch >='A' && ch<='z' || ch>='0' && ch<='9'))
			{
				flag = true;
				break;
			}
		}
		
		if (pw.length() <= 3 || pw.length() > 10)
		{
			System.out.println(pw + " 길이가 안맞음");
			return PWFORMAT;
		}
		
		if(flag == true)
		{
			System.out.println(id + " 영어만 등록 가능");
			return IDFORMAT;
		}
		
		String regex = "^[a-z][a-z0-9]{7,19}@[a-z0-9]{3,20}\\.(com|net|co\\.kr)$";
		if (Pattern.matches(regex, email))
		{}
		else
			return EMAILFORMAT;
		
		readMemberList();
		if(clientList.containsKey(id))
			return DUPLICATE;
		musicManager.createMusicList(id);
		clientList.put(id, new Member(id,pw,email));//(newMember);
		updateMemberList();
		return SUCCES;
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