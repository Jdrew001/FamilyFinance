package Income;

import Business.Constants;
import Business.UserProperties;
import Category.CategoryController;
import Models.Category;
import Models.Income;
import Repositories.CategoryRepository;
import Repositories.IncomeRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class IncomeController implements Initializable {

    public ObservableList<Income> incomes =  FXCollections.observableArrayList();
    IncomeRepository incomeRepository = new IncomeRepository();
    CategoryRepository categoryRepository = new CategoryRepository();
    double totalIncomeAmount = 0.0;

    //Input fields for new income prompt
    @FXML
    JFXTextField amountTxt;
    @FXML
    ComboBox categoryDropdown;
    @FXML
    JFXDatePicker incomeDatePicker;
    @FXML
    JFXButton submitBtn, cancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadChoiceBox(); //not going to work
    }

    public double getIncomeAmount()
    {
        incomes = incomeRepository.getIncomeByMonth(new Date());
        System.out.println(new Date());

        for(Income income : incomes)
        {
            totalIncomeAmount += income.getAmount();
        }

        return totalIncomeAmount;
    }

    public ObservableList<Income> loadIncome()
    {
        incomes = incomeRepository.getIncomeByMonth(new Date());

        return incomes;
    }

    public ObservableList<Income> loadIncome(Date date) {
        incomes.clear();
        incomes = incomeRepository.getIncomeByMonth(date);
        return incomes;
    }

    public void initializeTable(TableView table, ObservableList<Income> incomes)
    {
        table.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
            @Override
            public Boolean call(TableView.ResizeFeatures param) {
                return true;
            }
        });
        table.setEditable(false);
        TableColumn<Income, Number> amountCol = new TableColumn<>("amount");
        amountCol.setMinWidth(200);
        amountCol.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getAmount()));

        amountCol.setCellFactory(tc -> new TableCell<Income, Number>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if(empty) {
                    setText(null);
                } else {
                    DecimalFormat decimalFormat = new DecimalFormat("#.00");
                    setText("$ "+decimalFormat.format(value.doubleValue()));
                }
            }
        });

        TableColumn date = new TableColumn("Date");
        date.setMinWidth(200);
        date.setCellValueFactory(new PropertyValueFactory<Income, Date>("date"));
        TableColumn username = new TableColumn("Added By");
        username.setCellValueFactory(new PropertyValueFactory<Income, String>("username"));
        username.setMinWidth(200);
        TableColumn category = new TableColumn("Category");
        category.setCellValueFactory(new PropertyValueFactory<Income, String>("categoryName"));
        category.setMinWidth(200);
        TableColumn transaction = new TableColumn("Transaction");
        transaction.setCellValueFactory(new PropertyValueFactory<Income, String>("transactionName"));
        transaction.setMinWidth(200);


        table.setItems(incomes);
        table.getColumns().addAll(amountCol, date, username, category, transaction);
    }

    //load choice box
    public void loadChoiceBox()
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

    //handle table click events
    public void removeIncome(int id)
    {
        incomeRepository.deleteIncome(id);
    }

    @FXML
    public void submitBtnAction(ActionEvent e)
    {
        if(e.getSource().equals(submitBtn))
        {
            //perform action of adding income
            Category cat = (Category) categoryDropdown.getSelectionModel().getSelectedItem();

            if(incomeRepository.addincome(Double.parseDouble(amountTxt.getText()), cat.getId(),
                    Date.from(incomeDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), UserProperties.userId, Constants.credit))
            {
                submitBtn.getScene().getWindow().hide();
            } else {
                System.out.println("Show error here!!!"); //TODO CREATE VALIDATION HERE
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
