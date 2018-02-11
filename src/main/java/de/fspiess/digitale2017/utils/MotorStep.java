package de.fspiess.digitale2017.utils;

import java.util.LinkedList;
import java.util.Queue;

import de.fspiess.digitale2017.utils.Point;
import de.fspiess.digitale2017.utils.Bresenham;

public class MotorStep {
	
	// calculates deltas between each point in the Bresenham queue, the result is a queue of necessary motor steps for X, Y, Z
    public static Queue<Point> motorSteps(int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd){
    	int x1 = 0;
        int y1 = 0;
        int z1 = 0;
        int x2 = -1;
        int y2 = -1;
        int z2 = -1;
        
        //at first, calculate queue of Bresenham points:
        System.out.println("Start Bresenham queue build...");
        Queue<Point> line = Bresenham.line3D(xStart, yStart, zStart, xEnd, yEnd, zEnd);
        System.out.println("End Bresenham build.");
        
        Queue<Point> result = new LinkedList<Point>();
        Point point = new Point();
        
        //calculate deltas between subsequent Bresenham queue entries:
        for (Object item:line){
        	x1 = ((Point) item).getX();
        	y1 = ((Point) item).getY();
        	z1 = ((Point) item).getZ();
        	//System.out.printf("Point %d %d %d - ", ((Point) item).getX(), ((Point) item).getY(), ((Point) item).getZ());
        	if (x2 > -1){
        		point.setX(x1-x2);
        	}
        	if (y2 > -1){
        		point.setY(y1-y2);
        	}
        	if (z2 > -1){
        		point.setZ(z1-z2);
        		result.offer(point);
        	}
        	//System.out.printf("Step: %d %d %d\n", point.getX(), point.getY(), point.getZ());
        	
        	if (Math.abs(point.getX()) >1 || Math.abs(point.getY()) > 1 || Math.abs(point.getZ()) > 1){
        		System.out.println("ERROR in Bresenham algorithm - must not compute steps > 1 !!");
        	}
        	x2 = x1;
        	y2 = y1;
        	z2 = z1;
        }
        
        return result;
    }

}
