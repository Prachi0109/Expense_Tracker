package project;
import java.io.*;
import java.util.*;

public class ExpenseTracker {
    private static final String FILE_NAME = "expenses.txt";
    private static final List<String> expenses = new ArrayList<>();

    public static void main(String[] args) {
        loadExpenses();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Expense\n2. View Expenses\n3. Delete Expense\n4. Total\n5. Monthly Report\n6. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1 -> addExpense(scanner);
                case 2 -> viewExpenses();
                case 3 -> deleteExpense(scanner);
                case 4 -> calculateTotal();
                case 5 -> monthlyReport(scanner);
                case 6 -> { saveExpenses(); System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid choice. Try again!");
            }
        }
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("Amount: ₹");
        String amount = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Date (DD/MM/YYYY): ");
        String date = scanner.nextLine();

        expenses.add(amount + " | " + category + " | " + date);
        System.out.println("Expense added!");
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
        } else {
            System.out.println("\n==== Expenses ====");
            for (int i = 0; i < expenses.size(); i++) {
                System.out.println((i + 1) + ". " + expenses.get(i));
            }
        }
    }

    private static void deleteExpense(Scanner scanner) {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to delete.");
            return;
        }
        viewExpenses();
        System.out.print("Enter expense number to delete: ");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index > 0 && index <= expenses.size()) {
            System.out.println("Deleted: " + expenses.remove(index - 1));
            saveExpenses();
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private static void calculateTotal() {
        double total = expenses.stream()
                .mapToDouble(e -> Double.parseDouble(e.split(" \\| ")[0]))
                .sum();
        System.out.println("Total Expenses: ₹" + total);
    }

    private static void monthlyReport(Scanner scanner) {
        System.out.print("Enter month (1-12): ");
        int month = scanner.nextInt();
        System.out.print("Enter year (YYYY): ");
        int year = scanner.nextInt();
        scanner.nextLine();

        double monthlyTotal = 0;
        System.out.println("\n==== Monthly Report for " + month + "/" + year + " ====");

        for (String exp : expenses) {
            String[] details = exp.split(" \\| ");
            String date = details[2];
            int expMonth = Integer.parseInt(date.split("/")[1]);
            int expYear = Integer.parseInt(date.split("/")[2]);

            if (expMonth == month && expYear == year) {
                System.out.println(exp);
                monthlyTotal += Double.parseDouble(details[0]);
            }
        }
        System.out.println("Total: ₹" + monthlyTotal);
    }

    private static void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (String exp : expenses) writer.println(exp);
        } catch (IOException e) {
            System.out.println("Error saving expenses!");
        }
    }

    private static void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) expenses.add(line);
        } catch (IOException e) {
            System.out.println("No previous data found.");
        }
    }
}
