package com.jordan.ethics.ui;

public class YearDisplay {

	private int year;
	public YearDisplay(int year) {
		this.year = year;
	}
	
	public int getYear() {
		return year;
	}
	
	@Override
	public String toString() {
		return String.format("%d-%d", year, year + 1);
	}
}
