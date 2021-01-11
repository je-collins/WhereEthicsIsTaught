package com.jordan.ethics.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.jordan.ethics.Constants;

public class UI extends JFrame {

	private static final long serialVersionUID = 7794010669053911491L;

	public UI() {
		setTitle("Jordan's Ethics Calculator");
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void clear() {
		getContentPane().removeAll();
	}
	
	public JLabel createLabel(String text, int size) {
		JLabel label = new JLabel(text);
		label.setFont(new Font(Constants.FONT_NAME, Font.PLAIN, size));
		return label;
	}
	
	public JLabel createCenteredLabel(String text, int size) {
		JLabel label = new JLabel(text);
		label.setFont(new Font(Constants.FONT_NAME, Font.PLAIN, size));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		return label;
	}
	
	public JButton createMenuButton(String text, int size, UIMenu menu) {
		final UI ui = this;
		
		JButton button = new JButton(text);
		button.setFont(new Font(Constants.FONT_NAME, Font.PLAIN, size));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				menu.open(ui);
			}
		});
		return button;
	}
	
	public JButton createButton(String text, int size, ActionListener listener) {
		JButton button = new JButton(text);
		button.setFont(new Font(Constants.FONT_NAME, Font.PLAIN, size));
		if (listener != null) button.addActionListener(listener);
		return button;
	}
}
