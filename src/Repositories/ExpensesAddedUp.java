package Repositories;

import Models.Category;

public class ExpensesAddedUp {

    private Category category;
    private double total;

    public ExpensesAddedUp() {
        setCategory(new Category());
        setTotal(0.0);
    }

    public ExpensesAddedUp(Category category, double total)
    {
        setCategory(category);
        setTotal(total);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
