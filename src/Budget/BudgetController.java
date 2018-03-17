package Budget;

import Models.BudgetItems;
import Models.Expense;
import Repositories.BudgetRepository;
import Repositories.ExpenseRepository;
import Repositories.ExpensesAddedUp;
import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

public class BudgetController implements Initializable {

    BudgetRepository budgetRepository = new BudgetRepository();
    ExpenseRepository expenseRepository = new ExpenseRepository();
    ObservableList<BudgetItems> budgets = FXCollections.observableArrayList();
    ObservableList<ExpensesAddedUp> expensesAddedUp = FXCollections.observableArrayList();

    @FXML
    public JFXTreeTableView<BudgetItems> budgetTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        expensesAddedUp = expenseRepository.addUpExpensesCategoriesMonthly(new Date());
        initilizeTableView();
    }

    private void initilizeTableView()
    {
        budgets = budgetRepository.getBudgetItemsForMonth(new Date());
        budgetTable.setColumnResizePolicy(new Callback<TreeTableView.ResizeFeatures, Boolean>() {
            @Override
            public Boolean call(TreeTableView.ResizeFeatures param) {
                return true;
            }
        });

        budgetTable.setEditable(false);

        JFXTreeTableColumn<BudgetItems, String> categoryCol = new JFXTreeTableColumn<>("Category");
        categoryCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<BudgetItems, String> param) ->{
            if(categoryCol.validateValue(param)) return param.getValue().getValue().getCategoryName();
            else return categoryCol.getComputedValue(param);
        });
        categoryCol.setMinWidth(200);

        //Progress Bar
        JFXTreeTableColumn progressBarCol = new JFXTreeTableColumn("Progress");
        progressBarCol.setSortable(false);
        progressBarCol.setCellValueFactory(
                new Callback<JFXTreeTableColumn.CellDataFeatures<BudgetItems, Boolean>,
                        ObservableValue<Boolean>>() {

                    @Override
                    public ObservableValue<Boolean> call(JFXTreeTableColumn.CellDataFeatures<BudgetItems, Boolean> param) {
                        return new SimpleBooleanProperty(param.getValue() != null);
                    }
                });

        progressBarCol.setCellFactory(
                new Callback<JFXTreeTableColumn<BudgetItems, Boolean>, JFXTreeTableCell>() {

                    @Override
                    public JFXTreeTableCell call(JFXTreeTableColumn<BudgetItems, Boolean> param) {
                        return new ProgressCell();
                    }
                });

        JFXTreeTableColumn<BudgetItems, Number> remainingCol = new JFXTreeTableColumn<>("Remaining");
        remainingCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<BudgetItems, Number> param) ->{
            if(remainingCol.validateValue(param))
            {
                if(categoryExpensed().get(param.getValue().getValue().getCategory().getName()) != null) {
                    return param.getValue().getValue().getAmountProperty()
                            .subtract(categoryExpensed().get(param.getValue().getValue().getCategory().getName()));
                } else {
                    return param.getValue().getValue().getAmountProperty();
                }
            }
            else return remainingCol.getComputedValue(param);
        });


        JFXTreeTableColumn<BudgetItems, Number> budgetedAmount = new JFXTreeTableColumn<>("Budgeted");
        budgetedAmount.setCellValueFactory((TreeTableColumn.CellDataFeatures<BudgetItems, Number> param) ->{
            if(budgetedAmount.validateValue(param)) return param.getValue().getValue().getAmountProperty();
            else return budgetedAmount.getComputedValue(param);
        });


        final TreeItem<BudgetItems> root = new RecursiveTreeItem<BudgetItems>(budgets, RecursiveTreeObject::getChildren);
        budgetTable.getColumns().addAll(categoryCol, progressBarCol, remainingCol, budgetedAmount);
        budgetTable.setRoot(root);
        budgetTable.setShowRoot(false);
        categoryCol.prefWidthProperty().bind(budgetTable.widthProperty().multiply(0.25));
        progressBarCol.prefWidthProperty().bind(budgetTable.widthProperty().multiply(0.25));
        progressBarCol
        remainingCol.prefWidthProperty().bind(budgetTable.widthProperty().multiply(0.25));
        budgetedAmount.prefWidthProperty().bind(budgetTable.widthProperty().multiply(0.25));
        categoryExpensed();

    }

    /*
    * get all expense for the month.
    * Loop through the categories that have been added to a budget
    * Create a dictionary - what you need is to have the key = to the category name \n
    *                           value = to the amount you have spent in expenses for that category
    *
    * Once that is complete then you can substract each value from that key value expensed dictionary
    *
    *
    *
    *   Map<String, String> map = new HashMap<String, String>();
        map.put("dog", "type of animal");
        System.out.println(map.get("dog"));

    * */
    private Map<String, DoubleProperty> categoryExpensed()
    {
        Map<String, DoubleProperty> expenses = new HashMap<String, DoubleProperty>();
        for(ExpensesAddedUp ex : expensesAddedUp)
        {
            expenses.put(ex.getCategory().getName(), new SimpleDoubleProperty(ex.getTotal()));
        }

        return expenses;
    }

    class ProgressCell extends JFXTreeTableCell {
        JFXProgressBar bar = new JFXProgressBar();
        public ProgressCell() {
            bar.setPrefWidth(300);
        }

        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

            if(!empty){
                setGraphic(bar);

                BudgetItems rItem = BudgetItems.class.cast(this.getTreeTableRow().getItem());
                //System.out.println(rItem.getCategory().getName());
                if(categoryExpensed().get(rItem.getCategory().getName()) != null)
                {
                    this.setProgress(categoryExpensed().get(rItem.getCategory().getName()).doubleValue() / rItem.getAmount());
                }

            }
        }

        public void setProgress(double progress) {
            bar.setProgress(progress);
        }


    }
}


