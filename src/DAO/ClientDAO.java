package DAO;

import objectes.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per a la gestió de clients a la base de dades.
 * Proporciona operacions CRUD sobre la taula 'clients'.
 */
public class ClientDAO {

    private Connection conn;

    public ClientDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================================================
    // INSERT – Alta de clients
    // =========================================================

    /**
     * Insereix un client nou a la taula clients.
     *
     * @param c Client a inserir.
     * @return true si la inserció ha tingut èxit, false en cas contrari.
     */
    public boolean insert(Client c) {
        String sql = "INSERT INTO clients (dni, nom, email, telefon) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getDni());
            ps.setString(2, c.getNom());

            // email i telèfon són opcionals (poden ser NULL)
            if (c.getEmail() != null && !c.getEmail().isEmpty()) {
                ps.setString(3, c.getEmail());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }

            if (c.getTelefon() != null && !c.getTelefon().isEmpty()) {
                ps.setString(4, c.getTelefon());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }

            int filesAfectades = ps.executeUpdate();
            return filesAfectades > 0;

        } catch (SQLException e) {
            System.err.println("Error inserint client: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // SELECT – Consultes
    // =========================================================

    /**
     * Obté tots els clients de la taula, ordenats per DNI.
     */
    public List<Client> getAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY dni";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(mapRowToClient(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error llegint clients: " + e.getMessage());
        }
        return clients;
    }

    /**
     * Obté un client pel seu DNI.
     *
     * @param dni DNI del client a cercar.
     * @return El client trobat, o null si no existeix.
     */
    public Client getByDni(String dni) {
        String sql = "SELECT * FROM clients WHERE dni = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToClient(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error llegint client per DNI: " + e.getMessage());
        }
        return null;
    }

    /**
     * Comprova si un DNI ja existeix a la taula clients.
     *
     * @param dni DNI a comprovar.
     * @return true si ja existeix, false si és nou.
     */
    public boolean existeix(String dni) {
        return getByDni(dni) != null;
    }

    // =========================================================
    // UPDATE – Actualitzacions
    // =========================================================

    /**
     * Actualitza les dades d'un client existent (nom, email, telèfon).
     * El DNI és la clau i no es pot modificar.
     *
     * @param c Client amb les dades actualitzades.
     * @return true si l'actualització ha tingut èxit, false en cas contrari.
     */
    public boolean update(Client c) {
        String sql = "UPDATE clients SET nom = ?, email = ?, telefon = ? WHERE dni = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNom());

            if (c.getEmail() != null && !c.getEmail().isEmpty()) {
                ps.setString(2, c.getEmail());
            } else {
                ps.setNull(2, Types.VARCHAR);
            }

            if (c.getTelefon() != null && !c.getTelefon().isEmpty()) {
                ps.setString(3, c.getTelefon());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }

            ps.setString(4, c.getDni());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error actualitzant client: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // DELETE – Eliminacions
    // =========================================================

    /**
     * Elimina un client de la taula.
     * No elimina el client genèric '000' ni clients amb tiquets associats.
     *
     * @param dni DNI del client a eliminar.
     * @return true si s'ha eliminat correctament, false en cas contrari.
     */
    public boolean delete(String dni) {
        String sql = "DELETE FROM clients WHERE dni = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error eliminant client: " + e.getMessage());
            return false;
        }
    }

    /**
     * Comprova si un client té tiquets associats (per impedir la seva eliminació).
     *
     * @param dni DNI del client a comprovar.
     * @return true si té tiquets, false si no en té.
     */
    public boolean teTiquets(String dni) {
        String sql = "SELECT COUNT(*) FROM tiquets WHERE dni_client = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error comprovant tiquets del client: " + e.getMessage());
        }
        return false;
    }

    // =========================================================
    // AUXILIAR – Mapeig ResultSet
    // =========================================================

    /**
     * Converteix una fila del ResultSet en un objecte Client.
     */
    private Client mapRowToClient(ResultSet rs) throws SQLException {
        String dni     = rs.getString("dni");
        String nom     = rs.getString("nom");
        String email   = rs.getString("email");
        String telefon = rs.getString("telefon");
        return new Client(dni, nom, email, telefon);
    }
}
