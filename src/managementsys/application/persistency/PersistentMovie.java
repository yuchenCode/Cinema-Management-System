package managementsys.application.persistency;

import managementsys.application.domain.Movie;

public class PersistentMovie extends Movie {

    private int oid;

    PersistentMovie(int id, String n, int y, int d) {
        super(n, y, d);
        oid = id;
    }

    public int getId() {
        return oid;
    }
}
