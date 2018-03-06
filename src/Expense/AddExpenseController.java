package Expense;

import Business.AlertHelper;
import Business.Constants;
import Business.UserProperties;
import Models.Category;
import Repositories.CategoryRepository;
import Repositories.ExpenseRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class AddExpenseController implements Initializable {

    @FXML
    public JFXTextField amountTxt;

    @FXML
    public ComboBox categoryDropdown;

    @FXML
    public JFXDatePicker expenseDatePicker;

    @FXML
    public JFXButton submitBtn, cancelBtn;

    private CategoryRepository categoryRepository = new CategoryRepository();
    private ExpenseRepository expenseRepository = new ExpenseRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadChoiceBox();
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
    public void cancelBtnAction()
    {
        Stage stage = (Stage)cancelBtn.getScene().getWindow();
        stage.close();
    }

}
