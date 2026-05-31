package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per a les consultes d'informe de beneficis.
 *
 * Benefici per article = (preu_base_venda - cost_fabricació) * quantitat
 *   - Camisa:  cost = 40% del preu_base  →  marge = 60% del preu_base
 *   - Pantaló: cost = 45% del preu_base  →  marge = 55% del preu_base
 *
 * Les consultes fan el càlcul directament en SQL (eficient) i
 * retornen files FilaBenefici llestes per mostrar.
 */
public class BeneficiDAO {

    private Connection conn;

    // Percentatge de marge segons família (id: 1=camisa, 2=pantaló)
    // marge_camisa  = preu_base * 0.60
    // marge_pantalo = preu_base * 0.55
    private static final double MARGE_CAMISA  = 0.60;
    private static final double MARGE_PANTALO = 0.55;

    public BeneficiDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================================================
    // MODEL INTERN: fila de resultat d'informe
    // =========================================================

    /**
     * Representa una fila de l'informe de beneficis per article.
     */
    public static class FilaBeneficiArticle {
        public final int    idArticle;
        public final String nomArticle;
        public final String familia;
        public final int    unitatsVenudes;
        public final double totalVendes;     // suma preu_final de linies
        public final double totalBase;       // suma preu_base * quantitat
        public final double totalIva;        // suma iva (import) de linies
        public final double beneficiBrut;    // totalBase (sense IVA)
        public final double costFabricacio;  // totalBase * %cost
        public final double beneficiNet;     // beneficiBrut - costFabricacio

        public FilaBeneficiArticle(int idArticle, String nomArticle, String familia,
                                   int unitatsVenudes, double totalVendes,
                                   double totalBase, double totalIva,
                                   double beneficiBrut, double costFabricacio,
                                   double beneficiNet) {
            this.idArticle      = idArticle;
            this.nomArticle     = nomArticle;
            this.familia        = familia;
            this.unitatsVenudes = unitatsVenudes;
            this.totalVendes    = totalVendes;
            this.totalBase      = totalBase;
            this.totalIva       = totalIva;
            this.beneficiBrut   = beneficiBrut;
            this.costFabricacio = costFabricacio;
            this.beneficiNet    = beneficiNet;
        }
    }

    /**
     * Representa una fila de l'informe de beneficis per tiquet/venda.
     */
    public static class FilaBeneficiTiquet {
        public final int    idTiquet;
        public final String data;
        public final String dniClient;
        public final String nomClient;
        public final double totalBase;
        public final double totalIva;
        public final double totalFinal;

        public FilaBeneficiTiquet(int idTiquet, String data, String dniClient,
                                  String nomClient, double totalBase,
                                  double totalIva, double totalFinal) {
            this.idTiquet   = idTiquet;
            this.data       = data;
            this.dniClient  = dniClient;
            this.nomClient  = nomClient;
            this.totalBase  = totalBase;
            this.totalIva   = totalIva;
            this.totalFinal = totalFinal;
        }
    }

    // =========================================================
    // INFORME PER ARTICLE  (ordre: beneficiNet ASC o DESC)
    // =========================================================

