package de.fspiess.digitale2017.utils;

public class Calibration {
	
	/*
	 * calibration constants for plotter
	 */
	public final static double stepsPerMM = 1000.0/135.0;//re-evaluated 20171003
	public final static long BASELENGTH = 2110L;
	public final static long CORDLENGTH_RIGHT = 1960L;
	public final static long CORDLENGTH_LEFT = 715L;
	public final static long baseLength = BASELENGTH * (long)stepsPerMM;
}
