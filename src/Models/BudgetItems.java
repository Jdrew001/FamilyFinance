package Models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class BudgetItems extends RecursiveTreeObject<BudgetItems> {

    private int id;
    private int idBudget;
    private double amount;
    private Category category;

    public BudgetItems()
    {
        setId(0);
        setIdBudget(0);
        setAmount(0.0);
        setCategory(new Category());
    }

    public BudgetItems(int id, int idBudget, Date date, double amount, Category category)
    {
        setId(id);
        setIdBudget(idBudget);
        setAmount(amount);
        setCategory(category);
    }

    public BudgetItems(int idBudget, Date date)
    {
        setId(0);
        setIdBudget(idBudget);
        setAmount(0.0);
        setCategory(new Category());
    }

    public BudgetItems(int idBudget, double amount, Category category)
    {
        setId(0);
        setIdBudget(idBudget);
        setAmount(amount);
        setCategory(category);
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

    public StringProperty getCategoryName() {
        return new SimpleStringProperty(getCategory().getName());
    }

    public DoubleProperty getAmountProperty() {
        return new SimpleDoubleProperty(getAmount());
    }
}
