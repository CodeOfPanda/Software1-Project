package Main;

import View_Controller.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventoryProgram extends Application {

    @Override
    public void start(Stage Stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../resources/MainScreen.fxml"));
        Parent mainScreenRoot = loader.load();

        MainScreenController controller = loader.getController();
        controller.sampleData();

        Stage mainScreenStage = new Stage();
        Scene mainScene = new Scene(mainScreenRoot);

        mainScreenStage.setScene(mainScene);
        mainScreenStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
