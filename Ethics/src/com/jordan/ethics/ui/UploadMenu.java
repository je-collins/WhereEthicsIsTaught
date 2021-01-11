package com.jordan.ethics.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UploadMenu implements UIMenu {

	public void open(UI ui) {
		ui.clear();
		
		JPanel panel = new JPanel(new BorderLayout());
		ui.add(panel);
		
		// Upload Label
		panel.add(ui.createCenteredLabel("UPLOAD", 50), BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel(new GridBagLayout());
		panel.add(centerPanel);
		int y = 0;
		
		// File Select Button
		JButton fileSelectButton = new JButton("Select File");
		JLabel fileSelectLabel = new JLabel("No file selected.");
		
		{ // File Select
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = y;
			c.gridwidth = 4;
			centerPanel.add(fileSelectButton, c);
			c.gridx = 4;
			c.gridwidth = 2;
			centerPanel.add(fileSelectLabel, c);
		}
		
		// Graduate Check Box
		JCheckBox graduate = new JCheckBox("Graduate?");
		graduate.setHorizontalAlignment(JCheckBox.RIGHT);
		
		{ // Graduate Check Box
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 6;
			c.gridy = y++;
			c.gridwidth = 2;
			centerPanel.add(graduate, c);
		}
		
		// Upload File Table
		UploadFileTable table = new UploadFileTable();
		
		{ // Upload File Table
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = y++;
			c.gridwidth = 8;
			centerPanel.add(table, c);
		}
		
		// Buttons
		JButton uploadButton = new JButton("Upload Data");
		uploadButton.setEnabled(false);
		JButton backButton = new JButton("Back to Menu");
		JPanel south = new JPanel(new GridLayout(1, 2));
		south.add(uploadButton);
		south.add(backButton);
		panel.add(south, BorderLayout.SOUTH);
		
		// File Select Action Listener
		fileSelectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileSelect = new JFileChooser("Select data file.");
				fileSelect.setAcceptAllFileFilterUsed(false);
				fileSelect.setFileFilter(new FileNameExtensionFilter("Excel Workbook (*.xls)", "xls"));
				
				if (fileSelect.showOpenDialog(ui) == JFileChooser.APPROVE_OPTION) {
					File file = fileSelect.getSelectedFile();
					fileSelectLabel.setText(file.getName());
					table.updateDisplay(file);
					uploadButton.setEnabled(true);
					ui.pack();
				}
			}
		});
		
		uploadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					@Override
					public void run() {
						table.uploadData(graduate.isSelected());
					};
				}.start();
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
