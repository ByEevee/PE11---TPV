package objectes;
import java.sql.Connection;
import DAO.ArticleDAO;
import objectes.*;

/**
 * Servei de gestió d'articles.
 * Encapsula la lògica de negoci per a l'alta, modificació i eliminació d'articles.
 */
public class ArticleService {

    private ArticleDAO articleDAO;

    public ArticleService(Connection conn) {
        this.articleDAO = new ArticleDAO(conn);
    }

    // =========================================================
    // ALTA DE CAMISES
    // =========================================================

    /**
     * Afegeix una camisa nova al catàleg.
     * Valida les dades abans d'inserir.
     */
    public boolean altaCamisa(int id, String nom, double preuBase, int iva, int stock,
                               int tallaColl, int ampladaPit) {

        // Validacions
        if (!validarId(id)) {
            System.err.println("Error: L'ID ja existeix.");
            return false;
        }

        if (!validarNom(nom)) {
            System.err.println("Error: El nom no pot estar buit.");
            return false;
        }

        if (!validarPreu(preuBase)) {
            System.err.println("Error: El preu ha de ser positiu.");
            return false;
        }

        if (!validarIVA(iva)) {
            System.err.println("Error: L'IVA ha de ser entre 4 i 21.");
            return false;
        }

        if (!validarStock(stock)) {
            System.err.println("Error: El stock no pot ser negatiu.");
            return false;
        }

        if (!validarTallaColl(tallaColl)) {
            System.err.println("Error: La talla de coll ha de ser entre 36 i 52.");
            return false;
        }

        if (!validarAmpladaPit(ampladaPit)) {
            System.err.println("Error: L'amplada de pit ha de ser entre 10 i 15.");
            return false;
        }

        // Crear objecte
        Camisa camisa = new Camisa(id, nom, preuBase, iva, stock, tallaColl, ampladaPit);

        // Inserir a BD
        boolean resultat = articleDAO.insertCamisa(camisa);

        if (resultat) {
            System.out.println("Camisa afegida correctament!");
            System.out.println(camisa);
        } else {
            System.err.println("Error inserint la camisa a la BD.");
        }

        return resultat;
    }

    // =========================================================
    // ALTA DE PANTALONS
    // =========================================================

    /**
     * Afegeix un pantaló nou al catàleg.
     * Valida les dades abans d'inserir.
     */
    public boolean altaPantalo(int id, String nom, double preuBase, int iva, int stock,
                                int tallaCintura, int llargadaCamal) {

        // Validacions
        if (!validarId(id)) {
            System.err.println("Error: L'ID ja existeix.");
            return false;
        }

        if (!validarNom(nom)) {
            System.err.println("Error: El nom no pot estar buit.");
            return false;
        }

        if (!validarPreu(preuBase)) {
            System.err.println("Error: El preu ha de ser positiu.");
            return false;
        }

        if (!validarIVA(iva)) {
            System.err.println("Error: L'IVA ha de ser entre 4 i 21.");
            return false;
        }

        if (!validarStock(stock)) {
            System.err.println("Error: El stock no pot ser negatiu.");
            return false;
        }

        if (!validarTallaCintura(tallaCintura)) {
            System.err.println("Error: La talla de cintura ha de ser entre 24 i 56.");
            return false;
        }

        if (!validarLlargadaCamal(llargadaCamal)) {
            System.err.println("Error: La llargada del camal ha de ser entre 32 i 46.");
            return false;
        }

        // Crear objecte
        Pantalo pantalo = new Pantalo(id, nom, preuBase, iva, stock, tallaCintura, llargadaCamal);

        // Inserir a BD
        boolean resultat = articleDAO.insertPantalo(pantalo);

        if (resultat) {
            System.out.println("Pantaló afegit correctament!");
            System.out.println(pantalo);
        } else {
            System.err.println("Error inserint el pantaló a la BD.");
        }

        return resultat;
    }

    // =========================================================
    // VALIDACIONS
    // =========================================================

    private boolean validarId(int id) {
        return !articleDAO.existeix(id);
    }

    private boolean validarNom(String nom) {
        return nom != null && !nom.trim().isEmpty();
    }

    private boolean validarPreu(double preu) {
        return preu > 0;
    }

    private boolean validarIVA(int iva) {
        return iva >= 4 && iva <= 21;
    }

    private boolean validarStock(int stock) {
        return stock >= 0;
    }

    private boolean validarTallaColl(int talla) {
        return talla >= 36 && talla <= 52;
    }

    private boolean validarAmpladaPit(int amplada) {
        return amplada >= 10 && amplada <= 15;
    }

    private boolean validarTallaCintura(int talla) {
        return talla >= 24 && talla <= 56;
    }

    private boolean validarLlargadaCamal(int llargada) {
        return llargada >= 32 && llargada <= 46;
    }

    // =========================================================
    // GETTER
    // =========================================================

    public ArticleDAO getArticleDAO() {
        return articleDAO;
    }
}
