package Repositories;

import Models.BudgetItems;
import Business.AlertHelper;
import DBContext.DatabaseConnection;
import Models.Budget;
import Models.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Date;

public class BudgetRepository extends BaseRepository {

    ObservableList<Budget> budgets = FXCollections.observableArrayList();
    ObservableList<BudgetItems> budgetItems = FXCollections.observableArrayList();

    //get all budgets
    public ObservableList<Budget> getAllBudgets() {

        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{call get_all_budgets()}");
            statement.execute();
            result = statement.getResultSet();

            setBudget();

            try {
                close(conn, statement, result);
            } catch (Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }

            return budgets;

        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    //get budget from month
    public Budget getBudgetFromMonth(Date date)
    {
        Budget budget = null;

        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_budget_from_month(?) }");
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.execute();
            result = statement.getResultSet();

            if(result.first())
            {
                budget = new Budget(result.getInt("idBudget"), result.getDate("date"));
            }
            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }

            return budget;

        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    //create budget
    public boolean createBudget(Date date)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call create_budget(?) }");
            statement.setDate(1, new java.sql.Date(date.getTime()));

            statement.execute();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }
        } catch(SQLException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    //delete budget
    public boolean deleteBudget(int id)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call delete_budget(?) }");
            statement.setInt(1, id);
            statement.execute();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();

            return false;
        }

        return true;
    }

    //get budget items for month
    public ObservableList<BudgetItems> getBudgetItemsForMonth(Date date)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_budget_items_for_month(?)}");
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.execute();
            result = statement.getResultSet();

            setBudgetItem();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
            }

            return budgetItems;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    //get budget categories in month
    public ObservableList<BudgetItems> getBudgetCategoriesForMonth(Date date) {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_budget_categories_for_month(?)}");
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.execute();
            result = statement.getResultSet();

            setBudgetCategoryItem();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
            }

            return budgetItems;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    //get budget item by id
    public Budget getBudgetItemById(int id)
    {
        Budget budget = null;
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_budget_item_by_id(?) }");
            statement.setInt(1, id);
            statement.execute();
            result = statement.getResultSet();

            if(result.first())
            {
                budget = new Budget(result.getInt("id"), result.getInt("idBudget"), result.getDate("date"),
                        result.getDouble("amount"),
                        new Category(result.getInt("idcategory"), result.getString("categoryname")));
            }

            try {
                close(conn, statement, result);
            } catch (Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }

            return budget;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    //create budget item
    public boolean createBudgetItem(double amount, int idBudget, int idCategory)
    {
        try {
            conn =  DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call create_budget_item(?,?,?)}");
            statement.setDouble(1, amount);
            statement.setInt(2, idBudget);
            statement.setInt(3, idCategory);
            statement.execute();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }
        } catch(SQLException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    //update budget item
    public boolean updateBudgetItem(double amount, int idCategory, int id)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call update_budget_item(?,?,?) }");
            statement.setDouble(1, amount);
            statement.setInt(2, idCategory);
            statement.setInt(3, id);
            statement.execute();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();

            return false;
        }

        return true;
    }

    //delete budget item
    public boolean deleteBudgetItem(int id)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call delete_budget_item(?) }");
            statement.setInt(1, id);
            statement.execute();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
             return false;
        }

        return true;
    }

    private void setBudget()
    {
        try {
            while(result.next())
            {
                budgets.add(new Budget(result.getInt("idBudget"), result.getDate("date")));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void setBudgetItem()
    {
        try {
            while(result.next())
            {
                budgetItems.add(new BudgetItems(result.getInt("id"), result.getInt("idBudget"),
                        result.getDate("date"), result.getDouble("amount"), result.getString("budgetType"),
                        new Category(result.getInt("idcategory"), result.getString("categoryname"))));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void setBudgetCategoryItem()
    {
        try {
            while(result.next())
            {
                budgetItems.add(new BudgetItems(result.getInt("idBudget"), result.getDouble("amount"),
                        new Category(result.getInt("idcategory"), result.getString("categoryname"))));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
