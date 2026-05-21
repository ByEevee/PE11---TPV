package objectes;

import java.sql.Connection;
import DAO.ClientDAO;

/**
 * Servei de gestió de clients.
 * Encapsula la lògica de negoci per a l'alta, modificació i eliminació de clients.
 */
public class ClientService {

    private ClientDAO clientDAO;

    public ClientService(Connection conn) {
        this.clientDAO = new ClientDAO(conn);
    }

    // =========================================================
    // ALTA DE CLIENTS
    // =========================================================

    /**
     * Afegeix un client nou al sistema.
     * Valida totes les dades abans d'inserir.
     * El DNI '000' està reservat per al client genèric i no es pot usar.
     *
     * @param dni     Document d'identitat (clau primària, màx. 10 caràcters).
     * @param nom     Nom complet del client (obligatori).
     * @param email   Adreça de correu electrònic (opcional, pot estar buit).
     * @param telefon Número de telèfon (opcional, pot estar buit).
     * @return true si el client s'ha creat correctament, false en cas contrari.
     */
    public boolean altaClient(String dni, String nom, String email, String telefon) {

        // Validació: DNI no pot ser el client genèric protegit
        if (esClientGeneric(dni)) {
            System.err.println("Error: El DNI '000' està reservat per al Client Genèric del sistema.");
            return false;
        }

        // Validació: DNI no pot estar buit
        if (!validarDni(dni)) {
            System.err.println("Error: El DNI no pot estar buit i ha de tenir màxim 10 caràcters.");
            return false;
        }

        // Validació: DNI no pot existir ja a la BD
        if (!validarDniUnic(dni)) {
            System.err.println("Error: Ja existeix un client amb el DNI '" + dni + "'.");
            return false;
        }

        // Validació: nom obligatori
        if (!validarNom(nom)) {
            System.err.println("Error: El nom del client no pot estar buit.");
            return false;
        }

        // Validació: format email (si s'ha introduït)
        if (email != null && !email.isEmpty() && !validarEmail(email)) {
            System.err.println("Error: El format de l'email no és vàlid (ha de contenir '@').");
            return false;
        }

        // Validació: format telèfon (si s'ha introduït)
        if (telefon != null && !telefon.isEmpty() && !validarTelefon(telefon)) {
            System.err.println("Error: El telèfon només pot contenir dígits, espais i '+', i màxim 20 caràcters.");
            return false;
        }

        // Crear objecte Client
        Client client = new Client(dni, nom, email, telefon);

        // Inserir a la BD via DAO
        boolean resultat = clientDAO.insert(client);

        if (resultat) {
            System.out.println("Client afegit correctament!");
            System.out.println(client);
        } else {
            System.err.println("Error inserint el client a la base de dades.");
        }

        return resultat;
    }

    // =========================================================
    // VALIDACIONS
    // =========================================================

    /**
     * Comprova si el DNI correspon al client genèric protegit.
     */
    private boolean esClientGeneric(String dni) {
        return "000".equals(dni != null ? dni.trim() : "");
    }

    /**
     * Valida que el DNI no estigui buit i no superi els 10 caràcters.
     */
    private boolean validarDni(String dni) {
        return dni != null && !dni.trim().isEmpty() && dni.trim().length() <= 10;
    }

    /**
     * Valida que el DNI no existeixi ja a la base de dades.
     */
    private boolean validarDniUnic(String dni) {
        return !clientDAO.existeix(dni.trim());
    }

    /**
     * Valida que el nom no estigui buit.
     */
    private boolean validarNom(String nom) {
        return nom != null && !nom.trim().isEmpty();
    }

    /**
     * Valida el format bàsic d'un email: ha de contenir '@' i almenys un punt posterior.
     */
    private boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) return true; // opcional
        int at = email.indexOf('@');
        if (at <= 0) return false;
        int dot = email.lastIndexOf('.');
        return dot > at + 1 && dot < email.length() - 1;
    }

    /**
     * Valida el format del telèfon: dígits, espais, '+' i guions, màxim 20 caràcters.
     */
    private boolean validarTelefon(String telefon) {
        if (telefon == null || telefon.isEmpty()) return true; // opcional
        return telefon.length() <= 20 && telefon.matches("[+\\d\\s\\-]+");
    }

    // =========================================================
    // BAIXA DE CLIENTS
    // =========================================================

    /**
     * Elimina un client del sistema pel seu DNI.
     * No es pot eliminar el client genèric '000'.
     * No es pot eliminar un client que tingui tiquets associats.
     *
     * @param dni DNI del client a eliminar.
     * @return true si el client s'ha eliminat correctament, false en cas contrari.
     */
    public boolean baixaClient(String dni) {

        // Validació: DNI no pot ser el client genèric protegit
        if (esClientGeneric(dni)) {
            System.err.println("Error: No es pot eliminar el Client Genèric '000' del sistema.");
            return false;
        }

        // Validació: el client ha d'existir
        if (!clientDAO.existeix(dni.trim())) {
            System.err.println("Error: No existeix cap client amb el DNI '" + dni + "'.");
            return false;
        }

        // Validació: el client no pot tenir tiquets associats
        if (clientDAO.teTiquets(dni.trim())) {
            System.err.println("Error: No es pot eliminar el client '" + dni + "' perquè té tiquets associats.");
            return false;
        }

        boolean resultat = clientDAO.delete(dni.trim());

        if (resultat) {
            System.out.println("Client amb DNI '" + dni + "' eliminat correctament.");
        } else {
            System.err.println("Error eliminant el client de la base de dades.");
        }

        return resultat;
    }

    // =========================================================
    // GETTER
    // =========================================================

    public ClientDAO getClientDAO() {
        return clientDAO;
    }
}
