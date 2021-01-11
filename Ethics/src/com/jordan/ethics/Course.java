package com.jordan.ethics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Course {

	private static Course[] courses = null;
	
	public static void resetCache() {
		courses = null;
	}
	
	private String courseId;
	private College department;
	private ArrayList<Flag> flags;
	private Year[] years;
	
	public Course() {
		flags = new ArrayList<Flag>();
	}
	
	public Course setId(String id) {
		this.courseId = id;
		return this;
	}
	
	public String getId() {
		return this.courseId;
	}
	
	public Course setDepartment(College department) {
		this.department = department;
		return this;
	}
	
	public College getDepartment() {
		return this.department;
	}
	
	public Course addFlags(Flag...flags) {
		for (Flag flag : flags) this.flags.add(flag);
		return this;
	}
	
	public Flag[] getFlags() {
		return flags.toArray(new Flag[0]);
	}
	
	public int getFlagBitmask() {
		int mask = 0;
		for (Flag flag : this.flags) mask |= flag.getFlag();
		return mask;
	}
	
	public Course setYears(Year[] years) {
		this.years = years;
		return this;
	}
	
	public Year[] getYears() {
		return this.years;
	}
	
	public boolean insert() {
		try {
			JDBC.executeUpdate("insert into courses values('%s', '%s', %d);", Util.fixCourseId(getId()), Util.clean(getDepartment().toString()), getFlagBitmask());
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return courseId;
	}
	
	////////////////////////////////////////////
	
	private static Course read(ResultSet res) throws SQLException {
		Course c = new Course();
		c.setId(res.getString("courseId"));
		c.setDepartment(College.valueOf(res.getString("department")));
		c.addFlags(Flag.getFlags(res.getInt("flags")));
		c.setYears(Year.getYears(c.getId()));
		return c;
	}
	
	public static Course getCourse(String courseId) {
		try (ResultSet res = JDBC.executeQuery("select * from courses where courseId = '%s';", Util.fixCourseId(courseId))) {
			if (res.next()) {
				return read(res);
			}
		} catch (SQLException e) {
			Debug.error(e);
		}
		
		return null;
	}
	
	public static Course[] getAllCourses() {
		if (courses != null) return Course.courses;
		
		ArrayList<Course> courses = new ArrayList<Course>();
		try (ResultSet res = JDBC.executeQuery("select * from courses;")) {
			while (res.next()) {
				courses.add(read(res));
			}
		} catch (SQLException e) {
			Debug.error(e);
		}
		
		return Course.courses = courses.toArray(new Course[0]);
	}
}
