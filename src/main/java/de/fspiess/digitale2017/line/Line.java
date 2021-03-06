package de.fspiess.digitale2017.line;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fspiess.digitale2017.motor.MotorController;

//define this as an entity class:
@Entity
public class Line {
	
	final static Logger logger = LoggerFactory.getLogger(MotorController.class);
	
	@Id
	private String id;
	private int x1;
	private int y1;
	private int z1;
	private int x2;
	private int y2;
	private int z2;
	private int servoPosition;
	
	public Line() {
	}
	
	public Line(String id, int x1, int y1, int z1, int x2, int y2, int z2, int servoPosition) {
		super();
		this.id = id;
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.servoPosition = servoPosition;
	}
	
	//check if end of lastLine is same as beginning of next line
	public boolean isSeparate(Line otherLine){
		logger.info("to compare: otherLine.getX2()={} != this.x1={} || otherLine.getY2()={} != this.y1={} || otherLine.getZ2()={} != this.z1={}", 
				otherLine.getX2(), this.x1, otherLine.getY2(), this.y1, otherLine.getZ2(), this.z1);
		if (otherLine.getX2() != this.x1 || otherLine.getY2() != this.y1 || otherLine.getZ2() != this.z1){
			return true;
		}
		return false;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getX1() {
		return x1;
	}
	public void setX1(int x1) {
		this.x1 = x1;
	}
	
	public int getY1() {
		return y1;
	}
	public void setY1(int y1) {
		this.y1 = y1;
	}
	
	public int getZ1() {
		return z1;
	}
	public void setZ1(int z1) {
		this.z1 = z1;
	}
	
	public int getX2() {
		return x2;
	}
	public void setX2(int x2) {
		this.x2 = x2;
	}
	
	public int getY2() {
		return y2;
	}
	public void setY2(int y2) {
		this.y2 = y2;
	}
	
	public int getZ2() {
		return z2;
	}
	public void setZ2(int z2) {
		this.z2 = z2;
	}
	
	public int getServoPosition() {
		return servoPosition;
	}
	public void setServoPosition(int servo) {
		this.servoPosition = servoPosition;
	}
}
