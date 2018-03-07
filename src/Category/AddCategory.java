package Category;

import Business.AlertHelper;
import Business.SceneChanger;
import Models.Category;
import Repositories.CategoryRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCategory implements Initializable {

    CategoryRepository categoryRepository = new CategoryRepository();
    boolean isUpdate = false;
    private int categoryId = 0;
    private String categoryNameData = "";

    @FXML
    public JFXTextField categoryName;

    @FXML
    public Label headerTxt;

    @FXML
    public JFXButton submitBtn, cancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(UpdateCategory.isUpdate)
        {
            //Change the label of the heading
            headerTxt.setText("Update Category");
            categoryName.setText(UpdateCategory.category.getName());
        }
    }

    @FXML
    public void createCategory()
    {
        //This will validate whether or not you ahve put in data in the text box
        if(categoryName.getText().isEmpty())
        {
            //Show error dialog
            AlertHelper.showErrorDialog("Form Error", null, "Please ensure all information is typed in");
        } else {
            //if the update category -- static class -- is update is true then run all logic in if statement
            if(UpdateCategory.isUpdate)
            {
                //If the category repository reports that no errors in updating the given category, then execute code block
                if(categoryRepository.updateCategory(UpdateCategory.category.getId(), categoryName.getText()))
                {
                    //Since the Update was successful that means that data has changed so we want to change that to true to
                    //change the table in the CategoryController
                    UpdateCategory.hasChanged = true;

                    //hide the window that is calling this button -- the addCategory
                    submitBtn.getScene().getWindow().hide();
                } else {

                    //SHow error if the category cannot be updated
                    AlertHelper.showErrorDialog("Unknown Error", null, "An unknown error has occurred. Be sure you are connected to internet");
                }
            } else { // called when the option to 'update' is not happening. So when the user clicks the add category button

                //if adding category method returns true -- meaning a successful add then close window
                if(categoryRepository.addCategory(categoryName.getText()))
                {
                    submitBtn.getScene().getWindow().hide();
                    UpdateCategory.newCategory = true;
                } else {
                    AlertHelper.showErrorDialog("Unknown Error", null, "An unknown error has occurred. Be sure you are connected to internet");
                }
            }
        }
    }

    @FXML
    public void cancelWindow()
    {
        cancelBtn.getScene().getWindow().hide();
    }
}
