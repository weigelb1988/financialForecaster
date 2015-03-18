package forecaster.ui.panels.trees;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.joda.time.LocalDate;

import forecaster.database.MongoDBConnection;
import forecaster.money.Forecast;
import forecaster.money.Info;

public class ForecastTree extends BasicTreeUI implements TreeSelectionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTree tree;

	private Info info;

	private LinkedList<Forecast> forecasts;
	private DefaultTreeModel model;
	private JScrollPane scrollPane;
	public static int altCount = 0;

	public ForecastTree() {
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"Forecasted Data");
		info = MongoDBConnection.getMongoDBConnection().getInfo();
		forecasts = MongoDBConnection.getMongoDBConnection().fillForcast();

		createNodes(top);

		tree = new JTree(top);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		TreeCellRenderer renderer = new ForecastCellRenderer();
		tree.setCellRenderer(renderer);
		tree.setLargeModel(true);

	}

	private void createNodes(DefaultMutableTreeNode top) {
		int monthsToProject = info.getMonthsToProject();
		LocalDate endDate = LocalDate.now().plusMonths(monthsToProject);
		DefaultMutableTreeNode newYear = null;
		DefaultMutableTreeNode newMonth = null;
		for (LocalDate date = LocalDate.now(); date.isBefore(endDate); date = date
				.plusDays(1)) {
			if (date.getYear() > date.minusDays(1).getYear() || newYear == null) {
				newYear = new DefaultMutableTreeNode(Integer.toString(date
						.getYear()));
				top.add(newYear);
			}
			if (date.getMonthOfYear() != date.minusDays(1).getMonthOfYear()
					|| newMonth == null) {
				newMonth = new DefaultMutableTreeNode(date.monthOfYear()
						.getAsText());
				newYear.add(newMonth);

			}
			for (Forecast exp : new LinkedList<>(forecasts)) {

				if (exp.getExpense_date().equals(date.toString())) {

					forecasts.remove(exp);
					DefaultMutableTreeNode expense = new DefaultMutableTreeNode(
							exp);
					newMonth.add(expense);
					NumberFormat nf = NumberFormat.getCurrencyInstance();
					newMonth.setUserObject(date.monthOfYear().getAsText() + " ------ "
							+ nf.format(exp.getTotal()));

				} else {

					break;
				}

			}

		}
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public LinkedList<Forecast> getForecasts() {
		return forecasts;
	}

	public void setForecasts(LinkedList<Forecast> forecasts) {
		this.forecasts = forecasts;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub

	}

	public DefaultTreeModel getModel() {
		return model;
	}

	public void setModel(DefaultTreeModel model) {
		this.model = model;
	}

}

class ForecastCellRenderer implements TreeCellRenderer {

	private JLabel eomLabel = new JLabel(" ");
	private JLabel monthLabel = new JLabel(" ");

	private JLabel dateLabel = new JLabel(" ");

	private JLabel nameLabel = new JLabel(" ");

	private JLabel categoryLabel = new JLabel(" ");

	private JLabel amountLabel = new JLabel(" ");

	private JPanel renderer = new JPanel();

	private JLabel totalLabel = new JLabel(" ");

	private DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

	private Color backgroundSelectionColor;

	private Color backgroundNonSelectionColor;

	public ForecastCellRenderer() {
		dateLabel.setForeground(Color.BLACK);
		renderer.add(dateLabel);

		nameLabel.setForeground(Color.BLACK);
		renderer.add(nameLabel);

		categoryLabel.setForeground(Color.DARK_GRAY);
		renderer.add(categoryLabel);

		amountLabel.setHorizontalAlignment(JLabel.RIGHT);
		amountLabel.setForeground(Color.RED);

		renderer.add(amountLabel);

		totalLabel.setHorizontalAlignment(JLabel.RIGHT);
		totalLabel.setForeground(Color.RED);
		renderer.add(totalLabel);

		monthLabel.setForeground(Color.BLACK);
		renderer.add(monthLabel);

		eomLabel.setForeground(Color.BLACK);
		renderer.add(eomLabel);

		backgroundSelectionColor = defaultRenderer
				.getBackgroundSelectionColor();
		backgroundNonSelectionColor = new Color(193, 205, 193);// defaultRenderer.getBackgroundNonSelectionColor();
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		Component returnValue = null;
		if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
			Object userObject = ((DefaultMutableTreeNode) value)
					.getUserObject();
			if (userObject instanceof Forecast) {
				Forecast e = (Forecast) userObject;
				dateLabel.setText(e.getExpense_date());
				dateLabel.setPreferredSize(new Dimension(150, 20));
				nameLabel.setText(e.getName());
				nameLabel.setPreferredSize(new Dimension(200, 20));
				categoryLabel.setText(e.getCategory());
				categoryLabel.setPreferredSize(new Dimension(200, 20));
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				amountLabel.setText(nf.format(e.getAmount()));
				amountLabel.setPreferredSize(new Dimension(150, 20));
				totalLabel.setText(nf.format(e.getTotal()));
				totalLabel.setPreferredSize(new Dimension(150, 20));
				if (e.getCategory().equals("Income")) {
					amountLabel.setForeground(new Color(34, 139, 34));
				} else {
					amountLabel.setForeground(Color.RED);
				}
				if (e.getTotal() > 0) {
					totalLabel.setForeground(new Color(34, 139, 34));
				}
				if (selected) {

					renderer.setBackground(backgroundSelectionColor);
				} else {
					if (ForecastTree.altCount % 2 == 0) {
						renderer.setBackground(backgroundNonSelectionColor);
					} else {
						renderer.setBackground(Color.WHITE);
					}
					ForecastTree.altCount = ForecastTree.altCount + 1;
				}
				renderer.setEnabled(tree.isEnabled());
				returnValue = renderer;
			} 
		}
		if (returnValue == null) {
			returnValue = defaultRenderer.getTreeCellRendererComponent(tree,
					value, selected, expanded, leaf, row, hasFocus);
		}
		return returnValue;
	}
}
