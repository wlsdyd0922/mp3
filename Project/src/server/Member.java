package server;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Member implements Serializable{
	private String id;
	private String password;
	//private List<String> musicList = new ArrayList<String>();
	
	public Member(String id, String password) {
		super();
		this.id = id;
		this.password = password;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

//	public List<String> getMusicList() {
//		return musicList;
//	}
//
//	public void setMusicList(List<String> musicList) {
//		this.musicList = musicList;
//	}
}
