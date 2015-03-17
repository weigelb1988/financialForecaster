package forecaster.database;

import java.util.ArrayList;
import java.util.LinkedList;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.bulk.UpdateRequest;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import forecaster.money.Budget;
import forecaster.money.Expense;
import forecaster.money.Forecast;
import forecaster.money.Income;
import forecaster.money.Info;
import forecaster.money.Loan;

public class MongoDBConnection {
	MongoClient mongoClient;
	MongoDatabase db;
	MongoCollection<Document> collection;
	private static final MongoDBConnection mdbc = new MongoDBConnection(
			"127.0.0.1", 27017, "financial_forcaster", "brian");

	public MongoDBConnection(String ipAddress, int port, String dbName,
			String collectionName) {

		mongoClient = new MongoClient(ipAddress, port);
		db = mongoClient.getDatabase(dbName);
		collection = db.getCollection(collectionName);

	}

	public static MongoDBConnection getMongoDBConnection() {

		return mdbc;
	}

	/**
	 * @return the collection
	 */
	public MongoCollection<Document> getCollection() {
		return collection;
	}

	/**
	 * @param collection
	 *            the collection to set
	 */
	public void setCollection(MongoCollection<Document> collection) {
		this.collection = collection;
	}

	/**
	 * @return the mongoClient
	 */
	public MongoClient getMongoClient() {
		return mongoClient;
	}

