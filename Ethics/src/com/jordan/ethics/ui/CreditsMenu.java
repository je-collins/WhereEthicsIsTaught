package com.jordan.ethics.ui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class CreditsMenu implements UIMenu {

	public void open(UI ui) {
		ui.clear();
		
		JPanel panel = new JPanel(new GridLayout(4, 1));
		panel.add(ui.createCenteredLabel("     Credits     ", 50));
		panel.add(ui.createCenteredLabel("Jordan - Sat back and watched the whole thing", 20));
		panel.add(ui.createCenteredLabel("Christopher - Did all the work", 20));
		panel.add(ui.createMenuButton("Back", 20, new MainMenu()));
		ui.add(panel);
		ui.pack();
	}
}
