package financeapp.service;

import financeapp.model.Transaction;
import financeapp.model.User;
import java.util.Map;
import java.util.stream.Collectors;

public class FinanceService {
    public void addIncome (User user, String category, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        Transaction transaction = new Transaction(category, amount, true);
        user.getWallet().addTransaction(transaction);
    }

    public void addExpence (User user, String category, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        Transaction transaction = new Transaction(category, amount, false);
        user.getWallet().addTransaction(transaction);
    }

    public void setBudget (User user, String category, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Бюджет не может быть отрицательным");
        }
        user.getWallet().setBudget(category, amount);
    }

    public double getTotalIncome (User user) {
        return user.getWallet().getTransactions().stream()
                .filter(Transaction::isIncome)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpence (User user) {
        return user.getWallet().getTransactions().stream()
                .filter(transaction -> !transaction.isIncome())
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Map<String, Double> getIncomeByCategory (User user) {
        return user.getWallet().getTransactions().stream()
                .filter(Transaction::isIncome)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public Map<String, Double> getExpensesByCategory (User user) {
        return user.getWallet().getTransactions().stream()
                .filter(transaction -> !transaction.isIncome())
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public double getBudgetRemaining (User user, String category) {
        double budget = user.getWallet().getBudget(category) != null ?
                user.getWallet().getBudget(category) : 0.0;
        double spent = getExpensesByCategory(user).getOrDefault(category, 0.0);
        return budget - spent;
    }

    public boolean isBudgetExceeded (User user, String category) {
        return getBudgetRemaining(user, category) < 0;
    }

    public boolean areExpensesExceedingIncome (User user) {
        return getTotalExpence(user) > getTotalIncome(user);
    }
}
