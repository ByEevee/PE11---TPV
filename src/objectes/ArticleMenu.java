package objectes;
import java.util.Scanner;

/**
 * Menú interactiu per a la gestió d'articles (camises i pantalons).
 */
public class ArticleMenu {

    private Scanner sc;
    private ArticleService service;

    public ArticleMenu(ArticleService service) {
        this.service = service;
        this.sc = new Scanner(System.in);
    }

    /**
     * Mostra el menú principal d'articles.
     */
    public void mostraMenu() {
        boolean tornar = false;

        while (!tornar) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           GESTIÓ D'ARTICLES");
            System.out.println("=".repeat(50));
            System.out.println("1. Afegir una camisa");
            System.out.println("2. Afegir un pantaló");
            System.out.println("3. Veure tots els articles");
            System.out.println("4. Veure camises");
            System.out.println("5. Veure pantalons");
            System.out.println("6. Modificar un article");
            System.out.println("7. Eliminar un article");
            System.out.println("8. Importar articles des de JSON");
            System.out.println("0. Tornar enrere");
            System.out.println("=".repeat(50));
            System.out.print("Selecciona una opció: ");

            int opcio = llegirEnter();

            switch (opcio) {
                case 1:
                    afegirCamisa();
                    break;
                case 2:
                    afegirPantalo();
                    break;
                case 3:
                    verArticles();
                    break;
                case 4:
                    verCamises();
                    break;
                case 5:
                    verPantalons();
                    break;
                case 6:
                    modificarArticle();
                    break;
                case 7:
                    eliminarArticle();
                    break;
                case 8:
                    importarDesDeJSON();
                    break;
                case 0:
                    tornar = true;
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        }
    }

    // =========================================================
    // ALTA DE CAMISES
    // =========================================================

    private void afegirCamisa() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("ALTA DE CAMISA");
        System.out.println("-".repeat(50));

        System.out.print("ID: ");
        int id = llegirEnter();

        System.out.print("Nom: ");
        String nom = sc.nextLine().trim();

        System.out.print("Preu base ($): ");
        double preu = llegirDouble();

        System.out.print("IVA (4, 10 o 21): ");
        int iva = llegirEnter();

        System.out.print("Stock inicial: ");
        int stock = llegirEnter();

        System.out.print("Talla de coll (36-52): ");
        int tallaColl = llegirEnter();

        System.out.print("Amplada de pit (10-15): ");
        int ampladaPit = llegirEnter();

        boolean resultat = service.altaCamisa(id, nom, preu, iva, stock, tallaColl, ampladaPit);

