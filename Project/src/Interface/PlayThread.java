package Interface;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JOptionPane;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.player.Player;

public class PlayThread extends Thread {
	private boolean playflag = true;
	private float playTime = 0;
	private Player ap;
	private boolean allFlag = true;
	private FileInputStream fis;
	private BufferedInputStream bis;
	private int total;
	private int stopped;
	private int skip;

	public PlayThread() {
	}
	public PlayThread(int skip) {
		this.skip = skip;
	}

	public void run() {
		try {
			String select = MainUIwin.musicList.getSelectedValue();
			int selectNext = MainUIwin.musicList.getSelectedIndex() + 1;
			while (playflag) {
				Client cl = new Client();
				String a = cl.play(MainUIwin.MUSIC, select);
				MainUIwin.musicList.setSelectedValue(select, true);

				fis = new FileInputStream(a);

				total = fis.available();
				fis.skip(skip);
				bis = new BufferedInputStream(fis);
				ap = new Player(bis);
				Bitstream bit = new Bitstream(fis);

				MainUIwin.la1.setText(select);
				MainUIwin.musicList.setSelectedValue(select, true);
				MainUIwin.la4.setText("비트레이트 : " + (bit.readFrame().bitrate() / 1000) + "Kbps");
				MainUIwin.la5.setText("주파수 : " + (bit.readFrame().frequency() / 1000.0) + "Khz");
				playTime = (int) ((float) cl.getFileSize() * 8 / bit.readFrame().bitrate() * 10);
				playTime = playTime / 10;
				System.out.println(playTime);

				ap.play();
				ap.close();
				if (allFlag) {
					if (selectNext > MainUIwin.musicList.getLastVisibleIndex()) {
						select = MainUIwin.musicList.getModel().getElementAt(0).toString();
					} else {
						select = MainUIwin.musicList.getModel().getElementAt(selectNext).toString();
						System.out.println(select);
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
			skip = total-stopped;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.playflag = false;
		return skip;
	}
}
