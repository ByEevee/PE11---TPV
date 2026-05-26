package objectes;

public class LiniaFactura {

    private int idTiquet;
    private int idArticle;
    private int quantitat;
    private double preuBase;
    private double iva;
    private double preuFinal;

    // ===== CONSTRUCTOR =====

    public LiniaFactura(int idTiquet, int idArticle, int quantitat,
                        double preuBase, double iva, double preuFinal) {
        this.idTiquet  = idTiquet;
        this.idArticle = idArticle;
        this.quantitat = quantitat;
        this.preuBase  = preuBase;
        this.iva       = iva;
        this.preuFinal = preuFinal;
    }

    // ===== GETTERS & SETTERS =====

    public int getIdTiquet()               { return idTiquet; }
    public void setIdTiquet(int idTiquet)  { this.idTiquet = idTiquet; }

    public int getIdArticle()              { return idArticle; }

    public int getQuantitat()              { return quantitat; }

    public double getPreuBase()            { return preuBase; }

    public double getIva()                 { return iva; }

    public double getPreuFinal()           { return preuFinal; }
}