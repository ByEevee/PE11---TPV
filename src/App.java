import java.sql.Connection;
import java.sql.ResultSet;
import DAO.DatabaseManager;

public class App {

    public static void main(String[] args) {

        System.out.println("=== PROVANT CONNEXIÓ AMB MYSQL ===\n");

        Connection conn = DatabaseConnection.connect();

        if (conn != null) {
            System.out.println("Connexió correcta a tpv_botiga!\n");

            // Crear la DAO
            DatabaseManager db = new DatabaseManager(conn);

            System.out.println("=== PROVANT DAO ===");
            System.out.println("DAO funcionant\n");

            // Prueba SELECT real
            System.out.println("=== PROVANT SELECT ==="  );
            System.out.println("Llistant articles de la taula:\n");

            ResultSet rs = db.executeQuery("SELECT id, nom, familia, preu_base FROM articles");

            if (rs != null) {
                try {
                    int count = 0;
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nom = rs.getString("nom");
                        String familia = rs.getString("familia");
                        double preu = rs.getDouble("preu_base");

                        System.out.println(count + 1 + ". [" + id + "] " + nom + " (" + familia + ") - " + preu + "€");
                        count++;
                    }
                    System.out.println("\nTotal: " + count + " articles\n");

                } catch (Exception e) {
                    System.err.println("Error llegint resultats: " + e.getMessage());
                }
            }

            // Tancar la DAO
            db.close();

        } else {
            System.out.println("No s'ha pogut connectar. Revisa MySQL i la contrasenya.");
        }
    }
}