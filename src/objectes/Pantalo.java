package objectes;

public class Pantalo extends Article {

    private int tallaCintura;
    private int llargadaCamal;

    public Pantalo() {
        super();
    }

    public Pantalo(int id, String nom, double preuBase, int iva, int stock,
                   int tallaCintura, int llargadaCamal) {
        super(id, nom, Familia.PANTALO, preuBase, iva, stock);
        this.tallaCintura  = tallaCintura;
        this.llargadaCamal = llargadaCamal;
    }

    // ===== GETTERS & SETTERS =====

    public int getTallaCintura() { return tallaCintura; }
    public void setTallaCintura(int tallaCintura) { this.tallaCintura = tallaCintura; }

    public int getLlargadaCamal() { return llargadaCamal; }
    public void setLlargadaCamal(int llargadaCamal) { this.llargadaCamal = llargadaCamal; }

    // ===== CÀLCUL DE COSTOS DE PANTALÓ =====

    /**
     * Cost de fabricació estimat d'un pantaló:
     *   costFabricació = preuBase * 0.45
     *
     * El 45% del preu base cobreix teixit (major quantitat que camisa),
     * confecció i mà d'obra especialitzada.
     */
    public double getCostFabricacio() {
        return Math.round(preuBase * 0.45 * 100.0) / 100.0;
    }

    /**
     * Marge comercial d'un pantaló:
     *   marge = preuBase - costFabricació
     *         = preuBase * 0.55
     */
    public double getMarge() {
        return Math.round((preuBase - getCostFabricacio()) * 100.0) / 100.0;
    }

    /**
     * Percentatge de marge sobre el preu base (sempre 55% per a pantalons).
     */
    public double getPercentatgeMarge() {
        if (preuBase == 0) return 0.0;
        return Math.round((getMarge() / preuBase) * 10000.0) / 100.0;
    }

    // ===== DETALLS DE TALLA =====

    /**
     * Retorna les dades tècniques de talla del pantaló.
     */
    @Override
    public String getDetallsTalla() {
        return String.format("Cintura: %dcm  |  Camal: %dcm", tallaCintura, llargadaCamal);
    }

    // ===== TO STRING =====

    @Override
    public String toString() {
        return String.format(
            "[%d] %s (pantaló) | %s | %.2f€ (+%d%% IVA) → %.2f€ | Stock: %d",
            id, nom,
            getDetallsTalla(),
            preuBase, iva, getPreuFinal(),
            stock
        );
    }
}