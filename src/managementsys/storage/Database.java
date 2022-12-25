package managementsys.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Connection con;

    // Singleton:

    private static Database   uniqueInstance;

    public static Database getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Database();
        }
        return uniqueInstance;
    }

    private Database() {
        try {
            con = DriverManager.getConnection("jdbc:sqlite:./bs.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return con;
    }

    public int getMovieOid() {
        // get a new movie oid
        int id = 0;
        try {
            Statement stmt = con.createStatement();

            ResultSet rset = stmt.executeQuery("SELECT * FROM MovieOid");
            while (rset.next()) {
                id = rset.getInt(1);
            }
            rset.close();

            id++;

            stmt.executeUpdate("UPDATE MovieOid SET last_id = '" + id + "'");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    public int getScreeningOid() {
        // get a new screening oid
        int id = 0;
        try {
            Statement stmt = con.createStatement();

            ResultSet rset = stmt.executeQuery("SELECT * FROM ScreeningOid");
            while (rset.next()) {
                id = rset.getInt(1);
            }
            rset.close();

            id++;

            stmt.executeUpdate("UPDATE ScreeningOid SET last_id = '" + id + "'");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
