package Models;

import java.util.Date;

public class Budget {

    private int id;
    private int idBudget;
    private Date date;
    private double amount;
    private Category category;

    public Budget()
    {
        setId(0);
        setIdBudget(0);
        setDate(new Date());
        setAmount(0.0);
        setCategory(new Category());
    }

    public Budget(int id, int idBudget, Date date, double amount, Category category)
    {
        setId(id);
        setIdBudget(idBudget);
        setDate(date);
        setAmount(amount);
        setCategory(category);
    }

    public Budget(int idBudget, Date date)
    {
        setId(0);
        setIdBudget(idBudget);
        setDate(date);
        setAmount(0.0);
        setCategory(new Category());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(int idBudget) {
        this.idBudget = idBudget;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
