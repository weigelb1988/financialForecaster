package forecaster.ui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import forecaster.ui.panels.BudgetPanel;
import forecaster.ui.panels.ExpensePanel;
import forecaster.ui.panels.ForecastPanel;
import forecaster.ui.panels.IncomePanel;
import forecaster.ui.panels.LoanPanel;

public class ForecasterTabbedPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	public ForecasterTabbedPane() {
		super(new GridLayout(1, 1));

		JTabbedPane tabbedPane = new JTabbedPane();
		BudgetPanel bp = new BudgetPanel();
		LoanPanel lp = new LoanPanel();
		IncomePanel ip = new IncomePanel();
		ExpensePanel ep = new ExpensePanel();
		ForecastPanel fp = new ForecastPanel();
		
		
		tabbedPane.addTab("Budgets", null, bp.getBudgetComponent(), "Add and View Budgets");
		tabbedPane.addTab("Loans", null, lp.getLoanComponent(), "Add and View Loans");
		
		tabbedPane.addTab("Income", null, ip.getIncomeComponent(), "Add and View Incomes");

		tabbedPane.addTab("Expense", null, ep.getExpenseComponent(), "Add and View Expenses");
		tabbedPane.addTab("Forecast", null, fp.getExpenseComponent(), "View Forecast");
		tabbedPane.setPreferredSize(new Dimension(1380, 900));
		add(tabbedPane);

		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

	}

	

}
