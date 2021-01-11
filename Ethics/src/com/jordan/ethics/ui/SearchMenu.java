package com.jordan.ethics.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jordan.ethics.College;
import com.jordan.ethics.JDBC;

public class SearchMenu implements UIMenu {

	public void open(UI ui) {
		ui.clear();
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		ui.add(mainPanel);
		
		// Search Label
		mainPanel.add(ui.createCenteredLabel("SEARCH", 50), BorderLayout.NORTH);
		
		// Settings panel
		JPanel settings = new JPanel(new GridBagLayout());
		mainPanel.add(settings, BorderLayout.WEST);
		
		// Get available years
		ArrayList<YearDisplay> years = new ArrayList<YearDisplay>();
		try (ResultSet res = JDBC.executeQuery("select distinct(year) from years order by year asc;")) {
			while (res.next()) {
				years.add(new YearDisplay(res.getInt("year")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Start Year and End Year
		JComboBox<YearDisplay> startYear = new JComboBox<YearDisplay>();
		JComboBox<YearDisplay> endYear = new JComboBox<YearDisplay>();
		for (YearDisplay year : years) {
			startYear.addItem(year);
			endYear.addItem(year);
		}
		
		int y = 0;
		
		{ // Start Year
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = y++;
			c.gridwidth = 2;
			settings.add(new JLabel("Start Year: "), c);
			c.gridx = 2;
			settings.add(startYear, c);
		}
		
		{ // End Year
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = y++;
			c.gridwidth = 2;
			settings.add(new JLabel("End Year: "), c);
			c.gridx = 2;
			settings.add(endYear, c);
		}
		
		// Colleges Checkboxes
		JCheckBox[] colleges = new JCheckBox[College.values().length];
		for (int i = 0; i < colleges.length; i++) {
			colleges[i] = new JCheckBox(College.values()[i].toString());
		}
		
		{ // Colleges Label
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = y++;
			settings.add(new JLabel(" "), c);
			c.gridy = y++;
			settings.add(new JLabel("Colleges:"), c);
		}
		
		{ // Colleges
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 2;
			
			for (int i = 0; i < colleges.length; i += 2) {
				c.gridy = y++;
				c.gridx = 0;
				settings.add(colleges[i], c);
				
				if (i + 1 < colleges.length) {
					c.gridx = 2;
					settings.add(colleges[i + 1], c);
				}
			}
		}
		
		JButton selectAllColleges = new JButton("All");
		JButton selectNoneColleges = new JButton("None");
		
		{ // All and None buttons
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = y++;
			c.gridwidth = 2;
			settings.add(selectAllColleges, c);
			c.gridx = 2;
			settings.add(selectNoneColleges, c);
		}
		
		{ // Key Words Label
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = y++;
			settings.add(new JLabel(" "), c);
			c.gridy = y++;
			settings.add(new JLabel("Key Words:"), c);
		}
		
		// Create Key Words Box
		JTextField keyWords = new JTextField();
		
		{ // Key Words
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 4;
			c.gridx = 0;
			c.gridy = y++;
			settings.add(keyWords, c);
		}
		
		{ // Blank Line
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = y++;
			settings.add(new JLabel(" "), c);
		}
		
		// Create Settings Buttons
		JButton searchButton = new JButton("Search");
		JButton saveButton = new JButton("Save Tables");
		saveButton.setEnabled(false);
		JButton resetButton = new JButton("Reset Tables");
		JButton backButton = new JButton("Back to Menu");
		
		{ // Buttons
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridwidth = 4;
			c.gridx = 0;
			
			for (JButton button : new JButton[] {searchButton, saveButton, resetButton, backButton}) {
				c.gridy = y++;
				settings.add(button, c);
			}
		}
		
		// Table panel
		TableDisplay table = new TableDisplay();
		mainPanel.add(table, BorderLayout.CENTER);
		table.updateDisplay(new YearDisplay(0), new YearDisplay(-1), new ArrayList<College>(), new String[] {""});
		
		// Start Year Action Listener
		startYear.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					YearDisplay start = (YearDisplay)event.getItem();
					YearDisplay end = (YearDisplay)endYear.getSelectedItem();
					
					endYear.removeAllItems();
					for (YearDisplay year : years) {
						if (year.getYear() < start.getYear()) continue;
						endYear.addItem(year);
					}
					endYear.setSelectedItem(end);
				}
			}
		});
		
		// All Colleges Button Action Listener
		selectAllColleges.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				for (JCheckBox box : colleges) {
					box.setSelected(true);
				}
			}
		});
		
		// None Colleges Button Action Listener
		selectNoneColleges.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				for (JCheckBox box : colleges) {
					box.setSelected(false);
				}
			}
		});
		
		// Search Button Action Listener
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				ArrayList<College> list = new ArrayList<College>();
				for (JCheckBox box : colleges) {
					if (box.isSelected()) {
						list.add(College.valueOf(box.getText()));
					}
				}
				
				table.updateDisplay((YearDisplay)startYear.getSelectedItem(), (YearDisplay)endYear.getSelectedItem(), list, keyWords.getText().split(",\\s*"));
				saveButton.setEnabled(true);
			}
		});
		
		// Save Button Action Listener
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
//				JFileChooser fc = new JFileChooser();
//				fc.setName("Select Save Location");
//				
//				if (fc.showOpenDialog(ui) == JFileChooser.APPROVE_OPTION) {
//					File file = fc.getSelectedFile();
//					Debug.log("Printing to %s.", file.getAbsolutePath());
//					
//					try (PrintWriter out = new PrintWriter(file)) {
//						for (int j = 0; j < table.getColumnCount(); j++) {
//							out.print(table.getColumnName(j));
//							out.print(",");
//						}
//						out.println();
//						
//						for (int i = 0; i < table.getRowCount(); i++) {
//							for (int j = 0; j < table.getColumnCount(); j++) {
//								out.print(table.getValueAt(i, j));
//								out.print(",");
//							}
//							out.println();
//						}
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
			}
		});
		
		// Reset Button Action Listener
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				startYear.setSelectedIndex(0);
				endYear.setSelectedIndex(0);
				selectNoneColleges.doClick();
				keyWords.setText("");
				table.reset();
				saveButton.setEnabled(false);
			}
		});
		
		// Back Button Action Listener
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MainMenu().open(ui);
			}
		});
		
		ui.pack();
	}
}
