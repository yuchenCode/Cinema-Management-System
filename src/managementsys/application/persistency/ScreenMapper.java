package managementsys.application.persistency;

import managementsys.application.domain.Screen;
import managementsys.storage.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenMapper {
    private Map<Integer, PersistentScreen> cache;

    private PersistentScreen getFromCache(int oid){
        return cache.get(oid);
    }

    private PersistentScreen getFromCacheByNumber(int sno){
        for (PersistentScreen s : cache.values()){
            if (s.getNumber() == sno){
                return s;
            }
        }
        return null;
    }

    private void addToCache(PersistentScreen s){
        cache.put(s.getId(), s);
    }

    private ScreenMapper(){
        cache = new HashMap<Integer, PersistentScreen>();
        getScreens();
    }

    private static ScreenMapper uniqueInstance;

    public static ScreenMapper getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ScreenMapper();
        }
        return uniqueInstance;
    }

    public PersistentScreen getScreen(int sno){
        PersistentScreen s = getFromCacheByNumber(sno);
        return s;
    }

    PersistentScreen getScreenForOid(int oid){
        PersistentScreen s = getFromCache(oid);
        return s;
    }

    public List<Screen> getScreens(){
        if (cache.size() == 0){
            List<Screen> v = new ArrayList<Screen>();
            try{
                Database.getInstance();
                Statement stmt = Database.getConnection().createStatement();
                ResultSet rset = stmt.executeQuery("SELECT ROWID, number, screenname, capacity FROM `Screen` ORDER BY number");
                while (rset.next()) {
                    // get data for each row
                    PersistentScreen s = new PersistentScreen(
                            rset.getInt("ROWID"),
                            rset.getInt("number"), rset.getString("screenname"), rset.getInt("capacity"));
                    v.add(s);
                    addToCache(s);
                }
                // close connection
                rset.close();
                stmt.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
            return v;
        } else {
            return new ArrayList<Screen>(cache.values());
        }
    }
}
