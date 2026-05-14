package objectes;
import java.util.Scanner;

/**
 * Menú interactiu per a l'alta d'articles (camises i pantalons).
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

        System.out.print("Preu base (€): ");
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

        System.out.print("Preu base (€): ");
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
