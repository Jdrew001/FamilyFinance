package DBContext;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection dbConnection()
    {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/FamilyFinance", "dtatkison", "Onegodchurch1");
            //conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/FamilyFinance", "root", "Onegodchurch1");
        } catch(Exception ex) {
            ex.printStackTrace();

        }

        return conn;
    }

}
