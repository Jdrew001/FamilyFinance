package Repositories;

import Business.AlertHelper;
import Business.Constants;
import DBContext.DatabaseConnection;
import Models.Category;
import Models.Expense;
import Models.TransactionType;
import Models.User;
import com.sun.corba.se.impl.orb.DataCollectorBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Date;


public class ExpenseRepository extends BaseRepository
{
    ObservableList<Expense> expenses = FXCollections.observableArrayList();
    ObservableList<ExpensesAddedUp> expensesAddedUps = FXCollections.observableArrayList();

    //get all expenses
    public ObservableList<Expense> getAllExpenses() {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_all_expense() }");
            result = statement.getResultSet();

            setExpense();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }

            return expenses;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    //get expense by id
    public Expense getExpenseById(int id)
    {
        Expense expense = null;
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ get_expense_by_id(?) }");
            statement .setInt(1, id);
            statement.execute();
            result = statement.getResultSet();

            if(result.first())
            {
                User user = new User(result.getInt(Constants.idUser), result.getString(Constants.firstname), result.getString(Constants.lastname), result.getString(Constants.username));
                Category c = new Category(result.getInt(Constants.idCategory), result.getString(Constants.categoryName));
                TransactionType t = new TransactionType(result.getInt(Constants.idTransactionType), result.getString(Constants.transactionName));

                expense = new Expense(result.getInt(Constants.idIncome), result.getDouble(Constants.amount),
                        result.getDate(Constants.date), c, t, user);
            }

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }

            return expense;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //get expenses by month
    public ObservableList<Expense> getExpensesByMonth(Date date) {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_expense_by_month(?) }");
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.execute();
            result = statement.getResultSet();

            setExpense();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }

            return expenses;

        } catch(SQLException e) {
            e.printStackTrace();
            AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
        }


        return null;
    }

    public boolean addExpense(double amount, int categoryId, Date date, int userId, int transId)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall(" { call add_expense(?,?,?,?,?) } ");
            statement.setDouble(1, amount);
            statement.setInt(2, categoryId);
            statement.setDate(3, new java.sql.Date(date.getTime()));
            statement.setInt(4, userId);
            statement.setInt(5, transId);

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

    //UPDATE EXPENSE
    public boolean updateExpense(double amount, int categoryId, Date date, int userId, int transId, int incomeId)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call update_expense(?,?,?,?,?,?) }");
            statement.setDouble(1, amount);
            statement.setInt(2, categoryId);
            statement.setDate(3, new java.sql.Date(date.getTime()));
            statement.setInt(4, userId);
            statement.setInt(5, transId);
            statement.setInt(6, incomeId);

            statement.execute();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in income", e);
            }
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    //delete
    public boolean deleteExpense(int id)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall(" { call delete_expense(?) } ");
            statement.setInt(1, id);
            statement.execute();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    //get added up expenses per category in a month
    public ObservableList<ExpensesAddedUp> addUpExpensesCategoriesMonthly(Date date) {

        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall(" { call add_up_categories_in_month(?) } ");
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.execute();
            result = statement.getResultSet();

            setAddedExpenses();


            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in Expense", e);
            }

            return expensesAddedUps;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setExpense() {

        try {
            while(result.next())
            {
                User user = new User(result.getInt(Constants.idUser), result.getString(Constants.firstname), result.getString(Constants.lastname), result.getString(Constants.username));
                Category c = new Category(result.getInt(Constants.idCategory), result.getString(Constants.categoryName));
                TransactionType t = new TransactionType(result.getInt(Constants.idTransactionType), result.getString(Constants.transactionName));

                expenses.add(new Expense(result.getInt(Constants.idExpense), result.getDouble(Constants.amount), result.getDate(Constants.date), c, t, user));
            }
        } catch(Exception e) {
            System.out.println(e);
        }

    }

    private void setAddedExpenses()
    {
        try {
            while(result.next())
            {
                expensesAddedUps.add(new ExpensesAddedUp(new Category(result.getInt("categoryid"), result.getString("categoryname")), result.getDouble("total")));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
