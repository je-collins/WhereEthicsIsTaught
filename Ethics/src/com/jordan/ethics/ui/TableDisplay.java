package com.jordan.ethics.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.jordan.ethics.College;
import com.jordan.ethics.Debug;
import com.jordan.ethics.Util;

public class TableDisplay extends JPanel {

	private static final long serialVersionUID = 3187203601993764107L;
	
	public TableDisplay() {
		super();
		reset();
	}
	
	public void reset() {
		updateDisplay(new YearDisplay(0), new YearDisplay(-1), new ArrayList<College>(), new String[0]);
	}

	public void updateDisplay(YearDisplay startYear, YearDisplay endYear, List<College> colleges, String[] keyWords) {
		removeAll();
		
		Debug.log("Searching...");
		Debug.log("Years: %d to %d", startYear.getYear(), endYear.getYear());
		Debug.log("Colleges: %s", colleges);
		
		JTabbedPane tabs = new JTabbedPane();
		this.add(tabs);
		
		tabs.addTab("Overall", generateOverallTable(startYear.getYear(), endYear.getYear(), keyWords));
		for (College college : colleges) {
			tabs.addTab(college.name(), generateCollegeTable(startYear.getYear(), endYear.getYear(), college, keyWords));
		}
	}
	
	private static JPanel generateOverallTable(int start, int end, String[] keyWords) {
		Object[][] array = new Object[end - start + 1][9];
		for (int year = start; year <= end; year++) {
			array[year - start][0] = String.format("%d/%d", year, year + 1);
			array[year - start][1] = Util.getTotalCoursesInYear(year);
			array[year - start][2] = Util.getTotalCoursesInYearWithHours(year);
			array[year - start][3] = Util.getTotalCoursesInYearBySearch(year, keyWords);
			array[year - start][4] = Util.getTotalCoursesInYearBySearchWithHours(year, keyWords);
			array[year - start][5] = String.format("%1.2f%%", 100f * (int)array[year - start][3] / (int)array[year - start][1]);
			array[year - start][6] = String.format("%1.2f%%", 100f * (int)array[year - start][4] / (int)array[year - start][2]);
			array[year - start][7] = String.format("%1.2f%%", 100f * (int)array[year - start][2] / (int)array[year - start][1]);
			array[year - start][8] = String.format("%1.2f%%", 100f * (int)array[year - start][4] / (int)array[year - start][3]);
		}
		
		JPanel panel = new JPanel();
		JTable table = new JTable() {
			private static final long serialVersionUID = -225672961458244008L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		panel.add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		table.setModel(new DefaultTableModel(array, new String[] {"Academic Year", "Total Courses in Catalog", "Total Courses Actually Offered", "Ethics Courses in Catalog", "Ethics Courses Actually Offered", "% of Ethics Courses Listed in Catalog, across All UCF", "% of Ethics Courses Earning Credit-Hours across All UCF", "% of Listed Courses Taught", "% of Listed Ethics Courses Taught"}));
		return panel;
	}
	
	private static JPanel generateCollegeTable(int start, int end, College college, String[] keyWords) {
		Object[][] array = new Object[end - start + 1][7];
		for (int year = start; year <= end; year++) {
			array[year - start][0] = String.format("%d/%d", year, year + 1);
			array[year - start][1] = Util.getCollegeCoursesInYear(college, year);
			array[year - start][2] = Util.getCollegeCoursesInYearWithHours(college, year);
			array[year - start][3] = Util.getCollegeCoursesInYearBySearch(college, year, keyWords);
			array[year - start][4] = Util.getCollegeCoursesInYearBySearchWithHours(college, year, keyWords);
			array[year - start][5] = String.format("%1.2f%%", 100f * (int)array[year - start][3] / (int)array[year - start][1]);
			array[year - start][6] = String.format("%1.2f%%", 100f * (int)array[year - start][4] / (int)array[year - start][2]);
		}
		
		JPanel panel = new JPanel();
		JTable table = new JTable() {
			private static final long serialVersionUID = -225672961458244008L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		panel.add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
		table.setModel(new DefaultTableModel(array, new String[] {"Academic Year", "Total Courses in Catalog", "Total Courses Actually Offered", "Ethics Courses in Catalog", "Ethics Courses Actually Offered", "% of Ethics Courses Listed in Catalog, across College", "% of Ethics Courses Earning Credit-Hours across College"}));
		return panel;
	}
}
