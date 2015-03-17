package forecaster.ui.panels.tables;

import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import forecaster.database.MongoDBConnection;
import forecaster.money.Expense;
import forecaster.money.Forecast;

public class ForecastTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] colNames = { "Date","Name", "Amount", "Category", "Total" };

	/**
	 * @return the colNames
	 */
	public String[] getColNames() {
		return colNames;
	}

	private LinkedList<Forecast> data;

	public ForecastTableModel(LinkedList<Forecast> data) {
		this.data = data;
	}

	public String getColumnName(int col) {
		return colNames[col];
	}

	/**
	 * @return the data
	 */
	public LinkedList<Forecast> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(LinkedList<Forecast> data) {
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
		Forecast forcast = data.get(row);
		if (forcast == null) {
			return null;
		}
		switch (col) {
		case 0: 
			return forcast.getExpense_date();
		case 1:
			return forcast.getName();
		case 2:
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			return nf.format(forcast.getAmount());
		case 3:
			return forcast.getCategory();
		case 4:
			NumberFormat nf1 = NumberFormat.getCurrencyInstance();
			return nf1.format(forcast.getTotal());
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
		Forecast forcast = data.get(row);
		MongoDBConnection.getMongoDBConnection().removeForcast(forcast);
		switch (col) {
		case 0:
			forcast.setExpense_date((String) value);
			break;
		case 1:
			forcast.setName((String) value);
			break;
		case 2:
			double val = Double.parseDouble(((String)value).replace("$","").replace(",",""));
			forcast.setAmount((double) val);
			break;
		case 3:
			forcast.setCategory((String) value);
			break;
		case 4:
			double val2 = Double.parseDouble(((String)value).replace("$","").replace(",",""));
			forcast.setTotal((double) val2);
			break;
		}
		MongoDBConnection.getMongoDBConnection().insertForcast(forcast);
		data = MongoDBConnection.getMongoDBConnection().fillForcast();
		fireTableCellUpdated(row, col);
	}

	
}
