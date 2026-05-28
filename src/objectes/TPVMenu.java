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

        int idArticle;
        do {
            System.out.println("\n" + "-".repeat(50));

            System.out.print("ID Article (0 per finalitzar): ");
            idArticle = llegirEnter();


            Article article = articleDAO.getById(idArticle);

            if (article == null) {
                System.out.println("Aquest article no existeix.");
                continue;
            }

            System.out.println("Article: " + article.getNom());
            System.out.println("Stock disponible: " + article.getStock());

            // =====================================================
            // COMPROVAR STOCK = 0 (BLOQUEIG)
            // =====================================================

            if (article.getStock() == 0) {
                System.out.println("---------------------------------------------------");
                System.out.println("  ARTICLE SENSE STOCK - No es pot vendre.");
                System.out.println("  L'article \"" + article.getNom() + "\" no té");
                System.out.println("  unitats disponibles en aquest moment.");
                System.out.println("---------------------------------------------------");
                continue;
            }

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
                System.out.println("Stock insuficient. Stock disponible: " + article.getStock() + " unitat(s).");
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
        }while (idArticle != 0);

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

        imprimirTiquet(client, linies, idTiquet, totalBase, totalIva, totalFinal);

    }


    // =====================================================
    // IMPRESSIÓ DEL TIQUET
    // =====================================================

    private void imprimirTiquet(Client client, ArrayList<LiniaFactura> linies,
                                 int idTiquet, double totalBase, double totalIva, double totalFinal) {

        String sep  = "=".repeat(50);
        String line = "-".repeat(50);

        // CAPÇALERA
        System.out.println("\n" + sep);
        System.out.println("           BOTIGA TPV");
        System.out.println(sep);
        System.out.printf("  Tiquet núm.: %-10d%n", idTiquet);
        System.out.printf("  Data:        %-10s%n", LocalDate.now());
        System.out.println(line);
        System.out.printf("  Client: %s%n", client.getNom());
        System.out.printf("  DNI:    %s%n", client.getDni());
        System.out.println(line);

        // LÍNIES
        System.out.println("  ARTICLES:");
        System.out.println();
        for (LiniaFactura l : linies) {
            Article article = articleDAO.getById(l.getIdArticle());
            String nomArticle = (article != null) ? article.getNom() : "Article " + l.getIdArticle();
            System.out.printf("  %-25s x%d%n", nomArticle, l.getQuantitat());
            System.out.printf("  %-25s %8.2f $%n", "  Base:", l.getPreuBase());
            System.out.printf("  %-25s %8.2f $%n", "  IVA:", l.getIva());
            System.out.printf("  %-25s %8.2f $%n", "  Total:", l.getPreuFinal());
            System.out.println();
        }

        // TOTALS
        System.out.println(line);
        System.out.printf("  %-25s %8.2f $%n", "TOTAL BASE:",  totalBase);
        System.out.printf("  %-25s %8.2f $%n", "TOTAL IVA:",   totalIva);
        System.out.printf("  %-25s %8.2f $%n", "TOTAL FINAL:", totalFinal);

        // PEU
        System.out.println(sep);
        System.out.println("       Gràcies per la seva compra!");
        System.out.println(sep + "\n");
    }

    private int llegirEnter() {

        try {

            return Integer.parseInt(sc.nextLine().trim());

        } catch (NumberFormatException e) {

            System.out.print("Valor invàlid. Intenta-ho novament: ");
            return llegirEnter();
        }
    }
}