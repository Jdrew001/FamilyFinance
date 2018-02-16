package Models;

import java.text.DecimalFormat;
import java.util.Date;

public class Income {

    private int idIncome;
    private double amount;
    private Date date;
    private Category category;
    private TransactionType transactionType;
    private User user;

    public Income()
    {
        setIdIncome(0);
        setAmount(0);
        setDate(new Date());
        setCategory(new Category());
        setTransactionType(new TransactionType());
    }

    public Income(int idIncome, double amount, Date date, User user, Category category, TransactionType transactionType)
    {
        setIdIncome(idIncome);
        setAmount(amount);
        setDate(date);
        setUser(user);
        setCategory(category);
        setTransactionType(transactionType);
    }

    public Income(double amount, Date date, User user, Category category, TransactionType transactionType)
    {
        setAmount(amount);
        setDate(date);
        setUser(user);
        setCategory(category);
        setTransactionType(transactionType);
    }

    public int getIdIncome() {
        return idIncome;
    }

    public void setIdIncome(int idIncome) {
        this.idIncome = idIncome;
    }

    public double getAmount() {
        DecimalFormat df = new DecimalFormat("#.00");


        return Double.parseDouble(df.format(this.amount));
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

    public String getCategoryName() { return this.getCategory().getName(); }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getTransactionName() { return getTransactionType().getName(); }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getUsername() { return this.getUser().getUsername(); }
}
