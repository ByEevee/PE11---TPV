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

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getFamilia() { return familia; }
}
