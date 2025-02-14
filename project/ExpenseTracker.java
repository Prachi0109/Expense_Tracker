package project;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Expense implements Serializable {
	private static final long serialVersionUID = 1L;
	double amount;
	String category;
	String date; // Format: DD/MM/YYYY

	public Expense(double amount, String category, String date) {
		this.amount = amount;
		this.category = category;
		this.date = date;
	}

	public void displayExpense() {
		System.out.println("Amount: ₹" + amount + " | Category: " + category + " | Date: " + date);
	}

	public int getMonth() {
		return Integer.parseInt(date.split("/")[1]); // Extract month
	}

	public int getYear() {
		return Integer.parseInt(date.split("/")[2]); // Extract year
	}
}

public class ExpenseTracker {
	private static final String FILE_NAME = "expenses.dat";
	private static ArrayList<Expense> expenses = new ArrayList<>();

	public static void main(String[] args) {

		loadExpenses(); // Load previous data
		Scanner scanner = new Scanner(System.in);
		int choice;

		while (true) {
			System.out.println("\n==== Expense Tracker ====");
			System.out.println("1. Add Expense");
			System.out.println("2. View All Expenses");
			System.out.println("3. Calculate Total Expenses");
			System.out.println("4. Generate Monthly Report");
			System.out.println("5. Exit");
			System.out.print("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline

			switch (choice) {
			case 1:
				addExpense(scanner);
				break;
			case 2:
				viewExpenses();
				break;
			case 3:
				calculateTotal();
				break;
			case 4:
				generateMonthlyReport(scanner);
				break;
			case 5:
				saveExpenses(); // Save data before exit
				System.out.println("Exiting Expense Tracker. Goodbye!");
				scanner.close();
				return;
			default:
				System.out.println("Invalid choice! Please try again.");
			}
		}
	}

	private static void addExpense(Scanner scanner) {
		System.out.print("Enter amount: ₹");
		double amount = scanner.nextDouble();
		scanner.nextLine(); // Consume newline

		System.out.print("Enter category (Food, Travel, Shopping, etc.): ");
		String category = scanner.nextLine();

		System.out.print("Enter date (DD/MM/YYYY): ");
		String date = scanner.nextLine();

		expenses.add(new Expense(amount, category, date));
		System.out.println("Expense added successfully!");
	}

	private static void viewExpenses() {
		System.out.println("\n==== All Expenses ====");
		if (expenses.isEmpty()) {
			System.out.println("No expenses recorded.");
		} else {
			for (Expense exp : expenses) {
				exp.displayExpense();
			}
		}
	}

	private static void calculateTotal() {
		double total = 0;
		for (Expense exp : expenses) {
			total += exp.amount;
		}
		System.out.println("Total Expenses: ₹" + total);
	}

	private static void generateMonthlyReport(Scanner scanner) {
		System.out.print("Enter month (1-12): ");
		int month = scanner.nextInt();
		System.out.print("Enter year (YYYY): ");
		int year = scanner.nextInt();

		double monthlyTotal = 0;
		boolean found = false;

		System.out.println("\n==== Monthly Expense Report for " + month + "/" + year + " ====");
		for (Expense exp : expenses) {
			if (exp.getMonth() == month && exp.getYear() == year) {
				exp.displayExpense();
				monthlyTotal += exp.amount;
				found = true;
			}
		}

		if (!found) {
			System.out.println("No expenses recorded for this month.");
		} else {
			System.out.println("Total Expenses for " + month + "/" + year + ": ₹" + monthlyTotal);
		}
	}

	private static void saveExpenses() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
			oos.writeObject(expenses);
		} catch (IOException e) {
			System.out.println("Error saving expenses: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private static void loadExpenses() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
			expenses = (ArrayList<Expense>) ois.readObject();
		} catch (FileNotFoundException e) {
			System.out.println("No previous expense data found. Starting fresh.");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error loading expenses: " + e.getMessage());
		}
	}
}
