package forecaster.ui.panels;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.table.DefaultTableCellRenderer;

import forecaster.database.MongoDBConnection;
import forecaster.money.Expense;
import forecaster.ui.panels.tables.ExpenseTableModel;

public class ExpensePanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel expensePanel = null;

	private JComponent expenseComponent = null;
	private JTextField name_text;
	private JTextField amount_text;
	private JTextField date_text;
	private JTextField category_text;
	private ExpenseTableModel etm;
	private JTable expenseTable;
	private LinkedList<Expense> currentExpenses = null;

	public ExpensePanel() {
		currentExpenses = MongoDBConnection.getMongoDBConnection()
				.getExpense();
		etm = new ExpenseTableModel(currentExpenses);
		expenseComponent = makeExpensePanel("Expenses");

	}

	public JComponent getExpenseComponent() {
		return expenseComponent;
	}

	protected JComponent makeExpensePanel(String text) {
		expensePanel = new JPanel(false);
		expensePanel.setLayout(new SpringLayout());
		addExpenseCompents();
		addCurrentExpenseComponents();

		return expensePanel;
	}

	private JPopupMenu createPopupMenu() {
		JMenuItem item;
		JPopupMenu popup = new JPopupMenu();
		popup.add(item = new JMenuItem("Remove Expense"));
		item.addActionListener(this);
		item.setActionCommand("removeExpense");

		return popup;
	}

	private void addCurrentExpenseComponents() {

		SpringLayout layout = (SpringLayout) expensePanel.getLayout();

		expenseTable = new JTable(etm);

		expenseTable
				.setPreferredScrollableViewportSize(new Dimension(1300, 500));

		expenseTable.setFillsViewportHeight(true);

		expenseTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				int r = expenseTable.rowAtPoint(e.getPoint());
				if (r >= 0 && r < expenseTable.getRowCount()) {
					expenseTable.setRowSelectionInterval(r, r);
				} else {
					expenseTable.clearSelection();
				}
				int rowIndex = expenseTable.getSelectedRow();
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
		expenseTable.setDefaultRenderer(String.class, centerRender);
		expenseTable.setDefaultRenderer(Integer.class, centerRender);
		expenseTable.setDefaultRenderer(Double.class, centerRender);
		JScrollPane scrollPane = new JScrollPane(expenseTable);

		expensePanel.add(scrollPane);

		layout.putConstraint(SpringLayout.EAST, scrollPane, -5,
				SpringLayout.EAST, expensePanel);
		layout.putConstraint(SpringLayout.SOUTH, expensePanel, 5,
				SpringLayout.SOUTH, scrollPane);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5,
				SpringLayout.WEST, expensePanel);
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 50,
				SpringLayout.NORTH, expensePanel);

	}

	private void addExpenseCompents() {
		SpringLayout layout = (SpringLayout) expensePanel.getLayout();

		JLabel name_label = new JLabel("Expense Name:");
		name_text = new JTextField(20);
		JLabel amount_label = new JLabel("Amount:");
		amount_text = new JTextField(10);
		JLabel date_label = new JLabel("Date:");
		date_text = new JTextField(5);
		JLabel category_label = new JLabel("Category:");
		category_text = new JTextField(5);
		JButton addExpense = new JButton("    Add Expense   ");

		expensePanel.add(name_label);
		expensePanel.add(name_text);
		expensePanel.add(amount_label);
		expensePanel.add(amount_text);
		expensePanel.add(date_label);
		expensePanel.add(date_text);
		expensePanel.add(category_label);
		expensePanel.add(category_text);
		expensePanel.add(addExpense);

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
				.getConstraints(date_label);
		interestLabelCons.setX(Spring.sum(
				amountTextCons.getConstraint(SpringLayout.EAST),
				Spring.constant(5)));
		interestLabelCons.setY(Spring.constant(5));

		SpringLayout.Constraints interestTextCons = layout
				.getConstraints(date_text);
		interestTextCons.setX(Spring.sum(Spring.constant(5),
				interestLabelCons.getConstraint(SpringLayout.EAST)));
		interestTextCons.setY(Spring.constant(5));

		SpringLayout.Constraints paymentLabelCons = layout
				.getConstraints(category_label);
		paymentLabelCons.setX(Spring.sum(
				interestTextCons.getConstraint(SpringLayout.EAST),
				Spring.constant(5)));
		paymentLabelCons.setY(Spring.constant(5));

		SpringLayout.Constraints paymentTextCons = layout
				.getConstraints(category_text);
		paymentTextCons.setX(Spring.sum(Spring.constant(5),
				paymentLabelCons.getConstraint(SpringLayout.EAST)));
		paymentTextCons.setY(Spring.constant(5));

		addExpense.setVerticalTextPosition(AbstractButton.CENTER);
		addExpense.setHorizontalTextPosition(AbstractButton.LEADING); // left
		addExpense.setActionCommand("addExpense");
		addExpense.addActionListener(this);

		SpringLayout.Constraints buttonCons = layout.getConstraints(addExpense);
		buttonCons.setX(Spring.sum(Spring.constant(5),
				paymentTextCons.getConstraint(SpringLayout.EAST)));
		buttonCons.setY(Spring.constant(5));

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("addExpense".equals(e.getActionCommand())) {
			
			Expense expense = new Expense(name_text.getText(),
					(String)date_text.getText(), category_text.getText(),
					Double.parseDouble(amount_text.getText()));
			MongoDBConnection.getMongoDBConnection().insertExpense(expense);
		} else if ("removeExpense".equals(e.getActionCommand())) {
			int row = expenseTable.getSelectedRow();
			Expense expense = new Expense((String) expenseTable.getValueAt(row,
					0), (String) expenseTable.getValueAt(row, 3),(String) expenseTable.getValueAt(row,
							2),
					Double.parseDouble(((String)expenseTable.getValueAt(row, 1)).substring(1))
					);
			MongoDBConnection.getMongoDBConnection().removeExpense(expense);
		} else if ("editBudget".equals(e.getActionCommand())) {
			System.out.println("Attempt t oedit");
		}

		currentExpenses = MongoDBConnection.getMongoDBConnection()
				.getExpense();
		etm.setData(currentExpenses);
		etm.fireTableDataChanged();
		expensePanel.revalidate();
		expensePanel.repaint();
	}
}
