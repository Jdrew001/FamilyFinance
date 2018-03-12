package Business;

import Income.IncomeController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;

public class SceneChanger {

    public void changeScene(JFXButton btn, String scenePath, String title)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(scenePath));
            /*
             * if "fx:controller" is not set in fxml
             * fxmlLoader.setController(NewWindowController);
             */
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();

            btn.getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void showPrompt(URL url, String title, JFXButton button)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(url);
            /*
             * if "fx:controller" is not set in fxml
             * fxmlLoader.setController(NewWindowController);
             */
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(button.getScene().getWindow());
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Test");
        }
    }

    public void test (String scenePath, String title)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(scenePath));
            /*
             * if "fx:controller" is not set in fxml
             * fxmlLoader.setController(NewWindowController);
             */
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Test");
        }
    }
}
