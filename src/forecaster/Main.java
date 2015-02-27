package forecaster;

import forecaster.ui.ForecasterUI;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new ForecasterUI();

			}
		});

	}

}
