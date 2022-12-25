package managementsys.application.persistency;

import managementsys.application.domain.Screening;
import managementsys.storage.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ScreeningMapper {

    private static ScreeningMapper uniqueInstance;

    public static ScreeningMapper getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ScreeningMapper();
        }
        return uniqueInstance;
    }

    public List<Screening> getScreenings(LocalDate currentDate) {
        List<Screening> v = new ArrayList<Screening>();
        try {
            Database.getInstance();
            Statement stmt = Database.getConnection().createStatement();
            ResultSet rset = stmt.executeQuery("SELECT * FROM Screening WHERE date='" + currentDate + "'");
            while (rset.next()) {
                // get data for each row
                int oid = rset.getInt("oid");
                int screen = rset.getInt("screen_id");
                int movie = rset.getInt("movie_id");
                LocalTime stime = LocalTime.parse(rset.getString("time"));
                LocalDate sdate = LocalDate.parse(rset.getString("date"));
                PersistentScreen s = ScreenMapper.getInstance().getScreenForOid(screen);
                PersistentMovie m = MovieMapper.getInstance().getMovieForOid(movie);
                int n_tickets_sold = rset.getInt("tickets_sold");
                PersistentScreening sc = new PersistentScreening(oid, s, m, stime, sdate, n_tickets_sold);
                v.add(sc);
            }
            // close connection
            rset.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return v;
    }

    public PersistentScreening addScreening(PersistentScreen s, PersistentMovie m, LocalTime time, LocalDate date) {
        int oid = Database.getInstance().getScreeningOid();
        performUpdate("INSERT INTO Screening " + "VALUES ('" + oid + "', '" + s.getId() + "', '" + m.getId() + "', '" + date.toString() + "', '"
                + time.toString() + "', '" + 0 + "')");
        return new PersistentScreening(oid, s, m, time, date, 0);
    }

    public void updateScreening(Screening sc) {
        PersistentScreening ps = (PersistentScreening) sc;
        StringBuffer sql = new StringBuffer(128);

        sql.append("UPDATE Screening SET ");
        sql.append(" screen_id = ");
        sql.append(((PersistentScreen) ps.getScreen()).getId());
        sql.append(", movie_id = ");
        sql.append(((PersistentMovie) ps.getMovie()).getId());
        sql.append(", date = '");
        sql.append(ps.getDate().toString());
        sql.append("', time = '");
        sql.append(ps.getTime().toString());
        sql.append("' WHERE oid = ");
        sql.append(ps.getId());
        performUpdate(sql.toString());
    }

    public void deleteScreening(Screening sc) {
        performUpdate("DELETE FROM Screening WHERE oid = '" + ((PersistentScreening) sc).getId() + "'");
    }

    public void sellTickets(Screening sc, int n){
        int n_tickets_sold = sc.getSoldTicketNumber();
        int oid = ((PersistentScreening) sc).getId();
        performUpdate("UPDATE Screening SET tickets_sold = " + (n_tickets_sold+n) + " WHERE oid = '"+ oid + "'");
        sc.setSoldTicketNumber(n_tickets_sold+n);
    }

    private void performUpdate(String sql) {
        try {
            Database.getInstance();
            Statement stmt = Database.getConnection().createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}