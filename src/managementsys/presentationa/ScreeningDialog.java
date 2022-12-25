package managementsys.presentationa;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import managementsys.application.domain.Movie;
import managementsys.application.domain.Screen;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("restriction")
public class ScreeningDialog extends Dialog<ScreeningInfo>{
    private List<String>       screens;
    private List<String>       movies;
    private LocalDate          date;
    private ChoiceBox<String>  screenBox;
    private ChoiceBox<String>  movieBox;
    private String[]           times        = {
            "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
            "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30", "18:00", "18:30",
            "19:00", "19:30", "20:00", "20:30", "21:00", "21:30"};
    private ChoiceBox<String>  timeBox      = new ChoiceBox<String>(FXCollections.observableArrayList(times));
    private ButtonType         buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);

    public void init(List<Movie> mv_list, List<Screen> sc_list){
        // initialize choice box of screens
        screens = new ArrayList<>();
        for (Screen sc : sc_list){
            screens.add(sc.getNumber()+"-"+sc.getScreenName());
        }
        screenBox = new ChoiceBox<String>(FXCollections.observableArrayList(screens));
        // initialize choice box of movies
        movies = new ArrayList<>();
        for (Movie m : mv_list){
            movies.add(m.getName());
        }
        movieBox = new ChoiceBox<String>(FXCollections.observableArrayList(movies));
    }

    public ScreeningDialog (ScreeningInfo s, List<Movie> mv_list, List<Screen> sc_list, LocalDate date){
        this(mv_list, sc_list, date);
        screenBox.getSelectionModel().select(sc_list.get(s.screen_number-1).getScreenName());
        movieBox.getSelectionModel().select(s.movie_name);
        timeBox.getSelectionModel().select(s.time.toString());
    }

    public ScreeningDialog(List<Movie> mv_list, List<Screen> sc_list, LocalDate date) {
        super();
        this.date = date;
        init(mv_list, sc_list);
        Label label1 = new Label("Screen: ");
        Label label2 = new Label("Movie: ");
        Label label3 = new Label("Time: ");
        getDialogPane().getButtonTypes().add(buttonTypeOk);

        // conver result to ScreeningInfo
        setResultConverter(new Callback<ButtonType, ScreeningInfo>() {
            @Override
            public ScreeningInfo call(ButtonType b) {
                if (b == buttonTypeOk) {
                    return new ScreeningInfo(
                            screens.indexOf(screenBox.getValue()) + 1,
                            movieBox.getValue(),
                            mv_list.get(movies.indexOf(movieBox.getValue())).getYear(),
                            LocalTime.parse(timeBox.getValue()),
                            date, 0);
                }
                return null;
            }
        });

        // set listeners
        setTitle("New Screening");
        setHeaderText("Please enter the details for the new Screening");
        screenBox.getSelectionModel().selectedIndexProperty().addListener((e) -> {
            validateInput();
        });
        movieBox.getSelectionModel().selectedIndexProperty().addListener((e) -> {
            validateInput();
        });
        timeBox.getSelectionModel().selectedIndexProperty().addListener((e) -> {
            validateInput();
        });

        // set panels
        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(screenBox, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(movieBox, 2, 2);
        grid.add(label3, 1, 3);
        grid.add(timeBox, 2, 3);
        getDialogPane().setContent(grid);
        getDialogPane().lookupButton(buttonTypeOk).setDisable(true);
    }

    private void validateInput() {
        // validate input correctness
        boolean disable = !screenBox.getSelectionModel().isEmpty() &&  !movieBox.getSelectionModel().isEmpty() && !timeBox.getSelectionModel().isEmpty();
        getDialogPane().lookupButton(buttonTypeOk).setDisable(!disable);
    }
}
