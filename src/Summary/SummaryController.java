package Summary;

import Business.AlertHelper;
import Business.SceneChanger;
import Category.CategoryController;
import Expense.ExpenseController;
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
    JFXButton logoutBtn, incomeBtn, expensesBtn, journalBtn, categoriesBtn, summaryBtn, refreshBtn, addExpense, removeExpense, addCategory, removeCategory;

    @FXML
    JFXDatePicker expenseDatePicker;

    @FXML
    TableView expenseTable;

    @FXML
    ListView categoryListView;

    @FXML
    Label beginningBalLbl, totalIncomeLbl, totalExpenseLbl, totalSavingsLbl, cashBalLbl;

    //panes
    @FXML
    AnchorPane summaryPane, financePane, categoryPane, expensePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IncomeRepository incomeRepository = new IncomeRepository();
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
        s.changeScene(logoutBtn, "../login/login.fxml", "Login");
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
            sceneChanger.showPrompt("../Income/income.fxml", "Income", incomeBtn);
        } else if (e.getSource() == categoriesBtn) {
            categoryPane.toFront();
            categoryController.loadItems(categoryListView);
        } else if(e.getSource() == expensesBtn) {
            expensePane.toFront();
        }
    }

    //wire up the datepicker
    @FXML
    public void handleDatePicker(ActionEvent e) {

        if (e.getSource().equals(expenseDatePicker)) {
            expenseTable.getColumns().clear();
            Date date = Date.from(expenseDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            System.out.println(date);
            expenseController.initializeTable(expenseTable, expenseController.loadExpense(date));
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

    @FXML
    public void clickCategoryListItem(MouseEvent event)
    {
        System.out.println("Working");
        if(event.getClickCount() == 2)
        {
            categoryController.updateCategory(categoryListView.getSelectionModel().getSelectedItems());
        }
    }

    @FXML
    private void addNewExpense(ActionEvent e) {
        if(e.getSource().equals(addExpense)) {
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.showPrompt("../Expense/AddExpense.fxml", "Add Expense", addExpense);
        }
    }

    @FXML
    public void addNewCategory(ActionEvent e) {
        if(e.getSource().equals(addCategory))
        {
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.showPrompt("../Category/AddCategory.fxml", "Add Category", categoriesBtn);
        }
    }

    @FXML
    public void removeCategory(ActionEvent e) {
        if(e.getSource().equals(removeCategory))
        {
            categoryController.removeItem(categoryListView.getSelectionModel().getSelectedItems());
            categoryController.loadItems(categoryListView);
        }
    }
}
