package de.fspiess.digitale2017.utils;

public class Point {
	
	private int x;
	private int y;
	private int z;

	public Point() {	
	}
		
	public Point(int x, int y, int z){
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
}