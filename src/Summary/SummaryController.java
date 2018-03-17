package Summary;

import Budget.BudgetController;
import Business.AlertHelper;
import Business.SceneChanger;
import Category.CategoryController;
import Expense.ExpenseController;
import JournalEntries.JournalEntriesController;
import Models.Income;
import Models.User;
import Income.IncomeController;
import Repositories.IncomeRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class SummaryController implements Initializable {

    //load in all controllers
    IncomeController incomeController = new IncomeController();
    ExpenseController expenseController = new ExpenseController();
    CategoryController categoryController = new CategoryController();
    SceneChanger sceneChanger = new SceneChanger();

    @FXML
    JFXButton logoutBtn, incomeBtn, expensesBtn, journalBtn, budgetGoalsBtn, categoriesBtn, summaryBtn, refreshBtn;

    @FXML
    Label beginningBalLbl, totalIncomeLbl, totalExpenseLbl, totalSavingsLbl, cashBalLbl;

    //panes
    @FXML
    AnchorPane summaryPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCashBalance();
    }

    private void setCashBalance()
    {
        double total = 0;
        total = incomeAmount() - expenseAmount();

        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        if (total > 0)
        {
            cashBalLbl.setTextFill(Color.GREEN);
            cashBalLbl.setText("$" + decimalFormat.format(total));
        }
        else
        {
            cashBalLbl.setTextFill(Color.RED);
            cashBalLbl.setText("($" + decimalFormat.format(total) + ")");
        }
    }

    public void logout() {
        SceneChanger s = new SceneChanger();
        s.changeScene(logoutBtn, "/login/login.fxml", "Login");
    }

    @FXML
    public void refreshData()
    {
        setCashBalance();
    }

    //Changing the view panes -- NAVIGATION
    @FXML
    public void handleButtonAction(ActionEvent e) {
        System.out.println(e.getSource());
        if (e.getSource() == summaryBtn) {
            summaryPane.toFront();
        } else if(e.getSource() == incomeBtn) {
            sceneChanger.showPrompt(IncomeController.class.getResource("Income.fxml"), "Income", incomeBtn);
        } else if (e.getSource() == categoriesBtn) {
            sceneChanger.showPrompt(CategoryController.class.getResource("Category.fxml"), "Categories", categoriesBtn);
        } else if(e.getSource() == expensesBtn) {
            sceneChanger.showPrompt(ExpenseController.class.getResource("Expense.fxml"), "Expense", expensesBtn);
        } else if(e.getSource() == journalBtn) {
            sceneChanger.showPrompt(JournalEntriesController.class.getResource("JournalEntries.fxml"), "Journal Entries", journalBtn);
        } else if(e.getSource() == budgetGoalsBtn) {
            sceneChanger.showPrompt(BudgetController.class.getResource("Budget.fxml"), "Budget Goals", budgetGoalsBtn);
        }
    }

    private double incomeAmount() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double totalIncomeAmount = incomeController.getIncomeAmount();

        if (totalIncomeAmount > 0)
            totalIncomeLbl.setTextFill(Color.GREEN);
        else
            totalIncomeLbl.setTextFill(Color.RED);

        totalIncomeLbl.setText("$" + decimalFormat.format(totalIncomeAmount));

        return totalIncomeAmount;
    }

    private double expenseAmount() {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double totalExpenseAmount = expenseController.getExpenseAmount();

        if(totalExpenseAmount > 0)
        {
            totalExpenseLbl.setTextFill(Color.RED);
            totalExpenseLbl.setText("($" + decimalFormat.format(totalExpenseAmount) + ")");
        }
        else
        {
            totalExpenseLbl.setTextFill(Color.GREEN);
            totalExpenseLbl.setText("$" + decimalFormat.format(totalExpenseAmount));
        }

        return totalExpenseAmount;
    }
}
