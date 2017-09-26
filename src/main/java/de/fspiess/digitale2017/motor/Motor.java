package de.fspiess.digitale2017.motor;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

import de.fspiess.digitale2017.utils.RaspiUtils;

public class Motor {
	
	//overall wiringPi GPIO pin numbers:
	private static final int SERVOUP =      11;
    private static final int SERVODOWN=     5;
    private static final int LEFT_STEPPER01=11;
    private static final int LEFT_STEPPER02=10;
    private static final int LEFT_STEPPER03= 6;
    private static final int LEFT_STEPPER04=14;
    private static final int RIGHT_STEPPER01= 2;
    private static final int RIGHT_STEPPER02= 3;
    private static final int RIGHT_STEPPER03=12;
    private static final int RIGHT_STEPPER04=13;
    
    // Iterates between 0,1,2,3 and 0 again. For switching through the different motor coils.
    private int stepX = 0;
    private int STEP_PAUSE = 1;
    
    //motor instance wiringPi GPIO pin numbers:
    private int stepper1;
    private int stepper2;
    private int stepper3;
    private int stepper4;
    private String name;

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
		// 3rd stepper and maybe servo should follow here...
		
		if (RaspiUtils.isRaspberryPi()){
			System.out.println("Motor() constructor runs on Raspberry");

	        // set relevant GPIO as outputs:
	        GpioUtil.export(this.stepper1, GpioUtil.DIRECTION_OUT);
	        Gpio.pinMode(this.stepper1, Gpio.OUTPUT);
	        GpioUtil.export(this.stepper2, GpioUtil.DIRECTION_OUT);
	        Gpio.pinMode(this.stepper2, Gpio.OUTPUT);
	        GpioUtil.export(this.stepper3, GpioUtil.DIRECTION_OUT);
	        Gpio.pinMode(this.stepper3, Gpio.OUTPUT);
	        GpioUtil.export(this.stepper4, GpioUtil.DIRECTION_OUT);
	        Gpio.pinMode(this.stepper4, Gpio.OUTPUT);
		}else{
			System.out.println("Motor() constructor: System is not Raspberry");
		}
	}
	
	public void dumpConfig (){
		System.out.printf("Motor config: %s, %d %d %d %d\n", this.name, this.stepper1, this.stepper2, this.stepper3, this.stepper4);
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
		}else{
			System.out.println("Motor.motorsOff() : System is not Raspberry");
		}
	}
	

}
