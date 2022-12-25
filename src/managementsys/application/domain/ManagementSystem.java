package managementsys.application.domain;

import managementsys.application.persistency.PersistentScreening;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ManagementSystem {

    private LocalDate currentDate;
    private Cinema cinema = null;
    private List<Screening> currentScreenings;
    private List<Movie> currentMovies;
    private Screening selectedScreening;

    private List<ManagementObserver> observers  = new ArrayList<ManagementObserver>();
    private static ManagementSystem uniqueInstance;

    private ManagementSystem() {
        // constructor
        cinema = new Cinema();
    }

    public static ManagementSystem getInstance() {
        // get a ManagementSystem instance
        if (uniqueInstance == null) {
            uniqueInstance = new ManagementSystem();
        }
        return uniqueInstance;
    }

    public void addObserver(ManagementObserver o) {
        // add a new observer
        observers.add(o);
    }

    public void notifyObservers() {
        // inform all the observers about the changes
        for (ManagementObserver mo : observers) {
            mo.update();
        }
    }

    public boolean observerMessage(String message, boolean confirm) {
        // inform frontend of message
        ManagementObserver mo = (ManagementObserver) observers.get(0);
        return mo.message(message, confirm);
    }

    public void setDate(LocalDate date) {
        // set current date
        currentDate = date;
        currentMovies = cinema.getMovies();
        currentScreenings = cinema.getScreenings(currentDate);
        selectedScreening = null;
        notifyObservers();
    }

    public boolean addScreening(LocalDate date, LocalTime stime, int sno, String mname, int my) {
        // add a new screening
        if (!checkScreeningConflict(
                date, stime, sno,
                cinema.getMovie(mname, my).getDuration(),
                -1)) {
            Screening s = cinema.addScreening(date, stime, sno, mname, my);
            currentScreenings.add(s);
            notifyObservers();
            return true;
        }
        observerMessage("Screening conflict exist!", false);
        return false;
    }

    public boolean addMovie(String name, int year, int duration) {
        // add a new movie
        if (!checkMovieExist(name, year, duration)) {
            Movie m = cinema.addMovie(name, year, duration);
            currentMovies.add(m);
            notifyObservers();
            return true;
        }
        return false;
    }

    public void selectScreening(int sno, LocalTime time) {
        // select a screening
        selectedScreening = null;
        for (Screening si : currentScreenings) {
            if (si.getScreen().getNumber() == sno) {
                LocalTime end_time = si.getTime().plusMinutes(si.getMovie().getDuration());
                if (si.getTime().isBefore(time) && end_time.isAfter(time)) {
                    selectedScreening = si;
                }
            }
        }
        notifyObservers();
    }

    public void cancelSelected() {
        // cancel the selected screening
        if (selectedScreening != null) {
            // check whether the tickets of the screening is started to sell
            if (!checkTicketsSold(selectedScreening)){
                // confirm
                if (observerMessage("Are you sure?", true)) {
                    currentScreenings.remove(selectedScreening);
                    cinema.removeScreening(selectedScreening);
                    selectedScreening= null;
                    notifyObservers();
                }
            } else {
                observerMessage("Tickets selling has been started!", false);
            }
        } else {
            // no screening selected
            observerMessage("Please select a screening", false);
        }
    }

    public void changeSelected(LocalDate date, LocalTime time, int sno) {
        if (selectedScreening != null) {
            // check whether the tickets of the screening is started to sell
            if (!checkTicketsSold(selectedScreening)){
                // check screening conflict
                if (!checkScreeningConflict(
                        date, time, sno,
                        selectedScreening.getMovie().getDuration(),
                        ((PersistentScreening)selectedScreening).getId())) {
                    selectedScreening.setTime(time);
                    selectedScreening.setScreen(cinema.getScreen(sno));
                    cinema.updateScreening(selectedScreening);
                    notifyObservers();
                } else {
                    observerMessage("Screening conflict exist!", false);
                }
            } else {
                observerMessage("Tickets selling has been started!", false);
            }
        }
    }

    public boolean sellTickets(Screening sc, int n){
        if (!checkTicketsInsufficient(sc, n)){
            // tickets is enough
            cinema.sellTickets(sc, n);
            return true;
        } else {
            observerMessage("Tickets insufficient", false);
            return false;
        }
    }

    private boolean checkMovieExist(String name, int year, int duration) {
        // check whether the movie is existed
        boolean movieExist = false;
        for (Movie m : currentMovies) {
            if (m.getName().equals(name) && m.getYear() == year && m.getDuration() == duration) {
                movieExist = true;
                observerMessage("Movie Exist!", false);
            }
        }
        return movieExist;
    }

    private boolean checkScreeningConflict(LocalDate date, LocalTime time, int sno, int duration, int id) {
        boolean screeningConflict = false;
        List<Screening> sil = cinema.getScreenings(date);

        for (Screening si : sil) {
            if (id != -1){
                // if it is not a new screening
                if (id == ((PersistentScreening)si).getId()){
                    // skip the current screening
                    continue;
                }
            }
            // check the screening is start and finished between 10:00-22:00
            if (time.isBefore(LocalTime.of(10, 0)) || time.plusMinutes(duration).isAfter(LocalTime.of(22, 0))){
                return true;
            }
            if (sno == si.getScreen().getNumber()){
                LocalTime end_time = si.getTime().plusMinutes(si.getMovie().getDuration());
                LocalTime new_end_time = time.plusMinutes(duration);
                // check whether the start time and end time is not in the time range of another screening
                if (si.getTime().isBefore(time) && end_time.isAfter(time)) {
                    return true;
                }
                if (si.getTime().isBefore(new_end_time) && end_time.isAfter(new_end_time)){
                    return true;
                }
                // check whether the time range is in the time range of another screening
                if (time.isBefore(si.getTime()) && new_end_time.isAfter(end_time)){
                    return true;
                }
            }
        }
        return screeningConflict;
    }

    private boolean checkTicketsSold(Screening sc){
        // check whether the tickets selling is started
        return sc.getSoldTicketNumber() > 0;
    }

    private boolean checkTicketsInsufficient(Screening sc, int n){
        // check whether the tickets of the screening is enough
        if (sc.getSoldTicketNumber() + n > sc.getScreen().getCapacity()){
            return true;
        }
        return false;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public List<Screening> getScreenings() {
        return new ArrayList<Screening>(currentScreenings);
    }

    public List<Movie> getMovies() {
        return new ArrayList<Movie>(currentMovies);
    }

    public Screening getSelectedScreening() {
        return selectedScreening;
    }

    public static List<Screen> getScreens() {
        return Cinema.getScreens();
    }
}

