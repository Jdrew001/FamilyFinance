package Income;

import Business.AlertHelper;
import Business.Constants;
import Business.SceneChanger;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.reactfx.util.FxTimer;
import org.reactfx.util.Timer;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class IncomeController implements Initializable {

    public ObservableList<Income> incomes =  FXCollections.observableArrayList();
    IncomeRepository incomeRepository = new IncomeRepository();
    CategoryRepository categoryRepository = new CategoryRepository();
    double totalIncomeAmount = 0.0;
    private Timer timer;

    //Input fields for new income prompt
    @FXML
    TableView financeTable;
    @FXML
    JFXDatePicker incomeDatePicker;
    @FXML
    JFXButton addIncome, removeIncome;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable(financeTable, loadIncome(new Date()));
    }

    public double getIncomeAmount()
    {
        totalIncomeAmount = 0;
        incomes.clear();
        incomes = incomeRepository.getIncomeByMonth(new Date());

        for(Income income : incomes)
        {
            totalIncomeAmount += income.getAmount();
        }

        return totalIncomeAmount;
    }

    @FXML
    public void handleDatepicker(ActionEvent e)
    {
        if (e.getSource().equals(incomeDatePicker)) {
            financeTable.getColumns().clear();
            Date date = Date.from(incomeDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            initializeTable(financeTable, loadIncome(date));
        }
    }

    @FXML
    public void clickIncomeTableItem(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Income.class.cast(financeTable.getSelectionModel().getSelectedItem());
        }
    }

    private void refreshTableDynamically() {
        //run the timer to make sure that once user is finished with the update, the table gets updated
        timer = FxTimer.runPeriodically(Duration.ofMillis(10), () -> {
            if(UpdateIncome.newIncome)
            {
                UpdateIncome.newIncome = false;
                FxTimer.runLater(Duration.ofSeconds(2), () -> loadIncome(new Date()));
                timer.stop();
            }
        });
    }

    @FXML
    private void addNewIncome(ActionEvent e) {
        if (e.getSource().equals(addIncome)) {
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.showPrompt("../Income/AddIncome.fxml", "Add Income", addIncome);

            refreshTableDynamically();
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
        TableColumn<Income, Number> amountCol = new TableColumn<>("Amount");
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

    //handle table click events
    public void removeIncome(int id)
    {
        incomeRepository.deleteIncome(id);
    }

    private void handleDeletionIncome()
    {
        if(AlertHelper.showConfirmationDialog("Delete Confirmation",null, "Are you sure you want to delete this entry?"))
        {
            // Remove income and update table
            removeIncome(Income.class.cast(financeTable.getSelectionModel().getSelectedItem()).getIdIncome());
            financeTable.getColumns().clear();
            Date date = Date.from(incomeDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            initializeTable(financeTable, loadIncome(date));
        }
    }
}
