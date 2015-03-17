package forecaster.ui.panels.tables;

import java.text.NumberFormat;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

import forecaster.database.MongoDBConnection;
import forecaster.money.Budget;
import forecaster.money.Income;

public class IncomeTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] colNames = { "Name", "Amount", "Frequency" };

	/**
	 * @return the colNames
	 */
	public String[] getColNames() {
		return colNames;
	}

	private LinkedList<Income> data;

	public IncomeTableModel(LinkedList<Income> data) {
		this.data = data;
	}

	public String getColumnName(int col) {
		return colNames[col];
	}

	/**
	 * @return the data
	 */
	public LinkedList<Income> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(LinkedList<Income> data) {
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
		Income income = data.get(row);
		if (income == null) {
			return null;
		}
		switch (col) {
		case 0:
			return income.getName();
		case 1:
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			return nf.format(income.getAmount());
		case 2:
			return income.getFrequency();
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
		Income income = data.get(row);
		MongoDBConnection.getMongoDBConnection().removeIncome(income);
		switch (col) {
		case 0:
			income.setName((String) value);
			break;
		case 1:
			double val = Double.parseDouble(((String)value).replace("$","").replace(",", "")	);
			income.setAmount((double) val);
			break;
		case 2:
			income.setFrequency((String) value);
			break;
		}
		MongoDBConnection.getMongoDBConnection().insertIncome(income);
		data = MongoDBConnection.getMongoDBConnection().getIncomes();
		fireTableCellUpdated(row, col);
	}

}