        if (!resultat) {
            System.out.println("\nIntenta-ho novament amb dades vàlides.");
        }
    }

    // =========================================================
    // ALTA DE PANTALONS
    // =========================================================

    private void afegirPantalo() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("ALTA DE PANTALÓ");
        System.out.println("-".repeat(50));

        System.out.print("ID: ");
        int id = llegirEnter();

        System.out.print("Nom: ");
        String nom = sc.nextLine().trim();

        System.out.print("Preu base ($): ");
        double preu = llegirDouble();

        System.out.print("IVA (4, 10 o 21): ");
        int iva = llegirEnter();

        System.out.print("Stock inicial: ");
        int stock = llegirEnter();

        System.out.print("Talla de cintura (24-56): ");
        int tallaCintura = llegirEnter();

        System.out.print("Llargada del camal (32-46): ");
        int llargadaCamal = llegirEnter();

        boolean resultat = service.altaPantalo(id, nom, preu, iva, stock, tallaCintura, llargadaCamal);

        if (!resultat) {
            System.out.println("\nIntenta-ho novament amb dades vàlides.");
        }
    }

    // =========================================================
    // MODIFICACIÓ D'ARTICLES
    // =========================================================

    /**
     * Demana l'ID de l'article a modificar i sol·licita les noves dades.
     */
    private void modificarArticle() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("MODIFICACIÓ D'ARTICLE");
        System.out.println("-".repeat(50));

        System.out.print("ID de l'article a modificar: ");
        int id = llegirEnter();

        Article article = service.getArticleDAO().getById(id);

        if (article == null) {
            System.out.println("\nNo existeix cap article amb aquest ID.");
            return;
        }

        System.out.println("\nArticle actual:");
        System.out.println(article);

        System.out.print("\nNom: ");
        String nom = sc.nextLine().trim();

        System.out.print("Preu base ($): ");
        double preu = llegirDouble();

        System.out.print("IVA (4, 10 o 21): ");
        int iva = llegirEnter();

        System.out.print("Stock: ");
        int stock = llegirEnter();

        boolean resultat = false;

        if (article instanceof Camisa) {
            System.out.print("Talla de coll (36-52): ");
            int tallaColl = llegirEnter();

            System.out.print("Amplada de pit (10-15): ");
            int ampladaPit = llegirEnter();

            resultat = service.modificarArticle(id, nom, preu, iva, stock, tallaColl, ampladaPit);

        } else if (article instanceof Pantalo) {
            System.out.print("Talla de cintura (24-56): ");
            int tallaCintura = llegirEnter();

            System.out.print("Llargada del camal (32-46): ");
            int llargadaCamal = llegirEnter();

            resultat = service.modificarArticle(id, nom, preu, iva, stock, tallaCintura, llargadaCamal);
        }

        if (!resultat) {
            System.out.println("\nIntenta-ho novament amb dades vàlides.");
        }
    }

    // =========================================================
    // BAIXA D'ARTICLES
    // =========================================================

    /**
     * Demana l'ID de l'article a eliminar i sol·licita confirmació.
     */
    private void eliminarArticle() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("BAIXA D'ARTICLE");
        System.out.println("-".repeat(50));

        System.out.print("ID de l'article a eliminar: ");
        int id = llegirEnter();

        // Confirmació abans d'eliminar
        System.out.print("Estàs segur que vols eliminar l'article amb ID " + id + "? (s/n): ");
        String confirmacio = sc.nextLine().trim().toLowerCase();

        if (!confirmacio.equals("s")) {
            System.out.println("Operació cancel·lada.");
            return;
        }

        boolean resultat = service.baixaArticle(id);

        if (!resultat) {
            System.out.println("\nNo s'ha pogut eliminar l'article. Comprova l'ID i torna-ho a intentar.");
        }
    }

    // =========================================================
    // CONSULTES
    // =========================================================

    private void verArticles() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("TOTS ELS ARTICLES");
        System.out.println("-".repeat(50));

        var articles = service.getArticleDAO().getAll();

        if (articles.isEmpty()) {
            System.out.println("No hi ha articles al catàleg.");
        } else {
            for (int i = 0; i < articles.size(); i++) {
                System.out.println((i + 1) + ". " + articles.get(i));
            }
            System.out.println("\nTotal: " + articles.size() + " articles");
        }
    }

    private void verCamises() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("CAMISES");
        System.out.println("-".repeat(50));

        var camises = service.getArticleDAO().getByFamilia("camisa");

        if (camises.isEmpty()) {
            System.out.println("No hi ha camises al catàleg.");
        } else {
            for (int i = 0; i < camises.size(); i++) {
                System.out.println((i + 1) + ". " + camises.get(i));
            }
            System.out.println("\nTotal: " + camises.size() + " camises");
        }
    }

    private void verPantalons() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("PANTALONS");
        System.out.println("-".repeat(50));

        var pantalons = service.getArticleDAO().getByFamilia("pantaló");

        if (pantalons.isEmpty()) {
            System.out.println("No hi ha pantalons al catàleg.");
        } else {
            for (int i = 0; i < pantalons.size(); i++) {
                System.out.println((i + 1) + ". " + pantalons.get(i));
            }
            System.out.println("\nTotal: " + pantalons.size() + " pantalons");
        }
    }

    // =========================================================
    // IMPORTACIÓ DES DE JSON
    // =========================================================

    private static final String RUTA_JSON = "src/BBDD/PE11_articles.json";

    private void importarDesDeJSON() {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("IMPORTACIÓ D'ARTICLES DES DE JSON");
        System.out.println("-".repeat(50));

        // 1. Mostrar recompte
        int[] recompte = service.comptarPerFamilia(RUTA_JSON);
        System.out.println("Articles trobats al fitxer:");
        System.out.println("  · Camises:  " + recompte[0]);
        System.out.println("  · Pantalons: " + recompte[1]);
        System.out.println("  · TOTAL:    " + (recompte[0] + recompte[1]));

        // 2. Confirmació
        System.out.print("\nVols importar aquests articles? (s/n): ");
        String resp = sc.nextLine().trim().toLowerCase();

        if (!resp.equals("s")) {
            System.out.println("Importació cancel·lada.");
            return;
        }

        // 3. Importar (INSERT o UPDATE automàtic)
        int[] resultat = service.importarDesDeJSON(RUTA_JSON);

        // 4. Informar del resultat
        System.out.println("\n--- RESULTAT DE LA IMPORTACIÓ ---");
        System.out.println("  ✔ Articles nous inserits: " + resultat[0]);
        System.out.println("  ✔ Articles actualitzats:  " + resultat[1]);
        System.out.println("  ✗ Errors:                 " + resultat[2]);
        System.out.println("---------------------------------");
    }
    // =========================================================
    // UTILITATS
    // =========================================================

    private int llegirEnter() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.print("Valor invàlid. Intenta-ho novament: ");
            return llegirEnter();
        }
    }

    private double llegirDouble() {
        try {
            return Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.print("Valor invàlid. Intenta-ho novament: ");
            return llegirDouble();
        }
    }
}