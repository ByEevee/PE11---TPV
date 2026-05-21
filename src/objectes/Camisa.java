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
        this.tallaColl = tallaColl;
        this.ampladaPit = ampladaPit;
    }

    // ===== GETTERS & SETTERS =====

    public int getTallaColl() { return tallaColl; }
    public void setTallaColl(int tallaColl) { this.tallaColl = tallaColl; }

    public int getAmpladaPit() { return ampladaPit; }
    public void setAmpladaPit(int ampladaPit) { this.ampladaPit = ampladaPit; }

    // ===== TO STRING =====

    @Override
    public String toString() {
        return String.format("[%d] %s (camisa) | Coll: %d | Pit: %d | %.2f€ (+%d%% IVA) | Stock: %d",
                id, nom, tallaColl, ampladaPit, preuBase, iva, stock);
    }
}