package managementsys.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCreator {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:./bs.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE  IF NOT EXISTS MovieOid (\r\n" +
                        "       last_id     INT NOT NULL\r\n" +
                        ") ;";
                stmt.execute(sql);
                conn.createStatement();
                sql = "INSERT INTO MovieOid VALUES ('0')";
                stmt.execute(sql);
                stmt = conn.createStatement();
                sql = "CREATE TABLE  IF NOT EXISTS ScreeningOid (\r\n" +
                        "       last_id     INT NOT NULL\r\n" +
                        ") ;";
                stmt.execute(sql);
                conn.createStatement();
                sql = "INSERT INTO ScreeningOid VALUES ('0')";
                stmt.execute(sql);
                stmt = conn.createStatement();
                sql = "CREATE TABLE  IF NOT EXISTS Screen (\r\n" +
                        "       number      INT NOT NULL PRIMARY KEY,\r\n" +
                        "       screenname  VARCHAR(32),\r\n" +
                        "       capacity    INT NOT NULL\r\n" +
                        ") ;";
                stmt.execute(sql);
                stmt = conn.createStatement();
                sql = "CREATE TABLE Movie (\r\n" +
                        "       oid      INT NOT NULL PRIMARY KEY,\r\n" +
                        "       name     VARCHAR(32),\r\n" +
                        "       year     INT NOT NULL,\r\n" +
                        "       duration INT NOT NULL\r\n" +
                        ") ;";
                stmt.execute(sql);
                stmt = conn.createStatement();
                sql = "CREATE TABLE  IF NOT EXISTS Screening (\r\n" +
                        "       oid           INT NOT NULL PRIMARY KEY,\r\n" +
                        "       screen_id     INT NOT NULL,\r\n" +
                        "       movie_id      INT NOT NULL,\r\n" +
                        "       date          VARCHAR(32),\r\n" +
                        "       time          VARCHAR(32),\r\n" +
                        "       tickets_sold  INT NOT NULL\r\n" +
                        ") ;";
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
