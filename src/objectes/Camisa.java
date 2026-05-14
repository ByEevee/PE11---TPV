package objectes;

public class Camisa extends Article {
    private int tallaColl;
    private int ampladaPit;

    public Camisa(int id, String nom, double preuBase, int iva, int stock, int tallaColl, int ampladaPit) {
        super(id, nom, "camisa", preuBase, iva, stock);
        this.tallaColl = tallaColl;
        this.ampladaPit = ampladaPit;
    }
}
