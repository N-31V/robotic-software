package test;

import java.util.Arrays;

import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.motor.EV3LargeRegulatedMotor; 
import lejos.hardware.motor.EV3MediumRegulatedMotor; 

class ObjectDetectionBot { 
	
	static void fun(){
		
	}

	public static void main(String[] args) throws Exception { 
	
		final int MOTOR_BC_STEP_ROTATION = 230;//Размер шага для моторов B и C при вращении на 90 градусов. 
		final int MOTOR_A_STEP_TO_CATCH = 60; //Размер шага для среднего мотора для фиксации цели 
		final double DELTA=0.2;
		int rorl=1;
		
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		//Инициализируем моторы. 
		RegulatedMotor motorB = new EV3LargeRegulatedMotor(ev3.getPort("B")); 
		RegulatedMotor motorC = new EV3LargeRegulatedMotor(ev3.getPort("C")); 
		RegulatedMotor motorA = new EV3MediumRegulatedMotor(ev3.getPort("A")); 		
		//Инициализируем датчики. 
		SensorModes sensor1 = new EV3UltrasonicSensor(ev3.getPort("S1")); 
		SensorModes sensor2 = new EV3UltrasonicSensor(ev3.getPort("S2")); 		
		//Получаем поставщика замеров для измерения расстояния. 
		SampleProvider distance1 = sensor1.getMode("Distance"); 
		SampleProvider distance2 = sensor2.getMode("Distance"); 
		//Создаём массив для получения замеров. 
		//Размер массива запрашиваем у поставщика замеров. 
		float[] sample1 = new float[distance1.sampleSize()]; 
		float[] sample2 = new float[distance2.sampleSize()]; 

		distance1.fetchSample(sample1,0); 
		distance2.fetchSample(sample2,0); 
		double corridorLength1 = sample1[0]; 
		double corridorLength2 = sample2[0];	
		//Получаем длину коридора в метрах (или 2,5, если больше). 

		motorB.rotate(210);//Поворачиваем так, чтобы начать обход. 
		motorC.rotate(-210);
		motorB.waitComplete(); 
		motorC.waitComplete(); 
		distance1.fetchSample(sample1,0); 
		distance2.fetchSample(sample2,0); 
		double corridorWidth1 = sample1[0]; 
		double corridorWidth2 = sample2[0]; 
		//Получаем ширину коридора в метрах, на которую мы будем ориентироваться при проходе. 

		
		LCD.drawString("synhr", 0, 2); 
		int c = 0;
		while ((sample1[0]>=(corridorWidth1-DELTA)) && (sample2[0]>=(corridorWidth2-DELTA)))
		{ 
			LCD.clear();			
			distance1.fetchSample(sample1, 0); 
			distance2.fetchSample(sample2, 0); 
			motorB.synchronizeWith(new RegulatedMotor[] {motorC});//Синхронизируем скорости вращения моторов. 
			motorB.startSynchronization(); 
			motorB.rotate(100); 
			motorC.rotate(100); 
			motorB.endSynchronization();
			//motorB.waitComplete(); 
			//motorC.waitComplete(); 
			c++;
			LCD.drawInt(c, 0, 0); 
		} 	
		
		if (sample1[0] < (corridorWidth1 - DELTA)) { 
			rorl=1;
		}
		else{
			rorl=-1;
		}
		motorB.rotate(-MOTOR_BC_STEP_ROTATION*rorl); 
		motorC.rotate(MOTOR_BC_STEP_ROTATION*rorl);
		int degrees = (int) (360 * distance2.sampleSize()*100 / 17.58) - 17; 
		motorB.synchronizeWith(new RegulatedMotor[] {motorC}); 
		motorB.startSynchronization(); 
		motorC.rotate(degrees);
		motorB.rotate(degrees);//Подъезжаем к объекту. 
		motorB.endSynchronization();
		motorB.waitComplete(); 
		motorC.waitComplete(); 
		Delay.msDelay(3000); 		
		motorB.synchronizeWith(new RegulatedMotor[] {motorC}); 
		motorB.startSynchronization(); 
		motorC.rotate(360);
		motorB.rotate(360);//Подъезжаем к объекту. 
		motorB.endSynchronization();
		motorB.waitComplete(); 
		motorC.waitComplete(); 
		motorA.rotate(-1000); 
		motorB.synchronizeWith(new RegulatedMotor[] {motorC}); 
		motorB.startSynchronization(); 
		motorC.rotate(-degrees);
		motorB.rotate(-degrees);//Подъезжаем к объекту. 
		motorB.endSynchronization();
		motorB.waitComplete(); 
		motorC.waitComplete(); 
		Delay.msDelay(3000); 
		motorA.rotate(1000); 
		/*if (sample1[0] < (corridorWidth1 - DELTA)) { 
			motorB.rotate(-MOTOR_BC_STEP_ROTATION); 
			motorC.rotate(MOTOR_BC_STEP_ROTATION);//Поворачиваем к объекту. 
			int degrees = (int) (360 * distance2.sampleSize() / 17.58) - 20; 
			motorB.synchronizeWith(new RegulatedMotor[] {motorC}); 
			motorB.startSynchronization(); 
			motorC.rotate(degrees);
			motorB.rotate(degrees);//Подъезжаем к объекту. 
			LCD.drawString("2!", 0, 0); 
			Delay.msDelay(3000); 
			motorA.rotate(-300); 
			motorC.rotate(MOTOR_A_STEP_TO_CATCH);//Захват цели. 
			motorB.rotate(-degrees-20);//Возвращаемся на линию. 
			motorB.rotate(MOTOR_BC_STEP_ROTATION); 
			motorC.rotate(-MOTOR_BC_STEP_ROTATION); 
			motorB.waitComplete(); 
			motorC.waitComplete(); 
			} 
		else { 
			motorC.rotate(-MOTOR_BC_STEP_ROTATION); 
			motorB.rotate(MOTOR_BC_STEP_ROTATION);//Поворачиваем к объекту. 
			int degrees = (int) (360 * distance1.sampleSize() / 17.58) - 20; 
			motorB.synchronizeWith(new RegulatedMotor[] {motorC}); 
			motorB.startSynchronization(); 
			motorB.rotate(degrees);//Подъезжаем к объекту. 
			Delay.msDelay(3000); 
			LCD.drawString("3!", 0, 0); 
			motorA.rotate(-300); 
			motorC.rotate(MOTOR_A_STEP_TO_CATCH);//Захват цели. 
			motorB.rotate(-degrees-20);//Возвращаемся на линию. 
			motorB.rotate(-MOTOR_BC_STEP_ROTATION); 
			motorC.rotate(MOTOR_BC_STEP_ROTATION); 
			motorB.waitComplete(); 
			motorC.waitComplete(); 
			} */
		LCD.drawString("finish", 0, 6); 
		Delay.msDelay(2000);
	}
}


