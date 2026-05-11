package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    private Connection conn;

    /**
     * Constructor que recibe una conexión activa
     * @param conn Conexión a la base de datos
     */
    public DatabaseManager(Connection conn) {
        this.conn = conn;
    }

    /**
     * Ejecuta una consulta SELECT
     * @param sql Sentencia SQL SELECT
     * @return ResultSet con los resultados o null si hay error
     */
    public ResultSet executeQuery(String sql) {

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            return stmt.executeQuery();

        } catch (SQLException e) {

            System.err.println("Error en SELECT: " + e.getMessage());

            return null;
        }
    }

    /**
     * Ejecuta una operación INSERT, UPDATE o DELETE
     * @param sql Sentencia SQL de modificación
     * @return Número de filas afectadas o 0 si hay error
     */
    public int executeUpdate(String sql) {

        try {

            PreparedStatement stmt = conn.prepareStatement(sql);

            return stmt.executeUpdate();

        } catch (SQLException e) {

            System.err.println("Error en UPDATE: " + e.getMessage());

            return 0;
        }
    }

    /**
     * Cierra la conexión a la base de datos
     */
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
