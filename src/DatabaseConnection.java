import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // ===== CONFIGURACIÓ DE CONNEXIÓ =====
    private static final String URL      = "jdbc:mysql://localhost:3306/tpv_botiga";
    private static final String USER     = "root";
    private static final String PASSWORD = "";   

    /**
     * Retorna una connexió activa a la base de dades MySQL.
     * @return Connection o null si hi ha error
     */
    public static Connection connect() {
        try {
            

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;

        } catch (SQLException e) {
            System.err.println("Error de connexió: " + e.getMessage());
            return null;
        }
    }
}