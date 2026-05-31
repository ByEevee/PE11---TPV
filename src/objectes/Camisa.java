package objectes;

public class Camisa extends Article {

    private int tallaColl;
    private int ampladaPit;

    public Camisa() {
        super();
    }

    public Camisa(int id, String nom, double preuBase, int iva, int stock,
                  int tallaColl, int ampladaPit) {
        super(id, nom, Familia.CAMISA, preuBase, iva, stock);
        this.tallaColl  = tallaColl;
        this.ampladaPit = ampladaPit;
    }

    // ===== GETTERS & SETTERS =====

    public int getTallaColl() { return tallaColl; }
    public void setTallaColl(int tallaColl) { this.tallaColl = tallaColl; }

    public int getAmpladaPit() { return ampladaPit; }
    public void setAmpladaPit(int ampladaPit) { this.ampladaPit = ampladaPit; }

    // ===== CÀLCUL DE COSTOS DE CAMISA =====

    /**
     * Cost de fabricació estimat d'una camisa:
     *   costFabricació = preuBase * 0.40
     *
     * El 40% del preu base cobreix teixit, confecció i mà d'obra.
     */
    public double getCostFabricacio() {
        return Math.round(preuBase * 0.40 * 100.0) / 100.0;
    }

    /**
     * Marge comercial d'una camisa:
     *   marge = preuBase - costFabricació
     *         = preuBase * 0.60
     */
    public double getMarge() {
        return Math.round((preuBase - getCostFabricacio()) * 100.0) / 100.0;
    }

    /**
     * Percentatge de marge sobre el preu base (sempre 60% per a camises).
     */
    public double getPercentatgeMarge() {
        if (preuBase == 0) return 0.0;
        return Math.round((getMarge() / preuBase) * 10000.0) / 100.0;
    }

    // ===== DETALLS DE TALLA =====

    /**
     * Retorna les dades tècniques de talla de la camisa.
     */
    @Override
    public String getDetallsTalla() {
        return String.format("Coll: %dcm  |  Pit: %dcm", tallaColl, ampladaPit);
    }

    // ===== TO STRING =====

    @Override
    public String toString() {
        return String.format(
            "[%d] %s (camisa) | %s | %.2f€ (+%d%% IVA) → %.2f€ | Stock: %d",
            id, nom,
            getDetallsTalla(),
            preuBase, iva, getPreuFinal(),
            stock
        );
    }
}