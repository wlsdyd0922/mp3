package Interface.Player;

import java.io.*;
import java.util.*;
import javazoom.jl.decoder.*;
import javazoom.jl.player.*;

public class Music {

	public static void main(String[] args) {
 
		try {
			Scanner in = new Scanner(System.in);
			System.out.print("Mp3 파일 경로 :");
			String path = in.nextLine();
			Player p = new Player(new FileInputStream(path));
			System.out.println("play");
			p.play();
			p.close();
			System.out.println("stop");
		} catch (Exception e) {
			System.out.print("error");
			System.out.println(e);
		}

	}

}
