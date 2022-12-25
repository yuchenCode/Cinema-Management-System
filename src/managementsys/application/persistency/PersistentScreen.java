package managementsys.application.persistency;

import managementsys.application.domain.Screen;

public class PersistentScreen extends Screen {

    private int oid;

    PersistentScreen(int id, int no, String s, int c) {
        super(no, s, c);
        oid = id;
    }

    public int getId() {
        return oid;
    }

}
