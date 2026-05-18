package objectes;
import java.sql.Connection;
import DAO.ArticleDAO;

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

        Camisa camisa = new Camisa(id, nom, preuBase, iva, stock, tallaColl, ampladaPit);
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

        Pantalo pantalo = new Pantalo(id, nom, preuBase, iva, stock, tallaCintura, llargadaCamal);
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
    // MODIFICACIÓ D'ARTICLES
    // =========================================================

    /**
     * Modifica un article existent.
     * Accepta els nous valors i valida les dades.
     */
    public boolean modificarArticle(int id, String nom, double preuBase, int iva, int stock,
                                     Object param1, Object param2) {

        // Comprovem que l'article existeix
        Article article = articleDAO.getById(id);

        if (article == null) {
            System.err.println("Error: No existeix cap article amb l'ID " + id + ".");
            return false;
        }

        // Validem les dades comunes
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

        // Validem i actualitzem segons el tipus d'article
        boolean resultat = false;

        if (article instanceof Camisa) {
            int tallaColl = (Integer) param1;
            int ampladaPit = (Integer) param2;

            if (!validarTallaColl(tallaColl)) {
                System.err.println("Error: La talla de coll ha de ser entre 36 i 52.");
                return false;
            }
            if (!validarAmpladaPit(ampladaPit)) {
                System.err.println("Error: L'amplada de pit ha de ser entre 10 i 15.");
                return false;
            }

            Camisa camisa = new Camisa(id, nom, preuBase, iva, stock, tallaColl, ampladaPit);
            resultat = articleDAO.updateCamisa(camisa);

        } else if (article instanceof Pantalo) {
            int tallaCintura = (Integer) param1;
            int llargadaCamal = (Integer) param2;

            if (!validarTallaCintura(tallaCintura)) {
                System.err.println("Error: La talla de cintura ha de ser entre 24 i 56.");
                return false;
            }
            if (!validarLlargadaCamal(llargadaCamal)) {
                System.err.println("Error: La llargada del camal ha de ser entre 32 i 46.");
                return false;
            }

            Pantalo pantalo = new Pantalo(id, nom, preuBase, iva, stock, tallaCintura, llargadaCamal);
            resultat = articleDAO.updatePantalo(pantalo);
        }

        if (resultat) {
            System.out.println("Article modificat correctament!");
        } else {
            System.err.println("Error modificant l'article a la BD.");
        }

        return resultat;
    }

    // =========================================================
    // BAIXA D'ARTICLES
    // =========================================================

    /**
     * Elimina un article del catàleg.
     */
    public boolean baixaArticle(int id) {

        // Comprovem que l'article existeix
        Article article = articleDAO.getById(id);

        if (article == null) {
            System.err.println("Error: No existeix cap article amb l'ID " + id + ".");
            return false;
        }

        // Mostrem l'article que s'eliminarà
        System.out.println("\nArticle a eliminar:");
        System.out.println(article);

        // Eliminar de la BD
        boolean resultat = articleDAO.delete(id);

        if (resultat) {
            System.out.println("Article eliminat correctament.");
        } else {
            System.err.println("Error eliminant l'article de la BD.");
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