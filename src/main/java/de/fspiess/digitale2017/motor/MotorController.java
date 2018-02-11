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
import de.fspiess.digitale2017.utils.Bresenham;
import de.fspiess.digitale2017.utils.Calibration;
import de.fspiess.digitale2017.utils.CoordTrafo;
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
	
	// actual drawing section - move pen along the lines and between the lines, pen can be up or down
	@RequestMapping("/start")
	public String startMovement() throws InterruptedException{
		lineList = lineService.getAllLines();
		for (int i = 0; i < lineList.size(); i++) {
			//logger.info(lineList.get(i));
			line = lineList.get(i);
			//logger.info(line.getId());
			logger.info("line: {} {} {} {} {} {} {} {}", line.getId(), line.getX1(), line.getY1(), line.getZ1(), 
																			line.getX2(), line.getY2(), line.getZ2(), line.getServoPosition());
			Motor rightStepper=motorService.getMotor("right");
			//rightStepper.dumpConfig();
			Motor leftStepper=motorService.getMotor("left");
			//leftStepper.dumpConfig();
			Motor zStepper=motorService.getMotor("z");
			Motor servo=motorService.getMotor("servo");
		
			
			// if there was already a line before, set pen up and move from end of last line to beginning of next line,
			// i.e. allow for drawing lines which are not connected to each other
			
			/* commented out to test only the coord-trafo part!
			if (lastLine != null && line.isSeparate(lastLine)){
				logger.info("line is NOT linked to last line, so lift pen and move to next line start!");
				servo.servoUp();
				Queue<Point> interimStepQueue = MotorStep.motorSteps(	lastLine.getX2(), lastLine.getY2(), line.getZ2(), 
																		line.getX1(), line.getY1(), line.getZ1());
				for(Object item:interimStepQueue){
					// todo: insert sth like moveXYZ(.getX(), .getY(), .getZ())
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
			
			//log some lines to see how long the queue build takes:
			logger.info("Start stepQueue build...");
			//calculate Bresenham points of current line:
			Queue<Point> stepQueue = MotorStep.motorSteps(line.getX1(), line.getY1(), line.getZ1(), line.getX2(), line.getY2(), line.getZ2());
			logger.info("end stepQueue build.");

			//if servoPosition is other than 0, put pen "Down", i.e. on paper!
			if (line.getServoPosition() > 0){
				servo.servoDown();
			}else{
				servo.servoUp();
			}
			
			for(Object item:stepQueue){
				rightStepper.makeStep(((Point) item).getX());
				leftStepper.makeStep(((Point) item).getY());
				zStepper.makeStep(((Point) item).getZ());
			}*/
			// added coord-trafo:
			long[] cordLength = new long[3];
			cordLength[0] = Calibration.CORDLENGTH_RIGHT * (long)Calibration.stepsPerMM;
			cordLength[1] = Calibration.CORDLENGTH_LEFT * (long)Calibration.stepsPerMM;
			cordLength[2] = 0L;
			long x0 = (cordLength[1] * cordLength[1] - cordLength[0] * cordLength[0] + Calibration.baseLength * Calibration.baseLength)
					/ (2L * Calibration.baseLength);
			long y0 = (long)Math.sqrt(cordLength[0] * cordLength[0] - (Calibration.baseLength - x0) * (Calibration.baseLength - x0));
			
			Queue<Point> bresenhamPoints = Bresenham.line3D(line.getX1(), line.getY1(), line.getZ1(), line.getX2(), line.getY2(), line.getZ2());
			logger.info("COORD-TRAFO: now beginning to calculate thread lengths and move steppers:");
			for(Object item:bresenhamPoints){
				CoordTrafo.moveXYZ(	((Point) item).getX(),
									((Point) item).getY(),
									((Point) item).getZ(), x0, y0, cordLength, rightStepper, leftStepper);
				logger.info("draw loop: X={}, X={}, new lengthR={}, newLengthL={}", ((Point) item).getX(), ((Point) item).getY(), cordLength[0], cordLength[1]);
			}
			logger.info("COORD-TRAFO: done.");
			// end added coord-trafo.
		}
		
		// remember: as this class is annotated with "@RequestMapping", the returned String is directly shown in the browser/Postman window!
		return "number of lines: "+String.valueOf(lineList.size());
	}

	/*
	This moves each motor's thread by x/y/z millimeters
	 */
	@RequestMapping(method=RequestMethod.POST, value = "/moveMM/{x}/{y}/{z}/{stepPause}")
	public String moveOrigin(@PathVariable int x, @PathVariable int y, @PathVariable int z,
							 @PathVariable int stepPause) throws InterruptedException{
		
		Motor rightStepper=motorService.getMotor("right");
		Motor leftStepper=motorService.getMotor("left");
		Motor zStepper=motorService.getMotor("z");
		Motor servo=motorService.getMotor("servo");
		int maxLength = 1000; // max. motion distance in mm
		
		if (x > maxLength || y > maxLength || z > maxLength){
			return "motion length is too large, maybe an error?";
		}
		
		//when calibrating the origin, make sure that pen is up
		servo.servoUp();
		
		int stepsX = x*(int)Calibration.stepsPerMM;
		int stepsY = y*(int)Calibration.stepsPerMM;
		int stepsZ = z*(int)Calibration.stepsPerMM;
		
		// "debug" functionality does not work at all:
		if(logger.isDebugEnabled()){
			logger.debug("moving coordinates in millimeters: {}, {}, {}", x, y, z);
		}
		logger.info("info: moving coordinates in millimeters: {}, {}, {}", stepsX, stepsY, stepsZ);
		
		Queue<Point> stepQueue = MotorStep.motorSteps(0, 0, 0, stepsX, stepsY, stepsZ);
		
		/*for(Object item:stepQueue){
			rightStepper.makeStep(((Point) item).getX());
			leftStepper.makeStep(((Point) item).getY());
			zStepper.makeStep(((Point) item).getZ());
		}*/

		/* todo: avoid stepper movement if not needed - because each step consumes stepPause time! */
		for(Object item:stepQueue){
			rightStepper.makeStepWithStepPause(((Point) item).getX(), stepPause);
			leftStepper.makeStepWithStepPause(((Point) item).getY(), stepPause);
			zStepper.makeStepWithStepPause(((Point) item).getZ(), stepPause);
		}
		return "/moveMM done.";
	}


	@RequestMapping(method=RequestMethod.POST, value = "/checkMotorL")
	public String checkMotorLeft() throws InterruptedException{

		Motor leftStepper=motorService.getMotor("left");

		for(int i=0; i<50; i++){
			leftStepper.makeStep(1);
		}
		for(int i=0; i<50; i++){
			leftStepper.makeStep(-1);
		}
		logger.info("checkMotorLeft() invoked.");
		return "/checkMotorL done.";
	}

	@RequestMapping(method=RequestMethod.POST, value = "/checkMotorR")
	public String checkMotorRight() throws InterruptedException{

		Motor rightStepper=motorService.getMotor("right");

		for(int i=0; i<50; i++){
			rightStepper.makeStep(1);
		}
		for(int i=0; i<50; i++){
			rightStepper.makeStep(-1);
		}
		logger.info("checkMotorRight() invoked.");
		return "/checkMotorR done.";
	}


	@RequestMapping(method=RequestMethod.POST, value = "/checkMotorZ")
	public String checkMotorZ() throws InterruptedException{

		Motor zStepper=motorService.getMotor("z");

		for(int i=0; i<50; i++){
			zStepper.makeStep(1);
		}
		for(int i=0; i<50; i++){
			zStepper.makeStep(-1);
		}
		logger.info("checkMotorZ() invoked.");
		return "/checkMotorZ done.";
	}
	
	@RequestMapping("/end")
	public void turnOffGpioPins() throws InterruptedException{
		Motor.motorsOff();
	}
    
}