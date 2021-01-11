package com.jordan.ethics.ui;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.jordan.ethics.Constants;

public class ProgressBar extends JFrame {

	private static final long serialVersionUID = 3092043072247884743L;
	private JProgressBar bar;
	
	public ProgressBar(int max) {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		this.add(panel);
		
		JLabel label = new JLabel("Upload Progress:");
		label.setFont(new Font(Constants.FONT_NAME, Font.PLAIN, 20));
		panel.add(label);
		
		bar = new JProgressBar(0, max);
		bar.setValue(0);
		bar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		bar.setStringPainted(true);
		bar.setFont(new Font(Constants.FONT_NAME, Font.BOLD, 16));
		panel.add(bar);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}
	
	public void setValue(int value) {
		bar.setValue(value);
		bar.setString(String.format("%1.2f%%", 100f * value / bar.getMaximum()));
	}
}
