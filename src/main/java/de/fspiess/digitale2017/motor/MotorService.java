package de.fspiess.digitale2017.motor;

import org.springframework.stereotype.Service;

@Service
public class MotorService{
	
	//@Autowired
	Motor rightStepper = new Motor("right");
	//@Autowired
	Motor leftStepper = new Motor("left");
	Motor zStepper = new Motor("z");
	Motor servo = new Motor("servo");
	
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
		if (name == "z"){
			return zStepper;
		}
		if (name == "servo"){
			return servo;
		}
		return rightStepper;
	}
	
}
