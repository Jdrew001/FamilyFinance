package Summary;

import Business.SceneChanger;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class SummaryController implements Initializable {

    @FXML
    JFXButton logoutBtn;
    @FXML
    JFXButton incomeBtn;
    @FXML
    JFXButton expensesBtn;
    @FXML
    JFXButton journalBtn;
    @FXML
    JFXButton categoriesBtn;
    @FXML
    Label beginningBalLbl;
    @FXML
    Label totalIncomeLbl;
    @FXML
    Label totalExpenseLbl;
    @FXML
    Label totalSavingsLbl;
    @FXML
    Label cashBalLbl;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void logout() {
        SceneChanger s = new SceneChanger();
        s.changeScene(logoutBtn, "../login/login.fxml", "Login");
    }
}
