package objectes;

public class Pantalo extends Article {
    private int tallaCintura;
    private int llargadaCamal;

    public Pantalo(int id, String nom, double preuBase, int iva, int stock, int tallaCintura, int llargadaCamal) {
        super(id, nom, "pantaló", preuBase, iva, stock);
        this.tallaCintura = tallaCintura;
        this.llargadaCamal = llargadaCamal;
    }
}
