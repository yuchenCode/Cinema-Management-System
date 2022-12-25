package managementsys.presentationa;

import javafx.collections.FXCollections;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;


public class TicketsNumberDialog extends Dialog<TicketInfo>{
    private Integer[]  st      =   {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private ChoiceBox<Integer>  ticketBox     =   new ChoiceBox<>(FXCollections.observableArrayList(st));
    private ButtonType         buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);

    public TicketsNumberDialog(TicketInfo t){
        this();
        ticketBox.getSelectionModel().select(Integer.valueOf(t.number));
    }
    public TicketsNumberDialog(){
        super();
        Label label1 = new Label("Number: ") ;
        getDialogPane().getButtonTypes().add(buttonTypeOk);

        // conver result to ScreeningInfo
        setResultConverter(new Callback<ButtonType, TicketInfo>() {
            @Override
            public TicketInfo call(ButtonType b) {
                if (b == buttonTypeOk) {
                    return new TicketInfo(ticketBox.getValue());
                }
                return null;}
        });

        // set listeners
        setTitle("Buy ticket");
        setHeaderText("Please enter the details for Tickets");
        ticketBox.getSelectionModel().selectedIndexProperty().addListener((e) -> {
            validateInput();
        });

        // set panels
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(ticketBox, 2, 1);
        getDialogPane().setContent(grid);
        getDialogPane().lookupButton(buttonTypeOk).setDisable(true);
    }

    private void validateInput() {
        // validate input correctness
        boolean disable = !ticketBox.getSelectionModel().isEmpty();
        getDialogPane().lookupButton(buttonTypeOk).setDisable(!disable);
    }

}


