package com.jordan.ethics.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.jordan.ethics.Course;
import com.jordan.ethics.Flag;
import com.jordan.ethics.JDBC;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class UploadFileTable extends JPanel {
	
	private static final long serialVersionUID = -2035024281004066981L;

	public UploadFileTable() {
		super();
		reset();
	}
	
	public void reset() {
		updateDisplay(null);
	}

	public void updateDisplay(File file) {
		removeAll();
		add(generatePreview(file));
	}
	
	private Object[][] array;
	private JTable table;
	JTabbedPane tabs;
	
	private JTextField courseYear;
	private JComboBox<String> courseCode;
	private JComboBox<String> courseDepartment;
	private JComboBox<String> courseInfo;
	
	private JTextField yearStartYear;
	private JComboBox<String> yearCourseId;
	private JComboBox<String> yearHoursStart;
	private JComboBox<String> yearHoursEnd;
	
	private JPanel generatePreview(File file) {
		if (file == null) return new JPanel();
		
		try {
			Workbook w = Workbook.getWorkbook(file.getAbsoluteFile());
			Sheet sheet = w.getSheet(0);
			
			array = new Object[sheet.getRows()][sheet.getColumns()];
			
			// Fill Data
			for (int row = 0; row < sheet.getRows(); row++) {
				for (int col = 0; col < sheet.getColumns(); col++) {
					array[row][col] = sheet.getCell(col, row).getContents();
				}
			}
			
			String[] header = new String[array[0].length];
			for (int q = 0; q < header.length; q++) {
				header[q] = String.format("%,d", q + 1);
			}
			
			JPanel panel = new JPanel(new BorderLayout());
			table = new JTable() {
				private static final long serialVersionUID = -225672961458244008L;
				
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.getTableHeader().setReorderingAllowed(false);
			panel.add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
			table.setModel(new DefaultTableModel(array, header));
			
			
			tabs = new JTabbedPane();
			
			{
				JPanel coursePanel = new JPanel(new GridLayout(9, 2));
				coursePanel.add(new JLabel(""));
				coursePanel.add(new JLabel(""));
				
				courseYear = new JTextField();
				coursePanel.add(new JLabel("Year:"));
				coursePanel.add(courseYear);
				
				coursePanel.add(new JLabel(""));
				coursePanel.add(new JLabel(""));
				
				courseCode = new JComboBox<String>();
				coursePanel.add(new JLabel("Course Code:"));
				coursePanel.add(courseCode);
				
				coursePanel.add(new JLabel(""));
				coursePanel.add(new JLabel(""));
				
				courseDepartment = new JComboBox<String>();
				coursePanel.add(new JLabel("Department:"));
				coursePanel.add(courseDepartment);
				
				coursePanel.add(new JLabel(""));
				coursePanel.add(new JLabel(""));
				
				courseInfo = new JComboBox<String>();
				coursePanel.add(new JLabel("Course Info:"));
				coursePanel.add(courseInfo);
				
				coursePanel.add(new JLabel(""));
				coursePanel.add(new JLabel(""));
				
				tabs.addTab("Upload Course Catalog", coursePanel);
			}
			
			{
				JPanel yearPanel = new JPanel(new GridLayout(9, 2));
				yearPanel.add(new JLabel(""));
				yearPanel.add(new JLabel(""));
				
				yearCourseId = new JComboBox<String>();
				yearPanel.add(new JLabel("Course Code:"));
				yearPanel.add(yearCourseId);
				
				yearPanel.add(new JLabel(""));
				yearPanel.add(new JLabel(""));
				
				yearStartYear = new JTextField();
				yearPanel.add(new JLabel("Start Year:"));
				yearPanel.add(yearStartYear);
				
				yearPanel.add(new JLabel(""));
				yearPanel.add(new JLabel(""));
				
				yearHoursStart = new JComboBox<String>();
				yearPanel.add(new JLabel("Start:"));
				yearPanel.add(yearHoursStart);
				
				yearPanel.add(new JLabel(""));
				yearPanel.add(new JLabel(""));
				
				yearHoursEnd = new JComboBox<String>();
				yearPanel.add(new JLabel("End:"));
				yearPanel.add(yearHoursEnd);
				
				yearPanel.add(new JLabel(""));
				yearPanel.add(new JLabel(""));
				
				tabs.addTab("Upload Hours Data", yearPanel);
			}
			
			for (int col = 0; col < table.getColumnCount(); col++) {
				String c = table.getColumnName(col);
				courseCode.addItem(c);
				courseDepartment.addItem(c);
				courseInfo.addItem(c);
				yearCourseId.addItem(c);
				yearHoursStart.addItem(c);
				yearHoursEnd.addItem(c);
			}
			
			panel.add(tabs, BorderLayout.WEST);
			
			return panel;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
		
		return new JPanel();
	}
	
	public void uploadData(boolean graduate) {
		Course.resetCache();
		
		
		// Upload Course data
		if (tabs.getSelectedIndex() == 0) {
			int startYear = -1;
			try {
				startYear = Integer.parseInt(courseYear.getText());
			} catch (NumberFormatException e) {}
			
			if (startYear == -1) {
				uploadError("Please enter a valid year.");
				return;
			}
			
			int colCourseCode = Integer.parseInt(courseCode.getSelectedItem().toString());
			int colCourseDepartment = Integer.parseInt(courseDepartment.getSelectedItem().toString());
			int colCourseInfo = Integer.parseInt(courseInfo.getSelectedItem().toString());
			
			boolean updateNow = false;
			ArrayList<String> errors = new ArrayList<String>();
			
			for (int row = 0; row < array.length; row++) {
				if (table.getValueAt(row, colCourseCode).toString().length() == 0) continue;
				String courseInfoTable = table.getValueAt(row, colCourseInfo).toString();
				if (courseInfoTable.length() == 0) errors.add(table.getValueAt(row, colCourseCode).toString());
			}
			
			if (errors.size() == 1) {
				int option = JOptionPane.showConfirmDialog(null, String.format("There is %,d course with a blank description field. Would you like to fill in its description now?", errors.size()), "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				updateNow = option == JOptionPane.YES_OPTION;
			} else if (!errors.isEmpty()) {
				int option = JOptionPane.showConfirmDialog(null, String.format("There are %,d courses with blank description fields. Would you like to fill in their descriptions now?", errors.size()), "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				updateNow = option == JOptionPane.YES_OPTION;
			}
			
			ProgressBar progress = new ProgressBar(array.length);
			
			for (int row = 0; row < array.length; row++) {
				progress.setValue(row);
				if (table.getValueAt(row, colCourseCode).toString().length() == 0) continue;
				
				String courseInfoTable = table.getValueAt(row, colCourseInfo).toString();
				if (updateNow && courseInfoTable.length() == 0) {
					courseInfoTable = JOptionPane.showInputDialog(null, "Please input the course description for the course " + table.getValueAt(row, colCourseCode) + ".", "Error on Course Description", JOptionPane.YES_OPTION);
					if (courseInfoTable == null) courseInfoTable = "";
				}
				
				try (ResultSet res = JDBC.executeQuery("select * from courses where courseId = '%s';", table.getValueAt(row, colCourseCode))) {
					if (res.next()) {
						// Info already exists - update data
						JDBC.executeUpdate("update courses set courseDescription = '%s', department = '%s', flags = %d where courseId = '%s';",
								courseInfoTable,
								table.getValueAt(row, colCourseDepartment),
								graduate ? Flag.Graduate.getFlag() : 0,
								table.getValueAt(row, colCourseCode));
					} else {
						// Info doesn't exist - add it
						JDBC.executeUpdate("insert into courses values('%s', '', '%s', '%s', %d);",
								table.getValueAt(row, colCourseCode),
								courseInfoTable,
								table.getValueAt(row, colCourseDepartment),
								graduate ? Flag.Graduate.getFlag() : 0);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			progress.dispose();
			
			if (!updateNow) {
				boolean copy = false;
				if (errors.size() == 1) {
					copy = JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Would you like to copy the course code with a missing description?", "Missing Course Descriptions", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				} else if (!errors.isEmpty()) {
					copy = JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Would you like to copy the course codes with missing descriptions?", "Missing Course Descriptions", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				}
				
				if (copy) {
					StringSelection ss = new StringSelection(String.join("\n", errors));
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
				}
			}
		}
		
		// Upload Year Data
		else {
			int startYear = -1;
			try {
				startYear = Integer.parseInt(yearStartYear.getText());
			} catch (NumberFormatException e) {}
			
			if (startYear == -1) {
				uploadError("Please enter a valid starting year.");
				return;
			}
			
			int colYearCourseId = Integer.parseInt(yearCourseId.getSelectedItem().toString());
			int colYearHoursStart = Integer.parseInt(yearHoursStart.getSelectedItem().toString());
			int colYearHoursEnd = Integer.parseInt(yearHoursEnd.getSelectedItem().toString());
			
			ProgressBar progress = new ProgressBar(array.length);
			
			for (int row = 0; row < array.length; row++) {
				progress.setValue(row);
				if (table.getValueAt(row, colYearCourseId).toString().length() == 0) continue;
				
				for (int col = colYearHoursStart, dYear = 0; col <= colYearHoursEnd; col++, dYear++) {
					int hours = -1;
					try {
						hours = Integer.parseInt(table.getValueAt(row, col).toString());
					} catch (NumberFormatException e) {}
					
					if (hours < 0) continue;
					
					try (ResultSet res = JDBC.executeQuery("select * from years where courseId = '%s' and year = %d;", table.getValueAt(row, colYearCourseId), startYear + dYear)) {
						if (res.next()) {
							// Info already exists - update data
							JDBC.executeUpdate("update years set hours = %d where courseId = '%s' and year = %d;",
									hours,
									table.getValueAt(row, colYearCourseId),
									startYear + dYear);
						} else {
							// Info doesn't exist - add it
							JDBC.executeUpdate("insert into years values('%s', %d, %d);",
									table.getValueAt(row, colYearCourseId),
									startYear + dYear,
									hours);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			
			progress.dispose();
		}
	}
	
	public void uploadError(String message) {
		JOptionPane.showMessageDialog(null, message, "An Error has Occured", JOptionPane.ERROR_MESSAGE);
	}
}