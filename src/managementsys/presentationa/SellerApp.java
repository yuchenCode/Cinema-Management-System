package managementsys.presentationa;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SellerApp extends Application{

    public void start(Stage primaryStage){
        try{
            URL url = new File("sellerGui.fxml").toURI().toURL();
            VBox box = (VBox) FXMLLoader.load(url);// loads the GUI from the file and creates the StaffUI controller
            Scene scene = new Scene(box, SellerUI.LEFT_MARGIN + (SellerUI.SLOTS * SellerUI.COL_WIDTH), SellerUI.TOP_MARGIN + 10 * SellerUI.ROW_HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ManagementSystemApp");
            primaryStage.setHeight(SellerUI.TOP_MARGIN + 10 * SellerUI.ROW_HEIGHT + 90);
            primaryStage.setWidth(SellerUI.LEFT_MARGIN + (SellerUI.SLOTS * SellerUI.COL_WIDTH) + 50);
            primaryStage.setResizable(true);
            primaryStage.show();
            scene.setFill(Color.BROWN);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}