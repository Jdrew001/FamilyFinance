package Repositories;

import Business.Constants;
import DBContext.DatabaseConnection;
import Models.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class CategoryRepository extends BaseRepository {

    ObservableList<Category> categories = FXCollections.observableArrayList();

    //get all categories
    public  ObservableList<Category> getAllCategories()
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_all_categories() }");
            statement.execute();

            result = statement.getResultSet();

            while(result.next())
            {
                Category category = new Category(result.getInt(Constants.idCategory), result.getString(Constants.categoryName));

                categories.add(category);
            }

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
            }

            return categories;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //get category by id
    public Category getCategoryById(int id)
    {
        Category category = null;
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call get_category_by_id(?)}");
            statement.setInt(1, id);
            statement.execute();

            result = statement.getResultSet();

            if(result.first())
            {
                category = new Category(result.getInt(Constants.idCategory), result.getString(Constants.categoryName));
            }

            try {
                close(conn, statement, result);
            } catch(Exception e) {
                e.printStackTrace();
            }

            return category;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Add category
    public boolean addCategory(String name) {
        try {

            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call create_category(?) }");
            statement.setString(1, name);
            statement.execute();

            try {
                close(conn, statement, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch(SQLException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    //update category
    public boolean updateCategory(int idIncome, String name)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall("{ call update_category(?,?) }");
            statement.setString(1, name);
            statement.setInt(2, idIncome);

            statement.execute();

            try {
                close(conn, statement, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch(SQLException e) {
            e.printStackTrace();

            return false;
        }

        return true;
    }

    //delete Category
    public boolean deleteCategory(int idIncome)
    {
        try {
            conn = DatabaseConnection.dbConnection();
            statement = conn.prepareCall(" {call delete_category(?)} ");
            statement.setInt(1, idIncome);
            statement.execute();

        } catch(SQLException e) {
            e.printStackTrace();
        }

        try {
            close(conn, statement, result);
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
        return true;
    }
}
