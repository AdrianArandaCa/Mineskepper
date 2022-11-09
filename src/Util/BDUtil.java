/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Model.Player;
import persit.BaseDAO;

/**
 *
 * @author adrian
 */
public class BDUtil {

    /**
     * Creem l'estructura de la BDD
     *
     * @throws SQLException
     */
    public static void createStructure() throws SQLException {
        try {
            BaseDAO.connect();
            Connection conn = BaseDAO.getConn();

            String query = "CREATE TABLE jugador (nom VARCHAR(40), puntuacio INTEGER, dificultat INTEGER)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate();
            stmt.close();

            
        } catch (Exception e) {
            System.out.println("Ja n'hi han dades");
        }

    }

    /**
     * Netejem i esborrem les taules
     */
    public static void cleanTables() {
        
        try {
            BaseDAO.connect();
            Connection conn = BaseDAO.getConn();

            PreparedStatement stmt;
            stmt = conn.prepareStatement("drop table jugador");
            stmt.executeUpdate();
            stmt.close();
            
        } catch (Exception e) {
            System.out.println("No hi ha res a netejar!");
        }
    }
}
