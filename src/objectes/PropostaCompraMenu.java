package objectes;

import DAO.ArticleDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PropostaCompraMenu {

    private ArticleDAO articleDAO;
    private Scanner sc;

    public PropostaCompraMenu(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
        this.sc = new Scanner(System.in);
    }

    public void executar() {

        System.out.println("\n" + "=".repeat(55));
        System.out.println("       PROPOSTA DE RECOMPRA D'ARTICLES");
        System.out.println("=".repeat(55));

        System.out.print("Introdueix el llindar de stock: ");
        int llindar;
        try {
            llindar = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Valor invàlid.");
            return;
        }

        List<Article> articles = articleDAO.getArticlesPerSotaLlindar(llindar);

        if (articles.isEmpty()) {
            System.out.println("Cap article per sota del llindar " + llindar + ".");
            return;
        }

        // =====================================================
        // CONSTRUIR PROPOSTA
        // =====================================================

        List<PropostaCompra> proposta = new ArrayList<>();
        for (Article a : articles) {
            int quantitatSuggerida = llindar - a.getStock();
            proposta.add(new PropostaCompra(a.getId(), a.getNom(), quantitatSuggerida));
        }

        // =====================================================
        // MOSTRAR PROPOSTA
        // =====================================================

        String line = "-".repeat(55);
        System.out.println("\n" + line);
        System.out.printf("  %-6s %-25s %10s%n", "CODI", "NOM", "QUANTITAT");
        System.out.println(line);

        for (PropostaCompra p : proposta) {
            System.out.printf("  %-6d %-25s %10d%n",
                    p.getCodi(),
                    p.getNom(),
                    p.getQuantitat()
            );
        }

        System.out.println(line);

        // =====================================================
        // CONFIRMAR COMPRA
        // =====================================================

        System.out.print("\nConfirmes la compra? (s/n): ");
        String resposta = sc.nextLine().trim().toLowerCase();

        if (!resposta.equals("s")) {
            System.out.println("Compra cancel·lada.");
            return;
        }

        System.out.println("Compra confirmada.");
        System.out.println("=".repeat(55) + "\n");
    }
}