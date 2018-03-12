package JournalEntries;

import Business.AlertHelper;
import Business.Constants;
import Business.UserProperties;
import Models.Category;
import Repositories.CategoryRepository;
import Repositories.ExpenseRepository;
import Repositories.IncomeRepository;
import Repositories.JournalRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.print.DocFlavor;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class AddJournalController implements Initializable {

    @FXML
    public JFXTextField amountTxt, descriptionTxt;

    @FXML
    public ComboBox categoryDropdown, transactionType;

    @FXML
    public JFXDatePicker transactionDatePicker;

    @FXML
    public JFXButton submitBtn, cancelBtn;

    private JournalRepository journalRepository = new JournalRepository();
    private CategoryRepository categoryRepository = new CategoryRepository();
    private IncomeRepository incomeRepository = new IncomeRepository();
    private ExpenseRepository expenseRepository = new ExpenseRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Init category dropdown
        loadCategoryDropdown();
        loadTransactionDropdown();
    }

    //load category dropdown method
    private void loadCategoryDropdown()
    {
        //load in all categories
        categoryDropdown.setItems(categoryRepository.getAllCategories());
        categoryDropdown.setButtonCell(new ListCell<Category>() {
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null) {
                    setText(item.getId() +" " + item.getName());
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
                        if(item != null) {
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

    //load transactionTypeDropdown method
    private void loadTransactionDropdown()
    {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll("Debit", "Credit");
        transactionType.setItems(list);
    }

    //submit button event
    @FXML
    public void submitBtnPressed(ActionEvent event)
    {
        if(event.getSource() == submitBtn)
        {
            Category cat = (Category) categoryDropdown.getSelectionModel().getSelectedItem();
            String transaction = String.valueOf(transactionType.getSelectionModel().getSelectedItem());
            int transactionTypeId = 0;

            if(amountTxt.getText().isEmpty() || cat == null || descriptionTxt.getText().isEmpty() || transaction == null || transactionDatePicker.getValue() == null)
            {
                AlertHelper.showErrorDialog("Form Error", null, "Please ensure all information is typed in");
            } else {
                //submit to database
                if(transaction == "Debit")
                    transactionTypeId = Constants.debit;
                else
                    transactionTypeId = Constants.credit;

                if(journalRepository.addJournalEntry(Date.from(transactionDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        cat.getId(), descriptionTxt.getText(), Double.parseDouble(amountTxt.getText()), transactionTypeId))
                {
                    submitBtn.getScene().getWindow().hide();
                    if(transactionTypeId == Constants.debit)
                    {
                        // add expense
                        if(!expenseRepository.addExpense(Double.parseDouble(amountTxt.getText()), cat.getId(),
                                Date.from(transactionDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), UserProperties.userId, Constants.debit))
                            showError();
                    } else {
                        // add income
                        if(!incomeRepository.addincome(Double.parseDouble(amountTxt.getText()), cat.getId(),
                                Date.from(transactionDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), UserProperties.userId, Constants.credit))
                            showError();
                    }
                } else {
                    showError();
                }
            }
        }
    }

    //cancel button event
    @FXML
    public void cancelBtnPressed(ActionEvent event)
    {
        if(event.getSource() == cancelBtn)
        {
            Stage stage = (Stage)cancelBtn.getScene().getWindow();
            stage.close();
        }
    }

    private void showError()
    {
        AlertHelper.showErrorDialog("Unknown Error", null, "An unknown error has occurred. Be sure you are connected to internet");
    }

}
