package Models;

import java.util.Date;

public class Expense {

    private int idExpense;
    private double amount;
    private Date date;
    private Category category;
    private TransactionType transactionType;
    private User user;

    public Expense()
    {
        setIdExpense(0);
        setAmount(0.0);
        setDate(new Date());
        setCategory(new Category());
        setTransactionType(new TransactionType());
        setUser(new User());
    }

    public Expense(int idExpense, double amount, Date date, Category category, TransactionType transactionType, User user)
    {
        setIdExpense(idExpense);
        setAmount(amount);
        setDate(date);
        setCategory(category);
        setTransactionType(transactionType);
        setUser(user);
    }

    public int getIdExpense() {
        return idExpense;
    }

    public void setIdExpense(int idExpense) {
        this.idExpense = idExpense;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
