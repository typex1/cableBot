package de.fspiess.digitale2017.line;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pi4j.wiringpi.Gpio;

import de.fspiess.digitale2017.motor.Motor;
import de.fspiess.digitale2017.utils.RaspiUtils;

@RestController
public class LineController implements ServletContextListener{
	
	//make automatically generated LineService instance available to LineController:
	@Autowired
	private LineService lineService;
	
	/*
	 * default: GET request
	 */
	@RequestMapping("/lines")
	public List<Line> getAllLines(){
		return lineService.getAllLines();
	}
	
	@RequestMapping("/lines/{id}")
	public Line getLine(@PathVariable String id){
		return lineService.getLine(id);
	}
	
	@RequestMapping(method=RequestMethod.POST, value = "/lines")
	public void addLine(@RequestBody Line line){
		lineService.addLine(line);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value = "/lines/{id}")
	public void updateLine(@RequestBody Line line, @PathVariable String id){
		lineService.updateLine(id, line);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value = "/lines/{id}")
	public void deleteLine(@PathVariable String id){
		lineService.deleteLine(id);
	}
	
	public LineService getLineService(){
		return lineService;
	}
	
	//this is even called if the tomcat server is killed with "kill <PID>" !
	@Override
    public void contextDestroyed(final ServletContextEvent arg0) {
        System.out.println("Servlet Context destroyed");
        //put motor stop function here!
        Motor.motorsOff();
    }

	
    @Override
    public void contextInitialized(final ServletContextEvent arg0) {
        try {
            System.out.println("Creating Servlet Context");
            
            //this is definetely invoked at servlet startup, in contrast to the above main() method!
            if (RaspiUtils.isRaspberryPi()){
    			System.out.println("This is a Raspberry!");

    	        // setup wiringPi
    	        if (Gpio.wiringPiSetup() == -1) {
    	            System.out.println(" ==>> GPIO SETUP FAILED");
    	        }else{
    	        	System.out.println(" ==>> GPID Setup SUCCESSFUL");
    	        }
    		}else{
    			System.out.println("Platform is not Raspberry (arm), GPIO functions will not be called.");
    		}
        } finally {
            System.out.println("Servlet Context created");
        }
    }

}
