package Summary;

import Business.SceneChanger;
import Models.Income;
import Models.User;
import Income.IncomeController;
import Repositories.IncomeRepository;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Date;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class SummaryController implements Initializable {

    //load in all controllers
    IncomeController incomeController = new IncomeController();

    @FXML
    JFXButton logoutBtn, incomeBtn, expensesBtn, journalBtn, categoriesBtn, summaryBtn, refreshBtn;

    @FXML
    TableView financeTable;

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

    //panes
    @FXML
    AnchorPane summaryPane, financePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IncomeRepository incomeRepository = new IncomeRepository();
        incomeAmount();
        incomeController.initializeTable(financeTable);
    }

    public void logout() {
        SceneChanger s = new SceneChanger();
        s.changeScene(logoutBtn, "../login/login.fxml", "Login");
    }

    //Changing the view panes
    @FXML
    public void handleButtonAction(ActionEvent e)
    {
        System.out.println(e.getSource());
        if(e.getSource() == summaryBtn)
        {
            summaryPane.toFront();

        } else if (e.getSource() == incomeBtn) {
            financePane.toFront();

        }
    }

    private void incomeAmount()
    {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double totalIncomeAmount = incomeController.getIncomeAmount();

        if(totalIncomeAmount > 0)
            totalIncomeLbl.setTextFill(Color.GREEN);
        else
            totalIncomeLbl.setTextFill(Color.RED);

        totalIncomeLbl.setText("$" + decimalFormat.format(totalIncomeAmount));
    }
}
