package de.fspiess.digitale2017.motor;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

public class Motor {
	
    private static int SERVOUP =      11;
    private static int SERVODOWN=     5;
    private static int LEFT_STEPPER01=11;
    private static int LEFT_STEPPER02=10;
    private static int LEFT_STEPPER03= 6;
    private static int LEFT_STEPPER04=14;
    private static int RIGHT_STEPPER01= 2;
    private static int RIGHT_STEPPER02= 3;
    private static int RIGHT_STEPPER03=12;
    private static int RIGHT_STEPPER04=13;
    private static int StepX = 0;
    private static int STEP_PAUSE = 1;
    
    private int stepper1;
    private int stepper2;
    private int stepper3;
    private int stepper4;
	
	public static void motorsOff(){
      Gpio.digitalWrite(RIGHT_STEPPER01, 0);
      Gpio.digitalWrite(RIGHT_STEPPER02, 0);
      Gpio.digitalWrite(RIGHT_STEPPER03, 0);
      Gpio.digitalWrite(RIGHT_STEPPER04, 0);

      Gpio.digitalWrite(LEFT_STEPPER03, 0);
      Gpio.digitalWrite(LEFT_STEPPER01, 0);
      Gpio.digitalWrite(LEFT_STEPPER02, 0);
      Gpio.digitalWrite(LEFT_STEPPER04, 0);
	}

	public Motor(int stepper1, int stepper2, int stepper3, int stepper4) {
		this.stepper1 = stepper1;
		this.stepper2 = stepper2;
		this.stepper3 = stepper3;
		this.stepper4 = stepper4;
	}
	
    private void makeStepRight(int direction) throws InterruptedException {
        StepX += direction;

        if(StepX > 3){
          StepX = 0;
        }
        if(StepX < 0){
          StepX = 3;
        }

        if(StepX == 0){
          //System.out.println("step 0");
          Gpio.digitalWrite(RIGHT_STEPPER01, 1);
          Thread.sleep(STEP_PAUSE);
          Gpio.digitalWrite(RIGHT_STEPPER02, 0);
          Gpio.digitalWrite(RIGHT_STEPPER03, 0);
          Gpio.digitalWrite(RIGHT_STEPPER04, 0);
        }
        if(StepX == 1){
          //System.out.println("step 1");
          Gpio.digitalWrite(RIGHT_STEPPER03, 1);
          Thread.sleep(STEP_PAUSE);
          Gpio.digitalWrite(RIGHT_STEPPER01, 0);
          Gpio.digitalWrite(RIGHT_STEPPER02, 0);
          Gpio.digitalWrite(RIGHT_STEPPER04, 0);
        }
        if(StepX == 2){
          //System.out.println("step 2");
          Gpio.digitalWrite(RIGHT_STEPPER02, 1);
          Thread.sleep(STEP_PAUSE);
          Gpio.digitalWrite(RIGHT_STEPPER01, 0);
          Gpio.digitalWrite(RIGHT_STEPPER03, 0);
          Gpio.digitalWrite(RIGHT_STEPPER04, 0);
        }
        if(StepX == 3){
          //System.out.println("step 3");
          Gpio.digitalWrite(RIGHT_STEPPER04, 1);
          Thread.sleep(STEP_PAUSE);
          Gpio.digitalWrite(RIGHT_STEPPER01, 0);
          Gpio.digitalWrite(RIGHT_STEPPER02, 0);
          Gpio.digitalWrite(RIGHT_STEPPER03, 0);
        }

        Thread.sleep(STEP_PAUSE);
      }
	

}
