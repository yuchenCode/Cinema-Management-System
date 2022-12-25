package managementsys.presentationa;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

@SuppressWarnings("restriction")
public class MovieDialog extends Dialog<MovieInfo> {
    private TextField          nameField    = new TextField();
    private TextField          yearField    = new TextField();
    private TextField          timeField    = new TextField();
    private ButtonType         buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);

    public MovieDialog(MovieInfo m){
        this();
        nameField.setText(m.name);
        yearField.setText(m.year);
        timeField.setText(m.duration);
    }

    public MovieDialog(){
        super();
        Label label1 = new Label("Name: ");
        Label label2 = new Label("Year: ");
        Label label3 = new Label("Duration(min): ");
        getDialogPane().getButtonTypes().add(buttonTypeOk);

        // convert result to MovieInfo
        setResultConverter(new Callback<ButtonType, MovieInfo>() {
            @Override
            public MovieInfo call(ButtonType b) {
                if (b == buttonTypeOk) {
                    return new MovieInfo(nameField.getText(), yearField.getText(), timeField.getText());
                }
                return null;
            }
        });

        // set listeners
        setTitle("New Movie");
        setHeaderText("Please enter the details for the new Movie");
        nameField.textProperty().addListener((observable, oldValue, newValue) ->{validateInput();} );
        yearField.textProperty().addListener((observable, oldValue, newValue) ->{validateInput();} );
        timeField.textProperty().addListener((observable, oldValue, newValue) ->{validateInput();} );

        // set panels
        GridPane grid = new GridPane();
        grid.add(label1,1,1);
        grid.add(nameField,2,1);
        grid.add(label2,1,2);
        grid.add(yearField,2,2);
        grid.add(label3,1,3);
        grid.add(timeField, 2,3);
        getDialogPane().setContent(grid);
        getDialogPane().lookupButton(buttonTypeOk).setDisable(true);

    }

    private void validateInput() {
        // validate input correctness
        boolean disable = nameField.getText().length() > 0 && yearField.getText().length() > 0 && timeField.getText().length() > 0;
        getDialogPane().lookupButton(buttonTypeOk).setDisable(!disable);
    }
}
