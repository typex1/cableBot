package de.fspiess.digitale2017.motor;

import java.util.List;
import java.util.Queue;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.fspiess.digitale2017.CableBotApp;
import de.fspiess.digitale2017.line.Line;
import de.fspiess.digitale2017.line.LineController;
import de.fspiess.digitale2017.line.LineService;
import de.fspiess.digitale2017.utils.Point;
import de.fspiess.digitale2017.utils.RaspiUtils;
import de.fspiess.digitale2017.utils.MotorStep;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;

@RestController
public class MotorController{

	@Autowired
	private LineService lineService;
	@Autowired
	private MotorService motorService;
	private List<Line> lineList;
	private Line line;
	
	public MotorController() {	
		System.out.println("MotorController constructor!");
		
        // Attention: Gpio.wiringPiSetup() must only be called ONCE, and prior to each GPIO activities!
        // currently, the order of constructuor invocation when executing a .war on a real Tomcat instance is:
        // Controller class, Motor class, Service class, "Servlet context created Event".
        if (RaspiUtils.isRaspberryPi()){
			System.out.println("This is a Raspberry, will now setup GIPO ports!");

	        // setup wiringPi
	        if (Gpio.wiringPiSetup() == -1) {
	            System.out.println(" ==>> GPIO SETUP FAILED");
	        }else{
	        	System.out.println(" ==>> GPID Setup SUCCESSFUL");
	        }
		}else{
			System.out.println("Platform is not Raspberry (arm), GPIO functions will not be called.");
		}
	}
	
	
	@RequestMapping("/start")
	public String startMovement() throws InterruptedException{
		lineList = lineService.getAllLines();
		for (int i = 0; i < lineList.size(); i++) {
			//System.out.println(lineList.get(i));
			line = lineList.get(i);
			//System.out.println(line.getId());
			System.out.printf("line: %s %d %d %d %d %d %d %d\n", line.getId(), line.getX1(), line.getY1(), line.getZ1(), 
																			line.getX2(), line.getY2(), line.getZ2(), line.getServo());
		
			System.out.println("Start stepQueue build...");
			Queue<Point> stepQueue = MotorStep.motorSteps(line.getX1(), line.getY1(), line.getZ1(), line.getX2(), line.getY2(), line.getZ2());
			System.out.println("end stepQueue build.");
			Motor rightStepper=motorService.getMotor("right");
			//rightStepper.dumpConfig();
			Motor leftStepper=motorService.getMotor("left");
			//leftStepper.dumpConfig();
			
			for(Object item:stepQueue){
				rightStepper.makeStep(((Point) item).getX());
				leftStepper.makeStep(((Point) item).getY());
			}
		}
		
		
		
		return "number of lines: "+String.valueOf(lineList.size());
	}
	
	@RequestMapping("/end")
	public void turnOffGpioPins() throws InterruptedException{
		Motor.motorsOff();
	}
    
}