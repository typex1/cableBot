package de.fspiess.digitale2017.motor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pi4j.wiringpi.Gpio;

import de.fspiess.digitale2017.utils.RaspiUtils;

@Service
public class MotorService{
	
	//@Autowired
	Motor rightStepper = new Motor("right");
	//@Autowired
	Motor leftStepper = new Motor("left");
	
	public MotorService() {	
		System.out.println("MotorService constructor!");
	}
	
	public Motor getMotor(String name){
		if (name == "right"){
			return rightStepper;
		}
		if (name == "left"){
			return leftStepper;
		}
		return rightStepper;
	}
	
}
