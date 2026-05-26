package objectes;

public class Tiquet {

    private int id;
    private String dataCompra;
    private String dniClient;
    private double totalBase;
    private double totalIva;
    private double totalFinal;

    // ===== CONSTRUCTOR =====

    public Tiquet(String dataCompra, String dniClient,
                  double totalBase, double totalIva, double totalFinal) {
        this.dataCompra = dataCompra;
        this.dniClient  = dniClient;
        this.totalBase  = totalBase;
        this.totalIva   = totalIva;
        this.totalFinal = totalFinal;
    }

    // ===== GETTERS & SETTERS =====

    public int getId()                  { return id; }
    public void setId(int id)           { this.id = id; }

    public String getDataCompra()       { return dataCompra; }

    public String getDniClient()        { return dniClient; }

    public double getTotalBase()        { return totalBase; }

    public double getTotalIva()         { return totalIva; }

    public double getTotalFinal()       { return totalFinal; }
}