    /**
     * Retorna l'informe de beneficis agrupat per article.
     *
     * Fórmula SQL usada:
     *   totalBase      = SUM(lf.preu_base)
     *   marge_factor   = CASE WHEN f.id = 1 THEN 0.60 ELSE 0.55 END
     *   beneficiNet    = SUM(lf.preu_base) * marge_factor
     *   costFabricacio = SUM(lf.preu_base) * (1 - marge_factor)
     *
     * @param ascending  true = menor benefici primer, false = major primer
     */
    public List<FilaBeneficiArticle> getInformePerArticle(boolean ascending) {
        List<FilaBeneficiArticle> resultat = new ArrayList<>();

        String ordre = ascending ? "ASC" : "DESC";

        String sql =
            "SELECT " +
            "    a.id                          AS id_article, " +
            "    a.nom                         AS nom_article, " +
            "    f.nom                         AS familia, " +
            "    SUM(lf.quantitat)             AS unitats_venudes, " +
            "    SUM(lf.preu_final)            AS total_vendes, " +
            "    SUM(lf.preu_base)             AS total_base, " +
            "    SUM(lf.preu_final - lf.preu_base) AS total_iva, " +
            "    SUM(lf.preu_base)             AS benefici_brut, " +
            "    ROUND(SUM(lf.preu_base) * " +
            "        CASE WHEN f.id = 1 THEN (1 - " + MARGE_CAMISA  + ") " +
            "             ELSE                (1 - " + MARGE_PANTALO + ") END, 2) " +
            "                                 AS cost_fabricacio, " +
            "    ROUND(SUM(lf.preu_base) * " +
            "        CASE WHEN f.id = 1 THEN " + MARGE_CAMISA  + " " +
            "             ELSE               " + MARGE_PANTALO + " END, 2) " +
            "                                 AS benefici_net " +
            "FROM linies_factura lf " +
            "JOIN articles a ON lf.id_article = a.id " +
            "JOIN families f ON a.familia     = f.id " +
            "GROUP BY a.id, a.nom, f.nom " +
            "ORDER BY benefici_net " + ordre;

        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultat.add(new FilaBeneficiArticle(
                    rs.getInt("id_article"),
                    rs.getString("nom_article"),
                    rs.getString("familia"),
                    rs.getInt("unitats_venudes"),
                    rs.getDouble("total_vendes"),
                    rs.getDouble("total_base"),
                    rs.getDouble("total_iva"),
                    rs.getDouble("benefici_brut"),
                    rs.getDouble("cost_fabricacio"),
                    rs.getDouble("benefici_net")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error en informe per article: " + e.getMessage());
        }
        return resultat;
    }

    // =========================================================
    // INFORME PER TIQUET  (ordre: total_final ASC o DESC)
    // =========================================================

    /**
     * Retorna l'informe de tiquets/vendes amb totals.
     *
     * @param ascending  true = import menor primer, false = major primer
     */
    public List<FilaBeneficiTiquet> getInformePerTiquet(boolean ascending) {
        List<FilaBeneficiTiquet> resultat = new ArrayList<>();

        String ordre = ascending ? "ASC" : "DESC";

        String sql =
            "SELECT " +
            "    t.id           AS id_tiquet, " +
            "    t.data_compra  AS data, " +
            "    t.dni_client   AS dni_client, " +
            "    c.nom          AS nom_client, " +
            "    t.total_base   AS total_base, " +
            "    t.total_iva    AS total_iva, " +
            "    t.total_final  AS total_final " +
            "FROM tiquets t " +
            "JOIN clients c ON t.dni_client = c.dni " +
            "ORDER BY t.total_final " + ordre;

        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultat.add(new FilaBeneficiTiquet(
                    rs.getInt("id_tiquet"),
                    rs.getString("data"),
                    rs.getString("dni_client"),
                    rs.getString("nom_client"),
                    rs.getDouble("total_base"),
                    rs.getDouble("total_iva"),
                    rs.getDouble("total_final")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error en informe per tiquet: " + e.getMessage());
        }
        return resultat;
    }

    // =========================================================
    // TOTALS GLOBALS
    // =========================================================

    /**
     * Retorna un array double[] amb els totals globals de tota la botiga:
     * [0] = suma total_base de tots els tiquets
     * [1] = suma total_iva  de tots els tiquets
     * [2] = suma total_final de tots els tiquets
     * [3] = benefici net total (calculat per família)
     * [4] = cost fabricació total
     */
    public double[] getTotalsGlobals() {
        double[] totals = {0, 0, 0, 0, 0};

        String sql =
            "SELECT " +
            "    SUM(t.total_base)  AS suma_base, " +
            "    SUM(t.total_iva)   AS suma_iva, " +
            "    SUM(t.total_final) AS suma_final " +
            "FROM tiquets t";

        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            if (rs.next()) {
                totals[0] = rs.getDouble("suma_base");
                totals[1] = rs.getDouble("suma_iva");
                totals[2] = rs.getDouble("suma_final");
            }
        } catch (SQLException e) {
            System.err.println("Error calculant totals globals: " + e.getMessage());
        }

        // Benefici net i cost de fabricació globals (des de linies_factura)
        String sqlBenefici =
            "SELECT " +
            "    ROUND(SUM(lf.preu_base * " +
            "        CASE WHEN f.id = 1 THEN " + MARGE_CAMISA  + " " +
            "             ELSE               " + MARGE_PANTALO + " END), 2) AS benefici_net, " +
            "    ROUND(SUM(lf.preu_base * " +
            "        CASE WHEN f.id = 1 THEN (1 - " + MARGE_CAMISA  + ") " +
            "             ELSE                (1 - " + MARGE_PANTALO + ") END), 2) AS cost_total " +
            "FROM linies_factura lf " +
            "JOIN articles a ON lf.id_article = a.id " +
            "JOIN families f ON a.familia     = f.id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sqlBenefici)) {
            if (rs.next()) {
                totals[3] = rs.getDouble("benefici_net");
                totals[4] = rs.getDouble("cost_total");
            }
        } catch (SQLException e) {
            System.err.println("Error calculant benefici net global: " + e.getMessage());
        }

        return totals;
    }
}
