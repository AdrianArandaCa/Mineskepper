/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author adrian
 */
public class BaseDAO {

    static final String driver = "com.mysql.cj.jdbc.Driver";
    static final String url = "jdbc:mysql://localhost:3306/";
    static final String dbName = "AdrianArandaBuscaminas";
    static final String userName = "adrian";
    static final String password = "adrian";
    protected static Connection conn = null;

    /**
     * Fem la connexió
     */
    public static void connect() {
        if (conn == null) {
            try {
                Class.forName(driver);
                conn = DriverManager.getConnection(url + dbName, userName, password);
            } catch (SQLException | ClassNotFoundException ex) {
                throw new ExceptionInInitializerError(ex);
            }
        }

    }

    /**
     * Obtenim la connexió
     *
     * @return conn ens conectem aquesta
     */
    public static Connection getConn() {
        return conn;
    }

    /**
     * Tanquem connexió
     *
     * @throws SQLException
     */
    public static void close() throws SQLException {
        if (conn != null) {
            conn.close();
        }
        conn = null;
    }
}
