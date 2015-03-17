package forecaster.ui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.joda.time.LocalDate;

import forecaster.database.MongoDBConnection;
import forecaster.money.Budget;
import forecaster.money.Expense;
import forecaster.money.Forecast;
import forecaster.money.Income;
import forecaster.money.Info;
import forecaster.money.Loan;
import forecaster.ui.panels.tables.ForecastTableModel;
import forecaster.ui.panels.trees.ForecastTree;

public class ForecastPanel extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel forcastPanel = null;

	private JComponent expenseComponent = null;
	
	private ForecastTree ftm;
	private LinkedList<Forecast> forcastExpenses = null;
	private JScrollPane scrollPane;

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public ForecastPanel() {
		forcastExpenses = MongoDBConnection.getMongoDBConnection()
				.fillForcast();
		ftm = new ForecastTree();
		expenseComponent = makeExpensePanel("Forcast");
	}

	public JComponent getExpenseComponent() {
		return expenseComponent;
	}

	protected JComponent makeExpensePanel(String text) {
		forcastPanel = new JPanel(false);
		forcastPanel.setLayout(new SpringLayout());
		addForecastCompenents();
		addCurrentForcastComponents();

		return forcastPanel;
	}
	private void addForecastCompenents(){
		JButton generate = new JButton("     Forecast    ");
		generate.setActionCommand("forcast");
		generate.addActionListener(this);

		forcastPanel.add(generate);
		SpringLayout layout = (SpringLayout) forcastPanel.getLayout();
		SpringLayout.Constraints genLabelCons = layout
				.getConstraints(generate);
		genLabelCons.setX(Spring.constant(5));

		genLabelCons.setY(Spring.constant(7));
	}
	
	
	private void addCurrentForcastComponents() {

		SpringLayout layout = (SpringLayout) forcastPanel.getLayout();

		
		scrollPane = new JScrollPane(ftm.getTree());
		forcastPanel.add(scrollPane);
	

		layout.putConstraint(SpringLayout.EAST, scrollPane, -5,
				SpringLayout.EAST, forcastPanel);
		layout.putConstraint(SpringLayout.SOUTH, forcastPanel, 5,
				SpringLayout.SOUTH, scrollPane);
		layout.putConstraint(SpringLayout.WEST, scrollPane, 5,
				SpringLayout.WEST, forcastPanel);
		layout.putConstraint(SpringLayout.NORTH, scrollPane, 50,
				SpringLayout.NORTH, forcastPanel);

	}

	

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("addExpense".equals(e.getActionCommand())) {
			
			
		} else if ("removeForcast".equals(e.getActionCommand())) {
		
		} else if ("editBudget".equals(e.getActionCommand())) {
			System.out.println("Attempt t oedit");
		}else if("forcast".equals(e.getActionCommand())){
			MongoDBConnection.getMongoDBConnection().removeForcastData();
			generateForcastData();
			
			
			
		}

		forcastExpenses = MongoDBConnection.getMongoDBConnection()
				.fillForcast();
			
		forcastPanel.revalidate();
		forcastPanel.repaint();
	}


	public void generateForcastData() {
		LinkedList<Budget> budgets = MongoDBConnection.getMongoDBConnection()
				.getBudgets();
		LinkedList<Loan> loans = MongoDBConnection.getMongoDBConnection()
				.getLoans();
		Collections.sort(loans, Collections.reverseOrder());
		LinkedList<Expense> expenses = MongoDBConnection.getMongoDBConnection()
				.getExpense();
		LinkedList<Income> incomes = MongoDBConnection.getMongoDBConnection()
				.getIncomes();
		Info info = MongoDBConnection.getMongoDBConnection().getInfo();
		double currentAmount = info.getStartingAmount();
		double towardsDebt = info.getTowardsDebt();
		int monthsToProject = info.getMonthsToProject();
		LocalDate endDate = LocalDate.now().plusMonths(monthsToProject);
		int biweeklyPayCount = 0;
		int biweeklyBudgetCount = 0;
		for (LocalDate date = LocalDate.now(); date.isBefore(endDate); date = date
				.plusDays(1)) {
			if (date.getDayOfWeek() == 5) {
				biweeklyPayCount += 1;
			}
			if (date.getDayOfWeek() == 1) {
				biweeklyBudgetCount += 1;
			}
			for (Budget budget : budgets) {

				if (date.getDayOfWeek() == budget.getDay()
						&& biweeklyBudgetCount % 2 == 1
						&& budget.getFrequency().equals("bi-weekly")) {
					currentAmount = currentAmount - budget.getAmount();
					Forecast forecast = new Forecast(budget.getName(),
							date.toString(), budget.getCategory(),
							budget.getAmount(), currentAmount);
					MongoDBConnection.getMongoDBConnection().insertForcast(
							forecast);
				} else if (date.getDayOfWeek() == budget.getDay()
						&& budget.getFrequency().equals("weekly")) {
					currentAmount = currentAmount - budget.getAmount();
					Forecast forecast = new Forecast(budget.getName(),
							date.toString(), budget.getCategory(),
							budget.getAmount(), currentAmount);
					MongoDBConnection.getMongoDBConnection().insertForcast(
							forecast);
				} else if (budget.getFrequency().equals("monthly")
						&& date.getDayOfMonth() == budget.getDay()) {
					currentAmount = currentAmount - budget.getAmount();
					Forecast forecast = new Forecast(budget.getName(),
							date.toString(), budget.getCategory(),
							budget.getAmount(), currentAmount);
					MongoDBConnection.getMongoDBConnection().insertForcast(
							forecast);
				}

			}
			double totalLoans = 0;
			for (Loan loan : loans) {
				totalLoans = totalLoans + loan.getMonthlyPayment();
			}
			double additionalPayment = towardsDebt - totalLoans;
			for (Loan loan : loans) {
				
				if (loan.getAmount() <= 0) {
					additionalPayment =additionalPayment - loan.getAmount() + loan.getMonthlyPayment();
					loan.setAmount(0);

				} else {
					if (date.getDayOfMonth() == loan.getDueDay()) {
					
						currentAmount = currentAmount
								- loan.getMonthlyPayment();

						if (loan.equals(highestActiveInterestRate(loans))) {

							currentAmount = currentAmount - additionalPayment;
							Forecast forecast = new Forecast(loan.getName(),
									date.toString(), "Loan Payment",
									loan.getMonthlyPayment()
											+ additionalPayment, currentAmount);
							MongoDBConnection.getMongoDBConnection()
									.insertForcast(forecast);
							loan.setAmount(loan.getAmount() - loan.getMonthlyPayment()-additionalPayment);
						} else {

							Forecast forecast = new Forecast(loan.getName(),
									date.toString(), "Loan Payment",
									loan.getMonthlyPayment(), currentAmount);
							MongoDBConnection.getMongoDBConnection()
									.insertForcast(forecast);
							loan.setAmount(loan.getAmount() - loan.getMonthlyPayment());
						}
						
						
					}
				}

			}

			for (Expense expense : expenses) {
				if (date.toString().equals(expense.getExpense_date())) {
					currentAmount = currentAmount - expense.getAmount();
					Forecast forecast = new Forecast(expense.getName(),
							date.toString(), expense.getCategory(),
							expense.getAmount(), currentAmount);
					MongoDBConnection.getMongoDBConnection().insertForcast(
							forecast);
				}
			}

			for (Income income : incomes) {
				if (income.getFrequency().equals("bi-weekly")
						&& date.getDayOfWeek() == 5
						&& biweeklyPayCount % 2 == 1) {
					currentAmount = currentAmount + income.getAmount();
					Forecast forecast = new Forecast(income.getName(),
							date.toString(), "Income", income.getAmount(),
							currentAmount);
					MongoDBConnection.getMongoDBConnection().insertForcast(
							forecast);
				} else if (income.getFrequency().equals("weekly")
						&& date.getDayOfWeek() == 7) {
					currentAmount = currentAmount + income.getAmount();
					Forecast forecast = new Forecast(income.getName(),
							date.toString(), "Income", income.getAmount(),
							currentAmount);
					MongoDBConnection.getMongoDBConnection().insertForcast(
							forecast);
				}
			}
		}
	}

	public Loan highestActiveInterestRate(LinkedList<Loan> loans) {
		Loan highest = loans.getLast();
		for (Loan loan : loans) {
			if (loan.getInterestRate() > highest.getInterestRate()
					&& loan.getAmount() > 0) {
				highest = loan;
			}
		}
		return highest;
	}
	
	
}
