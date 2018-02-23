package Category;

import Business.AlertHelper;
import Models.Category;
import Models.Income;
import Repositories.CategoryRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class CategoryController {

    CategoryRepository categoryRepository = new CategoryRepository();

    @FXML
    public JFXTextField categoryName;
    @FXML
    public JFXButton submitBtn, cancelBtn;
    //load up date to list view
    public void loadItems(ListView<Category> categoryListView)
    {
        categoryListView.setCellFactory(param -> new ListCell<Category>() {
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);

                if(empty || category == null || category.getName() == null) {
                    setText(null);
                } else {
                    String c = category.getName().toUpperCase();
                    setText(c);
                }
            }
        });
        //load in the data from service
        categoryListView.getItems().clear();
        categoryListView.setItems(categoryRepository.getAllCategories());
    }

    @FXML
    public void createCategory()
    {
        if(categoryName.getText().isEmpty())
        {
            AlertHelper.showErrorDialog("Form Error", null, "Please ensure all information is typed in");
        } else {
            if(categoryRepository.addCategory(categoryName.getText()))
            {
                submitBtn.getScene().getWindow().hide();

            } else {
                AlertHelper.showErrorDialog("Unknown Error", null, "An unknown error has occurred. Be sure you are connected to internet");
            }
        }
    }

    @FXML
    public void cancelWindow()
    {
        cancelBtn.getScene().getWindow().hide();
    }

    public void removeItem(ObservableList selectedItems) {
        ObservableList<Category> categories = selectedItems;
        for (Category category:categories) {
            categoryRepository.deleteCategory(category.getId());
        }
    }
}