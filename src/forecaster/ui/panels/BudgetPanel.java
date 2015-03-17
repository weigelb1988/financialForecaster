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
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;

import forecaster.database.MongoDBConnection;
import forecaster.money.Budget;
import forecaster.ui.panels.tables.BudgetTableModel;

public class BudgetPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel budgetPanel = null;
	private BudgetTableModel btm;
	private JComponent budgetComponent = null;
	private LinkedList<Budget> currentBudgets = null;
	JTable budgetTable;
	private JTextField name_text;
	private JTextField amount_text;
	private JTextField freq_text;
	private JTextField category_text;
	private JTextField day_text;

	public BudgetPanel() {
		currentBudgets = MongoDBConnection.getMongoDBConnection().getBudgets();
		btm = new BudgetTableModel(currentBudgets);
		budgetComponent = makeBudgetPanel("Budgets");

	}

	public JComponent getBudgetComponent() {
		return budgetComponent;
	}

	protected JComponent makeBudgetPanel(String text) {
		budgetPanel = new JPanel(false);
		budgetPanel.setLayout(new SpringLayout());

		addBudgetCompents();
		addCurrentBudgets();
		return budgetPanel;
	}

	private JPopupMenu createPopupMenu() {
		JMenuItem item;
		JPopupMenu popup = new JPopupMenu();
		popup.add(item = new JMenuItem("Remove Budget"));
		item.addActionListener(this);
		item.setActionCommand("removeBudget");
		

		return popup;
	}

	private void addCurrentBudgets() {
		SpringLayout layout = (SpringLayout) budgetPanel.getLayout();

		budgetTable = new JTable(btm);

		budgetTable
				.setPreferredScrollableViewportSize(new Dimension(1300, 500));

		budgetTable.setFillsViewportHeight(true);

		budgetTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				int r = budgetTable.rowAtPoint(e.getPoint());
				if (r >= 0 && r < budgetTable.getRowCount()) {
					budgetTable.setRowSelectionInterval(r, r);
				} else {
					budgetTable.clearSelection();
				}
				int rowIndex = budgetTable.getSelectedRow();
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
		budgetTable.setDefaultRenderer(String.class, centerRender);
		budgetTable.setDefaultRenderer(Integer.class, centerRender);
		budgetTable.setDefaultRenderer(Double.class, centerRender);
		JScrollPane scrollPane = new JScrollPane(budgetTable);
		

		budgetPanel.add(scrollPane);

		layout.putConstraint(SpringLayout.EAST, scrollPane, -5,
				SpringLayout.EAST, budgetPanel);
		layout.putConstraint(SpringLayout.SOUTH, budgetPanel, 5,
				SpringLayout.SOUTH, scrollPane);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5,
				SpringLayout.WEST, budgetPanel);
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 50,
				SpringLayout.NORTH, budgetPanel);
	}

	private void addBudgetCompents() {
		SpringLayout layout = (SpringLayout) budgetPanel.getLayout();

		JLabel name_label = new JLabel("Name:");
		name_text = new JTextField(20);
		JLabel amount_label = new JLabel("Amount:");
		amount_text = new JTextField(10);
		JLabel category_label = new JLabel("Category:");
		category_text = new JTextField(5);
		JLabel freq_label = new JLabel("Frequency:");
		freq_text = new JTextField(5);
		JLabel day_label = new JLabel("Day of the Week(1-7) or Month(1-31)");
		day_text = new JTextField(5);
		JButton addDebt = new JButton("Add Budget");

		budgetPanel.add(name_label);
		budgetPanel.add(name_text);
		budgetPanel.add(amount_label);
		budgetPanel.add(amount_text);
		budgetPanel.add(category_label);
		budgetPanel.add(category_text);
		budgetPanel.add(freq_label);
		budgetPanel.add(freq_text);
		budgetPanel.add(day_label);
		budgetPanel.add(day_text);
		budgetPanel.add(addDebt);

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

		SpringLayout.Constraints categoryLabelCons = layout
				.getConstraints(category_label);
		categoryLabelCons.setX(Spring.sum(
				amountTextCons.getConstraint(SpringLayout.EAST),
				Spring.constant(5)));

		categoryLabelCons.setY(Spring.constant(5));

		SpringLayout.Constraints categoryTextCons = layout
				.getConstraints(category_text);
		categoryTextCons.setX(Spring.sum(Spring.constant(5),
				categoryLabelCons.getConstraint(SpringLayout.EAST)));

		categoryTextCons.setY(Spring.constant(5));

		SpringLayout.Constraints freqLabelCons = layout
				.getConstraints(freq_label);
		freqLabelCons.setX(Spring.sum(
				categoryTextCons.getConstraint(SpringLayout.EAST),
				Spring.constant(5)));

		freqLabelCons.setY(Spring.constant(5));

		SpringLayout.Constraints freqTextCons = layout
				.getConstraints(freq_text);
		freqTextCons.setX(Spring.sum(Spring.constant(5),
				freqLabelCons.getConstraint(SpringLayout.EAST)));

		freqTextCons.setY(Spring.constant(5));
		
		
		
		SpringLayout.Constraints dayLabelCons = layout
				.getConstraints(day_label);
		dayLabelCons.setX(Spring.sum(
				freqTextCons.getConstraint(SpringLayout.EAST),
				Spring.constant(5)));

		dayLabelCons.setY(Spring.constant(5));

		SpringLayout.Constraints dayTextCons = layout
				.getConstraints(day_text);
		dayTextCons.setX(Spring.sum(Spring.constant(5),
				dayLabelCons.getConstraint(SpringLayout.EAST)));

		dayTextCons.setY(Spring.constant(5));
		

		addDebt.setVerticalTextPosition(AbstractButton.CENTER);
		addDebt.setHorizontalTextPosition(AbstractButton.LEADING); // left
		addDebt.setActionCommand("addBudget");
		addDebt.addActionListener(this);

		SpringLayout.Constraints buttonCons = layout.getConstraints(addDebt);
		buttonCons.setX(Spring.sum(Spring.constant(5),
				dayTextCons.getConstraint(SpringLayout.EAST)));

		buttonCons.setY(Spring.constant(5));

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("addBudget".equals(e.getActionCommand())) {
			Budget budget = new Budget(name_text.getText(), category_text.getText(),
					Double.parseDouble(amount_text.getText().replace("$", "").replace(",", "")),
					freq_text.getText(), Integer.parseInt(day_text.getText()));
			MongoDBConnection.getMongoDBConnection().insertBudget(budget);
		} else if ("removeBudget".equals(e.getActionCommand())) {
			int row = budgetTable.getSelectedRow();
			int fin_day =0;
			Object day = budgetTable.getValueAt(row, 4);
			if(day instanceof String){
				switch((String)day){
				case "Monday":
					fin_day =1;
					break;
				case "Tuesday":
					fin_day =2;
					break;
				case "Wednesday":
					fin_day =3;
					break;
				case "Thursday":
					fin_day = 4;
					break;
				case "Friday":
					fin_day = 5;
					break;
				case "Saturday":
					fin_day = 6;
					break;
				case "Sunday":
					fin_day = 7;
					break;
				}
			}else{
				fin_day = ((int)day);
			}
			Budget budget = new Budget((String) budgetTable.getValueAt(row, 0),
					(String) budgetTable.getValueAt(row, 2),
					Double.parseDouble(((String)budgetTable.getValueAt(row, 1)).replace("$","").replace(",","")),
					(String) budgetTable.getValueAt(row, 3),
					fin_day);
			MongoDBConnection.getMongoDBConnection().removeBudget(budget);
		} 

		currentBudgets = MongoDBConnection.getMongoDBConnection().getBudgets();
		btm.setData(currentBudgets);
		btm.fireTableDataChanged();
		budgetPanel.revalidate();
		budgetPanel.repaint();

	}

}
