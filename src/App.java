import java.sql.Connection;

public class App {

    public static void main(String[] args) {

        System.out.println("Provant connexió amb MySQL...");

        Connection conn = DatabaseConnection.connect();

        if (conn != null) {
            System.out.println("Connexió correcta a tpv_botiga!");
            try {
                conn.close();   // tanquem la connexió de prova
            } catch (Exception e) {
                System.err.println("Error en tancar: " + e.getMessage());
            }
        } else {
            System.out.println(" No s'ha pogut connectar. Revisa MySQL i la contrasenya.");
        }
    }
}