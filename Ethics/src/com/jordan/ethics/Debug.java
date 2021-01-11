package com.jordan.ethics;

import java.util.GregorianCalendar;

public class Debug {

	public static void log(String format, Object...args) {
		String print = String.format(format, args);
		System.out.printf("[%1$tH:%1$tM:%1$tS] %2$s%n", new GregorianCalendar(), print);
	}
	
	public static void error(Exception e) {
		log("A(n) %s exception has occured.", e.getClass().getSimpleName());
		e.printStackTrace();
	}
}
