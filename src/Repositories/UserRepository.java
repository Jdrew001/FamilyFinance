package Repositories;

import Business.UserProperties;
import Models.User;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends BaseRepository {

    public boolean login(String username, String password) {

        //I want to return true if the user's creditials matched a record in the databa
        ResultSet s = performUserCheck("{ call get_user_login(?, ?)}", username, password);
        try {
            if(s.first()) {
                //set the logged in user here
                UserProperties.userId = s.getInt("idUser");
                UserProperties.username = s.getString("username");
                return true;
            }
            else
                return false;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                close(conn, statement, s);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public User getLoggedInUser(int id)
    {
        ResultSet result = performReadById("{ call get_user_by_id(?) }", id);
        User user = null;
        try {
            if(result.next()) {
                user = new User(result.getInt("idUser"), result.getString("username"), result.getString("password"), result.getString("email"), result.getString("firstname"), result.getString("lastname"));
            }
            return user;
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try {

            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public boolean updateUser(int id, String username, String password, String email, String firstname, String lastname)
    {
        String[] a = {firstname, lastname, username, password, email};
        if(updateById("{ call update_user(?,?,?,?,?,?) }", id, a)) return true; else return false;
    }

    public boolean deleteUser(int id)
    {
        if(deleteById("{ call delete_user(?) }", id)) return true; else return false;
    }
}
