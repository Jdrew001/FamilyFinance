package Repositories;

import Business.Constants;
import DBContext.DatabaseConnection;
import Models.Category;
import Models.Income;
import Models.TransactionType;
import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;
import java.sql.SQLException;

public class IncomeRepository extends BaseRepository {

    ObservableList<Income> incomes = FXCollections.observableArrayList();
    public ObservableList<Income> getAllIncome()
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_all_income()}");
            statement.execute();
            result = statement.getResultSet();

            while(result.next())
            {
                User user = new User(result.getInt(Constants.idUser), result.getString(Constants.firstname), result.getString(Constants.lastname), result.getString(Constants.username));
                Category c = new Category(result.getInt(Constants.idCategory), result.getString(Constants.categoryName));
                TransactionType t = new TransactionType(result.getInt(Constants.idTransactionType), result.getString(Constants.transactionName));

                incomes.add(new Income(result.getInt(Constants.idIncome), result.getInt(Constants.amount),
                        result.getDate(Constants.date), user, c, t));
            }

            try {
                close(conn, statement, result);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return incomes;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Income getIncomeById(int id)
    {
        Income income = null;
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_income_by_id(?)}");
            statement.setInt(1, id);
            statement.execute();
            result = statement.getResultSet();

            if(result.first())
            {
                User user = new User(result.getInt(Constants.idUser), result.getString(Constants.firstname), result.getString(Constants.lastname), result.getString(Constants.username));
                Category c = new Category(result.getInt(Constants.idCategory), result.getString(Constants.categoryName));
                TransactionType t = new TransactionType(result.getInt(Constants.idTransactionType), result.getString(Constants.transactionName));

                income = new Income(result.getInt(Constants.idIncome), result.getDouble(Constants.amount),
                        result.getDate(Constants.date), user, c, t);
            }

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
            }

            return income;
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    //income by month
    public ObservableList<Income> getIncomeByMonth(Date date)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_income_by_month(?)}");
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.execute();
            result = statement.getResultSet();

            while(result.next())
            {
                User user = new User(result.getInt(Constants.idUser), result.getString(Constants.firstname), result.getString(Constants.lastname), result.getString(Constants.username));
                Category c = new Category(result.getInt(Constants.idCategory), result.getString(Constants.categoryName));
                TransactionType t = new TransactionType(result.getInt(Constants.idTransactionType), result.getString(Constants.transactionName));

                incomes.add(new Income(result.getInt(Constants.idIncome), result.getDouble(Constants.amount),
                        result.getDate(Constants.date), user, c, t));
            }

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
            }

            return incomes;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateIncome(double amount, int categoryId, Date date, int userId, int transId, int incomeId)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call update_income(?,?,?,?,?,?) }");
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
            }
        } catch(SQLException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    public boolean deleteIncome(int id)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call delete_income(?) }");
            statement.setInt(1, id);
            statement.execute();

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch(SQLException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }
}
