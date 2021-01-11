package com.jordan.ethics.ui;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class MainMenu implements UIMenu {

	public void open(UI ui) {
		ui.clear();
		
		JPanel panel = new JPanel(new GridLayout(5, 1));
		panel.add(ui.createCenteredLabel("     ETHICS     ", 50));
		panel.add(ui.createMenuButton("Upload", 20, new UploadMenu()));
		panel.add(ui.createMenuButton("Search", 20, new SearchMenu()));
		panel.add(ui.createMenuButton("Edit", 20, new EditMenu()));
		panel.add(ui.createMenuButton("Credits", 20, new CreditsMenu()));
		ui.add(panel);
		ui.pack();
	}
}
