package objectes;

public class Pantalo extends Article {
    private int tallaCintura;
    private int llargadaCamal;

    public Pantalo() {
        super();
    }

    public Pantalo(int id, String nom, double preuBase, int iva, int stock, int tallaCintura, int llargadaCamal) {
        super(id, nom, "pantaló", preuBase, iva, stock);
        this.tallaCintura = tallaCintura;
        this.llargadaCamal = llargadaCamal;
    }

    // ===== GETTERS & SETTERS =====
    
    public int getTallaCintura() { return tallaCintura; }
    public void setTallaCintura(int tallaCintura) { this.tallaCintura = tallaCintura; }
    
    public int getLlargadaCamal() { return llargadaCamal; }
    public void setLlargadaCamal(int llargadaCamal) { this.llargadaCamal = llargadaCamal; }

    // ===== TO STRING =====
    
    @Override
    public String toString() {
        return String.format("[%d] %s (pantaló) | Cintura: %d | Camal: %d | %.2f€ (+%d%% IVA) | Stock: %d",
                id, nom, tallaCintura, llargadaCamal, preuBase, iva, stock);
    }
}