package test;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class HelloWorld {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Рисуем текст на дисплее.
	    LCD.drawString("Hello, world!", 0, 0);
	    //Ждём 5000 миллисекунд (5 сек.).
	    Delay.msDelay(5000);

	}

}
