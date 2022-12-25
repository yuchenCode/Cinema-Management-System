package managementsys.presentationa;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import managementsys.application.domain.ManagementObserver;
import managementsys.application.domain.ManagementSystem;
import managementsys.application.domain.*;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SellerUI implements ManagementObserver {
    final static int         LEFT_MARGIN   = 160;
    final static int         TOP_MARGIN    = 50;
    final static int         BOTTOM_MARGIN = 50;
    final static int         ROW_HEIGHT    = 50;
    final static int         COL_WIDTH     = 60;
    final static int         PPM           = 2;                     // Pixels per minute
    final static int         PPH           = 60 * PPM;              // Pixels per hours
    final static int         TZERO         = 10;                    // Earliest time shown
    final static int         SLOTS         = 24;                    // Number of booking slots shown
    private ManagementSystem ms;
    private LocalDate        displayedDate;
    private List<Screen>     screens        = new ArrayList<Screen>();
    private int              firstX, firstY, currentX, currentY;
    private boolean          mouseDown;

    @FXML private DatePicker datePicker;
    @FXML private Canvas     canvas;
    @FXML private VBox       box;


    public void initialize(){
        ms = ManagementSystem.getInstance();
        ms.setDate(LocalDate.now());
        ms.addObserver(this);
        screens = ManagementSystem.getScreens();
        datePicker.setValue(LocalDate.now());
        displayedDate = LocalDate.now();

        // code to be executed when mouse is pressed
       canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            currentX = firstX = (int) e.getX();
            currentY = firstY = (int) e.getY();
            if (e.getButton() == MouseButton.PRIMARY) {
                mouseDown = true;
                ms.selectScreening(yToScreen(firstY), xToTime(firstX));
            }
        });
        // code to be executed when mouse is reeleased
        canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, (e) -> {
            mouseDown = false;
            Screening b = ms.getSelectedScreening();
            if (b != null && (currentX != firstX || yToScreen(currentY) != b.getScreen().getNumber())) {
                ms.changeSelected(displayedDate, xToTime(timeToX(b.getTime()) + currentX - firstX), yToScreen(currentY));
            }
        });

        box.layout();
        update();
    }

    @Override
    public void update() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        screens = ManagementSystem.getScreens();
        canvas.setHeight(TOP_MARGIN + screens.size() * ROW_HEIGHT);
        canvas.setWidth(LEFT_MARGIN + (SLOTS * COL_WIDTH));
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setLineWidth(2.0);
        gc.setFill(Color.BLACK);
        //
        // // Draw screen outlines
        //
        gc.strokeLine(LEFT_MARGIN, 0, LEFT_MARGIN, canvas.getHeight());
        gc.strokeLine(0, TOP_MARGIN, canvas.getWidth(), TOP_MARGIN);
        // draw screens
        for (int i = 0; i < screens.size(); i++) {
            int y = TOP_MARGIN + (i + 1) * ROW_HEIGHT;
            gc.fillText(screens.get(i).getScreenName() + " (" + screens.get(i).getCapacity() + ")", 0, y - ROW_HEIGHT / 3);
            gc.strokeLine(LEFT_MARGIN, y, canvas.getWidth(), y);
        }
        // draw times
        LocalTime start = LocalTime.of(10, 0);
        for (int i = 0; i < SLOTS + 1; i++) {
            LocalTime show = start.plusMinutes(i * 30);
            String tmp = show.getHour() + ":" + (show.getMinute() > 9 ? show.getMinute() : "0" + show.getMinute());
            int x = LEFT_MARGIN + i * COL_WIDTH;
            gc.fillText(tmp, x + 15, 40);
            gc.strokeLine(x, TOP_MARGIN, x, canvas.getHeight());
        }
        // draw screenings
        List<Screening> enumS = ms.getScreenings();
        for (Screening b : enumS) {
            int x = timeToX(b.getTime());
            int y = screenToY(b.getScreen().getNumber());

            gc.setFill(Color.GRAY);
            gc.fillRect(x, y, durationToLength(b), ROW_HEIGHT);

            if (b == ms.getSelectedScreening()) {
                gc.setStroke(Color.RED);
                gc.strokeRect(x, y, durationToLength(b), ROW_HEIGHT);
                gc.setStroke(Color.BLACK);
            }
            gc.setFill(Color.WHITE);
            gc.fillText(b.getDetails(), x, y + ROW_HEIGHT / 2);
        }
        // highlight selected
        Screening b = ms.getSelectedScreening();
        if (mouseDown && b != null) {
            int x = timeToX(b.getTime()) + currentX - firstX;
            int y = screenToY(b.getScreen().getNumber()) + currentY - firstY;
            gc.setStroke(Color.RED);
            gc.strokeRect(x, y, durationToLength(b), ROW_HEIGHT);
            gc.setStroke(Color.BLACK);
        }
    }

    private int durationToLength(Screening sc){
        // convert the screening duration
        int duration = sc.getMovie().getDuration();
        return duration * PPM;
    }

    private int timeToX(LocalTime time) {
        // convert time to the pixel coordinates
        return LEFT_MARGIN + PPH * (time.getHour() - TZERO) + PPM * time.getMinute();
    }

    private LocalTime xToTime(int x) {
        // convert the coordinates to time
        x -= LEFT_MARGIN;
        int h = Math.max(0, (x / PPH) + TZERO);
        int m = Math.max(0, (x % PPH) / PPM);
        return LocalTime.of(h, m);
    }

    private int screenToY(int table) {// this assumes that the tables are continuously numbered from 1 to n-1
        return TOP_MARGIN + (ROW_HEIGHT * (table - 1));
    }

    private int yToScreen(int y) {
        return ((y - TOP_MARGIN) / ROW_HEIGHT) + 1;
    }

    public void nextDay() {
        // go to next day
        displayedDate = datePicker.getValue();
        displayedDate = displayedDate.plusDays(1);
        datePicker.setValue(displayedDate);
        ms.setDate(displayedDate);
    }

    public void prevDay() {
        // go to the previous day
        displayedDate = datePicker.getValue();
        displayedDate = displayedDate.minusDays(1);
        datePicker.setValue(displayedDate);
        ms.setDate(displayedDate);
    }

    public void showTicketsNumberDialog(TicketInfo ti) {
        // display the screening dialog
        TicketsNumberDialog sellTickets = new TicketsNumberDialog(ti);
        Optional<TicketInfo> result = sellTickets.showAndWait();

        if (result.isPresent()) {
            TicketInfo c = result.get();
            if (!ms.sellTickets(ms.getSelectedScreening(), c.number)) {
                // if information given by user is not correct
                showTicketsNumberDialog(c);
            }
        }
    }

    public void showTicketsNumberDialog() {
        if (ms.getSelectedScreening() == null){
            boolean response = message("Please select a screening", false);
            return;
        }
        TicketsNumberDialog sellTickets = new TicketsNumberDialog();
        Optional<TicketInfo> result = sellTickets.showAndWait();

        if (result.isPresent()) {
            TicketInfo c = result.get();
            if (!ms.sellTickets(ms.getSelectedScreening(), c.number)) {
                showTicketsNumberDialog(c);
            }
            update();
        }
    }

    public void showDate() {
        displayedDate = datePicker.getValue();
        ms.setDate(displayedDate);
    }

    @Override
    public boolean message(String s, boolean confirm) {
        Alert alert;
        if (confirm) {
            alert = new Alert(AlertType.CONFIRMATION);
        } else {
            alert = new Alert(AlertType.WARNING);
        }
        alert.setContentText(s);

        // to confirm
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
}
