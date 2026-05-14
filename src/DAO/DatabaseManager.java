package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private Connection conn;

    // Constructor que rep una conexió activa
    public DatabaseManager(Connection conn) {
        this.conn = conn;
    }

    //Ejecuta una consulta SELECT
     
    public ResultSet executeQuery(String sql) {

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            return stmt.executeQuery();

        } catch (SQLException e) {

            System.err.println("Error en SELECT: " + e.getMessage());

            return null;
        }
    }

    //Ejecuta una operación INSERT, UPDATE o DELETE
    
    public int executeUpdate(String sql) {

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            return stmt.executeUpdate();

        } catch (SQLException e) {

            System.err.println("Error en UPDATE: " + e.getMessage());

            return 0;
        }
    }

    // Cierra la conexión a la base de datos
    public void close() {

        try {

            if (conn != null) {
                conn.close();
                System.out.println("Connexió tancada");
            }

        } catch (SQLException e) {

            System.err.println("Error tancant connexió: " + e.getMessage());
        }
    }
}
