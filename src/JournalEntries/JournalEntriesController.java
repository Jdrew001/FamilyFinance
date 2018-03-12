package JournalEntries;

import Business.AlertHelper;
import Business.SceneChanger;
import Models.Income;
import Models.JournalEntries;
import Repositories.JournalRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class JournalEntriesController implements Initializable {

    @FXML
    public TableView journalTable;

    @FXML
    public JFXDatePicker journalDatePicker;

    @FXML
    public JFXButton addJournal, removeJournal;

    public ObservableList<JournalEntries> journalEntries = FXCollections.observableArrayList();
    JournalRepository journalRepository = new JournalRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTable(journalTable, loadJournals(new Date()));
    }

    public void initializeTable(TableView tableView, ObservableList<JournalEntries> entries)
    {
        tableView.setColumnResizePolicy(new Callback<TableView.ResizeFeatures, Boolean>() {
            @Override
            public Boolean call(TableView.ResizeFeatures param) {
                return true;
            }
        });
        tableView.setEditable(false);
        TableColumn date = new TableColumn("Date");
        date.setMinWidth(200);
        date.setCellValueFactory(new PropertyValueFactory<JournalEntries, Date>("date"));

        TableColumn<JournalEntries, Number> amountCol = new TableColumn<>("Amount");
        amountCol.setMinWidth(200);
        amountCol.setCellValueFactory(cellData -> new ReadOnlyDoubleWrapper(cellData.getValue().getAmount()));
        amountCol.setCellFactory(tc -> new TableCell<JournalEntries, Number>() {
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

        TableColumn description = new TableColumn("Description");
        description.setCellValueFactory(new PropertyValueFactory<JournalEntries, String>("description"));
        description.setMinWidth(200);

        TableColumn category = new TableColumn("Category");
        category.setCellValueFactory(new PropertyValueFactory<JournalEntries, String>("categoryName"));
        category.setMinWidth(200);


        TableColumn transaction = new TableColumn("Transaction Name");
        transaction.setCellValueFactory(new PropertyValueFactory<JournalEntries, String>("transactionName"));
        transaction.setMinWidth(200);

        tableView.setItems(journalEntries);
        tableView.getColumns().addAll(date, amountCol, description, category, transaction);
    }

    @FXML
    public void addJournal(ActionEvent event)
    {
        if(event.getSource() == addJournal)
        {
            SceneChanger changer = new SceneChanger();
            changer.showPrompt(AddJournalController.class.getResource("AddJournal.fxml"), "Add Journal", addJournal);
        }
    }

    @FXML
    public void deleteJournal(ActionEvent event)
    {
        //remove event
        if(event.getSource() == removeJournal)
        {
            if(journalTable.getSelectionModel().getSelectedItem() != null)
                handleDeletionJournal();
        }
    }

    private void removeJournal(int id)
    {
        journalRepository.deleteJournalEntry(id);
    }

    private void handleDeletionJournal()
    {
        if(AlertHelper.showConfirmationDialog("Delete Confirmation", null, "Are you sure you want to delete this entry?"))
        {
            removeJournal(JournalEntries.class.cast(journalTable.getSelectionModel().getSelectedItem()).getIdJournal());
            journalTable.getColumns().clear();
            initializeTable(journalTable, loadJournals(new Date()));
        }
    }

    public ObservableList<JournalEntries> loadJournals(Date date)
    {
        journalEntries.clear();
        journalEntries = journalRepository.getJournalEntriesByMonth(date);
        System.out.println(journalEntries);
        return journalEntries;
    }
}
