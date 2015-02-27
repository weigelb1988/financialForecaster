package forecaster.ui.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;
import javax.swing.table.DefaultTableCellRenderer;

import forecaster.database.MongoDBConnection;
import forecaster.money.Budget;
import forecaster.money.Income;
import forecaster.money.Loan;
import forecaster.ui.panels.tables.LoanTableModel;

public class LoanPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel loanPanel = null;

	private JComponent loanComponent = null;

	private LinkedList<Loan> currentLoans;

	private JTable loanTable;

	private LoanTableModel ltm;

	JTextField name_text;
	JTextField amount_text;
	JTextField interest_text;
	JTextField months_text;
	JTextField payment_text;
	JTextField due_text;

	public LoanPanel() {
		currentLoans = MongoDBConnection.getMongoDBConnection().getLoans();
		ltm = new LoanTableModel(currentLoans);
		loanComponent = makeLoanPanel("Loans");

	}

	public JComponent getLoanComponent() {
		return loanComponent;
	}

	protected JComponent makeLoanPanel(String text) {
		loanPanel = new JPanel(false);
		loanPanel.setLayout(new SpringLayout());
		addLoanCompents();
		addCurrentLoanComponents();

		return loanPanel;
	}

	private JPopupMenu createPopupMenu() {
		JMenuItem item;
		JPopupMenu popup = new JPopupMenu();
		popup.add(item = new JMenuItem("Remove Loan"));
		item.addActionListener(this);
		item.setActionCommand("removeLoan");

		return popup;
	}

	private void addCurrentLoanComponents() {
		SpringLayout layout = (SpringLayout) loanPanel.getLayout();

		loanTable = new JTable(ltm);

		loanTable.setPreferredScrollableViewportSize(new Dimension(1300, 500));

		loanTable.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(loanTable);

		loanPanel.add(scrollPane);

		loanTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				int r = loanTable.rowAtPoint(e.getPoint());
				if (r >= 0 && r < loanTable.getRowCount()) {
					loanTable.setRowSelectionInterval(r, r);
				} else {
					loanTable.clearSelection();
				}
				int rowIndex = loanTable.getSelectedRow();
				if (rowIndex < 0) {
					return;
				}
				if (e.getButton() == MouseEvent.BUTTON3
						&& e.getComponent() instanceof JTable) {
					JPopupMenu popup = createPopupMenu();
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		});

		DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
		centerRender.setHorizontalAlignment(JLabel.CENTER);
		loanTable.setDefaultRenderer(String.class, centerRender);
		loanTable.setDefaultRenderer(Double.class, centerRender);
		loanTable.setDefaultRenderer(Integer.class, centerRender);
		
		SpringLayout.Constraints tableCons = layout.getConstraints(loanTable);
		layout.putConstraint(SpringLayout.EAST, scrollPane, -5,
				SpringLayout.EAST, loanPanel);
		layout.putConstraint(SpringLayout.SOUTH, loanPanel, 5,
				SpringLayout.SOUTH, scrollPane);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5,
				SpringLayout.WEST, loanPanel);
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 50,
				SpringLayout.NORTH, loanPanel);

	}

	private void addLoanCompents() {
		SpringLayout layout = (SpringLayout) loanPanel.getLayout();

		Component[] components = loanPanel.getComponents();

		JLabel name_label = new JLabel("Loan Name:");
		name_text = new JTextField(20);
		JLabel amount_label = new JLabel("Amount:");
		amount_text = new JTextField(10);
		JLabel interest_label = new JLabel("Interest %:");
		interest_text = new JTextField(5);
		JLabel payment_label = new JLabel("Payment:");
		payment_text = new JTextField(5);
		JLabel months_label = new JLabel("Months Left");
		months_text = new JTextField(5);
		JLabel due_label = new JLabel("Due Day");
		due_text = new JTextField(5);
		JButton addDebt = new JButton("    Add Debt   ");

		loanPanel.add(name_label);
		loanPanel.add(name_text);
		loanPanel.add(amount_label);
		loanPanel.add(amount_text);
		loanPanel.add(interest_label);
		loanPanel.add(interest_text);
		loanPanel.add(payment_label);
		loanPanel.add(payment_text);
		loanPanel.add(due_label);
		loanPanel.add(due_text);
		loanPanel.add(months_label);
		loanPanel.add(months_text);
		loanPanel.add(addDebt);

		SpringLayout.Constraints labelcons = layout.getConstraints(name_label);
		labelcons.setX(Spring.constant(5));

		labelcons.setY(Spring.constant(5));

		SpringLayout.Constraints nameCons = layout.getConstraints(name_text);
		nameCons.setX(Spring.sum(Spring.constant(5),
				labelcons.getConstraint(SpringLayout.EAST)));

		nameCons.setY(Spring.constant(5));

		SpringLayout.Constraints amountLabelCons = layout
				.getConstraints(amount_label);
		amountLabelCons.setX(Spring.sum(
				nameCons.getConstraint(SpringLayout.EAST), Spring.constant(5)));

		amountLabelCons.setY(Spring.constant(5));

		SpringLayout.Constraints amountTextCons = layout
				.getConstraints(amount_text);
		amountTextCons.setX(Spring.sum(Spring.constant(5),
				amountLabelCons.getConstraint(SpringLayout.EAST)));

		amountTextCons.setY(Spring.constant(5));

		SpringLayout.Constraints interestLabelCons = layout
				.getConstraints(interest_label);
		interestLabelCons.setX(Spring.sum(
				amountTextCons.getConstraint(SpringLayout.EAST),
				Spring.constant(5)));

		interestLabelCons.setY(Spring.constant(5));

		SpringLayout.Constraints interestTextCons = layout
				.getConstraints(interest_text);
		interestTextCons.setX(Spring.sum(Spring.constant(5),
				interestLabelCons.getConstraint(SpringLayout.EAST)));

		interestTextCons.setY(Spring.constant(5));

		SpringLayout.Constraints paymentLabelCons = layout
				.getConstraints(payment_label);
		paymentLabelCons.setX(Spring.sum(
				interestTextCons.getConstraint(SpringLayout.EAST),
				Spring.constant(5)));

		paymentLabelCons.setY(Spring.constant(5));

		SpringLayout.Constraints paymentTextCons = layout
				.getConstraints(payment_text);
		paymentTextCons.setX(Spring.sum(Spring.constant(5),
				paymentLabelCons.getConstraint(SpringLayout.EAST)));

		paymentTextCons.setY(Spring.constant(5));

		SpringLayout.Constraints monthLabelCons = layout
				.getConstraints(months_label);
		monthLabelCons.setX(Spring.sum(
				paymentTextCons.getConstraint(SpringLayout.EAST),
				Spring.constant(5)));

		monthLabelCons.setY(Spring.constant(5));

		SpringLayout.Constraints monthTextCons = layout
				.getConstraints(months_text);
		monthTextCons.setX(Spring.sum(Spring.constant(5),
				monthLabelCons.getConstraint(SpringLayout.EAST)));

		monthTextCons.setY(Spring.constant(5));

		SpringLayout.Constraints dueLabelCons = layout
				.getConstraints(due_label);
		dueLabelCons.setX(Spring.sum(
				monthTextCons.getConstraint(SpringLayout.EAST),
				Spring.constant(5)));

		dueLabelCons.setY(Spring.constant(5));

		SpringLayout.Constraints dueTextCons = layout.getConstraints(due_text);
		dueTextCons.setX(Spring.sum(Spring.constant(5),
				dueLabelCons.getConstraint(SpringLayout.EAST)));

		dueTextCons.setY(Spring.constant(5));

		addDebt.setVerticalTextPosition(AbstractButton.CENTER);
		addDebt.setHorizontalTextPosition(AbstractButton.LEADING); // left
		addDebt.setActionCommand("addDebt");
		addDebt.addActionListener(this);

		SpringLayout.Constraints buttonCons = layout.getConstraints(addDebt);
		buttonCons.setX(Spring.sum(Spring.constant(5),
				dueTextCons.getConstraint(SpringLayout.EAST)));

		buttonCons.setY(Spring.constant(5));

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("addDebt".equals(e.getActionCommand())) {
			Loan loan = new Loan(name_text.getText(),
					Double.parseDouble(amount_text.getText()),
					Double.parseDouble(interest_text.getText()),
					Integer.parseInt(months_text.getText()),
					Double.parseDouble(payment_text.getText()),
					Integer.parseInt(due_text.getText()));
			MongoDBConnection.getMongoDBConnection().insertLoan(loan);

		} else if ("removeLoan".equals(e.getActionCommand())) {
			int row = loanTable.getSelectedRow();
			Loan loan = new Loan((String) loanTable.getValueAt(row, 0),
					Double.parseDouble(((String)loanTable.getValueAt(row, 1)).substring(1).replace(",", "")),
					(double) loanTable.getValueAt(row, 2),
					(int) loanTable.getValueAt(row, 4),
					(double) Double.parseDouble(((String)loanTable.getValueAt(row, 3)).substring(1).replace(",", "")),
					(int) loanTable.getValueAt(row, 5));
			MongoDBConnection.getMongoDBConnection().removeLoan(loan);
		} else if ("editLoan".equals(e.getActionCommand())) {
			System.out.println("Attempt t oedit");
		}
		currentLoans = MongoDBConnection.getMongoDBConnection().getLoans();
		ltm.setData(currentLoans);
		ltm.fireTableDataChanged();
		loanPanel.revalidate();
		loanPanel.repaint();

	}

}
