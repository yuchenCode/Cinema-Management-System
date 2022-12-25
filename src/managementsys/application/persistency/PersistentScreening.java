package managementsys.application.persistency;

import managementsys.application.domain.Movie;
import managementsys.application.domain.Screen;
import managementsys.application.domain.Screening;

import java.time.LocalDate;
import java.time.LocalTime;

public class PersistentScreening extends Screening {

    private int oid;

    PersistentScreening(int id, Screen s, Movie m, LocalTime t, LocalDate d, int n) {
        super(s, m, t, d, n);
        oid = id;
    }

    public int getId() {
        return oid;
    }
}