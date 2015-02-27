package forecaster.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import forecaster.database.MongoDBConnection;

public class ForecasterUI extends JFrame {

	/**
	 * 
	 */
	protected Container contentPane;
	private static final long serialVersionUID = 1L;
	
	public ForecasterUI() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			UIManager.put("Table.alternateRowColor", Color.LIGHT_GRAY);
			
			UIManager.put("Table.rowHeight", 28);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Create a new frame
		JFrame f = new JFrame("Financial Forcaster");
		SpringLayout layout = new SpringLayout();
		contentPane = new JPanel(layout, false);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(1400, 1000));
		PersonalInfoPane pip = new PersonalInfoPane();
		ForecasterTabbedPane ftp = new ForecasterTabbedPane();
		
		contentPane.add(pip, BorderLayout.CENTER);
		contentPane.add(ftp, BorderLayout.CENTER);
		
		SpringLayout.Constraints perInCons = layout.getConstraints(pip);
		perInCons.setX(Spring.constant(5));

		perInCons.setY(Spring.constant(5));
		
		SpringLayout.Constraints tabCons = layout.getConstraints(ftp);
		tabCons.setX(Spring.constant(5));

		tabCons.setY(Spring.sum(perInCons.getConstraint(SpringLayout.SOUTH), Spring.constant(5)));
		
		f.add(contentPane);
		f.pack();
		f.setVisible(true);

	}
}
