package objectes;

import DAO.BeneficiDAO.FilaBeneficiArticle;
import DAO.BeneficiDAO.FilaBeneficiTiquet;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

/**
 * Menú interactiu per a l'informe de beneficis.
 * Permet consultar beneficis per article o per tiquet,
 * en ordre ascendent o descendent.
 */
public class BeneficiMenu {

    private Scanner       sc;
    private BeneficiService service;

    private static final int COL_ID    = 5;
    private static final int COL_NOM   = 22;
    private static final int COL_FAM   = 8;
    private static final int COL_UNITS = 6;
    private static final int COL_EUR   = 10;

    public BeneficiMenu(Connection conn) {
        this.sc      = new Scanner(System.in);
        this.service = new BeneficiService(conn);
    }

    // =========================================================
    // MENÚ PRINCIPAL DE BENEFICIS
    // =========================================================

    public void mostraMenu() {
        boolean tornar = false;

        while (!tornar) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("         INFORME DE BENEFICIS");
            System.out.println("=".repeat(50));
            System.out.println("1. Beneficis per article  (ordre ascendent)");
            System.out.println("2. Beneficis per article  (ordre descendent)");
            System.out.println("3. Vendes per tiquet      (ordre ascendent)");
            System.out.println("4. Vendes per tiquet      (ordre descendent)");
            System.out.println("5. Resum global de beneficis");
            System.out.println("0. Tornar enrere");
            System.out.println("=".repeat(50));
            System.out.print("Selecciona una opció: ");

            int opcio = llegirEnter();

            switch (opcio) {
                case 1: mostrarInformeArticles(true);  break;
                case 2: mostrarInformeArticles(false); break;
                case 3: mostrarInformeTiquets(true);   break;
                case 4: mostrarInformeTiquets(false);  break;
                case 5: mostrarResumGlobal();           break;
                case 0: tornar = true;                  break;
                default: System.out.println("Opció no vàlida.");
            }
        }
    }

    // =========================================================
    // INFORME PER ARTICLE
    // =========================================================

    private void mostrarInformeArticles(boolean ascending) {
        String etiquetaOrdre = ascending ? "ASCENDENT (menor benefici primer)"
                                         : "DESCENDENT (major benefici primer)";

        System.out.println("\n" + "=".repeat(72));
        System.out.println("  BENEFICIS PER ARTICLE — Ordre: " + etiquetaOrdre);
        System.out.println("=".repeat(72));

        List<FilaBeneficiArticle> llista = service.getInformePerArticle(ascending);

        if (llista.isEmpty()) {
            System.out.println("  No hi ha dades de vendes registrades.");
            System.out.println("=".repeat(72));
            return;
        }

        // Capçalera de la taula
        System.out.printf("  %-" + COL_ID  + "s %-" + COL_NOM + "s %-" + COL_FAM + "s "
                        + "%" + COL_UNITS + "s %" + COL_EUR + "s %" + COL_EUR + "s "
                        + "%" + COL_EUR   + "s %" + COL_EUR + "s%n",
            "ID", "ARTICLE", "FAMÍLIA", "UNITATS",
            "BASE€", "COST€", "BENEFICI€", "% MARGE");
        System.out.println("  " + "-".repeat(70));

        double sumaBenefici = 0;
        double sumaBase     = 0;
        double sumaCost     = 0;

        for (FilaBeneficiArticle f : llista) {
            double percentatgeMarge = (f.totalBase > 0)
                ? (f.beneficiNet / f.totalBase) * 100.0
                : 0.0;

            System.out.printf("  %-" + COL_ID  + "d %-" + COL_NOM + "s %-" + COL_FAM + "s "
                            + "%" + COL_UNITS + "d %" + COL_EUR + ".2f %" + COL_EUR + ".2f "
                            + "%" + COL_EUR   + ".2f %" + (COL_EUR - 1) + ".1f%%%n",
                f.idArticle,
                truncar(f.nomArticle, COL_NOM - 1),
                truncar(f.familia,    COL_FAM - 1),
                f.unitatsVenudes,
                f.totalBase,
                f.costFabricacio,
                f.beneficiNet,
                percentatgeMarge
            );

            sumaBenefici += f.beneficiNet;
            sumaBase     += f.totalBase;
            sumaCost     += f.costFabricacio;
        }

        // Totals de la taula
        System.out.println("  " + "=".repeat(70));
        double pctTotal = (sumaBase > 0) ? (sumaBenefici / sumaBase) * 100.0 : 0.0;
        System.out.printf("  %-" + (COL_ID + COL_NOM + COL_FAM + COL_UNITS + 4) + "s "
                        + "%" + COL_EUR + ".2f %" + COL_EUR + ".2f "
                        + "%" + COL_EUR + ".2f %" + (COL_EUR - 1) + ".1f%%%n",
            "TOTAL (" + llista.size() + " articles)",
            sumaBase, sumaCost, sumaBenefici, pctTotal);
        System.out.println("=".repeat(72));

        // Destacar millor i pitjor
        FilaBeneficiArticle millor  = service.getMillorArticle(llista);
        FilaBeneficiArticle pitjor  = service.getPitjorArticle(llista);

        System.out.printf("  ★ Millor article:  [%d] %s → %.2f€%n",
            millor.idArticle, millor.nomArticle, millor.beneficiNet);
        System.out.printf("  ▼ Pitjor article:  [%d] %s → %.2f€%n",
            pitjor.idArticle, pitjor.nomArticle, pitjor.beneficiNet);
        System.out.println("=".repeat(72));
    }

    // =========================================================
    // INFORME PER TIQUET
    // =========================================================

    private void mostrarInformeTiquets(boolean ascending) {
        String etiquetaOrdre = ascending ? "ASCENDENT (import menor primer)"
                                         : "DESCENDENT (import major primer)";

        System.out.println("\n" + "=".repeat(72));
        System.out.println("  VENDES PER TIQUET — Ordre: " + etiquetaOrdre);
        System.out.println("=".repeat(72));

        List<FilaBeneficiTiquet> llista = service.getInformePerTiquet(ascending);

        if (llista.isEmpty()) {
            System.out.println("  No hi ha tiquets registrats.");
            System.out.println("=".repeat(72));
            return;
        }

        // Capçalera
        System.out.printf("  %-5s %-12s %-20s %10s %10s %10s%n",
            "TIQ.", "DATA", "CLIENT", "BASE€", "IVA€", "TOTAL€");
        System.out.println("  " + "-".repeat(70));

        double sumaBase  = 0;
        double sumaIva   = 0;
        double sumaTotal = 0;

        for (FilaBeneficiTiquet f : llista) {
            String nomClient = truncar(f.nomClient, 19);
            System.out.printf("  %-5d %-12s %-20s %10.2f %10.2f %10.2f%n",
                f.idTiquet, f.data, nomClient,
                f.totalBase, f.totalIva, f.totalFinal);

            sumaBase  += f.totalBase;
            sumaIva   += f.totalIva;
            sumaTotal += f.totalFinal;
        }

        System.out.println("  " + "=".repeat(70));
        System.out.printf("  %-38s %10.2f %10.2f %10.2f%n",
            "TOTAL (" + llista.size() + " tiquets)",
            sumaBase, sumaIva, sumaTotal);
        System.out.println("=".repeat(72));
    }

    // =========================================================
    // RESUM GLOBAL
    // =========================================================

    private void mostrarResumGlobal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("        RESUM GLOBAL DE BENEFICIS");
        System.out.println("=".repeat(50));

        double[] t = service.getTotalsGlobals();
        // t[0]=base, t[1]=iva, t[2]=final, t[3]=beneficiNet, t[4]=costFabricació

        if (t[2] == 0) {
            System.out.println("  No hi ha vendes registrades.");
            System.out.println("=".repeat(50));
            return;
        }

        double pctBenefici = (t[0] > 0) ? (t[3] / t[0]) * 100.0 : 0.0;

        System.out.printf("  %-30s %10.2f€%n", "Total vendes (base):",   t[0]);
        System.out.printf("  %-30s %10.2f€%n", "Total IVA recaptat:",    t[1]);
        System.out.printf("  %-30s %10.2f€%n", "Total vendes (final):",  t[2]);
        System.out.println("  " + "-".repeat(44));
        System.out.printf("  %-30s %10.2f€%n", "Cost de fabricació:",    t[4]);
        System.out.printf("  %-30s %10.2f€%n", "Benefici net:",          t[3]);
        System.out.printf("  %-30s %9.1f%%%n",  "Marge sobre vendes:",   pctBenefici);
        System.out.println("=".repeat(50));
        System.out.println("  (Camisa: marge 60% | Pantaló: marge 55%)");
        System.out.println("=".repeat(50));
    }

    // =========================================================
    // UTILITATS
    // =========================================================

    private String truncar(String text, int max) {
        if (text == null) return "";
        return text.length() > max ? text.substring(0, max - 2) + ".." : text;
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
