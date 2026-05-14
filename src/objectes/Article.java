package objectes;

public abstract class Article {
    protected int id;
    protected String nom;
    protected String familia;
    protected double preuBase;
    protected int iva;
    protected int stock;

    public Article(int id, String nom, String familia, double preuBase, int iva, int stock) {
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
    
    public String getFamilia() { return familia; }
    public void setFamilia(String familia) { this.familia = familia; }
    
    public double getPreuBase() { return preuBase; }
    public void setPreuBase(double preuBase) { this.preuBase = preuBase; }
    
    public int getIva() { return iva; }
    public void setIva(int iva) { this.iva = iva; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    // ===== UTILITATS =====
    
    public double getPreuFinal() {
        return preuBase * (1 + iva / 100.0);
    }

    public boolean teStock() {
        return stock > 0;
    }
}