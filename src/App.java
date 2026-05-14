import java.sql.Connection;
import objectes.ArticleMenu;
import objectes.ArticleService;

public class App {

    public static void main(String[] args) {

        System.out.println("=== SISTEMA DE GESTIÓ DE BOTIGA (TPV) ===\n");

        // Conexión a la base de datos
        System.out.println("Connectant a la base de dades...\n");
        Connection conn = DatabaseConnection.connect();

        if (conn != null) {
            System.out.println("Connexió correcta a tpv_botiga!\n");

            // Crear servicio de articles
            ArticleService service = new ArticleService(conn);

            // Crear menú y mostrarlo
            ArticleMenu menu = new ArticleMenu(service);
            menu.mostraMenu();

            // Tancar la connexió al salir del menú
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    System.out.println("\nConnexió tancada correctament.");
                }
            } catch (Exception e) {
                System.err.println("Error tancant la connexió: " + e.getMessage());
            }

        } else {
            System.err.println("No s'ha pogut connectar a la base de dades.");
            System.err.println("Revisa que:");
            System.err.println("  - MySQL estigui en execució");
            System.err.println("  - La base de dades 'tpv_botiga' existeixi");
            System.err.println("  - L'usuari i contrasenya siguin correctes");
        }
    }
}