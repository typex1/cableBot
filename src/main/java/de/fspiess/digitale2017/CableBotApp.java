package de.fspiess.digitale2017;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.pi4j.wiringpi.Gpio;

import de.fspiess.digitale2017.motor.Motor;
import de.fspiess.digitale2017.utils.RaspiUtils;


@SpringBootApplication
public class CableBotApp extends SpringBootServletInitializer implements ServletContextListener{
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CableBotApp.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(CableBotApp.class, args);
	}
	
	// this is called if tomcat servlet (CableBot) is starting up
	// HINT: do NOT put this in the Controller class as it is invoked AFTER the Service class, i.e. AFTER instanciating the Motor instances!
	// motivation: Gpio.wiringPiSetup() must be called at very start and must NOT be called more than once!
    @Override
    public void contextInitialized(final ServletContextEvent arg0) {
        try {
            System.out.println("Creating Servlet Context");
            

        } finally {
            System.out.println("Servlet Context created");
        }
    }
    
    // this is even called if the tomcat server is killed with "kill <PID>" !
    // motivation: switch off current from all motor coils, otherwise motor keeps being under current!
  	@Override
	public void contextDestroyed(final ServletContextEvent arg0) {
	      System.out.println("Servlet Context destroyed");
	      //put motor stop function here!
	      Motor.motorsOff();
    }
	
}