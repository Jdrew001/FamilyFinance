package Category;

import Business.AlertHelper;
import Business.SceneChanger;
import Business.TimeHelper;
import Models.Category;
import Models.Income;
import Repositories.CategoryRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.reactfx.util.FxTimer;

import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import org.reactfx.util.Timer;

public class CategoryController implements Initializable {

    CategoryRepository categoryRepository = new CategoryRepository();

    private int categoryId = 0;
    boolean isUpdate = false;
    private Timer timer;
    private boolean newCategory = false;

    @FXML
    public JFXButton addCategory, removeCategory;

    @FXML
    ListView categoryListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadItems(categoryListView);
    }

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
    public void addNewCategory(ActionEvent e) {
        if(e.getSource().equals(addCategory))
        {
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.showPrompt(AddCategory.class.getResource("AddCategory.fxml"), "Add Category", addCategory);
            refreshTableDynamically();
        }
    }

    @FXML
    public void removeCategory(ActionEvent e) {
        if(e.getSource().equals(removeCategory))
        {
            removeItem(categoryListView.getSelectionModel().getSelectedItems());
            loadItems(categoryListView);
        }
    }

    @FXML
    public void clickCategoryListItem(MouseEvent event)
    {
        System.out.println("Working");
        if(event.getClickCount() == 2)
        {
            updateCategory(categoryListView.getSelectionModel().getSelectedItems());
        }
    }

    public void updateCategory(ObservableList selectedItems) {
        ObservableList<Category> categories = selectedItems;
        SceneChanger sceneChanger = new SceneChanger();
        //set the update to be true
        isUpdate = true;

        for(Category category: categories)
        {
            Category categoryModel = new Category(category.getId(), category.getName());
            UpdateCategory.category = categoryModel;
            UpdateCategory.isUpdate = isUpdate;
        }

        //show the prompt to update the view
        sceneChanger.showPrompt(AddCategory.class.getResource("AddCategory.fxml"), "Add Category", addCategory);
        refreshTableDynamically();

    }

    public void removeItem(ObservableList selectedItems) {
        ObservableList<Category> categories = selectedItems;
        for (Category category:categories) {
            categoryRepository.deleteCategory(category.getId());
        }
    }

    private void refreshTableDynamically()
    {
        //run the timer to make sure that once user is finished with the update, the table gets updated
        timer = FxTimer.runPeriodically(Duration.ofMillis(10), () -> {
            if(UpdateCategory.isUpdate && UpdateCategory.hasChanged || UpdateCategory.newCategory)
            {
                //Set the update category has changed and is update to false so that this won't run anylonger
                UpdateCategory.hasChanged = false; //changed in the Add Category class
                UpdateCategory.isUpdate = false; // changed above and checked in the category file
                UpdateCategory.newCategory = false;
                FxTimer.runLater(Duration.ofSeconds(2), () -> {
                    loadItems(categoryListView);
                });
                timer.stop();
            }
        });
    }
}
