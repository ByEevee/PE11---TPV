package objectes;

/**
 * Classe model que representa un client de la botiga.
 * El client '000' és el client genèric protegit del sistema.
 */
public class Client {

    private String dni;
    private String nom;
    private String email;
    private String telefon;

    // ===== CONSTRUCTORS =====

    public Client() {}

    public Client(String dni, String nom, String email, String telefon) {
        this.dni    = dni;
        this.nom    = nom;
        this.email  = email;
        this.telefon = telefon;
    }


    // ===== GETTERS & SETTERS =====

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    // ===== UTILITATS =====

    /**
     * Indica si aquest client és el client genèric protegit del sistema.
     */
    public boolean esClientGeneric() {
        return "000".equals(dni);
    }

    // ===== TO STRING =====

    @Override
    public String toString() {
        String emailMostrat   = (email   != null && !email.isEmpty())   ? email   : "—";
        String telefonMostrat = (telefon != null && !telefon.isEmpty()) ? telefon : "—";
        return String.format("[%s] %s | Email: %s | Telèfon: %s",
                dni, nom, emailMostrat, telefonMostrat);
    }

    
}
