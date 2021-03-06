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
		ObjectDetectionBot bot = new ObjectDetectionBot(ev3, camera);
		bot.FindObject();
        Delay.msDelay(1000);
	}
}
