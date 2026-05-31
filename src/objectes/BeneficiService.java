package objectes;

import DAO.BeneficiDAO;
import DAO.BeneficiDAO.FilaBeneficiArticle;
import DAO.BeneficiDAO.FilaBeneficiTiquet;

import java.sql.Connection;
import java.util.List;

/**
 * Servei de beneficis.
 * Orquestra les consultes del DAO i calcula estadístiques derivades.
 */
public class BeneficiService {

    private BeneficiDAO beneficiDAO;

    public BeneficiService(Connection conn) {
        this.beneficiDAO = new BeneficiDAO(conn);
    }

    // =========================================================
    // INFORME PER ARTICLE
    // =========================================================

    /**
     * Retorna la llista de beneficis per article, ordenada asc o desc.
     *
     * @param ascending true = menor benefici primer
     */
    public List<FilaBeneficiArticle> getInformePerArticle(boolean ascending) {
        return beneficiDAO.getInformePerArticle(ascending);
    }

    // =========================================================
    // INFORME PER TIQUET
    // =========================================================

    /**
     * Retorna la llista de tiquets ordenada per import asc o desc.
     *
     * @param ascending true = import menor primer
     */
    public List<FilaBeneficiTiquet> getInformePerTiquet(boolean ascending) {
        return beneficiDAO.getInformePerTiquet(ascending);
    }

    // =========================================================
    // TOTALS GLOBALS
    // =========================================================

    /**
     * Retorna els totals globals de la botiga.
     * Índexs: [0]=base, [1]=iva, [2]=final, [3]=beneficiNet, [4]=costFabricació
     */
    public double[] getTotalsGlobals() {
        return beneficiDAO.getTotalsGlobals();
    }

    // =========================================================
    // ESTADÍSTIQUES DE LA LLISTA D'ARTICLES
    // =========================================================

    /**
     * Retorna el millor article (major benefici net) d'una llista.
     * La llista ha d'estar ordenada DESC per treballar correctament.
     */
    public FilaBeneficiArticle getMillorArticle(List<FilaBeneficiArticle> llista) {
        if (llista == null || llista.isEmpty()) return null;
        FilaBeneficiArticle millor = llista.get(0);
        for (FilaBeneficiArticle f : llista) {
            if (f.beneficiNet > millor.beneficiNet) millor = f;
        }
        return millor;
    }

    /**
     * Retorna el pitjor article (menor benefici net) d'una llista.
     */
    public FilaBeneficiArticle getPitjorArticle(List<FilaBeneficiArticle> llista) {
        if (llista == null || llista.isEmpty()) return null;
        FilaBeneficiArticle pitjor = llista.get(0);
        for (FilaBeneficiArticle f : llista) {
            if (f.beneficiNet < pitjor.beneficiNet) pitjor = f;
        }
        return pitjor;
    }
}
