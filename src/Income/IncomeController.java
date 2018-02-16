package Income;

import Business.DecimalColumnFactory;
import Models.Category;
import Models.Income;
import Models.TransactionType;
import Models.User;
import Repositories.IncomeRepository;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

public class IncomeController {

    public ObservableList<Income> incomes =  FXCollections.observableArrayList();
    IncomeRepository incomeRepository = new IncomeRepository();
    double totalIncomeAmount = 0.0;

    public double getIncomeAmount()
    {
        incomes = incomeRepository.getIncomeByMonth(new Date());

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

    public void initializeTable(TableView table)
    {
        table.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
            @Override
            public Boolean call(TableView.ResizeFeatures param) {
                return true;
            }
        });
        ObservableList<Income> incomes = loadIncome();
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
                    setText(decimalFormat.format(value.doubleValue()));
                }
            }
        });




        amountCol.getCellFactory();


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


        table.setItems(loadIncome());
        table.getColumns().addAll(amountCol, date, username, category, transaction);
    }
}
