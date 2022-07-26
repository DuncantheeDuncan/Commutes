package co_mute.dbcon;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String url1 = "jdbc:mysql://localhost:3306/co_mute";
    private static final String user = "root";
    private static final String password = "toor@234!";


    public static Connection EstablishDbConnection() throws SQLException {
        Connection conn = null;
        try {

            conn = DriverManager.getConnection(url1, user, password);

            if (conn != null) {
                System.out.println("Established connection");
            }
        } catch (SQLException s) {
            return (Connection) conn.getWarnings();
        }

        return conn;
    }

    public void RunSqlScrip(Connection conn) throws FileNotFoundException {

        ScriptRunner sr = new ScriptRunner(conn);
        Reader reader = new BufferedReader(new FileReader("src/main/resources/runScript.sql"));
        sr.runScript(reader);
    }


}
