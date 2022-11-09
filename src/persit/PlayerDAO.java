/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Model.Player;
import persit.BaseDAO;

/**
 *
 * @author adrian
 */
public class PlayerDAO extends BaseDAO {

    /**
     * Constructor
     */
    public PlayerDAO() {
        this.connect();
    }

    /**
     * Obtenim categoria
     *
     * @param id de la categoria que volem
     * @return categoria
     * @throws SQLException
     */
    public Player getJugador(String nom, int dificultat) throws SQLException {
        PreparedStatement stmt;
        ResultSet rs;
        Player jugador = null;

        String query = "SELECT * FROM jugador WHERE nom = ? and dificultat = ?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, nom);
        stmt.setInt(2, dificultat);
        rs = stmt.executeQuery();

        if (rs.next()) {

            jugador = new Player("");
            jugador.setName(rs.getString("nom"));
            jugador.setPunctuation(rs.getInt("puntuacio"));
            jugador.setDificultat(rs.getInt("dificultat"));

        }
        rs.close();
        stmt.close();
        return jugador;
    }

    /**
     * insertem una nova categoria
     *
     * @param jugador a insertar
     * @return boolean si s'ha insertat o no
     * @throws SQLException
     */
    public boolean insert(Player jugador) throws SQLException {
        String query = "INSERT INTO jugador (nom, puntuacio, dificultat) VALUES (?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setString(1, jugador.getName());
        stmt.setInt(2, jugador.getPunctuation());
        stmt.setInt(3, jugador.getDificultat());

        int affectedRows = stmt.executeUpdate();
        stmt.close();
        return affectedRows > 0;
    }

    /**
     * Esborrem una categoria pel seu id
     *
     * @param id de la categoria a esborrar
     * @return boolean si s'ha esborrat o no
     * @throws SQLException
     */
    public boolean delete(String nom) throws SQLException {

        String query = "DELETE FROM jugador WHERE nom = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, nom);
        stmt.executeUpdate();

        int count = stmt.executeUpdate();
        stmt.close();
        return count > 0;
    }

    /**
     * actualitzem categoria
     *
     * @param jugador a actualitzar
     * @return boolean si s'ha actualitzat o no
     * @throws SQLException
     */
    public boolean update(Player jugador) throws SQLException {
        String query = "UPDATE jugador SET puntuacio= ?, dificultat = ? WHERE nom = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, jugador.getPunctuation());
        stmt.setInt(2, jugador.getDificultat());
        stmt.setString(3, jugador.getName());

        int count = stmt.executeUpdate();
        stmt.close();
        return count > 0;
    }

    /**
     * ArrayList de categoria que fem la consulta
     *
     * @return categories llistat de totes les categories
     */
    public ArrayList<Player> consultaJugadors() {
        ArrayList<Player> jugadors = new ArrayList();
        try {
            ResultSet rs;
            PreparedStatement stmt;

            String query = "SELECT * FROM jugador ORDER BY puntuacio desc";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Player jugador = new Player("");
                jugador.setName(rs.getString("nom"));
                jugador.setPunctuation(rs.getInt("puntuacio"));
                jugador.setDificultat(rs.getInt("dificultat"));
                jugadors.add(jugador);
            }
            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jugadors;

    }
}
