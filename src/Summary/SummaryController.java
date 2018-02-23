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

    @FXML
    JFXButton logoutBtn, incomeBtn, expensesBtn, journalBtn, categoriesBtn, summaryBtn, refreshBtn, addIncome, removeIncome, addExpense, removeExpense, addCategory, removeCategory;

    @FXML
    JFXDatePicker incomeDatePicker, expenseDatePicker;

    @FXML
    TableView financeTable, expenseTable;

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

        } else if (e.getSource() == incomeBtn) {
            financePane.toFront(); // income pane
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
        if (e.getSource().equals(incomeDatePicker)) {
            financeTable.getColumns().clear();
            Date date = Date.from(incomeDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            incomeController.initializeTable(financeTable, incomeController.loadIncome(date));

        } else if (e.getSource().equals(expenseDatePicker)) {
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
    public void clickIncomeTableItem(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Income.class.cast(financeTable.getSelectionModel().getSelectedItem());
        } else if (event.getClickCount() == 2) {
            handleDeletionIncome();
        }
    }

    @FXML
    private void addNewIncome(ActionEvent e) {
        if (e.getSource().equals(addIncome)) {
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.showPrompt("../Income/AddIncome.fxml", "Add Income");
        }
    }

    @FXML
    private void addNewExpense(ActionEvent e) {
        if(e.getSource().equals(addExpense)) {
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.showPrompt("../Expense/AddExpense.fxml", "Add Expense");
        }
    }

    @FXML
    private void removeIncomeEntry(ActionEvent e)
    {
        if(e.getSource().equals(removeIncome)) {
            if(financeTable.getSelectionModel().getSelectedItem() != null)
                handleDeletionIncome();
        }
    }

    @FXML
    public void addNewCategory(ActionEvent e) {
        if(e.getSource().equals(addCategory))
        {
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.showPrompt("../Category/AddCategory.fxml", "Add Category");
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

    private void handleDeletionIncome()
    {
        if(AlertHelper.showConfirmationDialog("Delete Confirmation",null, "Are you sure you want to delete this entry?"))
        {
            // Remove income and update table
            incomeController.removeIncome(Income.class.cast(financeTable.getSelectionModel().getSelectedItem()).getIdIncome());
            financeTable.getColumns().clear();
            Date date = Date.from(incomeDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            incomeController.initializeTable(financeTable, incomeController.loadIncome(date));
        }
    }
}
