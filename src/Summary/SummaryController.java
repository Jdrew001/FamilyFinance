package Summary;

import Business.SceneChanger;
import Models.Income;
import Models.User;
import Repositories.IncomeRepository;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Date;
import java.text.DecimalFormat;
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

    public ObservableList<Income> incomes =  FXCollections.observableArrayList();
    double totalIncomeAmount = 0.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IncomeRepository incomeRepository = new IncomeRepository();
        System.out.println(incomeRepository.deleteIncome(5));
        incomeAmount();




    }

    public void logout() {
        SceneChanger s = new SceneChanger();
        s.changeScene(logoutBtn, "../login/login.fxml", "Login");
    }

    private void incomeAmount()
    {
        IncomeRepository incomeRepository = new IncomeRepository();
        incomes = incomeRepository.getIncomeByMonth(new Date());

        for(Income income : incomes)
        {
            totalIncomeAmount += income.getAmount();
        }

        System.out.println(totalIncomeAmount);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        if(totalIncomeAmount > 0)
            totalIncomeLbl.setTextFill(Color.GREEN);
        else
            totalIncomeLbl.setTextFill(Color.RED);

        totalIncomeLbl.setText("$" + decimalFormat.format(totalIncomeAmount));
    }
}
