package forecaster.money;

import java.util.Date;

public class Forecast {
	private String name;
	private String date;
	private String category;
	private double amount;
	private double total;
	
	public Forecast(String name, String date, String category, double amount, double total){
		this.name = name;
		this.date = date;
		this.category = category;
		this.amount = amount;
		this.setTotal(total);
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
		return date;
	}

	/**
	 * @param expense_date the expense_date to set
	 */
	public void setExpense_date(String expense_date) {
		this.date = expense_date;
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

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}
}
