package JournalEntries;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class JournalEntriesController implements Initializable {

    @FXML
    public TableView journalTable;

    @FXML
    public JFXDatePicker journalDatePicker;

    @FXML
    public JFXButton addJournal, removeJournal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
