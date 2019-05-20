package mainpack;

import java.io.IOException;
import java.net.UnknownHostException;
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
	
	CameraClient camera;
	int MOTOR_BC_STEP_ROTATION = 230;//Размер шага для моторов B и C при вращении на 90 градусов. 
	int MOTOR_A_STEP_TO_CATCH = 60; //Размер шага для среднего мотора для фиксации цели 
	double DELTA=0.2;
	int rorl=1;
	EV3 ev3;
	RegulatedMotor motorB; 
	RegulatedMotor motorC; 
	RegulatedMotor motorA; 		
	SensorModes sensor1; 
	SensorModes sensor2; 		
	SampleProvider distance1; 
	SampleProvider distance2; 
	float[] sample1; 
	float[] sample2; 
	double corridorLength1; 
	double corridorLength2;	
	double corridorWidth1; 
	double corridorWidth2;
	int side;
	float dist;
	
	ObjectDetectionBot(EV3 ev, CameraClient cam){
		this.ev3 = ev;
		this.camera = cam;
		//Инициализируем моторы. 
		this.motorB = new EV3LargeRegulatedMotor(this.ev3.getPort("B")); 
		this.motorC = new EV3LargeRegulatedMotor(this.ev3.getPort("C")); 
		this.motorA = new EV3MediumRegulatedMotor(this.ev3.getPort("A")); 		
		//Инициализируем датчики. 
		this.sensor1 = new EV3UltrasonicSensor(this.ev3.getPort("S1")); 
		this.sensor2 = new EV3UltrasonicSensor(this.ev3.getPort("S2")); 		
		//Получаем поставщика замеров для измерения расстояния. 
		this.distance1 = this.sensor1.getMode("Distance"); 
		this.distance2 = this.sensor2.getMode("Distance"); 
		//Создаём массив для получения замеров. 
		//Размер массива запрашиваем у поставщика замеров. 
		this.sample1 = new float[this.distance1.sampleSize()]; 
		this.sample2 = new float[this.distance2.sampleSize()]; 

		this.distance1.fetchSample(this.sample1,0); 
		this.distance2.fetchSample(this.sample2,0); 
		this.corridorLength1 = this.sample1[0]; 
		this.corridorLength2 = this.sample2[0];	
		//Получаем длину коридора в метрах (или 2,5, если больше). 

		this.motorB.rotate(210);//Поворачиваем так, чтобы начать обход. 
		this.motorC.rotate(-210);
		this.motorB.waitComplete(); 
		this.motorC.waitComplete(); 
		this.distance1.fetchSample(this.sample1,0); 
		this.distance2.fetchSample(this.sample2,0); 
		this.corridorWidth1 = this.sample1[0]; 
		this.corridorWidth2 = this.sample2[0]; 
		//Получаем ширину коридора в метрах, на которую мы будем ориентироваться при проходе. 
	}
	
	
	void findobject() throws UnknownHostException, IOException{
		boolean object=false;
		while(!object){
			while ((this.sample1[0]>=(this.corridorWidth1-this.DELTA)) && (this.sample2[0]>=(this.corridorWidth2-this.DELTA)))
			{ 
				LCD.clear();			
				this.distance1.fetchSample(this.sample1, 0); 
				this.distance2.fetchSample(this.sample2, 0); 
				this.GoAhead(100);
			} 
			if (this.sample1[0] < (this.corridorWidth1 - this.DELTA)){
				this.side=-1;
				this.dist=this.sample1[0];				
			}
			else{
				this.side=1;
				this.dist=this.sample2[0];				
			}
			this.motorB.rotate(-this.MOTOR_BC_STEP_ROTATION*side); 
			this.motorC.rotate(this.MOTOR_BC_STEP_ROTATION*side);			
			if (this.camera.SendPhoto(9090)){
				object=true;
			}
			else{
				this.motorB.rotate(this.MOTOR_BC_STEP_ROTATION*side); 
				this.motorC.rotate(-this.MOTOR_BC_STEP_ROTATION*side);
				this.GoAhead(180);
			}
		}			
	}
	
	void GripObject(){
		int degrees = (int) (360 * this.dist * 100 / 17.58); 
		this.GoAheadWait(degrees);
		this.motorA.rotate(-1000); 
		this.GoAheadWait(-degrees);
		int back=0;
		switch (this.side) {
			case 1:
				this.distance1.fetchSample(this.sample1, 0);
				back = (int) (360 * (this.sample1[0]*100-10) / 17.58 ); 
				break;
			case -1:
				this.distance2.fetchSample(this.sample2, 0);
				back = (int) (360 * (this.sample2[0]*100-10) / 17.58);
				break;
		}
		this.motorB.rotate(-this.MOTOR_BC_STEP_ROTATION*side); 
		this.motorC.rotate(this.MOTOR_BC_STEP_ROTATION*side);
		this.GoAheadWait(back);
		this.motorA.rotate(1000); 
	}
	
	void GoAhead(int degrees){
		this.motorB.synchronizeWith(new RegulatedMotor[] {this.motorC}); 
		this.motorB.startSynchronization(); 
		this.motorC.rotate(degrees);
		this.motorB.rotate(degrees); 
		this.motorB.endSynchronization();
	}
	
	void GoAheadWait(int degrees){
		this.GoAhead(degrees);
		motorB.waitComplete(); 
		motorC.waitComplete(); 		
	}

}


