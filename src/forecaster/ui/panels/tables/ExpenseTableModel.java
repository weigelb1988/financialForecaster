package forecaster.ui.panels.tables;

import java.text.NumberFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

import forecaster.database.MongoDBConnection;
import forecaster.money.Expense;

public class ExpenseTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] colNames = { "Name", "Amount", "Category", "Date" };

	/**
	 * @return the colNames
	 */
	public String[] getColNames() {
		return colNames;
	}

	private LinkedList<Expense> data;

	public ExpenseTableModel(LinkedList<Expense> data) {
		this.data = data;
	}

	public String getColumnName(int col) {
		return colNames[col];
	}

	/**
	 * @return the data
	 */
	public LinkedList<Expense> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(LinkedList<Expense> data) {
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
		Expense expense = data.get(row);
		if (expense == null) {
			return null;
		}
		switch (col) {
		case 0:
			return expense.getName();
		case 1:
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			return nf.format(expense.getAmount());
		case 2:
			return expense.getCategory();
		case 3:
			return expense.getExpense_date();
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
		Expense expense = data.get(row);
		MongoDBConnection.getMongoDBConnection().removeExpense(expense);
		switch (col) {
		case 0:
			expense.setName((String) value);
			break;
		case 1:
			double val = Double.parseDouble(((String)value).replace("$","").replace(",",""));
			expense.setAmount((double) val);
			break;
		case 2:
			expense.setCategory((String) value);
			break;
		case 3:
			expense.setExpense_date((String) value);
			break;
		}
		MongoDBConnection.getMongoDBConnection().insertExpense(expense);
		data = MongoDBConnection.getMongoDBConnection().getExpense();
		fireTableCellUpdated(row, col);
	}

}
