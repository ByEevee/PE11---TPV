import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import DAO.DatabaseManager;
import objectes.Article;

public class App {

    public static void main(String[] args) {

        System.out.println("=== PROVANT CONNEXIÓ AMB MYSQL ===\n");

        Connection conn = DatabaseConnection.connect();

        if (conn != null) {
            System.out.println("Connexió correcta a tpv_botiga!\n");

            // LECTURA JSON 
            System.out.println("=== PROVANT LECTURA JSON  ===");
            JSONConnection jsonConn = new JSONConnection();
            
            // Passem la ruta relativa segons la teva estructura de carpetes
            String rutaJson = "src/BBDD/PE11_articles.json"; 
            List<Article> articlesImportats = jsonConn.leerArticles(rutaJson);

            if (!articlesImportats.isEmpty()) {
                System.out.println("S'han llegit " + articlesImportats.size() + " articles del fitxer JSON correctament.\n");
                
                // Mostrar un parell per verificar que l'estructura en memòria és correcta 
                for (int i = 0; i < Math.min(3, articlesImportats.size()); i++) {
                    Article a = articlesImportats.get(i);
                    System.out.println("Importat: " + a.getNom() + " [" + a.getFamilia() + "]");
                }
                System.out.println("");
            } else {
                System.out.println("Atenció: No s'han pogut carregar articles del JSON. Revisa la ruta: " + rutaJson + "\n");
            }

            //PROVA DE LA DAO
            DatabaseManager db = new DatabaseManager(conn);

            System.out.println("=== PROVANT DAO ===");
            System.out.println("DAO funcionant\n");

            // Prova SELECT real
            System.out.println("=== PROVANT SELECT ===");
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
                    System.out.println("\nTotal: " + count + " articles a la BD\n");

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