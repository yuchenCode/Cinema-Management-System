package managementsys.application.domain;

import managementsys.application.persistency.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Cinema {
    // Mappers
    ScreenMapper sm = ScreenMapper.getInstance();
    MovieMapper mm = MovieMapper.getInstance();
    ScreeningMapper sim = ScreeningMapper.getInstance();

    Screen getScreen(int n) {
        // get the screen according to the screen number
        return sm.getScreen(n);
    }

    Movie getMovie(String name, int year) {
        // get a movie according to the given name and year
        return mm.getMovie(name, year);
    }

    List<Screening> getScreenings(LocalDate currentDate) {
        // get all screenings according to the given date
        return sim.getScreenings(currentDate);
    }

    List<Movie> getMovies() {
        // get all movies in the cinema
        return MovieMapper.getInstance().getMovies();
    }

    static List<Screen> getScreens(){
        // get all screens in the cinema
        return ScreenMapper.getInstance().getScreens();
    }

    public Screening addScreening(LocalDate date, LocalTime stime, int sno, String mname, int my) {
        // add a new screening
        Screen s = getScreen(sno);
        Movie m = getMovie(mname, my);
        return sim.addScreening((PersistentScreen) s, (PersistentMovie) m, stime, date);
    }

    public Movie addMovie(String name, int year, int duration) {
        // add a new movie
        return mm.addMovie(name, year, duration);
    }

    public void updateScreening(Screening b) {
        // update screening
        sim.updateScreening(b);
    }

    public void removeScreening(Screening b) {
        // remove screening
        sim.deleteScreening(b);
    }

    public void sellTickets(Screening si, int n){
        // sell tickets
        sim.sellTickets(si, n);
    }
}
