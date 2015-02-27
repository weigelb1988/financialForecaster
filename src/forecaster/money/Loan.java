package forecaster.money;

public class Loan implements Comparable<Loan>{
	
	private String name = null;
	private double amount;
	private double interestRate;
	private int monthsRemaining;
	private double monthlyPayment;
	private int dueDay;
	
	public Loan(String name, double amount, double interest, int months, double payment, int dueDay){
		this.name = name;
		this.amount = amount;
		this.interestRate = interest;
		this.monthsRemaining = months;
		this.monthlyPayment = payment;
		this.dueDay = dueDay;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public int getMonthsRemaining() {
		return monthsRemaining;
	}

	public void setMonthsRemaining(int monthsRemaining) {
		this.monthsRemaining = monthsRemaining;
	}

	public double getMonthlyPayment() {
		return monthlyPayment;
	}

	public void setMonthlyPayment(double monthlyPayment) {
		this.monthlyPayment = monthlyPayment;
	}

	/**
	 * @return the dueday
	 */
	public int getDueDay() {
		return dueDay;
	}

	/**
	 * @param dueday the dueday to set
	 */
	public void setDueDay(int dueDay) {
		this.dueDay = dueDay;
	}

	@Override
	public int compareTo(Loan o) {
		double comparedInterest = o.getInterestRate();
		if(this.interestRate > comparedInterest){
			return 1;
		}else if(this.interestRate == comparedInterest){
			return 0;
		}else{
			return -1;
		}
			
	}
	
	public String toString(){
		return "NAME: " + this.name + " PRINCIPLE: " + this.amount + " PAYMENT: " + this.monthlyPayment;
	}

	public boolean equals(Loan l){
		if(l.getName().equals(this.name) && l.getInterestRate() == this.interestRate && l.getMonthsRemaining() == this.monthsRemaining && l.getDueDay() == this.dueDay){
			return true;
		}else{
			return false;
		}
	}
	
	
}
