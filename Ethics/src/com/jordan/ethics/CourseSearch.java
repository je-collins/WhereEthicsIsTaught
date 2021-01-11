package com.jordan.ethics;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseSearch {

	private ArrayList<String> search;
	private boolean doSearch;
	private int year;
	private boolean doYear;
	private int hours;
	private boolean doHours;
	private College college;
	private boolean doDepartment;
	
	public CourseSearch() {
		search = new ArrayList<String>();
	}
	
	public void addSearchWords(String...words) {
		for (String word : words) search.add(word.toLowerCase());
		doSearch = true;
	}
	
	public void setYear(int year) {
		this.year = year;
		doYear = true;
	}
	
	public void setMinimumHours(int hours) {
		this.hours = hours;
		doHours = true;
	}
	
	public void setDepartment(College college) {
		this.college = college;
		doDepartment = true;
	}
	
	public Year[] search() {
		ArrayList<Year> years = new ArrayList<Year>();
		
		HashMap<String, Course> courses = new HashMap<String, Course>();
		for (Course course : Course.getAllCourses()) {
			courses.put(course.getId(), course);
		}
		
		for (Year year : Year.getAllYears()) {
			year.setCourse(courses.get(year.getCourseId()));
			
			if (this.doSearch) {
				boolean hasWord = false;
				
				for (String word : this.search) {
					if (year.getCourseInfo().toLowerCase().contains(word)) {
						hasWord = true;
						break;
					}
				}
				
				if (!hasWord) continue;
			}
			
			if (this.doYear) {
				if (this.year != year.getYear()) continue;
			}
			
			if (this.doHours) {
				if (year.getHours() < this.hours) continue;
			}
			
			if (doDepartment) {
				if (year.getCourse().getDepartment() != college) continue;
			}
			
			years.add(year);
		}
		
		return years.toArray(new Year[0]);
	}
}
