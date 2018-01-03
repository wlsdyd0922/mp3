package Interface;

import java.io.FileInputStream;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.player.Player;

public class PlayThread extends Thread {
	private Player p;
	private boolean playflag = false;
	private float playTime = 0;

	public PlayThread() {
	}

	public void run() {
		try {
			while (!playflag) {
				Client cl = new Client();
				String a = cl.play(MainUIwin.MUSIC);
				p = new Player(new FileInputStream(a));
				Bitstream bit = new Bitstream(new FileInputStream(a));
				MainUIwin.la1.setText(MainUIwin.musicList.getSelectedValue());
				MainUIwin.la4.setText("비트레이트 : " + (bit.readFrame().bitrate() / 1000) + "Kbps");
				MainUIwin.la5.setText("주파수 : " + (bit.readFrame().frequency() / 1000.0) + "Khz");
				playTime = (int) ((float) cl.getFileSize() * 8 / bit.readFrame().bitrate() * 10);
				playTime = playTime / 10;
				System.out.println(playTime);
				bit.close();
				p.play();
				p.close();
				playflag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void kill() {
		p.close();
		this.playflag = true;
	}
}
