package test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ObjectDetectionBot {

	public static void main(String[] args) throws Exception {
	//Загружаем библиотеку OpenCV.
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	//Создаём вспомогательный класс для захвата видео.
	VideoCapture videoCapture = new VideoCapture(0);
	//Задаём ширину и высоту кадра, с которым будем работать. 
	videoCapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 160);
	videoCapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 120);
	//Захватываем камеру для работы с видео.
	if (videoCapture.open(0))
	//Выдаём сообщение, что камера готова.
	LCD.drawString("Camera is ready.", 0, 0);
	else {
	//Выдаём сообщение, что камера не готова.
	LCD.drawString("Camera is not ready.", 0, 0);
	Delay.msDelay(5000);
	return;
	}
	
	final int MOTOR_BC_STEP_ROTATION = 180;//Размер шага для моторов B и C при вращении на 90 градусов.
	final int MOTOR_A_STEP_TO_CATCH = 60; //Размер шага для среднего мотора для фиксации цели
	
	//Инициализируем моторы.
	RegulatedMotor motorB = new EV3LargeRegulatedMotor(BrickFinder.getDefault().getPort("B"));
	RegulatedMotor motorC = new EV3LargeRegulatedMotor(BrickFinder.getDefault().getPort("C"));
	RegulatedMotor motorA = new EV3MediumRegulatedMotor(BrickFinder.getDefault().getPort("A"));
	
	//Получаем порты.
	Port port1 = LocalEV3.get().getPort("S4");
	Port port2 = LocalEV3.get().getPort("S1");
	
	//Инициализируем датчики.
	SensorModes sensor1 = new EV3UltrasonicSensor(port1);
	SensorModes sensor2 = new EV3UltrasonicSensor(port2);
	
	//Получаем поставщика замеров для измерения расстояния.
	SampleProvider distance1 = sensor1.getMode("Distance");
	SampleProvider distance2 = sensor2.getMode("Distance");
	//Создаём массив для получения замеров.
	//Размер массива запрашиваем у поставщика замеров.
	
	double corridorLength1 = distance1.sampleSize();
	double corridorLength2 = distance2.sampleSize();
	//Получаем длину коридора в метрах (или 2,5, если больше).
	
	motorB.rotate(MOTOR_BC_STEP_ROTATION);//Поворачиваем так, чтобы начать обход.
	
	SampleProvider distance1 = sensor1.getMode("Distance");
	SampleProvider distance2 = sensor2.getMode("Distance");
	
	double corridorWidth1 = distance1.sampleSize();
	double corridorWidth2 = distance2.sampleSize();
	//Получаем ширину коридора в метрах, на которую мы будем ориентироваться при проходе.
	
	motorB.synchronizeWith(new RegulatedMotor[] {motorC});//Синхронизируем скорости вращения моторов.
	motorB.startSynchronization();
	do {
		motorB.rotate(60, true);
		motorB.waitComplete();
		distance1 = sensor1.getMode("Distance");
		distance2 = sensor2.getMode("Distance");
	} while (distance1 >= (corridorWidth1 - 0.1)) or (distance2 >= (corridorWidth2 - 0.1))//Поиск возможных объектов.
	motorB.stop(true);//Выключение моторов.
	motorB.endSynchronization();//Рассинхронизируем скорости вращения моторов.
	
	if (distance1.sampleSize() >= (corridorWidth1 - 0.1)) {
	motorB.rotate(-MOTOR_BC_STEP_ROTATION);
	motorC.rotate(MOTOR_BC_STEP_ROTATION);//Поворачиваем к объекту.
	float degrees = (360 * distance2 / 17.58) - 20;
	motorB.synchronizeWith(new RegulatedMotor[] {motorC});
	motorB.startSynchronization();
	motorB.rotate(degrees);//Подъезжаем к объекту
	Delay.msDelay(3000)
	motorB.rotate(20);
	motorC.rotate(MOTOR_A_STEP_TO_CATCH);//Захват цели.
	motorB.rotate(-degrees-20);//Возвращаемся на линию.
	motorB.rotate(MOTOR_BC_STEP_ROTATION);
	motorC.rotate(-MOTOR_BC_STEP_ROTATION);
	}
	else {
	motorС.rotate(-MOTOR_BC_STEP_ROTATION);
	motorB.rotate(MOTOR_BC_STEP_ROTATION);//Поворачиваем к объекту.
	float degrees = (360 * distance1 / 17.58) - 20;
	motorB.synchronizeWith(new RegulatedMotor[] {motorC});
	motorB.startSynchronization();
	motorВ.rotate(degrees);//Подъезжаем к объекту.
	Delay.msDelay(3000)
	motorВ.rotate(20);
	motorС.rotate(MOTOR_A_STEP_TO_CATCH);//Захват цели.
	motorВ.rotate(-degrees-20);//Возвращаемся на линию.
	motorВ.rotate(-MOTOR_BC_STEP_ROTATION);
	motorC.rotate(MOTOR_BC_STEP_ROTATION);
	}
}
