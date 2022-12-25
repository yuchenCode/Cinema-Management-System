package managementsys.presentationa;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AdminApp extends Application{


    public void start(Stage primaryStage){
        try{
            URL url = new File("staffGui.fxml").toURI().toURL();
            VBox box = (VBox) FXMLLoader.load(url);// loads the GUI from the file and creates the StaffUI controller
            Scene scene = new Scene(box, AdminUI.LEFT_MARGIN + (AdminUI.SLOTS * AdminUI.COL_WIDTH), AdminUI.TOP_MARGIN + 10 * AdminUI.ROW_HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ManagementSystemApp");
            primaryStage.setHeight(AdminUI.TOP_MARGIN + 10 * AdminUI.ROW_HEIGHT + 90);
            primaryStage.setWidth(AdminUI.LEFT_MARGIN + (AdminUI.SLOTS * AdminUI.COL_WIDTH) + 50);
            primaryStage.setResizable(true);
            primaryStage.show();
            scene.setFill(Color.BROWN);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}