package objectes;

public class PropostaCompra {
    private int codi;
    private String nom;
    private int quantitat;

    public PropostaCompra(int codi, String nom, int quantitat) {
        this.codi = codi;
        this.nom = nom;
        this.quantitat = quantitat;
    }

    public int getCodi() { return codi; }
    public String getNom() { return nom; }
    public int getQuantitat() { return quantitat; }
}
