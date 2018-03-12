package Income;

import Business.AlertHelper;
import Business.Constants;
import Business.UserProperties;
import Category.UpdateCategory;
import Models.Category;
import Repositories.CategoryRepository;
import Repositories.IncomeRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class AddIncomeController implements Initializable {

    @FXML
    JFXTextField amountTxt;

    @FXML
    Label headerTxt;

    @FXML
    JFXDatePicker incomeDatePicker;

    @FXML
    JFXButton submitBtn, cancelBtn;

    @FXML
    ComboBox categoryDropdown;

    IncomeRepository incomeRepository = new IncomeRepository();
    CategoryRepository categoryRepository = new CategoryRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadChoiceBox();
    }

    //load choice box
    private void loadChoiceBox()
    {
        //load in all the categories
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

    @FXML
    public void submitBtnAction(ActionEvent e)
    {
        if(e.getSource().equals(submitBtn))
        {
            //perform action of adding income
            Category cat = (Category) categoryDropdown.getSelectionModel().getSelectedItem();

            if(amountTxt.getText().isEmpty() || incomeDatePicker.getValue() == null || cat == null)
            {
                AlertHelper.showErrorDialog("Form Error", null, "Please ensure all information is typed in");
            } else {
                try {
                    if(incomeRepository.addincome(Double.parseDouble(amountTxt.getText()), cat.getId(),
                            Date.from(incomeDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), UserProperties.userId, Constants.credit))
                    {
                        submitBtn.getScene().getWindow().hide();
                        UpdateIncome.newIncome = true;
                    } else {
                        AlertHelper.showErrorDialog("Unknown Error", null, "An unknown error has occurred. Be sure you are connected to internet");
                    }
                } catch(Exception ex) {
                    AlertHelper.showErrorDialog("Form Error", null, "Please ensure that only correct data is entered");
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
