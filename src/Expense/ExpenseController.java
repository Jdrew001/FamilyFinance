package Expense;

import Business.AlertHelper;
import Business.Constants;
import Business.UserProperties;
import Models.Category;
import Models.Expense;
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
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class ExpenseController implements Initializable {

    ExpenseRepository expenseRepository = new ExpenseRepository();
    public ObservableList<Expense> expenses = FXCollections.observableArrayList();
    CategoryRepository categoryRepository = new CategoryRepository();
    double totalExpenseAmount = 0.0;

    @FXML
    public JFXTextField amountTxt;

    @FXML
    public ComboBox categoryDropdown;

    @FXML
    public JFXDatePicker expenseDatePicker;

    @FXML
    public JFXButton submitBtn, cancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadChoiceBox();
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

        for(int i = 0; i < 2; i++)
        {
            System.out.println(expenses.get(i).getUser().getUsername());
        }

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

    public void loadChoiceBox() {
        //load in all the categories
        categoryDropdown.setItems(categoryRepository.getAllCategories());
        categoryDropdown.setButtonCell(new ListCell<Category>() {
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getId() + " " + item.getName());
                } else {
                    setText(null);
                }
            }
        });
        categoryDropdown.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>() {
            @Override
            public ListCell call(ListView<Category> p) {

                final ListCell<Category> cell = new ListCell<Category>() {
                    @Override
                    protected void updateItem(Category item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        } else {
                            setText(null);
                        }
                    }
                };

                return cell;
            }
        });
    }

    public ObservableList<Expense> loadExpense() {
        expenses = expenseRepository.getExpensesByMonth(new Date());

        return expenses;
    }

    public ObservableList<Expense> loadExpense(Date date) {
        expenses.clear();
        expenses = expenseRepository.getExpensesByMonth(date);
        return expenses;
    }

    @FXML
    public void submitBtnAction(ActionEvent e) {
        if(e.getSource().equals(submitBtn))
        {
            //perform action of adding income
            Category cat = (Category) categoryDropdown.getSelectionModel().getSelectedItem();

            if(amountTxt.getText().isEmpty() || expenseDatePicker.getValue() == null || cat == null)
            {
                AlertHelper.showErrorDialog("Form Error", null, "Please ensure all information is typed in");
            } else {
                if(expenseRepository.addExpense(Double.parseDouble(amountTxt.getText()), cat.getId(),
                        Date.from(expenseDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), UserProperties.userId, Constants.credit))
                {
                    submitBtn.getScene().getWindow().hide();
                } else {
                    AlertHelper.showErrorDialog("Unknown Error", null, "An unknown error has occurred. Be sure you are connected to internet");
                }
            }
        }
    }

    @FXML
    public void canceBtnAction(ActionEvent e)
    {
        Stage stage = (Stage)cancelBtn.getScene().getWindow();
        stage.close();
    }
}
