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
    // INSERT - Alta d'articles
    // =========================================================

    /**
     * Insereix una camisa nova a la taula articles.
     */
    public boolean insertCamisa(Camisa c) {
        String sql = "INSERT INTO articles " +
                     "(id, nom, familia, talla_coll, amplada_pit, preu_base, iva, stock) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getId());
            ps.setString(2, c.getNom());
            ps.setString(3, c.getFamilia());
            ps.setInt(4, c.getTallaColl());
            ps.setInt(5, c.getAmpladaPit());
            ps.setDouble(6, c.getPreuBase());
            ps.setInt(7, c.getIva());
            ps.setInt(8, c.getStock());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error inserint camisa: " + e.getMessage());
            return false;
        }
    }

    /**
     * Insereix un pantaló nou a la taula articles.
     */
    public boolean insertPantalo(Pantalo p) {
        String sql = "INSERT INTO articles " +
                     "(id, nom, familia, talla_cintura, llargada_camal, preu_base, iva, stock) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setString(2, p.getNom());
            ps.setString(3, p.getFamilia());
            ps.setInt(4, p.getTallaCintura());
            ps.setInt(5, p.getLlargadaCamal());
            ps.setDouble(6, p.getPreuBase());
            ps.setInt(7, p.getIva());
            ps.setInt(8, p.getStock());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error inserint pantaló: " + e.getMessage());
            return false;
        }
    }

    // =========================================================
    // SELECT - Consultes
    // =========================================================

    /**
     * Obté tots els articles.
     */
    public List<Article> getAll() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles ORDER BY id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                articles.add(mapRowToArticle(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error llegint articles: " + e.getMessage());
        }
        return articles;
    }

    /**
     * Obté un article pel seu ID.
     */
    public Article getById(int id) {
        String sql = "SELECT * FROM articles WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToArticle(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error llegint article per ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obté todos los articles d'una familia.
     */
    public List<Article> getByFamilia(String familia) {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE familia = ? ORDER BY id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, familia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    articles.add(mapRowToArticle(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error llegint articles per familia: " + e.getMessage());
        }
        return articles;
    }

    /**
     * Comprova si un ID ja existeix a la taula.
     */
    public boolean existeix(int id) {
        return getById(id) != null;
    }

    // =========================================================
    // UPDATE - Actualitzacions
    // =========================================================

    /**
     * Actualitza el stock d'un article.
     */
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

    /**
     * Actualitza una camisa sencera.
     */
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

    /**
     * Actualitza un pantaló sencer.
     */
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
    // DELETE - Eliminacions
    // =========================================================

    /**
     * Elimina un article.
     */
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

    /**
     * Convierte un ResultSet a un Article (Camisa o Pantalo).
     */
    private Article mapRowToArticle(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nom = rs.getString("nom");
        String familia = rs.getString("familia");
        double preuBase = rs.getDouble("preu_base");
        int iva = rs.getInt("iva");
        int stock = rs.getInt("stock");

        if ("camisa".equals(familia)) {
            int tallaColl = rs.getInt("talla_coll");
            int ampladaPit = rs.getInt("amplada_pit");
            return new Camisa(id, nom, preuBase, iva, stock, tallaColl, ampladaPit);
        } else {
            int tallaCintura = rs.getInt("talla_cintura");
            int llargadaCamal = rs.getInt("llargada_camal");
            return new Pantalo(id, nom, preuBase, iva, stock, tallaCintura, llargadaCamal);
        }
    }
}
