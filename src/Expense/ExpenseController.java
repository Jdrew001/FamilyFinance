package Expense;

import Business.AlertHelper;
import Business.Constants;
import Business.SceneChanger;
import Business.UserProperties;
import Models.Category;
import Models.Expense;
import Models.Income;
import javafx.event.ActionEvent;
import Repositories.CategoryRepository;
import Repositories.ExpenseRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.reactfx.util.FxTimer;
import org.reactfx.util.Timer;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class ExpenseController implements Initializable {

    //FXML components
    @FXML
    public JFXButton addExpense, removeExpense;

    @FXML
    TableView expenseTable;
    @FXML
    JFXDatePicker expenseDatePicker;

    ExpenseRepository expenseRepository = new ExpenseRepository();
    public ObservableList<Expense> expenses = FXCollections.observableArrayList();
    double totalExpenseAmount = 0.0;
    private Timer timer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable(expenseTable, loadExpense(new Date()));
    }

    public double getExpenseAmount()
    {
        totalExpenseAmount = 0;
        expenses.clear();
        expenses = expenseRepository.getExpensesByMonth(new Date());

        for(Expense expense : expenses)
        {
            totalExpenseAmount += expense.getAmount();
        }

        return totalExpenseAmount;
    }

    public void initializeTable(TableView table, ObservableList<Expense> expenses) {
        table.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
            @Override
            public Boolean call(TableView.ResizeFeatures param) {
                return true;
            }
        });
        table.setEditable(false);
        TableColumn<Expense, Number> amountCol = new TableColumn<Expense, Number>("Amount");
        amountCol.setMinWidth(200);
        amountCol.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getAmount()));

        amountCol.setCellFactory(tc -> new TableCell<Expense, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    setText("$ " + decimalFormat.format(value.doubleValue()));
                }
            }
        });

        TableColumn date = new TableColumn("Date");
        date.setMinWidth(200);
        date.setCellValueFactory(new PropertyValueFactory<Expense, Date>("date"));
        TableColumn username = new TableColumn("Added By");
        username.setCellValueFactory(new PropertyValueFactory<Expense, String>("username"));
        username.setMinWidth(200);
        TableColumn category = new TableColumn("Category");
        category.setCellValueFactory(new PropertyValueFactory<Expense, String>("categoryName"));
        category.setMinWidth(200);
        TableColumn transaction = new TableColumn("Transaction");
        transaction.setCellValueFactory(new PropertyValueFactory<Expense, String>("transactionName"));
        transaction.setMinWidth(200);

        table.setItems(expenses);
        table.getColumns().addAll(amountCol, date, username, category, transaction);

    }

    @FXML
    public void handleDatePicker(ActionEvent e) {

        if (e.getSource().equals(expenseDatePicker)) {
            expenseTable.getColumns().clear();
            Date date = Date.from(expenseDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            System.out.println(date);
            initializeTable(expenseTable, loadExpense(date));
        }
    }

    @FXML
    public void clickExpenseTableItem(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Income.class.cast(expenseTable.getSelectionModel().getSelectedItem());
        } else if (event.getClickCount() == 2) {
            handleDeletionIncome();
        }
    }

    @FXML
    private void addNewExpense(ActionEvent e) {
        if(e.getSource().equals(addExpense)) {
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.showPrompt("../Expense/AddExpense.fxml", "Add Expense", addExpense);

            refreshTableDynamically();
        }
    }

    @FXML
    private void removeExpenseEntry(ActionEvent e)
    {
        if(e.getSource().equals(removeExpense)) {
            if(expenseTable.getSelectionModel().getSelectedItem() != null)
                handleDeletionIncome();
        }
    }

    public ObservableList<Expense> loadExpense(Date date) {
        expenses.clear();
        System.out.println("Called");
        expenses = expenseRepository.getExpensesByMonth(date);
        System.out.println(expenses);
        return expenses;
    }

    //handle table click events
    public void removeIncome(int id)
    {
        expenseRepository.deleteExpense(id);
    }

    private void handleDeletionIncome()
    {
        if(AlertHelper.showConfirmationDialog("Delete Confirmation",null, "Are you sure you want to delete this entry?"))
        {
            // Remove income and update table
            removeIncome(Income.class.cast(expenseTable.getSelectionModel().getSelectedItem()).getIdIncome());
            expenseTable.getColumns().clear();
            Date date = Date.from(expenseDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            initializeTable(expenseTable, loadExpense(date));
        }
    }

    private void refreshTableDynamically() {
        //run the timer to make sure that once user is finished with the update, the table gets updated
        timer = FxTimer.runPeriodically(Duration.ofMillis(10), () -> {
            if(UpdateExpense.newExpense)
            {
                UpdateExpense.newExpense = false;
                FxTimer.runLater(Duration.ofSeconds(2), () -> loadExpense(new Date()));
                timer.stop();
            }
        });
    }
}
