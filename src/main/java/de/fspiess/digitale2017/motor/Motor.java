package de.fspiess.digitale2017.motor;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import com.pi4j.wiringpi.SoftPwm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fspiess.digitale2017.utils.RaspiUtils;

public class Motor {
	
	//overall wiringPi GPIO pin numbers:
    private static final int LEFT_STEPPER01=11;
    private static final int LEFT_STEPPER02=10;
    private static final int LEFT_STEPPER03= 6;
    private static final int LEFT_STEPPER04=14;
    private static final int RIGHT_STEPPER01= 2;
    private static final int RIGHT_STEPPER02= 3;
    private static final int RIGHT_STEPPER03=12;
    private static final int RIGHT_STEPPER04=13;
    private static final int Z_STEPPER01=26;
    private static final int Z_STEPPER02=27;
    private static final int Z_STEPPER03=28;
    private static final int Z_STEPPER04=29;
    private static final int SERVO_PIN=0;
    
    private static final int SERVO_INIT=11;
    private static final int SERVO_RANGE=200;
    private static final int SERVO_UP=11;
    private static final int SERVO_DOWN=5;
    private static final int SERVO_OFF=0;
    
    final static Logger logger = LoggerFactory.getLogger(Motor.class);
    
    // Iterates between 0,1,2,3 and 0 again. For switching through the different motor coils.
    private int stepX = 0;
    private int STEP_PAUSE = 1;
    
    //motor instance wiringPi GPIO pin numbers:
    private int stepper1 = -1;
    private int stepper2 = -1;
    private int stepper3 = -1;
    private int stepper4 = -1;
    private String name;
    private int servoPin = -1;
    private int servoInit = -1;
    private int servoRange = -1;
    private int servoUp = -1;
    private int servoDown = -1;
    private int servoOff = SERVO_OFF;

	public Motor() {	
	}
	
	public Motor(String name){
		super();
		this.name = name;
		if (this.name == "left"){
			this.stepper1 = LEFT_STEPPER01;
			this.stepper2 = LEFT_STEPPER02;
			this.stepper3 = LEFT_STEPPER03;
			this.stepper4 = LEFT_STEPPER04;
		}
		if (this.name == "right"){
			this.stepper1 = RIGHT_STEPPER01;
			this.stepper2 = RIGHT_STEPPER02;
			this.stepper3 = RIGHT_STEPPER03;
			this.stepper4 = RIGHT_STEPPER04;
		}
		if (this.name == "z"){
			this.stepper1 = Z_STEPPER01;
			this.stepper2 = Z_STEPPER02;
			this.stepper3 = Z_STEPPER03;
			this.stepper4 = Z_STEPPER04;
		}
		if (this.name == "servo"){
			this.servoPin = SERVO_PIN;
		    this.servoInit = SERVO_INIT;
		    this.servoRange = SERVO_RANGE;
		    this.servoUp = SERVO_UP;
		    this.servoDown = SERVO_DOWN;
		}
		// 3rd stepper and maybe servo should follow here...
		
		if (RaspiUtils.isRaspberryPi()){
			System.out.println("Motor() constructor runs on Raspberry");

	        // set relevant GPIO as outputs - only valid for stepper motors, not for servo !
			if(this.stepper1 != -1 && this.stepper2 != -1 && this.stepper3 != -1 && this.stepper4 != -1){
		        GpioUtil.export(this.stepper1, GpioUtil.DIRECTION_OUT);
		        Gpio.pinMode(this.stepper1, Gpio.OUTPUT);
		        GpioUtil.export(this.stepper2, GpioUtil.DIRECTION_OUT);
		        Gpio.pinMode(this.stepper2, Gpio.OUTPUT);
		        GpioUtil.export(this.stepper3, GpioUtil.DIRECTION_OUT);
		        Gpio.pinMode(this.stepper3, Gpio.OUTPUT);
		        GpioUtil.export(this.stepper4, GpioUtil.DIRECTION_OUT);
		        Gpio.pinMode(this.stepper4, Gpio.OUTPUT);
			}
			if (this.servoPin != -1){
				SoftPwm.softPwmCreate(this.servoPin, this.servoInit, this.servoRange);
				//avoid "shivering" of servo motor by explicitly switching it off:
				SoftPwm.softPwmWrite(this.servoPin, SERVO_OFF);
				logger.info("info: softPwmCreate(): servoPin = {}, servoUp = {}, servoRange = {}", this.servoPin, this.servoUp, this.servoRange);
			}
		}else{
			System.out.println("Motor() constructor: System is not Raspberry");
		}
	}
	
