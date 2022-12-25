package managementsys.presentationa;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScreeningInfo {
    int     screen_number;
    String     movie_name;
    int        movie_year;
    LocalTime  time;
    LocalDate  date;
    int     soldTicketNumber;

    public ScreeningInfo(int sn, String mn, int my, LocalTime t, LocalDate d, int stn) {
        screen_number = sn;
        movie_name = mn;
        movie_year = my;
        time = t;
        date = d;
        soldTicketNumber = stn;
    }
}
