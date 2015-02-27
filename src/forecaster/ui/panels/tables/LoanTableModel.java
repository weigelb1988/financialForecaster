package forecaster.ui.panels.tables;

import java.text.NumberFormat;
import java.util.LinkedList;

import javax.swing.table.AbstractTableModel;

import forecaster.database.MongoDBConnection;
import forecaster.money.Budget;
import forecaster.money.Loan;

public class LoanTableModel extends AbstractTableModel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] colNames = {"Name", "Amount", "Interest Rate", "Monthly Payment", "Months Left", "Due Date"};
	
	/**
	 * @return the colNames
	 */
	public String[] getColNames() {
		return colNames;
	}
	private LinkedList<Loan> data;
	
	public LoanTableModel(LinkedList<Loan> data){
		this.data = data;
	}
	
	public String getColumnName(int col){
		return colNames[col];
	}
	/**
	 * @return the data
	 */
	public LinkedList<Loan> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(LinkedList<Loan> data) {
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
		Loan loan = data.get(row);
		if(loan == null){
			return null;
		}
		switch (col){
		case 0:
			return loan.getName();
		case 1:
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			return nf.format(loan.getAmount());
		case 2:
			NumberFormat nf1 = NumberFormat.getPercentInstance();
			nf1.setMinimumFractionDigits(2);
			return nf1.format(loan.getInterestRate());
		case 3:
			NumberFormat nf2 = NumberFormat.getCurrencyInstance();
			return nf2.format(loan.getMonthlyPayment());
		case 4:
			return loan.getMonthsRemaining();
		case 5:
			return loan.getDueDay();
		default:
			return null;
		}
	}
	
	 public Class getColumnClass(int c) {
		 Object value=this.getValueAt(0,c);
		 return (value==null?Object.class:value.getClass());
     }

     public boolean isCellEditable(int row, int col) {
         //Note that the data/cell address is constant,
         //no matter where the cell appears onscreen.
        
             return true;
     }
     
     public void setValueAt(Object value, int row, int col) {
    	 Loan loan = data.get(row);
		MongoDBConnection.getMongoDBConnection().removeLoan(loan);
    	 switch (col){
    	 case 0:
 			loan.setName((String) value);
 			break;
 		 case 1:
 			double val = Double.parseDouble(((String)value).substring(1));
 			loan.setAmount((double)val);
 			break;
 		 case 2:
 			double val1 = Double.parseDouble(((String)value).substring(0,((String)value).length()-1));
 			val1 = val1/100;
 		 	loan.setInterestRate(val1);
 		 	break;
 		 case 3:
 			double val2 = Double.parseDouble(((String)value).substring(1));
 		 	loan.setMonthlyPayment((double)val2);
 		 	break;
 		 case 4:
 			loan.setMonthsRemaining((int)value);
 			break;
 		 case 5:
 			loan.setDueDay((int)value);
 			break;
 		}
    	MongoDBConnection.getMongoDBConnection().insertLoan(loan);
    	data=MongoDBConnection.getMongoDBConnection().getLoans();
    	
        fireTableCellUpdated(row, col);
     }

}