	public void dumpConfig (){
		System.out.printf("Motor config: %s, %d %d %d %d\n", this.name, this.stepper1, this.stepper2, this.stepper3, this.stepper4);
		System.out.printf("Servo config: %s, %d %d %d %d %d\n", this.name, this.servoPin, this.servoInit, this.servoRange, this.servoUp, this.servoDown);
	}

	
    public void makeStep(int direction) throws InterruptedException {
	    stepX += direction;
	    //System.out.printf("Motor.makestep() - stepX = %d", stepX);
	
	    if(stepX > 3){
	      stepX = 0;
	    }
	    if(stepX < 0){
	      stepX = 3;
	    }
	
	    if(stepX == 0 && RaspiUtils.isRaspberryPi()){
	      //System.out.println("step 0");
	      Gpio.digitalWrite(this.stepper1, 1);
	      Thread.sleep(STEP_PAUSE);
	      Gpio.digitalWrite(this.stepper2, 0);
	      Gpio.digitalWrite(this.stepper3, 0);
	      Gpio.digitalWrite(this.stepper4, 0);
	    }
	    if(stepX == 1 && RaspiUtils.isRaspberryPi()){
	      //System.out.println("step 1");
	      Gpio.digitalWrite(this.stepper3, 1);
	      Thread.sleep(STEP_PAUSE);
	      Gpio.digitalWrite(this.stepper1, 0);
	      Gpio.digitalWrite(this.stepper2, 0);
	      Gpio.digitalWrite(this.stepper4, 0);
	    }
	    if(stepX == 2 && RaspiUtils.isRaspberryPi()){
	      //System.out.println("step 2");
	      Gpio.digitalWrite(this.stepper2, 1);
	      Thread.sleep(STEP_PAUSE);
	      Gpio.digitalWrite(this.stepper1, 0);
	      Gpio.digitalWrite(this.stepper3, 0);
	      Gpio.digitalWrite(this.stepper4, 0);
	    }
	    if(stepX == 3 && RaspiUtils.isRaspberryPi()){
	      //System.out.println("step 3");
	      Gpio.digitalWrite(this.stepper4, 1);
	      Thread.sleep(STEP_PAUSE);
	      Gpio.digitalWrite(this.stepper1, 0);
	      Gpio.digitalWrite(this.stepper2, 0);
	      Gpio.digitalWrite(this.stepper3, 0);
	    }
	
	    Thread.sleep(STEP_PAUSE);
	}
    
    public void servoUp() throws InterruptedException{
    	if(RaspiUtils.isRaspberryPi()){
	    	SoftPwm.softPwmWrite(this.servoPin, this.servoUp);
	    	Thread.sleep(500);
	    	SoftPwm.softPwmWrite(this.servoPin, this.servoOff);
    	}
    	logger.info("info: servoUp(): servoPin = {}, servoUp = {}", this.servoPin, this.servoUp);
    }
    
    public void servoDown() throws InterruptedException{
    	if(RaspiUtils.isRaspberryPi()){
	    	SoftPwm.softPwmWrite(this.servoPin, this.servoDown);
	    	Thread.sleep(500);
	    	SoftPwm.softPwmWrite(this.servoPin, this.servoOff);
    	}
    	logger.info("info: servoDown(): servoPin = {}, servoUp = {}", this.servoPin, this.servoUp);
    }
    
    public static void motorsOff(){
		if (RaspiUtils.isRaspberryPi()){
			System.out.println("Motor.motorsOff() runs on Raspberry");
			Gpio.digitalWrite(RIGHT_STEPPER01, 0);
			Gpio.digitalWrite(RIGHT_STEPPER02, 0);
			Gpio.digitalWrite(RIGHT_STEPPER03, 0);
			Gpio.digitalWrite(RIGHT_STEPPER04, 0);
			Gpio.digitalWrite(LEFT_STEPPER03, 0);
			Gpio.digitalWrite(LEFT_STEPPER01, 0);
			Gpio.digitalWrite(LEFT_STEPPER02, 0);
			Gpio.digitalWrite(LEFT_STEPPER04, 0);
			Gpio.digitalWrite(Z_STEPPER03, 0);
			Gpio.digitalWrite(Z_STEPPER01, 0);
			Gpio.digitalWrite(Z_STEPPER02, 0);
			Gpio.digitalWrite(Z_STEPPER04, 0);
			SoftPwm.softPwmWrite(SERVO_PIN, SERVO_OFF);
			//comment this out as it might not be reversible:
			//SoftPwm.softPwmStop(SERVO_PIN);
		}else{
			System.out.println("Motor.motorsOff() : System is not Raspberry");
		}
	}
	

}
