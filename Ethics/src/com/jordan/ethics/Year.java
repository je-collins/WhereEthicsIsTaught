package com.jordan.ethics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Year {

	private String courseId;
	private int year;
	private String courseInfo;
	private int hours;
	private Course course;
	
	public Year setCourseId(String courseId) {
		this.courseId = courseId;
		return this;
	}
	
	public String getCourseId() {
		return this.courseId;
	}
	
	public Year setYear(int year) {
		this.year = year;
		return this;
	}
	
	public int getYear() {
		return this.year;
	}
	
	public Year setCourseInfo(String courseInfo) {
		this.courseInfo = courseInfo;
		return this;
	}
	
	public String getCourseInfo() {
		return courseInfo;
	}
	
	public Year setHours(int hours) {
		this.hours = hours;
		return this;
	}
	
	public int getHours() {
		return this.hours;
	}
	
	public Year setCourse(Course course) {
		this.course = course;
		return this;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public boolean insert() {
		try {
			JDBC.executeUpdate("insert into years values('%s', %d, '%s', %d);", Util.fixCourseId(getCourseId()), getYear(), Util.clean(getCourseInfo()), getHours());
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return String.format("%d-%d : %,d Hours", year, year + 1, hours);
	}
	
	/////////////////////////////////
	
	private static Year read(ResultSet res) throws SQLException {
		Year y = new Year();
		y.setCourseId(res.getString("courseId"));
		y.setYear(res.getInt("year"));
		y.setCourseInfo(res.getString("courseInfo"));
		y.setHours(res.getInt("hours"));
		return y;
	}
	
	public static Year getYear(String courseId, int year) {
		try (ResultSet res = JDBC.executeQuery("select * from years where courseId = '%s' and year = %d;", Util.fixCourseId(courseId), year)) {
			if (res.next()) {
				return read(res);
			}
		} catch (SQLException e) {
			Debug.error(e);
		}
		
		return null;
	}
	
	public static Year[] getYears(String courseId) {
		ArrayList<Year> years = new ArrayList<Year>();
		try (ResultSet res = JDBC.executeQuery("select * from years where courseId = '%s';", Util.fixCourseId(courseId))) {
			while (res.next()) {
				years.add(read(res));
			}
		} catch (SQLException e) {
			Debug.error(e);
		}
		
		return years.toArray(new Year[0]);
	}
	
	public static Year[] getAllYears() {
		ArrayList<Year> years = new ArrayList<Year>();
		
		try (ResultSet res = JDBC.executeQuery("select * from years;")) {
			while (res.next()) {
				years.add(read(res));
			}
		} catch (SQLException e) {
			Debug.error(e);
		}
		
		return years.toArray(new Year[0]);
	}
}
