package objectes;

public abstract class Article {
    protected int id;
    protected String nom;
    protected int familia;   // Ara és un enter (veure Familia.java)
    protected double preuBase;
    protected int iva;
    protected int stock;

    public Article(int id, String nom, int familia, double preuBase, int iva, int stock) {
        this.id = id;
        this.nom = nom;
        this.familia = familia;
        this.preuBase = preuBase;
        this.iva = iva;
        this.stock = stock;
    }

    public Article() {}

    // ===== GETTERS & SETTERS =====

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public int getFamilia() { return familia; }
    public void setFamilia(int familia) { this.familia = familia; }

    /** Retorna el nom llegible de la família (ex: "camisa"). */
    public String getFamiliaNom() { return Familia.toNom(familia); }

    public double getPreuBase() { return preuBase; }
    public void setPreuBase(double preuBase) { this.preuBase = preuBase; }

    public int getIva() { return iva; }
    public void setIva(int iva) { this.iva = iva; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    // ===== CÀLCUL DE COSTOS =====

    /**
     * Retorna l'import de l'IVA per a una unitat.
     * importIVA = preuBase * (iva / 100)
     */
    public double getImportIva() {
        return Math.round(preuBase * (iva / 100.0) * 100.0) / 100.0;
    }

    /**
     * Retorna el preu final (base + IVA) per a una unitat.
     * preuFinal = preuBase * (1 + iva / 100)
     */
    public double getPreuFinal() {
        return Math.round(preuBase * (1 + iva / 100.0) * 100.0) / 100.0;
    }

    /**
     * Retorna el preu final per a una quantitat donada.
     * total = preuFinal * quantitat
     */
    public double getPreuFinalQuantitat(int quantitat) {
        return Math.round(getPreuFinal() * quantitat * 100.0) / 100.0;
    }

    /**
     * Retorna l'import de l'IVA per a una quantitat donada.
     */
    public double getImportIvaQuantitat(int quantitat) {
        return Math.round(getImportIva() * quantitat * 100.0) / 100.0;
    }

    /**
     * Retorna la base imposable per a una quantitat donada.
     * base = preuBase * quantitat
     */
    public double getBaseQuantitat(int quantitat) {
        return Math.round(preuBase * quantitat * 100.0) / 100.0;
    }

    /** Indica si l'article té stock disponible. */
    public boolean teStock() {
        return stock > 0;
    }

    /**
     * Retorna un resum de costos formatat per a una quantitat donada.
     * Útil per mostrar al TPV o als menús.
     */
    public String resumCostos(int quantitat) {
        return String.format(
            "Base: %.2f$  |  IVA (%d%%): %.2f$  |  Total: %.2f~$",
            getBaseQuantitat(quantitat),
            iva,
            getImportIvaQuantitat(quantitat),
            getPreuFinalQuantitat(quantitat)
        );
    }

    /**
     * Mètode abstracte que cada subclasse implementa per
     * retornar les dades tècniques de talla/mida.
     */
    public abstract String getDetallsTalla();
}