	/**
	 * @param mongoClient
	 *            the mongoClient to set
	 */
	public void setMongoCleint(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	/**
	 * @return the db
	 */
	public MongoDatabase getDb() {
		return db;
	}

	/**
	 * @param db
	 *            the db to set
	 */
	public void setDb(MongoDatabase db) {
		this.db = db;
	}

	@SuppressWarnings("unchecked")
	public LinkedList<Budget> getBudgets() {
		LinkedList<Budget> current = new LinkedList<Budget>();
		Document myDoc = collection.find(new Document("category", "budgets"))
				.first();
		ArrayList<Document> data = (ArrayList<Document>) myDoc.get("data");
		try {
			for (Document budget : data) {
				String name = (String) budget.get("name");
				String category = (String) budget.get("category");
				int iAmount = 0;
				double dAmount = 0.00;
				try {
					iAmount = (int) budget.get("amount");
				} catch (Exception e) {
					dAmount = (double) budget.get("amount");
				}
				String frequency = (String) budget.get("frequency");
				int day = (int) budget.get("day");
				if (iAmount != 0) {
					current.add(new Budget(name, category, iAmount, frequency,
							day));
				} else {
					current.add(new Budget(name, category, dAmount, frequency,
							day));
				}
			}
		} catch (NullPointerException e) {

		}

		return current;
	}

	public boolean insertBudget(Budget budget) {
		BasicDBObject budgetTop = new BasicDBObject("category", "budgets");
		Document bud = new Document("$push", new Document("data", new Document(
				"name", budget.getName())
				.append("category", budget.getCategory())
				.append("frequency", budget.getFrequency())
				.append("amount", budget.getAmount())
				.append("day", budget.getDay())));
		collection.updateOne(budgetTop, bud);
		return true;
	}

	public boolean removeBudget(Budget budget) {

		BasicDBObject budgetTop = new BasicDBObject("category", "budgets");
		Document bud = new Document("$pull", new Document("data", new Document(
				"name", budget.getName())
				.append("category", budget.getCategory())
				.append("frequency", budget.getFrequency())
				.append("amount", budget.getAmount())
				.append("day", budget.getDay())));
		collection.updateOne(budgetTop, bud);

		return true;
	}

	@SuppressWarnings("unchecked")
	public LinkedList<Loan> getLoans() {
		LinkedList<Loan> current = new LinkedList<Loan>();
		Document myDoc = collection.find(new Document("category", "loans"))
				.first();
		ArrayList<Document> data = (ArrayList<Document>) myDoc.get("data");
		try {
			for (Document loan : data) {
				String name = (String) loan.get("name");
				double interest = (double) loan.get("interest-rate");
				int iAmount = 0;
				double dAmount = 0.00;
				try {
					iAmount = (int) loan.get("amount");
				} catch (Exception e) {
					dAmount = (double) loan.get("amount");
				}
				int months = (int) loan.get("months-left");
				double dPay = 0.00;
				int iPay = 0;
				try {
					iPay = (int) loan.get("monthly-payment");
				} catch (Exception e0) {
					dPay = (double) loan.getDouble("monthly-payment");
				}
				int dueDay = (int) loan.get("due-day");

				if (iPay != 0 && iAmount != 0) {
					current.add(new Loan(name, iAmount, interest, months, iPay,
							dueDay));
				} else if (iPay != 0 && dAmount != 0) {
					current.add(new Loan(name, dAmount, interest, months, iPay,
							dueDay));
				} else if (dPay != 0 && iAmount != 0) {
					current.add(new Loan(name, iAmount, interest, months, dPay,
							dueDay));
				} else {
					current.add(new Loan(name, dAmount, interest, months, dPay,
							dueDay));
				}
			}
		} catch (NullPointerException e) {

		}

		return current;
	}

	public boolean insertLoan(Loan loan) {
		BasicDBObject loanTop = new BasicDBObject("category", "loans");
		Document bud = new Document("$push", new Document("data", new Document(
				"name", loan.getName())
				.append("monthly-payment", loan.getMonthlyPayment())
				.append("interest-rate", loan.getInterestRate())
				.append("amount", loan.getAmount())
				.append("months-left", loan.getMonthsRemaining())
				.append("due-day", loan.getDueDay())));
		collection.updateOne(loanTop, bud);
		return true;
	}

	public boolean insertIncome(Income income) {
		BasicDBObject incomeTop = new BasicDBObject("category", "incomes");
		Document bud = new Document("$push", new Document("data", new Document(
				"name", income.getName()).append("frequency",
				income.getFrequency()).append("amount", income.getAmount())));
		collection.updateOne(incomeTop, bud);
		return true;
	}

	@SuppressWarnings("unchecked")
	public LinkedList<Income> getIncomes() {
		LinkedList<Income> current = new LinkedList<Income>();
		Document myDoc = collection.find(new Document("category", "incomes"))
				.first();
		ArrayList<Document> data = (ArrayList<Document>) myDoc.get("data");
		try {
			for (Document income : data) {
				String name = (String) income.get("name");
				int iAmount = 0;
				double dAmount = 0.00;
				try {
					iAmount = (int) income.get("amount");
				} catch (Exception e) {
					dAmount = (double) income.get("amount");
				}
				String frequency = (String) income.get("frequency");

				if (iAmount != 0) {
					current.add(new Income(name, frequency, iAmount));
				} else {
					current.add(new Income(name, frequency, dAmount));
				}
			}
		} catch (NullPointerException e) {

		}
		return current;
	}

	public boolean removeIncome(Income income) {
		BasicDBObject incomeTop = new BasicDBObject("category", "incomes");
		Document bud = new Document("$pull", new Document("data", new Document(
				"name", income.getName()).append("frequency",
				income.getFrequency()).append("amount", income.getAmount())));
		collection.updateOne(incomeTop, bud);
		return true;
	}

	public boolean removeLoan(Loan loan) {
		BasicDBObject loanTop = new BasicDBObject("category", "loans");
		Document bud = new Document("$pull", new Document("data", new Document(
				"name", loan.getName())
				.append("monthly-payment", loan.getMonthlyPayment())
				.append("interest-rate", loan.getInterestRate())
				.append("amount", loan.getAmount())
				.append("months-left", loan.getMonthsRemaining())
				.append("due-day", loan.getDueDay())));
		collection.updateOne(loanTop, bud);
		return true;

	}

	public boolean insertExpense(Expense expense) {
		BasicDBObject incomeTop = new BasicDBObject("category", "expense");
		Document bud = new Document("$push", new Document("data", new Document(
				"name", expense.getName())
				.append("expense-date", expense.getExpense_date())
				.append("category", expense.getCategory())
				.append("amount", expense.getAmount())));
		collection.updateOne(incomeTop, bud);
		return true;
	}

	@SuppressWarnings("unchecked")
	public LinkedList<Expense> getExpense() {
		LinkedList<Expense> current = new LinkedList<Expense>();
		Document myDoc = collection.find(new Document("category", "expense"))
				.first();
		ArrayList<Document> data = (ArrayList<Document>) myDoc.get("data");
		try {
			for (Document expense : data) {
				String name = (String) expense.get("name");
				int iAmount = 0;
				double dAmount = 0.00;
				try {
					iAmount = (int) expense.get("amount");
				} catch (Exception e) {
					dAmount = (double) expense.get("amount");
				}
				String date = (String) expense.get("expense-date");
				String category = (String) expense.get("category");

				if (iAmount != 0) {
					current.add(new Expense(name, date, category, iAmount));
				} else {
					current.add(new Expense(name, date, category, dAmount));
				}
			}
		} catch (NullPointerException e) {

		}

		return current;
	}

	public boolean removeExpense(Expense expense) {
		BasicDBObject incomeTop = new BasicDBObject("category", "expense");
		Document bud = new Document("$pull", new Document("data", new Document(
				"name", expense.getName())
				.append("expense-date", expense.getExpense_date())
				.append("category", expense.getCategory())
				.append("amount", expense.getAmount())));
		collection.updateOne(incomeTop, bud);
		return true;
	}

	public boolean insertInfo(Info info) {
		BasicDBObject infoTop = new BasicDBObject("category", "info");
		Document bud = new Document("$push", new Document("data", new Document(
				"name", info.getName())
				.append("start-amount", info.getStartingAmount())
				.append("month-to-debt", info.getTowardsDebt())
				.append("project-months", info.getMonthsToProject())));
		UpdateResult ur = collection.updateOne(infoTop, bud);
		return ur.wasAcknowledged();

	}

	public boolean removeInfo(Info info) {
		BasicDBObject infoTop = new BasicDBObject("category", "info");
		Document bud = new Document("$pull", new Document("data", new Document(
				"name", info.getName())
				.append("start-amount", info.getStartingAmount())
				.append("month-to-debt", info.getTowardsDebt())
				.append("project-months", info.getMonthsToProject())));
		UpdateResult ur = collection.updateOne(infoTop, bud);
		return ur.wasAcknowledged();

	}

	@SuppressWarnings("unchecked")
	public Info getInfo() {

		Document myDoc = collection.find(new Document("category", "info"))
				.first();
		ArrayList<Document> data = (ArrayList<Document>) myDoc.get("data");
		try {
			Document first = data.get(0);
			return new Info((String) first.getString("name"),
					(double) first.get("start-amount"),
					(double) first.get("month-to-debt"),
					(int) first.get("project-months"));
		} catch (Exception e) {
			return new Info(new String(), 0, 0, 0);
		}
	}

	public boolean removeForcast(Forecast forcast) {
		BasicDBObject incomeTop = new BasicDBObject("category", "forcast");
		Document bud = new Document("$pull", new Document("data", new Document(
				"name", forcast.getName())
				.append("expense-date", forcast.getExpense_date())
				.append("category", forcast.getCategory())
				.append("amount", forcast.getAmount())
				.append("total", forcast.getTotal())));
		collection.updateOne(incomeTop, bud);
		return true;

	}

	public boolean insertForcast(Forecast forcast) {
		BasicDBObject forcastTop = new BasicDBObject("category", "forcast");
		Document cast = new Document("$push", new Document("data",
				new Document("name", forcast.getName())
						.append("expense-date", forcast.getExpense_date())
						.append("category", forcast.getCategory())
						.append("amount", forcast.getAmount())
						.append("total", forcast.getTotal())));
		collection.updateOne(forcastTop, cast);
		return true;

	}

	@SuppressWarnings("unchecked")
	public LinkedList<Forecast> fillForcast() {
		LinkedList<Forecast> current = new LinkedList<Forecast>();
		Document myDoc = collection.find(new Document("category", "forcast"))
				.first();

		ArrayList<Document> data = (ArrayList<Document>) myDoc.get("data");
		try {
			for (Document forcast : data) {
				String name = (String) forcast.get("name");
				int iAmount = 0;
				double dAmount = 0.00;
				try {
					iAmount = (int) forcast.get("amount");
				} catch (Exception e) {
					dAmount = (double) forcast.get("amount");
				}
				String date = (String) forcast.get("expense-date");
				String category = (String) forcast.get("category");
				double total = (double) forcast.get("total");

				if (iAmount != 0) {
					current.add(new Forecast(name, date, category, iAmount,
							total));
				} else {
					current.add(new Forecast(name, date, category, dAmount,
							total));
				}
			}
		} catch (NullPointerException e) {

		}
		return current;
	}

	public boolean removeForcastData() {
		BasicDBObject incomeTop = new BasicDBObject("category", "forcast");
		Document bud = new Document("$pull", new Document("data", new Document()));
		collection.updateOne(incomeTop, bud);
		return true;

	}
}
