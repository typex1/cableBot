package de.fspiess.digitale2017;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.pi4j.wiringpi.Gpio;

import de.fspiess.digitale2017.CableBotApp;

@SpringBootApplication
public class CableBotApp extends SpringBootServletInitializer implements ServletContextListener{
	
	private static String OS = System.getProperty("os.arch").toLowerCase();
	
    public static boolean isRaspberryPi() {
        return (OS.indexOf("arm") >= 0);
    }
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CableBotApp.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(CableBotApp.class, args);
	}
	
	//this is even called if the tomcat server is killed with "kill <PID>" !
	@Override
    public void contextDestroyed(final ServletContextEvent arg0) {
        System.out.println("Servlet Context destroyed");
        //put motor stop function here!
    }

    @Override
    public void contextInitialized(final ServletContextEvent arg0) {
        try {
            System.out.println("Creating Servlet Context");
            
            //this is definetely invoked at servlet startup, in contrast to the above main() method!
            if (CableBotApp.isRaspberryPi()){
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