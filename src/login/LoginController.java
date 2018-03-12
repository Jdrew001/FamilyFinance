package login;

import Business.AlertHelper;
import DBContext.DatabaseConnection;
import Repositories.UserRepository;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements Initializable {

    @FXML
    public JFXButton loginBtn;

    @FXML
    public TextField usernameTxt;

    @FXML
    public PasswordField passwordTxt;

    UserRepository userRepository = new UserRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DatabaseConnection.dbConnection();
    }

    @FXML
    public void login()
    {
        if(userRepository.login(usernameTxt.getText(), passwordTxt.getText()))
        {
            try {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Summary/Summary.fxml"));
                /*
                 * if "fx:controller" is not set in fxml
                 * fxmlLoader.setController(NewWindowController);
                 */
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Main");
                stage.setScene(scene);
                stage.show();

                loginBtn.getScene().getWindow().hide();
            } catch(IOException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.log(Level.SEVERE, "Failed to create new Window.", e);

            }
        } else {
            //TODO show alert dialog showing it didn't work
            AlertHelper.showErrorDialog("Login Failed", null, "Please try to login again. An Error occurred");
        }

    }
}
