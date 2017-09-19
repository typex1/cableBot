package de.fspiess.digitale2017.start;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.fspiess.digitale2017.CableBotApp;
import de.fspiess.digitale2017.line.Line;
import de.fspiess.digitale2017.line.LineService;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

@RestController
public class StartController {

	@Autowired
	private LineService lineService;
	private List<Line> lineList;
	private Line line;
	
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
    private static int StepX = 0;
    private static final int STEP_PAUSE = 1;
    
    private void motorsOff(){
        Gpio.digitalWrite(RIGHT_STEPPER01, 0);
        Gpio.digitalWrite(RIGHT_STEPPER02, 0);
        Gpio.digitalWrite(RIGHT_STEPPER03, 0);
        Gpio.digitalWrite(RIGHT_STEPPER04, 0);

        Gpio.digitalWrite(LEFT_STEPPER03, 0);
        Gpio.digitalWrite(LEFT_STEPPER01, 0);
        Gpio.digitalWrite(LEFT_STEPPER02, 0);
        Gpio.digitalWrite(LEFT_STEPPER04, 0);
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
	
	@RequestMapping("/start")
	public String startLinement() throws InterruptedException{
		lineList = lineService.getAllLines();
		for (int i = 0; i < lineList.size(); i++) {
			//System.out.println(lineList.get(i));
			line = lineList.get(i);
			//System.out.println(line.getId());
			System.out.printf("line: %s %d %d %d %d %d %d %d\n", line.getId(), line.getX1(), line.getY1(), line.getZ1(), 
																			line.getX2(), line.getY2(), line.getZ2(), line.getServo());
		}
		if (CableBotApp.isRaspberryPi()){
			System.out.println("Raspberry");
	        int width = 1000;

	        // set relevant GPIO as outputs:
	        GpioUtil.export(RIGHT_STEPPER01, GpioUtil.DIRECTION_OUT);
	        Gpio.pinMode(RIGHT_STEPPER01, Gpio.OUTPUT);
	        GpioUtil.export(RIGHT_STEPPER02, GpioUtil.DIRECTION_OUT);
	        Gpio.pinMode(RIGHT_STEPPER02, Gpio.OUTPUT);
	        GpioUtil.export(RIGHT_STEPPER03, GpioUtil.DIRECTION_OUT);
	        Gpio.pinMode(RIGHT_STEPPER03, Gpio.OUTPUT);
	        GpioUtil.export(RIGHT_STEPPER04, GpioUtil.DIRECTION_OUT);
	        Gpio.pinMode(RIGHT_STEPPER04, Gpio.OUTPUT);
	        for(int i=0; i < width; i++){
	            makeStepRight(-1);
	        }
		}else{
			System.out.println("Is not Raspberry");
		}
		return "number of lines: "+String.valueOf(lineList.size());
	}
}