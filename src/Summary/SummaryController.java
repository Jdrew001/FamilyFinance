package Summary;

import Business.SceneChanger;
import Models.Income;
import Models.User;
import Income.IncomeController;
import Repositories.IncomeRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class SummaryController implements Initializable {

    //load in all controllers
    IncomeController incomeController = new IncomeController();

    @FXML
    JFXButton logoutBtn, incomeBtn, expensesBtn, journalBtn, categoriesBtn, summaryBtn, refreshBtn;

    @FXML
    JFXDatePicker incomeDatePicker, expenseDatePicker;

    @FXML
    TableView financeTable;

    @FXML
    Label beginningBalLbl, totalIncomeLbl, totalExpenseLbl, totalSavingsLbl, cashBalLbl;

    //panes
    @FXML
    AnchorPane summaryPane, financePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IncomeRepository incomeRepository = new IncomeRepository();
        incomeAmount();
    }

    public void logout() {
        SceneChanger s = new SceneChanger();
        s.changeScene(logoutBtn, "../login/login.fxml", "Login");
    }

    //Changing the view panes -- NAVIGATION
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

    //wire up the datepicker
    @FXML
    public void handleDatePicker(ActionEvent e)
    {
        if(e.getSource().equals(incomeDatePicker))
        {
            financeTable.getColumns().clear();
            Date date = Date.from(incomeDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            incomeController.initializeTable(financeTable, incomeController.loadIncome(date));

        } else if(e.getSource().equals(expenseDatePicker)) {

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
