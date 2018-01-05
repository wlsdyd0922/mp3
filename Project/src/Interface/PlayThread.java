package Interface;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

public class PlayThread extends Thread {
	private boolean playflag = true;
	private boolean allFlag = false;
	private boolean infiFlag = false;
	private boolean ranFlag = false;
	private int total;
	private int stopped;
	private int skip;
	private int selectNext;
	private int selectPrev;
	private int select;

	private float playTime = 0;
	private Player ap;
	private FileInputStream fis;
	private BufferedInputStream bis;

	private List<Integer> suff = new ArrayList<>();

	public PlayThread() {
	}

	public PlayThread(int skip) {
		this.skip = skip;
	}

	public void run() {
		try {
			selectPrev = MainUIwin.musicList.getSelectedIndex() - 1;
			select = MainUIwin.musicList.getSelectedIndex();
			selectNext = MainUIwin.musicList.getSelectedIndex() + 1;
			while (playflag) {
				if (ranFlag) {
					selectNext = (int) (Math.random() * MainUIwin.musicList.getLastVisibleIndex());
				}
				Client cl = new Client();
				String a = cl.play(MainUIwin.MUSIC, MainUIwin.musicList.getModel().getElementAt(select).toString());
				MainUIwin.musicList.setSelectedIndex(select);

				fis = new FileInputStream(a);

				total = fis.available();
				fis.skip(skip);
				bis = new BufferedInputStream(fis);
				ap = new Player(bis);
				Bitstream bit = new Bitstream(fis);

				MainUIwin.la1.setText(MainUIwin.musicList.getModel().getElementAt(select).toString());
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
							select = 0;
							selectNext = 1;
						}
					} else {
						select = selectNext;
						selectNext = selectNext + 1;
					}
				} else {
					if (!infiFlag) {
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

	public int getSelectNext() {
		if (selectNext > MainUIwin.musicList.getLastVisibleIndex()) {
			return 0;
		}
		return this.selectNext;
	}
	public int getSelect() {
		return this.select;
	}

	public int getSelectPrev() {
		if (selectPrev < 0) {
			return MainUIwin.musicList.getLastVisibleIndex();
		}
		return this.selectPrev;
	}

	public void setInfFlag(boolean infflag) {
		this.infiFlag = infflag;
	}

	public void setRanFlag(boolean ranFlag) {
		this.ranFlag = ranFlag;
	}

	public boolean getRanFlag() {
		return this.ranFlag;
	}

	public boolean getInfFlag() {
		return this.infiFlag;
	}
}
