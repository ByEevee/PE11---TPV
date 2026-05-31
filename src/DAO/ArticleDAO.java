package DAO;

import objectes.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDAO {

    private Connection conn;

    public ArticleDAO(Connection conn) {
        this.conn = conn;
    }

    // =========================================================
    // INSERT
    // =========================================================

    public boolean insertCamisa(Camisa c) {
        String sql = "INSERT INTO articles " +
                     "(id, nom, familia, talla_coll, amplada_pit, preu_base, iva, stock) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getId());
            ps.setString(2, c.getNom());
            ps.setInt(3, c.getFamilia());       // INT
            ps.setInt(4, c.getTallaColl());
            ps.setInt(5, c.getAmpladaPit());
            ps.setDouble(6, c.getPreuBase());
            ps.setInt(7, c.getIva());
            ps.setInt(8, c.getStock());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserint camisa: " + e.getMessage());
            return false;
        }
    }

    public boolean insertPantalo(Pantalo p) {
        String sql = "INSERT INTO articles " +
                     "(id, nom, familia, talla_cintura, llargada_camal, preu_base, iva, stock) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setString(2, p.getNom());
            ps.setInt(3, p.getFamilia());       // INT
            ps.setInt(4, p.getTallaCintura());
            ps.setInt(5, p.getLlargadaCamal());
            ps.setDouble(6, p.getPreuBase());
            ps.setInt(7, p.getIva());
            ps.setInt(8, p.getStock());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserint pantaló: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // SELECT
    // =========================================================

    public List<Article> getAll() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles ORDER BY id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) articles.add(mapRowToArticle(rs));
        } catch (SQLException e) {
            System.err.println("Error llegint articles: " + e.getMessage());
        }
        return articles;
    }

    public Article getById(int id) {
        String sql = "SELECT * FROM articles WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRowToArticle(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error llegint article per ID: " + e.getMessage());
        }
        return null;
    }

    /** Cerca per codi numèric (ex: Familia.CAMISA). */
    public List<Article> getByFamilia(int codiFamilia) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE familia = ? ORDER BY id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codiFamilia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) articles.add(mapRowToArticle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error llegint articles per familia: " + e.getMessage());
        }
        return articles;
    }

    public boolean existeix(int id) {
        return getById(id) != null;
    }

    // =========================================================
    // UPDATE
    // =========================================================

    public boolean updateStock(int id, int nouStock) {
        String sql = "UPDATE articles SET stock = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, nouStock);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualitzant stock: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCamisa(Camisa c) {
        String sql = "UPDATE articles SET nom = ?, preu_base = ?, iva = ?, stock = ?, " +
                     "talla_coll = ?, amplada_pit = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNom());
            ps.setDouble(2, c.getPreuBase());
            ps.setInt(3, c.getIva());
            ps.setInt(4, c.getStock());
            ps.setInt(5, c.getTallaColl());
            ps.setInt(6, c.getAmpladaPit());
            ps.setInt(7, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualitzant camisa: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePantalo(Pantalo p) {
        String sql = "UPDATE articles SET nom = ?, preu_base = ?, iva = ?, stock = ?, " +
                     "talla_cintura = ?, llargada_camal = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setDouble(2, p.getPreuBase());
            ps.setInt(3, p.getIva());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getTallaCintura());
            ps.setInt(6, p.getLlargadaCamal());
            ps.setInt(7, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualitzant pantaló: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // DELETE
    // =========================================================

    public boolean delete(int id) {
        String sql = "DELETE FROM articles WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminant article: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // AUXILIAR - Mapeig ResultSet
    // =========================================================

    private Article mapRowToArticle(ResultSet rs) throws SQLException {
        int id          = rs.getInt("id");
        String nom      = rs.getString("nom");
        int familia     = rs.getInt("familia");   // llegim INT
        double preuBase = rs.getDouble("preu_base");
        int iva         = rs.getInt("iva");
        int stock       = rs.getInt("stock");

        if (familia == Familia.CAMISA) {
            int tallaColl  = rs.getInt("talla_coll");
            int ampladaPit = rs.getInt("amplada_pit");
            return new Camisa(id, nom, preuBase, iva, stock, tallaColl, ampladaPit);
        } else if (familia == Familia.PANTALO) {
            int tallaCintura  = rs.getInt("talla_cintura");
            int llargadaCamal = rs.getInt("llargada_camal");
            return new Pantalo(id, nom, preuBase, iva, stock, tallaCintura, llargadaCamal);
        } else {
            throw new SQLException("Família desconeguda: " + familia);
        }
    }

    // =========================================================
    // CONSULTA ARTICLES PER SOTA DEL LLINDAR
    // =========================================================

    public List<Article> getArticlesPerSotaLlindar(int llindar) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE stock < ? ORDER BY id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, llindar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) articles.add(mapRowToArticle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error consultant articles per sota del llindar: " + e.getMessage());
        }
        return articles;
    }
}