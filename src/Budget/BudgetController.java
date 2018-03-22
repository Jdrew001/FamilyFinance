package Budget;

import Business.SceneChanger;
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
import javafx.event.ActionEvent;
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
    ObservableList<BudgetItems> variableItems = FXCollections.observableArrayList();
    ObservableList<BudgetItems> fixedItems = FXCollections.observableArrayList();
    ObservableList<ExpensesAddedUp> expensesAddedUp = FXCollections.observableArrayList();

    @FXML
    public JFXTreeTableView<BudgetItems> variableTable, fixedTable;

    @FXML
    public Label totalRemainingTxt, totalSpentTxt, totalBudgetTxt;

    @FXML
    public JFXButton addBudget;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //checkForBudget();
        expensesAddedUp = expenseRepository.addUpExpensesCategoriesMonthly(new Date());
        budgets = budgetRepository.getBudgetItemsForMonth(new Date());
        changeItemArrays();
        initilizeVariableTableView();
        initilizeFixedTableView();
        initilizeBottomBar();
    }

    @FXML
    public void addNewBudget(ActionEvent e)
    {
        if(e.getSource().equals(addBudget))
        {
            SceneChanger s = new SceneChanger();
            s.showPrompt(AddNewBudget.class.getResource("AddNewBudget.fxml"), "New Budget", addBudget);
        }
    }

    private void initilizeBottomBar()
    {
        double totalAmount = 0.0;
        double totalBudget = 0.0;
        double totalRemaining = 0.0;
        for(ExpensesAddedUp ex : expensesAddedUp)
        {
            totalAmount += ex.getTotal();
        }

        for(BudgetItems b : budgets)
        {
            totalBudget += b.getAmount();
        }

        totalRemaining = totalBudget - totalAmount;
        totalSpentTxt.setText(String.format("($%.2f)", totalAmount));


        if(totalRemaining < 0)
        {
            totalRemainingTxt.setText(String.format("($%.2f)", totalRemaining));
        } else {
            totalRemainingTxt.setText(String.format("$%.2f", totalRemaining));
        }
        totalBudgetTxt.setText(String.format("$%.2f", totalBudget));
    }

    private void changeItemArrays()
    {
        for(BudgetItems b : budgets)
        {
            if(b.getBudgetType().equalsIgnoreCase("variable"))
            {
                variableItems.add(b);
            } else if(b.getBudgetType().equalsIgnoreCase("fixed")) {
                fixedItems.add(b);
            }
        }
    }

    private void initilizeVariableTableView()
    {
        variableTable.setColumnResizePolicy(new Callback<TreeTableView.ResizeFeatures, Boolean>() {
            @Override
            public Boolean call(TreeTableView.ResizeFeatures param) {
                return true;
            }
        });

        variableTable.setEditable(false);

        JFXTreeTableColumn<BudgetItems, String> categoryCol = new JFXTreeTableColumn<>("Category");
        categoryCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<BudgetItems, String> param) ->{
            if(categoryCol.validateValue(param)) return param.getValue().getValue().getCategoryName();
            else return categoryCol.getComputedValue(param);
        });
        categoryCol.setMinWidth(200);
        categoryCol.setSortable(false);

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

        JFXTreeTableColumn<BudgetItems, Number> remainingCol = new JFXTreeTableColumn<>("Spent");
        remainingCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<BudgetItems, Number> param) ->{
            if(remainingCol.validateValue(param))
            {


                if(categoryExpensed().get(param.getValue().getValue().getCategory().getName()) != null) {
                    return categoryExpensed().get(param.getValue().getValue().getCategory().getName());
                } else {
                    return new SimpleDoubleProperty(0.00);
                }
            }
            else return remainingCol.getComputedValue(param);
        });
        remainingCol.setSortable(false);


        JFXTreeTableColumn<BudgetItems, Number> budgetedAmount = new JFXTreeTableColumn<>("Budgeted");
        budgetedAmount.setCellValueFactory((TreeTableColumn.CellDataFeatures<BudgetItems, Number> param) ->{
            if(budgetedAmount.validateValue(param)) return param.getValue().getValue().getAmountProperty();
            else return budgetedAmount.getComputedValue(param);
        });
        budgetedAmount.setSortable(false);


        final TreeItem<BudgetItems> root = new RecursiveTreeItem<BudgetItems>(variableItems, RecursiveTreeObject::getChildren);
        variableTable.getColumns().addAll(categoryCol, progressBarCol, remainingCol, budgetedAmount);
        variableTable.setRoot(root);
        variableTable.setShowRoot(false);
        variableTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
        categoryCol.prefWidthProperty().bind(variableTable.widthProperty().multiply(0.25));
        progressBarCol.prefWidthProperty().bind(variableTable.widthProperty().multiply(0.25));
        remainingCol.prefWidthProperty().bind(variableTable.widthProperty().multiply(0.25));
        budgetedAmount.prefWidthProperty().bind(variableTable.widthProperty().multiply(0.25));
        categoryExpensed();
    }

    private void initilizeFixedTableView()
    {
        fixedTable.setColumnResizePolicy(new Callback<TreeTableView.ResizeFeatures, Boolean>() {
            @Override
            public Boolean call(TreeTableView.ResizeFeatures param) {
                return true;
            }
        });

        fixedTable.setEditable(false);

        JFXTreeTableColumn<BudgetItems, String> categoryCol = new JFXTreeTableColumn<>("Category");
        categoryCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<BudgetItems, String> param) ->{
            if(categoryCol.validateValue(param)) return param.getValue().getValue().getCategoryName();
            else return categoryCol.getComputedValue(param);
        });
        categoryCol.setMinWidth(200);
        categoryCol.setSortable(false);

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

        JFXTreeTableColumn<BudgetItems, Number> remainingCol = new JFXTreeTableColumn<>("Spent");
        remainingCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<BudgetItems, Number> param) ->{
            if(remainingCol.validateValue(param))
            {


                if(categoryExpensed().get(param.getValue().getValue().getCategory().getName()) != null) {
                    return categoryExpensed().get(param.getValue().getValue().getCategory().getName());
                } else {
                    return new SimpleDoubleProperty(0.00);
                }
            }
            else return remainingCol.getComputedValue(param);
        });
        remainingCol.setSortable(false);


        JFXTreeTableColumn<BudgetItems, Number> budgetedAmount = new JFXTreeTableColumn<>("Budgeted");
        budgetedAmount.setCellValueFactory((TreeTableColumn.CellDataFeatures<BudgetItems, Number> param) ->{
            if(budgetedAmount.validateValue(param)) return param.getValue().getValue().getAmountProperty();
            else return budgetedAmount.getComputedValue(param);
        });
        budgetedAmount.setSortable(false);


        final TreeItem<BudgetItems> root = new RecursiveTreeItem<BudgetItems>(fixedItems, RecursiveTreeObject::getChildren);
        fixedTable.getColumns().addAll(categoryCol, progressBarCol, remainingCol, budgetedAmount);
        fixedTable.setRoot(root);
        fixedTable.setShowRoot(false);
        fixedTable.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
        categoryCol.prefWidthProperty().bind(fixedTable.widthProperty().multiply(0.25));
        progressBarCol.prefWidthProperty().bind(fixedTable.widthProperty().multiply(0.25));
        remainingCol.prefWidthProperty().bind(fixedTable.widthProperty().multiply(0.25));
        budgetedAmount.prefWidthProperty().bind(fixedTable.widthProperty().multiply(0.25));
        categoryExpensed();
    }



    private Map<String, DoubleProperty> categoryExpensed()
    {
        Map<String, DoubleProperty> expenses = new HashMap<String, DoubleProperty>();
        for(ExpensesAddedUp ex : expensesAddedUp)
        {
            expenses.put(ex.getCategory().getName(), new SimpleDoubleProperty(ex.getTotal()));
        }

        return expenses;
    }

    private void checkForBudget()
    {
        System.out.println(budgetRepository.getBudgetFromMonth(new Date()));
        if(budgetRepository.getBudgetFromMonth(new Date()) != null)
        {
            addBudget.setVisible(false);
        } else {
            addBudget.setVisible(true);
        }
    }



    class ProgressCell extends JFXTreeTableCell {
        JFXProgressBar bar = new JFXProgressBar();
        public ProgressCell() {
            bar.setPrefWidth(300);
            bar.setStyle("-fx-background-color: #2980b9");
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


