package DBContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection dbConnection() {
        Connection conn = null;
        Properties properties = new Properties();
        InputStream input = null;

        try {
            input = DatabaseConnection.class.getResourceAsStream("/Resources/config.properties");
            properties.load(input);

            conn = DriverManager.getConnection(properties.getProperty("localUrl"), properties.getProperty("user"), properties.getProperty("password"));
            //conn = DriverManager.getConnection(properties.getProperty("ProductionDatabase"), properties.getProperty("user"), properties.getProperty("password"));
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(input != null) {
                try {
                    input.close();
                } catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return conn;
    }

}
