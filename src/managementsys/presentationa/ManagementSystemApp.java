package managementsys.presentationa;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ManagementSystemApp extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage){
        try{
            URL url = new File("home.fxml").toURI().toURL();
            VBox box = (VBox) FXMLLoader.load(url);  // loads the GUI from the file and creates the StaffUI controller
            Scene scene = new Scene(box, 300, 300);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ManagementSystemApp");
            primaryStage.setResizable(true);
            primaryStage.show();
            scene.setFill(Color.BROWN);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
