package Category;

import Models.Category;
import Repositories.CategoryRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
        categoryListView.setItems(categoryRepository.getAllCategories());
    }

    @FXML
    public void createCategory()
    {
        if(categoryName.getText().isEmpty())
        {
            //error out TODO
        } else {
            if(categoryRepository.addCategory(categoryName.getText()))
            {
                submitBtn.getScene().getWindow().hide();

            } else {
                //show error when inputting the data TODO
            }
        }
    }

    @FXML
    public void cancelWindow()
    {
        cancelBtn.getScene().getWindow().hide();
    }
}
