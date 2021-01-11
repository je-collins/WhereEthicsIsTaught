package test;

import javax.swing.JOptionPane;

import com.jordan.ethics.ui.ProgressBar;

public class Test {

	public static void main(String[] args) throws InterruptedException {
	
		String x = JOptionPane.showInputDialog(null, "Fix yo shizz", "Error", JOptionPane.YES_OPTION);
		System.out.println(x);
		
		long time = 1_000;
		int count = 77;
		
		ProgressBar bar = new ProgressBar(count);
		
		for (int q = 0; q <= count; q++) {
			bar.setValue(q);
			Thread.sleep(time / count);
		}
		
		System.exit(0);
	}
}
