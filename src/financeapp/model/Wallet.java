package financeapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Transaction> transactions;
    private Map<String, Double> budgets;

    public Wallet() {
        this.transactions = new ArrayList<>();
        this.budgets = new HashMap<>();
    }

    public List<Transaction> getTransactions() { return transactions; }

    public void addTransaction (Transaction transaction) { transactions.add(transaction); }

    public void setBudget (String category, Double amount) { budgets.put(category, amount); }

    public Double getBudget (String category) { return budgets.get(category); }
}
