package de.fspiess.digitale2017.utils;

public class Calibration {
	
	/*
	 * calibration constants for plotter, all in mm.
	 */
	public final static double stepsPerMM = 1000.0/145.0;//all values re-measured 20180128
	public final static long BASELENGTH = 2200L;
	public final static long CORDLENGTH_RIGHT = 1630L;
	public final static long CORDLENGTH_LEFT = 945L;
	public final static long baseLength = BASELENGTH * (long)stepsPerMM;
}
