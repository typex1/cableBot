package de.fspiess.digitale2017.motor;

import java.util.List;
import java.util.Queue;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.fspiess.digitale2017.line.Line;
import de.fspiess.digitale2017.line.LineService;
import de.fspiess.digitale2017.utils.Point;
import de.fspiess.digitale2017.utils.RaspiUtils;
import de.fspiess.digitale2017.utils.Calibration;
import de.fspiess.digitale2017.utils.MotorStep;

import com.pi4j.wiringpi.Gpio;

@RestController
public class MotorController{

	@Autowired
	private LineService lineService;
	@Autowired
	private MotorService motorService;
	private List<Line> lineList;
	private Line line;
	private Line lastLine;
	
	final static Logger logger = LoggerFactory.getLogger(MotorController.class);
	
	public MotorController() {	
		logger.info("MotorController constructor!");
		
        // Attention: Gpio.wiringPiSetup() must only be called ONCE, and prior to each GPIO activities!
        // currently, the order of constructuor invocation when executing a .war on a *real* Tomcat instance (not in STS) is:
        // MotorController class, Motor class, MotorService class, then comes "Servlet context created Event".
        if (RaspiUtils.isRaspberryPi()){
			logger.info("This is a Raspberry, will now setup GIPO ports!");

	        // setup wiringPi
	        if (Gpio.wiringPiSetup() == -1) {
	            logger.info(" ==>> GPIO SETUP FAILED");
	        }else{
	        	logger.info(" ==>> GPID Setup SUCCESSFUL");
	        }
		}else{
			logger.info("Platform is not Raspberry (arm), GPIO functions will not be called.");
		}
	}
	
	
	@RequestMapping("/start")
	public String startMovement() throws InterruptedException{
		lineList = lineService.getAllLines();
		for (int i = 0; i < lineList.size(); i++) {
			//logger.info(lineList.get(i));
			line = lineList.get(i);
			//logger.info(line.getId());
			logger.info("line: {} {} {} {} {} {} {} {}", line.getId(), line.getX1(), line.getY1(), line.getZ1(), 
																			line.getX2(), line.getY2(), line.getZ2(), line.getServo());
			Motor rightStepper=motorService.getMotor("right");
			//rightStepper.dumpConfig();
			Motor leftStepper=motorService.getMotor("left");
			//leftStepper.dumpConfig();
			Motor zStepper=motorService.getMotor("z");
			Motor servo=motorService.getMotor("servo");
		
			
			// if there was already a line before, set pen up and move from end of last line to beginning of next line,
			// i.e. allow for drawing lines which are not connected to each other
			if (lastLine != null && line.isSeparate(lastLine)){
				logger.info("line is NOT linked to last line, so lift pen and move to next line start!");
				servo.servoUp();
				Queue<Point> interimStepQueue = MotorStep.motorSteps(	lastLine.getX2(), lastLine.getY2(), line.getZ2(), 
																		line.getX1(), line.getY1(), line.getZ1());
				for(Object item:interimStepQueue){
					rightStepper.makeStep(((Point) item).getX());
					leftStepper.makeStep(((Point) item).getY());
					zStepper.makeStep(((Point) item).getZ());
				}
			}else{
				logger.info("line is very first in queue OR line IS linked to last line.");
			}
			// if lastLine is null, i.e. pen is at origin, move pen to beginning of first line.
			if (lastLine == null){
				logger.info("pen is at origin, move pen to beginning of first line!");
				servo.servoUp();
				Queue<Point> interimStepQueue = MotorStep.motorSteps(	0, 0, 0, line.getX1(), line.getY1(), line.getZ1());
				for(Object item:interimStepQueue){
					rightStepper.makeStep(((Point) item).getX());
					leftStepper.makeStep(((Point) item).getY());
					zStepper.makeStep(((Point) item).getZ());
				}
			}
			lastLine = line;
			
			logger.info("Start stepQueue build...");
			//calculate Bresenham points of current line:
			Queue<Point> stepQueue = MotorStep.motorSteps(line.getX1(), line.getY1(), line.getZ1(), line.getX2(), line.getY2(), line.getZ2());
			logger.info("end stepQueue build.");

			
			for(Object item:stepQueue){
				rightStepper.makeStep(((Point) item).getX());
				leftStepper.makeStep(((Point) item).getY());
				zStepper.makeStep(((Point) item).getZ());
			}
		}
		
		return "number of lines: "+String.valueOf(lineList.size());
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/moveMM/{x}/{y}/{z}")
	public void moveOrigin(@PathVariable int x, @PathVariable int y, @PathVariable int z) throws InterruptedException{
		
		Motor rightStepper=motorService.getMotor("right");
		Motor leftStepper=motorService.getMotor("left");
		Motor zStepper=motorService.getMotor("z");
		Motor servo=motorService.getMotor("servo");
		
		//when calibrating the origin, make sure that pen is up
		servo.servoUp();
		
		int moveX = x*(int)Calibration.stepsPerMM;
		int moveY = y*(int)Calibration.stepsPerMM;
		int moveZ = z*(int)Calibration.stepsPerMM;
		
		
		
		// "debug" functionality does not work at all:
		if(logger.isDebugEnabled()){
			logger.debug("moving coordinates in millimeters: {}, {}, {}", x, y, z);
		}
		logger.info("info: moving coordinates in millimeters: {}, {}, {}", moveX, moveY, moveZ);
		
		Queue<Point> stepQueue = MotorStep.motorSteps(0, 0, 0, moveX, moveY, moveZ);
		
		for(Object item:stepQueue){
			rightStepper.makeStep(((Point) item).getX());
			leftStepper.makeStep(((Point) item).getY());
			zStepper.makeStep(((Point) item).getZ());
		}
	}
	
	@RequestMapping("/end")
	public void turnOffGpioPins() throws InterruptedException{
		Motor.motorsOff();
	}
    
}