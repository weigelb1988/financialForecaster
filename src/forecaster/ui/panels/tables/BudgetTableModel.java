package forecaster.ui.panels.tables;

import java.text.NumberFormat;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

import forecaster.database.MongoDBConnection;
import forecaster.money.Budget;

public class BudgetTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] colNames = { "Name", "Amount", "Category", "Frequency", "Day" };

	/**
	 * @return the colNames
	 */
	public String[] getColNames() {
		return colNames;
	}

	private LinkedList<Budget> data;

	public BudgetTableModel(LinkedList<Budget> data) {
		this.data = data;
	}

	public String getColumnName(int col) {
		return colNames[col];
	}

	/**
	 * @return the data
	 */
	public LinkedList<Budget> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(LinkedList<Budget> data) {
		this.data = data;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Budget budget = data.get(row);
		if (budget == null) {
			return null;
		}
		switch (col) {
		case 0:
			return budget.getName();
		case 1:
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			return nf.format(budget.getAmount());
		case 2:
			return budget.getCategory();
		case 3:
			return budget.getFrequency();
		case 4:
			if (budget.getFrequency().equals("bi-weekly") || budget.getFrequency().equals("weekly")){
				switch (budget.getDay()){
				case 1:
					return "Monday";
				case 2:
					return "Tuesday";
				case 3:
					return "Wednesday";
				case 4:
					return "Thursday";
				case 5:
					return "Friday";
				case 6: 
					return "Saturday";
				case 7:
					return "Sunday";
				}
			}else{
				return budget.getDay();
			}
				
		default:
			return null;
		}
	}

	public Class getColumnClass(int c) {
		Object value = this.getValueAt(0, c);
		return (value == null ? Object.class : value.getClass());

	}

	public boolean isCellEditable(int row, int col) {
		// Note that the data/cell address is constant,
		// no matter where the cell appears onscreen.
	
			return true;
		
	}

	public void setValueAt(Object value, int row, int col) {
		Budget budget = data.get(row);
		MongoDBConnection.getMongoDBConnection().removeBudget(budget);
		switch (col) {
		case 0:
			budget.setName((String) value);
			break;
		case 1:
			double val2 = Double.parseDouble(((String)value).substring(1));
			budget.setAmount((double) val2);
			break;
		case 2:
			budget.setCategory((String) value);
			break;
		case 3:
			budget.setFrequency((String) value);
			break;
		case 4:
			if(value instanceof String){
				switch((String)value){
				case "Monday":
					budget.setDay(1);
					break;
				case "Tuesday":
					budget.setDay(2);
					break;
				case "Wednesday":
					budget.setDay(3);
					break;
				case "Thursday":
					budget.setDay(4);
					break;
				case "Friday":
					budget.setDay(5);
					break;
				case "Saturday":
					budget.setDay(6);
					break;
				case "Sunday":
					budget.setDay(7);
					break;
				}
			}else{
				budget.setDay((int)value);
			}
		}
		MongoDBConnection.getMongoDBConnection().insertBudget(budget);
		data = MongoDBConnection.getMongoDBConnection().getBudgets();
		fireTableCellUpdated(row, col);
	}

}
