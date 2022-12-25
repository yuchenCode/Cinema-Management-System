package managementsys.application.persistency;

import managementsys.application.domain.Movie;
import managementsys.application.domain.Screen;
import managementsys.storage.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieMapper {

    private Map<Integer, PersistentMovie> cache;

    private PersistentMovie getFromCache(int oid){
        return (PersistentMovie) cache.get(oid);
    }

    private PersistentMovie getFromCacheByDetails(String title, int year){
        for (PersistentMovie m : cache.values()){
            if (title.equals(m.getName()) && year == m.getYear()){
                return m;
            }
        }
        return null;
    }

    private void addToCache(PersistentMovie m){
        cache.put(m.getId(), m);
    }

    private MovieMapper() {
        cache = new HashMap<Integer, PersistentMovie>();
    }

    private static MovieMapper uniqueInstance;

    public static MovieMapper getInstance(){
        if (uniqueInstance == null){
            uniqueInstance = new MovieMapper();
        }
        return uniqueInstance;
    }

    public PersistentMovie getMovie(String n, int y){
        // get a movie according to name and year
        PersistentMovie m = getFromCacheByDetails(n, y);
        if (m == null){
            m = getMovie("SELECT * FROM Movie WHERE name = '" + n + "' AND year = '" + y + "'");
            if (m != null){
                addToCache(m);
            }
        }
        return m;
    }

    PersistentMovie getMovieForOid(int oid){
        // get a movie according to oid
        PersistentMovie m = getFromCache(oid);
        if (m == null){
            m = getMovie("SELECT * FROM Movie WHERE oid ='" + oid + "'");
            if (m != null){
                addToCache(m);
            }
        }
        return m;
    }

    public PersistentMovie addMovie(String name, int year, int duration){
        // add a new movie
        PersistentMovie m = getFromCacheByDetails(name, year);
        int oid = Database.getInstance().getMovieOid();
        if (m == null){
            try{
                Database.getInstance();
                Statement stmt = Database.getConnection().createStatement();
                stmt.executeUpdate("INSERT INTO Movie (oid, name, year, duration)" + "VALUES ('" + oid + "', '" + name + "', '" + year + "', '" + duration + "')");
                stmt.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
            m = getMovie(name, year);
        }
        return m;
    }

    private PersistentMovie getMovie(String sql){
        // get movie use sql
        PersistentMovie m = null;
        try {
            Database.getInstance();
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rset = stmt.executeQuery(sql);
            while (rset.next()){
                // get date
                int oid = rset.getRow();
                String name = rset.getString("name");
                int year = rset.getInt("year");
                int duration = rset.getInt("duration");
                m = new PersistentMovie(oid, name, year, duration);
            }
            rset.close();
            stmt.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return m;
    }

    public List<Movie> getMovies(){
        // get movies
        if (cache.size() == 0){
            List<Movie> v = new ArrayList<Movie>();
            try{
                Database.getInstance();
                Statement stmt = Database.getConnection().createStatement();
                ResultSet rset = stmt.executeQuery("SELECT oid, name, year, duration FROM `Movie` ORDER BY oid");
                while (rset.next()) {
                    // get data for each row
                    PersistentMovie m = new PersistentMovie(
                            rset.getInt("oid"),
                            rset.getString("name"), rset.getInt("year"), rset.getInt("duration"));
                    v.add(m);
                    addToCache(m);
                }
                // close connection
                rset.close();
                stmt.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
            return v;
        } else {
            return new ArrayList<Movie>(cache.values());
        }
    }
}
