package Controller;

import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import persit.*;
import Model.*;
import View.ViewRanking;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author adrian
 */
public class CtlRanking implements WindowListener, ActionListener {

    CtlViewPrincipal principal;
    CtlGame game;
    ViewRanking viewRanking;
    PlayerDAO jDao;

    /**
     * Constructor
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public CtlRanking(boolean v) throws SQLException, ClassNotFoundException {
        if (v) {
            this.viewRanking = new ViewRanking(this);
            this.viewRanking.setTitle("RANKING JUGADORS");
            this.viewRanking.setInfo(getPlayerDataTable());
            this.viewRanking.getkButton1().addActionListener(this);
            this.viewRanking.setVisible(true);
            this.viewRanking.setLocationRelativeTo(null);
        } else {
            this.viewRanking = new ViewRanking(this);
            this.viewRanking.setTitle("RANKING JUGADORS");
            this.viewRanking.setInfo(getPlayerDataTable());
            this.viewRanking.setVisible(false);
            this.viewRanking.setLocationRelativeTo(null);
        }

    }

    /**
     * Retornem la taula de jugadors
     *
     * @return tm TableModel del jugador
     */
    private TableModel getPlayerDataTable() {
        DefaultTableModel tm = new DefaultTableModel();
        jDao = new PlayerDAO();
        tm.addColumn("Nom");
        tm.addColumn("Puntuacio");
        tm.addColumn("Dificultat");
        ArrayList<Player> jugadors = new ArrayList();
        jugadors = jDao.consultaJugadors();
        for (Player j : jugadors) {
            Object[] row = new Object[3];
            row[0] = j.getName();
            row[1] = j.getPunctuation();
            row[2] = j.getDificultat();
            tm.addRow(row);
        }
        return tm;
    }

    /**
     * Gravem jugador y comprovem si existeix el mateix nom, y si la puntuació
     * es mes alta que la trobada.
     *
     * @param jugador
     * @return boolean si s'ha pogut insertar o no
     * @throws SQLException
     */
    public boolean saveJugador(Player jugador) throws SQLException {
        Player j = jDao.getJugador(jugador.getName(), jugador.getDificultat());
        int difficulty = 0;
        if (j != null) {
            difficulty = j.getDificultat();
        }

        if (j == null || difficulty != jugador.getDificultat()) {
            jDao.insert(jugador);
            return true;
        } else if (j.getPunctuation() <  jugador.getPunctuation()) {
            jDao.update(jugador);
            return true;
        }
        return false;
    }

    /**
     * Boto Menu
     *
     * @param a
     */
    @Override
    public void actionPerformed(ActionEvent a) {
        if (a.getActionCommand().equals("Menu")) {
            principal = new CtlViewPrincipal();
            principal.run();
            viewRanking.setVisible(false);
        }
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

}
