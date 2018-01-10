package Interface;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.*;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.player.Player;

public class PlayThread extends Thread {
	private boolean playflag = true;
	private boolean allFlag = false;
	private boolean infiFlag = false;
	private boolean ranFlag = false;
	boolean flag = true;
	private int total;
	private int stopped;
	private int skip;
	private int selectNext;
	private int selectPrev;
	private int select;
	private int skip11;
	private String a;
	private Format f1 = new DecimalFormat("00");
	private Format f2 = new DecimalFormat("00");

	private float playTime = 0;
	private int playTime1 = 0;
	private Player ap;
	private FileInputStream fis;
	private BufferedInputStream bis;

	private List<Integer> suff = new ArrayList<>();

	public PlayThread() {
		MainUIwin.sl.setValue(0);
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
				a = cl.play(MainUIwin.MUSIC, MainUIwin.musicList.getModel().getElementAt(select).toString());
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
				// System.out.println(playTime);
				MainUIwin.sl.setMinimum(0);
				MainUIwin.sl.setMaximum(total);

				flag = true;
				Thread th1 = new Thread() {
					public void run() {
						while (flag) {
							int tick = (int) (total / playTime);
							skip += tick;
							MainUIwin.sl.setValue(skip);
							playTime1 = skip / tick - 1;
							
							MainUIwin.tl2.setText("" + playTime1 / 60 + ":" + f1.format(playTime1 % 60));
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
				th1.setDaemon(true);
				th1.start();

				
				MainUIwin.tl1.setText((int) (playTime / 60)+ ":" + f1.format((int) (playTime % 60)));
				ap.play();
				ap.close();

				skip = 0;
				flag = false;

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

	public void delete(File file) {
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File[] list = file.listFiles();
			if (list != null) {
				for (File f : list) {
					delete(f);
				}
			}
			file.delete();
		}
	}

	public void kill() {
		if (ap != null) {
			ap.close();
		}
		delete(new File(a));
		this.playflag = false;
	}

	public int stopper() {
		try {
			stopped = fis.available();
			ap.close();
			skip = total - stopped;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.playflag = false;
		return skip;
	}

	public int stopper11() {
		try {
			stopped = fis.available();
			skip11 = total - stopped;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.playflag = false;
		return skip11;
	}

	public int getPlayTime() {
		return (int) this.playTime;
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

	public int getTotal() {
		return this.total;
	}

}
