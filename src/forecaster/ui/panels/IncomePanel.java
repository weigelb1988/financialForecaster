package forecaster.ui.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import forecaster.money.Income;
import forecaster.ui.panels.tables.IncomeTableModel;

public class IncomePanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel incomePanel = null;

	private JComponent incomeComponent = null;
	private LinkedList<Income> currentIncome;

	private IncomeTableModel itm;
	JTable incomeTable;
	private JTextField name_text;
	private JTextField amount_text;
	private JTextField freq_text;

	public IncomePanel() {
		currentIncome = MongoDBConnection.getMongoDBConnection().getIncomes();
		itm = new IncomeTableModel(currentIncome);
		incomeComponent = makeIncomePanel("Income");

	}

	public JComponent getIncomeComponent() {
		return incomeComponent;
	}

	protected JComponent makeIncomePanel(String text) {
		incomePanel = new JPanel(false);
		incomePanel.setLayout(new SpringLayout());

		addIncomeCompents();
		addCurrentIncomeCompenents();

		return incomePanel;
	}

	private JPopupMenu createPopupMenu() {
		JMenuItem item;
		JPopupMenu popup = new JPopupMenu();
		popup.add(item = new JMenuItem("Remove Income"));
		item.addActionListener(this);
		item.setActionCommand("removeIncome");

		return popup;
	}

	private void addCurrentIncomeCompenents() {
		SpringLayout layout = (SpringLayout) incomePanel.getLayout();

		incomeTable = new JTable(itm);

		incomeTable
				.setPreferredScrollableViewportSize(new Dimension(1300, 500));

		incomeTable.setFillsViewportHeight(true);

		JScrollPane scrollPane = new JScrollPane(incomeTable);

		incomePanel.add(scrollPane);

		incomeTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {

				int r = incomeTable.rowAtPoint(e.getPoint());
				if (r >= 0 && r < incomeTable.getRowCount()) {
					incomeTable.setRowSelectionInterval(r, r);
				} else {
					incomeTable.clearSelection();
				}
				int rowIndex = incomeTable.getSelectedRow();
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
		incomeTable.setDefaultRenderer(String.class, centerRender);
		incomeTable.setDefaultRenderer(Double.class, centerRender);

		SpringLayout.Constraints tableCons = layout.getConstraints(incomeTable);
		layout.putConstraint(SpringLayout.EAST, scrollPane, -5,
				SpringLayout.EAST, incomePanel);
		layout.putConstraint(SpringLayout.SOUTH, incomePanel, 5,
				SpringLayout.SOUTH, scrollPane);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5,
				SpringLayout.WEST, incomePanel);
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 50,
				SpringLayout.NORTH, incomePanel);

	}

	private void addIncomeCompents() {
		SpringLayout layout = (SpringLayout) incomePanel.getLayout();

		Component[] components = incomePanel.getComponents();
		SpringLayout.Constraints lowestCons = new Constraints();
		if (components.length > 0) {
			JButton lowestComp = (JButton) incomePanel.getComponents()[incomePanel
					.getComponents().length - 1];
			lowestCons = layout.getConstraints(lowestComp);
			lowestComp.setActionCommand("removeIncome");
			lowestComp.setText("Remove Income");
		}

		JLabel name_label = new JLabel("Income Name:");
		name_text = new JTextField(20);
		JLabel amount_label = new JLabel("Amount:");
		amount_text = new JTextField(10);
		JLabel freq_label = new JLabel("Frequency");
		freq_text = new JTextField(5);
		JButton addIncome = new JButton("    Add Income   ");

		incomePanel.add(name_label);
		incomePanel.add(name_text);
		incomePanel.add(amount_label);
		incomePanel.add(amount_text);
		incomePanel.add(freq_label);
		incomePanel.add(freq_text);
		incomePanel.add(addIncome);

		SpringLayout.Constraints labelcons = layout.getConstraints(name_label);
		labelcons.setX(Spring.constant(5));
		if (components.length > 0) {
			labelcons.setY(Spring.sum(
					lowestCons.getConstraint(SpringLayout.SOUTH),
					Spring.constant(5)));
		} else {
			labelcons.setY(Spring.constant(5));
		}

		SpringLayout.Constraints nameCons = layout.getConstraints(name_text);
		nameCons.setX(Spring.sum(Spring.constant(5),
				labelcons.getConstraint(SpringLayout.EAST)));
		if (components.length > 0) {
			nameCons.setY(Spring.sum(
					lowestCons.getConstraint(SpringLayout.SOUTH),
					Spring.constant(5)));
		} else {
			nameCons.setY(Spring.constant(5));
		}

		SpringLayout.Constraints amountLabelCons = layout
				.getConstraints(amount_label);
		amountLabelCons.setX(Spring.sum(
				nameCons.getConstraint(SpringLayout.EAST), Spring.constant(5)));
		if (components.length > 0) {
			amountLabelCons.setY(Spring.sum(
					lowestCons.getConstraint(SpringLayout.SOUTH),
					Spring.constant(5)));
		} else {
			amountLabelCons.setY(Spring.constant(5));
		}

		SpringLayout.Constraints amountTextCons = layout
				.getConstraints(amount_text);
		amountTextCons.setX(Spring.sum(Spring.constant(5),
				amountLabelCons.getConstraint(SpringLayout.EAST)));
		if (components.length > 0) {
			amountTextCons.setY(Spring.sum(
					lowestCons.getConstraint(SpringLayout.SOUTH),
					Spring.constant(5)));
		} else {
			amountTextCons.setY(Spring.constant(5));
		}

		SpringLayout.Constraints monthLabelCons = layout
				.getConstraints(freq_label);
		monthLabelCons.setX(Spring.sum(
				amountTextCons.getConstraint(SpringLayout.EAST),
				Spring.constant(5)));
		if (components.length > 0) {
			monthLabelCons.setY(Spring.sum(
					lowestCons.getConstraint(SpringLayout.SOUTH),
					Spring.constant(5)));
		} else {
			monthLabelCons.setY(Spring.constant(5));
		}

		SpringLayout.Constraints monthTextCons = layout
				.getConstraints(freq_text);
		monthTextCons.setX(Spring.sum(Spring.constant(5),
				monthLabelCons.getConstraint(SpringLayout.EAST)));
		if (components.length > 0) {
			monthTextCons.setY(Spring.sum(
					lowestCons.getConstraint(SpringLayout.SOUTH),
					Spring.constant(5)));
		} else {
			monthTextCons.setY(Spring.constant(5));
		}

		addIncome.setVerticalTextPosition(AbstractButton.CENTER);
		addIncome.setHorizontalTextPosition(AbstractButton.LEADING); // left
		addIncome.setActionCommand("addIncome");
		addIncome.addActionListener(this);

		SpringLayout.Constraints buttonCons = layout.getConstraints(addIncome);
		buttonCons.setX(Spring.sum(Spring.constant(5),
				monthTextCons.getConstraint(SpringLayout.EAST)));
		if (components.length > 0) {
			buttonCons.setY(Spring.sum(
					lowestCons.getConstraint(SpringLayout.SOUTH),
					Spring.constant(5)));
		} else {
			buttonCons.setY(Spring.constant(5));
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("addIncome".equals(e.getActionCommand())) {
			Income income = new Income(name_text.getText(),
					freq_text.getText(), Double.parseDouble(amount_text
							.getText()));
			MongoDBConnection.getMongoDBConnection().insertIncome(income);
			
		} else if ("removeIncome".equals(e.getActionCommand())) {
			int row = incomeTable.getSelectedRow();
			Income income = new Income((String)incomeTable.getValueAt(row, 0), (String)incomeTable.getValueAt(row, 2), Double.parseDouble(((String)incomeTable.getValueAt(row, 1)).substring(1).replace(",", "")));
			MongoDBConnection.getMongoDBConnection().removeIncome(income);
		} else if ("editIncome".equals(e.getActionCommand())) {
			System.out.println("Attempt t oedit");
		}
		currentIncome = MongoDBConnection.getMongoDBConnection().getIncomes();
		itm.setData(currentIncome);
		itm.fireTableDataChanged();
		incomePanel.revalidate();
		incomePanel.repaint();
	}

}
