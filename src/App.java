import java.sql.Connection;
import java.util.Scanner;
import objectes.ArticleMenu;
import objectes.ArticleService;
import objectes.ClientMenu;
import objectes.ClientService;

public class App {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("=== SISTEMA DE GESTIÓ DE BOTIGA (TPV) ===\n");
        System.out.println("Connectant a la base de dades...\n");
        Connection conn = DatabaseConnection.connect();

        if (conn != null) {
            System.out.println("Connexió correcta a tpv_botiga!\n");

            ArticleService service = new ArticleService(conn);
            ArticleMenu articleMenu = new ArticleMenu(service);

            boolean sortir = false;

            while (!sortir) {
                System.out.println("\n" + "==================================================");
                System.out.println("      SISTEMA DE GESTIÓ DE BOTIGA - TPV");
                System.out.println( "==================================================");
                System.out.println("1. Importació d'articles des de JSON");
                System.out.println("2. Gestió d'articles");
                System.out.println("3. Gestió de clients");
                System.out.println("4. TPV - Registrar venda");
                System.out.println("5. Consultes de vendes per client");
                System.out.println("6. Consultes de vendes per article");
                System.out.println("7. Càlcul de beneficis totals");
                System.out.println("8. Recompra automàtica d'articles");
                System.out.println("0. Sortir");
                System.out.println( "==================================================");
                System.out.print("Selecciona una opció: ");

                int opcio;
                try {
                    opcio = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Opció no vàlida.");
                    continue;
                }

                switch (opcio) {
                    case 1:
                        articleMenu.importarDesDeJSON();
                        break;
                    case 2:
                        articleMenu.mostraMenu();
                        break;
                    case 3:
                        ClientService clientService = new ClientService(conn);
                        ClientMenu clientMenu = new ClientMenu(clientService);
                        clientMenu.mostraMenu();
                        break;
                    case 4:
                        DAO.ClientDAO clientDAOtpv    = new DAO.ClientDAO(conn);
                        DAO.ArticleDAO articleDAOtpv  = new DAO.ArticleDAO(conn);
                        DAO.TiquetDAO tiquetDAO       = new DAO.TiquetDAO(conn);
                        DAO.LiniaFacturaDAO liniaDAO  = new DAO.LiniaFacturaDAO(conn);
                        objectes.TPVMenu tpvMenu = new objectes.TPVMenu(clientDAOtpv, articleDAOtpv, tiquetDAO, liniaDAO);
                        tpvMenu.iniciarVenda();
                        break;
                    case 5:
                        DAO.TiquetDAO tiquetDAOconsulta = new DAO.TiquetDAO(conn);
                        tiquetDAOconsulta.consultaVendesPerClient();
                        break;
                    case 6:
                        DAO.LiniaFacturaDAO liniaDAOconsulta = new DAO.LiniaFacturaDAO(conn);
                        liniaDAOconsulta.consultaVendesPerArticle();
                        break;
                    case 7:
                        System.out.println("\n[Aquí anirà el càlcul de beneficis]");
                        break;
                    case 8:
                        System.out.println("\n[Aquí anirà la recompra automàtica]");
                        break;
                    case 0:
                        sortir = true;
                        System.out.println("\nFins aviat!");
                        break;
                    default:
                        System.out.println("Opció no vàlida.");
                }
            }

            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("Connexió tancada correctament.");
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