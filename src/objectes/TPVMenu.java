package objectes;

import DAO.ClientDAO;
import DAO.ArticleDAO;
import DAO.TiquetDAO;
import DAO.LiniaFacturaDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class TPVMenu {

    private Scanner sc;

    private ClientDAO clientDAO;
    private ArticleDAO articleDAO;
    private TiquetDAO tiquetDAO;
    private LiniaFacturaDAO liniaDAO;

    public TPVMenu(ClientDAO clientDAO,
                   ArticleDAO articleDAO,
                   TiquetDAO tiquetDAO,
                   LiniaFacturaDAO liniaDAO) {

        this.sc = new Scanner(System.in);

        this.clientDAO = clientDAO;
        this.articleDAO = articleDAO;
        this.tiquetDAO = tiquetDAO;
        this.liniaDAO = liniaDAO;
    }

    public void iniciarVenda() {

        System.out.println("\n" + "=".repeat(50));
        System.out.println("                TPV");
        System.out.println("=".repeat(50));

        // =====================================================
        // CLIENT
        // =====================================================

        System.out.print("DNI del client: ");
        String dni = sc.nextLine().trim();

        Client client = clientDAO.getByDni(dni);

        if (client == null) {
            System.out.println("El client no existeix.");
            return;
        }

        // =====================================================
        // LLISTA DE LINIES
        // =====================================================

        ArrayList<LiniaFactura> linies = new ArrayList<>();

        // =====================================================
        // AFEGIR ARTICLES
        // =====================================================

        while (true) {

            System.out.println("\n" + "-".repeat(50));

            System.out.print("ID Article (0 per finalitzar): ");
            int idArticle = llegirEnter();

            if (idArticle == 0) {
                break;
            }

            Article article = articleDAO.getById(idArticle);

            if (article == null) {
                System.out.println("Aquest article no existeix.");
                continue;
            }

            System.out.println("Article: " + article.getNom());
            System.out.println("Stock disponible: " + article.getStock());

            System.out.print("Quantitat: ");
            int quantitat = llegirEnter();

            // =====================================================
            // COMPROVAR STOCK
            // =====================================================

            if (quantitat <= 0) {
                System.out.println("La quantitat ha de ser superior a 0.");
                continue;
            }

            if (article.getStock() < quantitat) {
                System.out.println("No hi ha stock suficient.");
                continue;
            }

            // =====================================================
            // CALCULS
            // =====================================================

            double base = article.getPreuBase() * quantitat;

            double iva = base * (article.getIva() / 100.0);

            double total = base + iva;

            // =====================================================
            // CREAR LINIA
            // =====================================================

            LiniaFactura linia = new LiniaFactura(
                    0,
                    idArticle,
                    quantitat,
                    base,
                    iva,
                    total
            );

            linies.add(linia);

            // =====================================================
            // ACTUALITZAR STOCK
            // =====================================================

            article.setStock(article.getStock() - quantitat);

            articleDAO.updateStock(article.getId(), article.getStock());

            System.out.println("Article afegit correctament.");
        }

        // =====================================================
        // COMPROVAR SI HI HA LINIES
        // =====================================================

        if (linies.isEmpty()) {
            System.out.println("No s'ha afegit cap article.");
            return;
        }

        // =====================================================
        // CALCULAR TOTALS
        // =====================================================

        double totalBase = 0;
        double totalIva = 0;
        double totalFinal = 0;

        for (LiniaFactura l : linies) {

            totalBase += l.getPreuBase();
            totalIva += l.getIva();
            totalFinal += l.getPreuFinal();
        }

        // =====================================================
        // CREAR TIQUET
        // =====================================================

        Tiquet tiquet = new Tiquet(
                LocalDate.now().toString(),
                dni,
                totalBase,
                totalIva,
                totalFinal
        );

        // =====================================================
        // GUARDAR TIQUET
        // =====================================================

        int idTiquet = tiquetDAO.insert(tiquet);

        // =====================================================
        // GUARDAR LINIES
        // =====================================================

        for (LiniaFactura l : linies) {

            l.setIdTiquet(idTiquet);

            liniaDAO.insert(l);
        }

        // =====================================================
        // MOSTRAR TIQUET
        // =====================================================

        System.out.println("\n" + "=".repeat(50));
        System.out.println("              TIQUET FINAL");
        System.out.println("=".repeat(50));

        System.out.println("Client: " + client.getNom());
        System.out.println("DNI: " + client.getDni());

        System.out.println("\nARTICLES:");

        for (LiniaFactura l : linies) {

            System.out.println(
                    "Article ID: " + l.getIdArticle()
                            + " | Quantitat: " + l.getQuantitat()
                            + " | Total: " + String.format("%.2f", l.getPreuFinal()) + "€"
            );
        }

        System.out.println("\n" + "-".repeat(50));

        System.out.println("TOTAL BASE: " + String.format("%.2f", totalBase) + "€");
        System.out.println("TOTAL IVA: " + String.format("%.2f", totalIva) + "€");
        System.out.println("TOTAL FINAL: " + String.format("%.2f", totalFinal) + "€");

        System.out.println("=".repeat(50));
    }

    // =====================================================
    // UTILITAT
    // =====================================================

    private int llegirEnter() {

        try {

            return Integer.parseInt(sc.nextLine().trim());

        } catch (NumberFormatException e) {

            System.out.print("Valor invàlid. Intenta-ho novament: ");
            return llegirEnter();
        }
    }
}