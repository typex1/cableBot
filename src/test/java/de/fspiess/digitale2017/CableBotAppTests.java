package de.fspiess.digitale2017;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pi4j.wiringpi.Gpio;

import de.fspiess.digitale2017.utils.RaspiUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CableBotAppTests {
	
    private static final int LEFT_STEPPER01=26;
    private static final int LEFT_STEPPER02=27;
    private static final int LEFT_STEPPER03=28;
    private static final int LEFT_STEPPER04=29;
    
	int stepper1 = LEFT_STEPPER01;
	int stepper2 = LEFT_STEPPER02;
	int stepper3 = LEFT_STEPPER03;
	int stepper4 = LEFT_STEPPER04;
    // Iterates between 0,1,2,3 and 0 again. For switching through the different motor coils.
    private int stepX = 0;
    private int STEP_PAUSE = 4;// 2 is best value for a smooth, fast move.

	@Test
	public void contextLoads() throws InterruptedException {
		runMotorSteps();
	}
	
	private void runMotorSteps() throws InterruptedException {
		for(int i=0; i<200; i++){
			makeStep(1);
		}
		System.out.println("\n\n *TEST* runMotorSteps done!\n\n");
	}
	
    private void makeStep(int direction) throws InterruptedException {
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
	      Gpio.digitalWrite(this.stepper2, 0);
	      Gpio.digitalWrite(this.stepper3, 0);
	      Gpio.digitalWrite(this.stepper4, 0);
	      //sleepMillis(STEP_PAUSE);
	      Thread.sleep(STEP_PAUSE);
	    }
	    if(stepX == 1 && RaspiUtils.isRaspberryPi()){
	      //System.out.println("step 1");
	      Gpio.digitalWrite(this.stepper3, 1);
	      Gpio.digitalWrite(this.stepper1, 0);
	      Gpio.digitalWrite(this.stepper2, 0);
	      Gpio.digitalWrite(this.stepper4, 0);
	      //sleepMillis(STEP_PAUSE);
	      Thread.sleep(STEP_PAUSE);
	    }
	    if(stepX == 2 && RaspiUtils.isRaspberryPi()){
	      //System.out.println("step 2");
	      Gpio.digitalWrite(this.stepper2, 1);
	      Gpio.digitalWrite(this.stepper1, 0);
	      Gpio.digitalWrite(this.stepper3, 0);
	      Gpio.digitalWrite(this.stepper4, 0);
	      //sleepMillis(STEP_PAUSE);
	      Thread.sleep(STEP_PAUSE);
	    }
	    if(stepX == 3 && RaspiUtils.isRaspberryPi()){
	      //System.out.println("step 3");
	      Gpio.digitalWrite(this.stepper4, 1);
	      Gpio.digitalWrite(this.stepper1, 0);
	      Gpio.digitalWrite(this.stepper2, 0);
	      Gpio.digitalWrite(this.stepper3, 0);
	      //sleepMillis(STEP_PAUSE);
	      Thread.sleep(STEP_PAUSE);
	    }
	
	    //Thread.sleep(STEP_PAUSE);
	}

}
