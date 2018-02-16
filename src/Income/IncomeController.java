package Income;

import Models.Income;
import Repositories.IncomeRepository;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.text.DecimalFormat;
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
                    setText(decimalFormat.format(value.doubleValue()));
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
}
