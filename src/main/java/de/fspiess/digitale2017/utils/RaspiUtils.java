package de.fspiess.digitale2017.utils;

public class RaspiUtils {
	
	private static String OS = System.getProperty("os.arch").toLowerCase();
	
    public static boolean isRaspberryPi() {
        return (OS.indexOf("arm") >= 0);
    	//mock: avoid arm detection:
    	//return (OS.indexOf("XY") >= 0);
    }

}