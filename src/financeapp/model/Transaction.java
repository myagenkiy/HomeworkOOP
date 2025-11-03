package financeapp.model;

import java.util.Objects;

public class Transaction {
    private String category;
    private double amount;
    private boolean isIncome;

    public Transaction(String category, double amount, boolean isIncome) {
        this.category = category;
        this.amount = amount;
        this.isIncome = isIncome;
    }

    public String getCategory() { return category; }

    public double getAmount() { return amount; }

    public boolean isIncome() { return isIncome; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(amount, that.amount) == 0 && isIncome == that.isIncome && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, amount, isIncome);
    }
}
