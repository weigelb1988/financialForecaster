package forecaster.money;

public class Info {

	private String name;
	private double startingAmount;
	private double towardsDebt;
	private int monthsToProject;
	public Info(String name, double startingAmount, double towardsDebt, int monthsToProject){
		this.name = name;
		this.startingAmount = startingAmount;
		this.towardsDebt = towardsDebt;
		this.monthsToProject = monthsToProject;
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
	 * @return the startingAmount
	 */
	public double getStartingAmount() {
		return startingAmount;
	}
	/**
	 * @param startingAmount the startingAmount to set
	 */
	public void setStartingAmount(double startingAmount) {
		this.startingAmount = startingAmount;
	}
	/**
	 * @return the towardsDebt
	 */
	public double getTowardsDebt() {
		return towardsDebt;
	}
	/**
	 * @param towardsDebt the towardsDebt to set
	 */
	public void setTowardsDebt(double towardsDebt) {
		this.towardsDebt = towardsDebt;
	}
	/**
	 * @return the monthsToProject
	 */
	public int getMonthsToProject() {
		return monthsToProject;
	}
	/**
	 * @param monthsToProject the monthsToProject to set
	 */
	public void setMonthsToProject(int monthsToProject) {
		this.monthsToProject = monthsToProject;
	}
}
