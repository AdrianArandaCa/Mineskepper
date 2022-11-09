package Controller;

import View.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author adrian
 */
public class CtlDifficulty implements ActionListener, WindowListener {

    ViewPrincipal viewPrincipal;
    CtlViewPrincipal principal;
    CtlGame game;
    ViewDifficulty viewDifficulty;
    ViewRanking viewRanking;
    String j = "";

    /**
     * Controlador
     *
     * @param player
     */
    public CtlDifficulty(String player) {
        viewDifficulty = new ViewDifficulty(this.viewPrincipal, true);
        viewDifficulty.getRbFacil().addActionListener(this);
        viewDifficulty.getRbNormal().addActionListener(this);
        viewDifficulty.getRbDificil().addActionListener(this);
        this.j = player;
    }

    /**
     * Iniciem vista
     */
    public void run() {
        viewDifficulty.setLocationRelativeTo(null);
        viewDifficulty.setTitle("Dificultat");
        viewDifficulty.setVisible(true);
    }

    /**
     * Botons dificultat
     *
     * @param a
     */
    @Override
    public void actionPerformed(ActionEvent a) {
//Botó Facil
        if (a.getActionCommand().equals("Facil")) {
            begin(1);
//Boto Mitg
        } else if (a.getActionCommand().equals("Mitg")) {
            begin(2);
//Boto Dificil
        } else {
            begin(3);
        }
    }
/**
 * Començar el joc
 * @param codi 
 */
    private void begin(int codi) {
        game = new CtlGame(j, codi);
        game.run();
        viewDifficulty.setVisible(false);

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
