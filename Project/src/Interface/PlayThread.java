package Interface;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.player.Player;

public class PlayThread extends Thread {
	private boolean playflag = true;
	private boolean allFlag = false;
	private boolean infiFlag = false;
	private boolean ranFlag = false;
	private int total;
	private int stopped;
	private int skip;
	private int selectNext;
	private String select;
	
	private float playTime = 0;
	private Player ap;
	private FileInputStream fis;
	private BufferedInputStream bis;


	private int[] suf = new int[MainUIwin.musicList.getLastVisibleIndex()];
	private List<Integer> suff = new ArrayList<>();


	public PlayThread() {
	}

	public PlayThread(int skip) {
		this.skip = skip;
	}

	public void run() {
		try {
			select = MainUIwin.musicList.getSelectedValue();
			selectNext = MainUIwin.musicList.getSelectedIndex() + 1;
			while (playflag) {
				Client cl = new Client();
				String a =cl.play(MainUIwin.MUSIC, select);
				MainUIwin.musicList.setSelectedValue(select, true);

				fis = new FileInputStream(a);

				total = fis.available();
				fis.skip(total - 50000);
				bis = new BufferedInputStream(fis);
				ap = new Player(bis);
				Bitstream bit = new Bitstream(fis);

				MainUIwin.la1.setText(select);
				MainUIwin.la4.setText("비트레이트 : " + (bit.readFrame().bitrate() / 1000) + "Kbps");
				MainUIwin.la5.setText("주파수 : " + (bit.readFrame().frequency() / 1000.0) + "Khz");
				playTime = (int) ((float) cl.getFileSize() * 8 / bit.readFrame().bitrate() * 10);
				playTime = playTime / 10;
				System.out.println(playTime);

				ap.play();
				ap.close();

				if (allFlag) {
					if (selectNext > MainUIwin.musicList.getLastVisibleIndex()) {
						if (!infiFlag) {
							playflag = false;
						} else {
							select = MainUIwin.musicList.getModel().getElementAt(0).toString();
							selectNext = 1;
						}
					} else {
						select = MainUIwin.musicList.getModel().getElementAt(selectNext).toString();
						selectNext = selectNext + 1;
					}
				}else {
					if(!infiFlag) {
						playflag = false;	
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void kill() {
		ap.close();
		this.playflag = false;
	}

	public int stopper() {
		try {
			stopped = fis.available();
			ap.close();
			System.out.println(stopped);
			skip = total - stopped;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.playflag = false;
		return skip;
	}

	public void setAllFlag(boolean allflag) {
		this.allFlag = allflag;
	}

	public boolean getAllFlag() {
		return this.allFlag;
	}

	public void setInfFlag(boolean infflag) {
		this.infiFlag = infflag;
	}

	public boolean getInfFlag() {
		return this.infiFlag;
	}

	public void suffe() {
		for (int i = 0; i < suf.length; i++) {
			suf[i] = i;
			suff.add(i);
		}
		Collections.shuffle(suff);
	}
}
