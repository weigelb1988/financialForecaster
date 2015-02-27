package forecaster.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;

import org.joda.time.LocalDate;

import forecaster.database.MongoDBConnection;
import forecaster.money.Budget;
import forecaster.money.Expense;
import forecaster.money.Forecast;
import forecaster.money.Income;
import forecaster.money.Info;
import forecaster.money.Loan;

public class PersonalInfoPane extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JTextField name_text;
	private JTextField starting_text;
	private JTextField debt_amount_text;
	private JTextField proj_text;
	private Info info;

	public PersonalInfoPane() {
		super(new GridLayout(1, 1));
		contentPanel = new JPanel(false);
		contentPanel.setLayout(new SpringLayout());
		contentPanel.setPreferredSize(new Dimension(1380, 40));
		info = MongoDBConnection.getMongoDBConnection().getInfo();
		JLabel name_label = new JLabel("Name");
		name_text = new JTextField(20);
		name_text.setText(info.getName());
		JLabel starting_label = new JLabel("Starting Amount");
		starting_text = new JTextField(10);
		starting_text.setText(new Double(info.getStartingAmount()).toString());
		JLabel debt_amount_label = new JLabel("Amount Towards Debt");
		debt_amount_text = new JTextField(10);
		debt_amount_text.setText(new Double(info.getTowardsDebt()).toString());

		JLabel proj_label = new JLabel("Months to Forecast");
		proj_text = new JTextField(10);
		proj_text.setText(new Integer(info.getMonthsToProject()).toString());

		JButton saver = new JButton("Save");
		saver.setActionCommand("savePersonal");
		saver.addActionListener(this);

		


		contentPanel.add(name_label);
		contentPanel.add(name_text);
		contentPanel.add(starting_label);
		contentPanel.add(starting_text);
		contentPanel.add(debt_amount_label);
		contentPanel.add(debt_amount_text);
		contentPanel.add(proj_label);
		contentPanel.add(proj_text);

		contentPanel.add(saver);
		SpringLayout layout = (SpringLayout) contentPanel.getLayout();
		SpringLayout.Constraints nameLabelCons = layout
				.getConstraints(name_label);
		nameLabelCons.setX(Spring.constant(5));

		nameLabelCons.setY(Spring.constant(7));

		SpringLayout.Constraints nameTextCons = layout
				.getConstraints(name_text);
		nameTextCons.setX(Spring.sum(Spring.constant(5),
				nameLabelCons.getConstraint(SpringLayout.EAST)));

		nameTextCons.setY(Spring.constant(7));

		SpringLayout.Constraints startLabelCons = layout
				.getConstraints(starting_label);
		startLabelCons.setX(Spring.sum(Spring.constant(5),
				nameTextCons.getConstraint(SpringLayout.EAST)));

		startLabelCons.setY(Spring.constant(7));

		SpringLayout.Constraints startTextCons = layout
				.getConstraints(starting_text);
		startTextCons.setX(Spring.sum(Spring.constant(5),
				startLabelCons.getConstraint(SpringLayout.EAST)));

		startTextCons.setY(Spring.constant(7));

		SpringLayout.Constraints debtLabelCons = layout
				.getConstraints(debt_amount_label);
		debtLabelCons.setX(Spring.sum(Spring.constant(5),
				startTextCons.getConstraint(SpringLayout.EAST)));

		debtLabelCons.setY(Spring.constant(7));

		SpringLayout.Constraints debtTextCons = layout
				.getConstraints(debt_amount_text);
		debtTextCons.setX(Spring.sum(Spring.constant(5),
				debtLabelCons.getConstraint(SpringLayout.EAST)));

		debtTextCons.setY(Spring.constant(7));

		SpringLayout.Constraints projLabelCons = layout
				.getConstraints(proj_label);
		projLabelCons.setX(Spring.sum(Spring.constant(5),
				debtTextCons.getConstraint(SpringLayout.EAST)));

		projLabelCons.setY(Spring.constant(7));

		SpringLayout.Constraints projTextCons = layout
				.getConstraints(proj_text);
		projTextCons.setX(Spring.sum(Spring.constant(5),
				projLabelCons.getConstraint(SpringLayout.EAST)));

		projTextCons.setY(Spring.constant(7));

		SpringLayout.Constraints saveCons = layout.getConstraints(saver);
		saveCons.setX(Spring.sum(Spring.constant(5),
				projTextCons.getConstraint(SpringLayout.EAST)));

		saveCons.setY(Spring.constant(5));

		
		add(contentPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("savePersonal".equals(e.getActionCommand())) {
			try {
				MongoDBConnection.getMongoDBConnection().removeInfo(info);
			} catch (Exception ex) {

			}
			Info new_info = new Info(name_text.getText(),
					Double.parseDouble(starting_text.getText()),
					Double.parseDouble(debt_amount_text.getText()),
					Integer.parseInt(proj_text.getText()));
			MongoDBConnection.getMongoDBConnection().insertInfo(new_info);
			info = MongoDBConnection.getMongoDBConnection().getInfo();
		}

	}

	
}
