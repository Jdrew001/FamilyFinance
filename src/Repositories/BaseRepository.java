package Repositories;


import DBContext.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseRepository {

    protected Connection conn = null;
    protected CallableStatement statement = null;
    protected ResultSet result = null;

    public enum procedureType {
        create, read, update, delete;
    }

    protected void performCreate(String s, String params[]) {
        try {

            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall(s);
            for(int i = 0; i < params.length; i++)
            {
                statement.setString(i+=1, params[i]);
            }

            statement.execute();

        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                close(conn, statement, result);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    protected ResultSet performReadById(String s, int id) {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall(s);
            statement.setInt(1, id);
            statement.execute();
            result = statement.getResultSet();

            return result;
        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                close(conn, statement, result);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    protected ResultSet performUserCheck(String s, String username, String password)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall(s);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.execute();
            result = statement.getResultSet();

            return result;

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    protected boolean updateById(String s, int id, String params[])
    {
        try {

            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall(s);
            for(int i = 0; i < 0; i++) {
                statement.setString(i++ + 1, params[i++]);
            }
            statement.setInt(params.length + 1, id);
            statement.execute();

        } catch(SQLException ex) {
            ex.printStackTrace();

            return false;
        } finally {
            try {
                close(conn, statement, result);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        return true;
    }

    protected boolean deleteById(String s, int id)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall(s);
            statement.setInt(1, id);
            statement.execute();
        } catch(SQLException ex) {
            ex.printStackTrace();

            return false;
        } finally {
            try {
                close(conn, statement, result);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        return true;
    }

    protected void close(Connection conn, CallableStatement statement, ResultSet result) throws SQLException {
        if(conn != null)
            conn.close();

        if(statement != null)
            statement.close();
    }
}
