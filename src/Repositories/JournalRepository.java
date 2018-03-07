package Repositories;

import Business.AlertHelper;
import Business.Constants;
import DBContext.DatabaseConnection;
import Models.JournalEntries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Date;

public class JournalRepository extends BaseRepository {

    ObservableList<JournalEntries> journalEntries = FXCollections.observableArrayList();

    //get all journal entries
    public ObservableList<JournalEntries> getAllJournalEntries()
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_all_from_journal() }");
            statement.execute();
            result = statement.getResultSet();

            setJournalEntries();

            try {
                close(conn, statement, result);
            } catch(Exception ex) {
                ex.printStackTrace();
            }

            return journalEntries;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //get all by month

    public ObservableList<JournalEntries> getJournalEntriesByMonth(Date date) {
        try {

            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ get_journal_from_month(?) }");
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.execute();
            result =  statement.getResultSet();

            setJournalEntries();

            try {
                close(conn, statement, result);
            } catch(Exception ex) {
                ex.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in journal", ex);
            }

            return journalEntries;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    //get one by id
    public JournalEntries getJournalEntryById(int id)
    {
        JournalEntries journalEntries = null;
        try {

            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ get_from_journal_by_id(?) }");
            statement.setInt(1, id);
            statement.execute();

            result = statement.getResultSet();

            if(result.first())
            {
                journalEntries = new JournalEntries(result.getInt(Constants.idJournal),
                        result.getDate(Constants.date), result.getDouble(Constants.amount), result.getString(Constants.categoryName),
                        result.getString(Constants.transactionName));
            }

            try {
                close(conn, statement, result);
            } catch(Exception ex) {
                ex.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in journal", ex);
            }

            return journalEntries;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //update journal entry
    //TODO ADD LATER -- MAY NOT NEED

    //add journal entry
    public boolean addJournalEntry(Date date, int categoryId, String description, double amount, int transactionId) //TODO CHANGE STORED PROC FOR AMOUNT TO BE DOUBLE... CHANGE WHOLE TABLE
    {
        try {

            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("create_journal(?,?,?,?,?)");
            statement.setDate(1, new java.sql.Date(date.getTime()));
            statement.setInt(2, categoryId);
            statement.setString(3, description);
            statement.setDouble(4, amount);
            statement.setInt(5, transactionId);

            statement.execute();

            try {
                close(conn, statement, result);
            } catch(Exception ex) {
                ex.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in journal", ex);
            }
        } catch(SQLException e) {
            e.printStackTrace();

            return false;
        }
        return true;
    }

    //delete journal entry
    public boolean deleteJournalEntry(int id)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ delete_journal(?) }");
            statement.setInt(1, id);
            statement.execute();


            try {
                close(conn, statement, result);
            } catch(Exception ex) {
                ex.printStackTrace();
                AlertHelper.showExceptionDialog("Exception", null, "Error in journal", ex);
            }
        } catch(SQLException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    private void setJournalEntries() throws SQLException {
        while(result.next())
        {
            journalEntries.add(new JournalEntries(result.getInt(Constants.idJournal),
                    result.getDate(Constants.date), result.getDouble(Constants.amount), result.getString(Constants.categoryName),
                    result.getString(Constants.transactionName)));
        }
    }
}