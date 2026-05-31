package DAO;

import objectes.Tiquet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TiquetDAO {

    private Connection conn;

    public TiquetDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================================================
    // INSERT
    // =========================================================

    public int insert(Tiquet t) {
        String sql = "INSERT INTO tiquets (data_compra, dni_client, total_base, total_iva, total_final) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getDataCompra());
            ps.setString(2, t.getDniClient());
            ps.setDouble(3, t.getTotalBase());
            ps.setDouble(4, t.getTotalIva());
            ps.setDouble(5, t.getTotalFinal());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error inserint tiquet: " + e.getMessage());
        }
        return -1;
    }

    // =========================================================
    // SELECT
    // =========================================================

    public List<Tiquet> getByDniClient(String dniClient) {
        List<Tiquet> tiquets = new ArrayList<>();
        String sql = "SELECT * FROM tiquets WHERE dni_client = ? ORDER BY id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dniClient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) tiquets.add(mapRowToTiquet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error llegint tiquets del client: " + e.getMessage());
        }
        return tiquets;
    }

    public Tiquet getById(int id) {
        String sql = "SELECT * FROM tiquets WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToTiquet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error llegint tiquet per ID: " + e.getMessage());
        }
        return null;
    }

    // =========================================================
    // AUXILIAR - Mapeig ResultSet
    // =========================================================

    private Tiquet mapRowToTiquet(ResultSet rs) throws SQLException {
        Tiquet t = new Tiquet(
                rs.getString("data_compra"),
                rs.getString("dni_client"),
                rs.getDouble("total_base"),
                rs.getDouble("total_iva"),
                rs.getDouble("total_final")
        );
        t.setId(rs.getInt("id"));
        return t;
    }

    // =========================================================
    // CONSULTA VENDES PER CLIENT
    // =========================================================

    public void consultaVendesPerClient() {
        String sql = "SELECT c.dni, c.nom, COUNT(t.id) AS num_tiquets, " +
                     "SUM(t.total_final) AS total_gastat " +
                     "FROM clients c " +
                     "LEFT JOIN tiquets t ON c.dni = t.dni_client " +
                     "GROUP BY c.dni, c.nom " +
                     "ORDER BY total_gastat DESC";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            String sep  = "=".repeat(60);
            String line = "-".repeat(60);

            System.out.println("\n" + sep);
            System.out.println("         CONSULTA DE VENDES PER CLIENT");
            System.out.println(sep);
            System.out.printf("  %-12s %-20s %8s %12s%n", "DNI", "NOM", "TIQUETS", "TOTAL");
            System.out.println(line);

            while (rs.next()) {
                System.out.printf("  %-12s %-20s %8d %11.2f$%n",
                        rs.getString("dni"),
                        rs.getString("nom"),
                        rs.getInt("num_tiquets"),
                        rs.getDouble("total_gastat")
                );
            }

            System.out.println(sep + "\n");

        } catch (SQLException e) {
            System.err.println("Error en la consulta de vendes per client: " + e.getMessage());
        }
    }
}
