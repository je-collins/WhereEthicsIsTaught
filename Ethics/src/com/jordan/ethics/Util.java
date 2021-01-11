package com.jordan.ethics;

public class Util {

	public static String fixCourseId(String id) {
		return id.toUpperCase().replaceAll("\\W+", "");
	}
	
	public static String clean(String string) {
		string = string.replace("'", "''");
		return string;
	}
	
	public static int getTotalCoursesInYear(int year) {
		CourseSearch search = new CourseSearch();
		search.setYear(year);
		search.setMinimumHours(0);
		return search.search().length;
	}
	
	public static int getTotalCoursesInYearWithHours(int year) {
		CourseSearch search = new CourseSearch();
		search.setYear(year);
		search.setMinimumHours(1);
		return search.search().length;
	}
	
	public static int getTotalCoursesInYearBySearch(int year, String...words) {
		CourseSearch search = new CourseSearch();
		search.setYear(year);
		search.addSearchWords(words);
		search.setMinimumHours(0);
		return search.search().length;
	}
	
	public static int getTotalCoursesInYearBySearchWithHours(int year, String...words) {
		CourseSearch search = new CourseSearch();
		search.setYear(year);
		search.addSearchWords(words);
		search.setMinimumHours(1);
		return search.search().length;
	}
	
	public static int getCollegeCoursesInYear(College college, int year) {
		CourseSearch search = new CourseSearch();
		search.setYear(year);
		search.setMinimumHours(0);
		search.setDepartment(college);
		return search.search().length;
	}
	
	public static int getCollegeCoursesInYearWithHours(College college, int year) {
		CourseSearch search = new CourseSearch();
		search.setYear(year);
		search.setMinimumHours(1);
		search.setDepartment(college);
		return search.search().length;
	}
	
	public static int getCollegeCoursesInYearBySearch(College college, int year, String...words) {
		CourseSearch search = new CourseSearch();
		search.setYear(year);
		search.addSearchWords(words);
		search.setMinimumHours(0);
		search.setDepartment(college);
		return search.search().length;
	}
	
	public static int getCollegeCoursesInYearBySearchWithHours(College college, int year, String...words) {
		CourseSearch search = new CourseSearch();
		search.setYear(year);
		search.addSearchWords(words);
		search.setMinimumHours(1);
		search.setDepartment(college);
		return search.search().length;
	}
}
