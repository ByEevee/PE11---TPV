import java.sql.Connection;
import objectes.ArticleMenu;
import objectes.ArticleService;

public class App {

    public static void main(String[] args) {

        System.out.println("=== SISTEMA DE GESTIÓ DE BOTIGA (TPV) ===\n");

        
        System.out.println("Connectant a la base de dades...\n");
        Connection conn = DatabaseConnection.connect();

        if (conn != null) {
            System.out.println("Connexió correcta a tpv_botiga!\n");

            
            ArticleService service = new ArticleService(conn);

            
            ArticleMenu menu = new ArticleMenu(service);
            menu.mostraMenu();

            
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