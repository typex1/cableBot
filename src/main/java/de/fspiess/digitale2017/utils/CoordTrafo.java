package de.fspiess.digitale2017.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fspiess.digitale2017.motor.Motor;
import de.fspiess.digitale2017.motor.MotorController;

public class CoordTrafo {
	
	final static Logger logger = LoggerFactory.getLogger(MotorController.class);
	
    public static void moveXYZ(long x, long y, long z, long x0, long y0, long[] cordLength, Motor right, Motor left) throws InterruptedException{
	  long newCordLengthLeft;
	  long newCordLengthRight;
	
	  x += x0;
	  y += y0;
	  
	  logger.info("moveXYZ: cordlength[0]={} cordLength[1]={}", cordLength[0], cordLength[1]);
	
	  newCordLengthLeft = (long) Math.sqrt((double)(x * x) + (double)(y * y));
	  newCordLengthRight = (long) Math.sqrt((double)((Calibration.baseLength-x) * (Calibration.baseLength-x)) + (double)(y * y));
	
	  // I think because due to the sqrt() calculation, the single step approach by Bresenham
	  // might be compromised, here the new lengths (left, right) are approximated step by step
	  // both for left and right cords:
	  while(newCordLengthLeft > cordLength[1]){
	    left.makeStep(1);
	    cordLength[1]++;
	  }
	  while(newCordLengthLeft < cordLength[1]){
	    left.makeStep(-1);
	    cordLength[1]--;
	  }
	
	  while(newCordLengthRight > cordLength[0]){
	    right.makeStep(1);
	    cordLength[0]++;
	  }
	  while(newCordLengthRight < cordLength[0]){
	    right.makeStep(-1);
	    cordLength[0]--;
	  }
    }
}