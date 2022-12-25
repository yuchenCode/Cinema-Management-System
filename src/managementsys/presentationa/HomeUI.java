package managementsys.presentationa;

import javafx.stage.Stage;
import managementsys.application.domain.ManagementObserver;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class HomeUI implements ManagementObserver {
    @FXML private VBox box;

    public void initialize(){
        box.layout();
        update();
    }

    @Override
    public void update() {

    }

    @Override
    public boolean message(String s, boolean confirm) {
        return true;
    }

    public void jumpAdminUI() {
        // to admin ui
        AdminApp staffApp = new AdminApp();
        box.getScene().getWindow().hide();
        staffApp.start(new Stage());
    }

    public void jumpSellerUI() {
        // to seller ui
        SellerApp sellerApp = new SellerApp();
        box.getScene().getWindow().hide();
        sellerApp.start(new Stage());
    }
}