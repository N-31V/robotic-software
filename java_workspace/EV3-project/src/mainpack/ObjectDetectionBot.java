package mainpack;

import java.io.IOException;
import java.net.UnknownHostException;

import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import lejos.utility.Delay;
import lejos.hardware.motor.EV3LargeRegulatedMotor; 
import lejos.hardware.motor.EV3MediumRegulatedMotor; 

class ObjectDetectionBot { 
	
	CameraClient camera;
	int MOTOR_BC_STEP_ROTATION = 230;//Размер шага для моторов B и C при вращении на 90 градусов. 
	int MOTOR_A_STEP_TO_CATCH = 60; //Размер шага для среднего мотора для фиксации цели 
	double DELTA=0.3;
	int rorl=1;
	EV3 ev3;
	RegulatedMotor motorB; 
	RegulatedMotor motorC; 
	RegulatedMotor motorA; 		
	SensorModes sensor1; 
	SensorModes sensor2; 		
	SampleProvider distance1; 
	SampleProvider distance2;
	SampleProvider average1;
	SampleProvider average2;
	EV3GyroSensor	sensor;
	SampleProvider	sp;
    float [] sample;
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
		this.sensor = new EV3GyroSensor(this.ev3.getPort("S3"));
		
		
		
		//Получаем поставщика замеров для измерения расстояния. 
		this.distance1 = this.sensor1.getMode("Distance"); 
		this.distance2 = this.sensor2.getMode("Distance"); 
		this.average1 = new MeanFilter(distance1, 3);
		this.average2 = new MeanFilter(distance2, 3);
		this.sp = sensor.getAngleAndRateMode();
		//Создаём массив для получения замеров. 
		//Размер массива запрашиваем у поставщика замеров. 
		this.sample1 =  new float[average1.sampleSize()]; 
		this.sample2 =  new float[average2.sampleSize()];
		this.sample = new float[sp.sampleSize()];
		this.sensor.reset();

		this.Dist();
		this.corridorLength1 = this.sample1[0]; 
		this.corridorLength2 = this.sample2[0];
		//Получаем длину коридора в метрах (или 2,5, если больше). 

		this.Turn(1);
		//Delay.msDelay(3000);
		this.Dist();
		this.corridorWidth1 = this.sample1[0]; 
		this.corridorWidth2 = this.sample2[0]; 
		//Получаем ширину коридора в метрах, на которую мы будем ориентироваться при проходе. 
	}
	
	
	void FindObject() throws UnknownHostException, IOException{
		boolean object=false;
		while(!object){
			while ((this.sample1[0]>=(this.corridorWidth1-this.DELTA)) && (this.sample2[0]>=(this.corridorWidth2-this.DELTA)))
			{ 		
				this.Dist();
				this.GoAhead(100);
			}
			this.GoAheadWait(420);
			this.Dist();
			if (this.sample1[0] < (this.corridorWidth1 - this.DELTA)){
				this.side=1;
				this.dist=this.sample1[0];
				object=this.GetPhoto(side);
			}
			else{
				if(this.sample2[0] < (this.corridorWidth2 - this.DELTA)){
					this.side=-1;
					this.dist=this.sample2[0];
					object=this.GetPhoto(side);
				}
			}
			this.GoAheadWait(100);
		}			
	}
	
	boolean GetPhoto(int side) throws UnknownHostException, IOException{
		boolean object=false;
		this.Turn(side);		
		if (this.camera.SendPhoto(9090)){
			this.CloserObject();
			object=true;
		}
		else{
			this.Turn(-side);
			this.GoAheadWait(720);
			object=false;
		}
		return(object);
	}
	
	void CloserObject() throws UnknownHostException, IOException{
		int degrees = (int) (360 * (this.dist * 100  / 17.58 - 1)); 
		this.GoAheadWait(degrees);
		if (this.camera.SendPhoto(9099)){
			this.GripObject(degrees);
		}
		else{
			this.GoAheadWait(-degrees);
			this.Turn(-side);
			this.GoAheadWait(720);
			this.FindObject();
		}
	}
	
	void GripObject(int degrees){
		this.GoAheadWait(345);
		this.motorA.rotate(-1000); 
		this.GoAheadWait(-degrees);
		int back=0;
		switch (this.side) {
			case 1:
				this.average1.fetchSample(this.sample1, 0);
				this.average1.fetchSample(this.sample1, 0);
				this.average1.fetchSample(this.sample1, 0);
				back = (int) (360 * (this.sample1[0]*100-10) / 17.58 ); 
				break;
			case -1:
				this.average2.fetchSample(this.sample2, 0);
				this.average2.fetchSample(this.sample2, 0);
				this.average2.fetchSample(this.sample2, 0);
				back = (int) (360 * (this.sample2[0]*100-10) / 17.58);
				break;
		}
		this.Turn(side);
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
	
	void Turn(int rotate){
		this.sensor.reset();
		double k = 2;
		float e = 90;
		while (e > 0 || e < -2 ){			
			int degrees = (int) (k*e);
			this.motorB.rotate(degrees*rotate);//Поворачиваем так, чтобы начать обход. 
			this.motorC.rotate(-degrees*rotate);
			this.sp.fetchSample(sample, 0);	
			e=90-Math.abs(this.sample[0]);
		this.motorB.waitComplete(); 
		this.motorC.waitComplete(); 
		}
	}

	void Dist(){
		this.average1.fetchSample(this.sample1, 0); 
		this.average2.fetchSample(this.sample2, 0);
		this.average1.fetchSample(this.sample1, 0); 
		this.average2.fetchSample(this.sample2, 0);
		this.average1.fetchSample(this.sample1, 0); 
		this.average2.fetchSample(this.sample2, 0);
	}
	
    protected void finalize() throws IOException {
    	this.sensor.close();
    }
}


