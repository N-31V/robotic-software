package mainpack;

import java.io.IOException;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class MainClass {

	public static void main(String[] args) throws IOException {
		EV3 ev3 = (EV3) BrickFinder.getLocal(); // подключение к локальному ev3
		CameraClient camera = new CameraClient(ev3, "10.0.1.2");
		String resp=camera.SendPhoto(); 
        LCD.drawString(resp, 0, 0);
        Delay.msDelay(5000);
	}
}
