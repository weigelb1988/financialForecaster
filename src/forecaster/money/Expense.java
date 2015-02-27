package forecaster.money;

import java.util.Date;

public class Expense {
	private String name;
	private String expense_date;
	private String category;
	private double amount;
	
	public Expense(String name, String expense_date, String category, double amount){
		this.name = name;
		this.expense_date = expense_date;
		this.category = category;
		this.amount = amount;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the expense_date
	 */
	public String getExpense_date() {
		return expense_date;
	}

	/**
	 * @param expense_date the expense_date to set
	 */
	public void setExpense_date(String expense_date) {
		this.expense_date = expense_date;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
