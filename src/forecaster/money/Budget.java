package forecaster.money;

import java.util.ArrayList;

public class Budget {
	private String name;
	private String category;
	private double amount;
	private String frequency;
	private int day;
	
	/**
	 * @param name
	 * @param category
	 * @param amount
	 * @param frequency
	 */
	public Budget(String name, String category, double amount, String frequency, int day) {
		super();
		this.name = name;
		this.category = category;
		this.amount = amount;
		this.frequency = frequency;
		this.day = day;
	}
	public Budget() {
		
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
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	/**
	 * @return the days
	 */
	public int getDay() {
		return day;
	}
	/**
	 * @param days the days to set
	 */
	public void setDay(int day) {
		this.day = day;
	}
	
	
	
}
