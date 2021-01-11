package com.jordan.ethics.ui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class EditMenu implements UIMenu {

	public void open(UI ui) {
		ui.clear();
		
		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(ui.createCenteredLabel("     EDIT     ", 50));
		panel.add(ui.createMenuButton("Back", 20, new MainMenu()));
		ui.add(panel);
		ui.pack();
	}
}
