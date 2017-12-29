package server;

import java.io.Serializable;

public class MusicInfo implements Serializable{
	private String musicName;
	private String musicWriter;
	private String genre;
	private int playTime;
	private int bitRate; 
	
	public MusicInfo(String musicName, String musicWriter, String genre, int playTime, int bitRate) {
		super();
		this.musicName = musicName;
		this.musicWriter = musicWriter;
		this.genre = genre;
		this.playTime = playTime;
		this.bitRate = bitRate;
	}
	
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public int getBitRate() {
		return bitRate;
	}
	public void setBitRate(int bitRate) {
		this.bitRate = bitRate;
	}
	public String getMusicName() {
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public String getMusicWriter() {
		return musicWriter;
	}
	public void setMusicWriter(String musicWriter) {
		this.musicWriter = musicWriter;
	}
	public int getPlayTime() {
		return playTime;
	}
	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}
}
