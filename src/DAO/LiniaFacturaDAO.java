package DAO;

import objectes.LiniaFactura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LiniaFacturaDAO {

    private Connection conn;

    public LiniaFacturaDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================================================
    // INSERT
    // =========================================================

    public boolean insert(LiniaFactura l) {
        String sql = "INSERT INTO linies_factura (id_tiquet, id_article, quantitat, preu_base, iva, preu_final) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, l.getIdTiquet());
            ps.setInt(2, l.getIdArticle());
            ps.setInt(3, l.getQuantitat());
            ps.setDouble(4, l.getPreuBase());
            ps.setDouble(5, l.getIva());
            ps.setDouble(6, l.getPreuFinal());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserint linia de factura: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // SELECT
    // =========================================================

    public List<LiniaFactura> getByIdTiquet(int idTiquet) {
        List<LiniaFactura> linies = new ArrayList<>();
        String sql = "SELECT * FROM linies_factura WHERE id_tiquet = ? ORDER BY id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTiquet);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) linies.add(mapRowToLinia(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error llegint linies del tiquet: " + e.getMessage());
        }
        return linies;
    }

    // =========================================================
    // AUXILIAR - Mapeig ResultSet
    // =========================================================

    private LiniaFactura mapRowToLinia(ResultSet rs) throws SQLException {
        return new LiniaFactura(
                rs.getInt("id_tiquet"),
                rs.getInt("id_article"),
                rs.getInt("quantitat"),
                rs.getDouble("preu_base"),
                rs.getDouble("iva"),
                rs.getDouble("preu_final")
        );
    }
}