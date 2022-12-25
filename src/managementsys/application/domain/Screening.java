package managementsys.application.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Screening {

    private Screen screen;
    private Movie movie;
    private LocalTime time;
    private LocalDate date;
    private int soldTicketNumber;

    public Screening(Screen s, Movie m, LocalTime t, LocalDate d, int n) {
        screen = s;
        movie = m;
        time = t;
        date = d;
        soldTicketNumber = n;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setSoldTicketNumber(int soldTicketNumber) {
        this.soldTicketNumber = soldTicketNumber;
    }

    public Screen getScreen() {
        return screen;
    }

    public Movie getMovie() {
        return movie;
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getSoldTicketNumber() {
        return soldTicketNumber;
    }

    public String getDetails() {
        StringBuffer details = new StringBuffer(64);
        details.append(movie.getName());
        details.append("-");
        details.append(movie.getYear());
        details.append(" (");
        details.append(movie.getDuration());
        details.append("min)");
        details.append("\n");
        details.append(screen.getNumber());
        details.append("-");
        details.append(screen.getScreenName());
        details.append(" (");
        details.append(screen.getCapacity() - soldTicketNumber);
        details.append(")");
        return details.toString();
    }
}
