package Controller;

import View.ViewPrincipal;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author adrian
 */
public class CtlViewPrincipal implements ActionListener, WindowListener {

    CtlGame game;
    CtlRanking ranking;
    ViewPrincipal viewPrincipal;
    CtlDifficulty difficulty;
    File file = new File("buscaminas.bin");

    public CtlViewPrincipal() {
        viewPrincipal = new ViewPrincipal();
        viewPrincipal.getKbtmJugar().addActionListener(this);
        viewPrincipal.getkCarregar().addActionListener(this);
        viewPrincipal.getKbtmRanking().addActionListener(this);
    }
    
/**
 * Arranquem vista principal
 */
    public void run() {
        viewPrincipal.setVisible(true);
        viewPrincipal.setTitle("Principal");
        viewPrincipal.setLocationRelativeTo(null);
    }

/**
 * Comencem joc
 */
    private void begin() {
        String jugador = viewPrincipal.getNomUsuari().getText();
        difficulty = new CtlDifficulty(jugador);
        difficulty.run();
        viewPrincipal.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent a) {
        UIManager UI = new UIManager();
        UI.put("OptionPane.background", Color.darkGray);
        UI.put("Panel.background", Color.darkGray);
        UI.put("OptionPane.messageForeground", Color.white);

//Botó carregar partida
        if (a.getActionCommand().equals("Carregar")) {
            if (file.exists()) {
                final String[] buttons = {"Si", "No"};
                String message = "Hi ha una partida guardada, desea recuperar-la?";
                int i = JOptionPane.showOptionDialog(viewPrincipal, message, "Avís", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[0]);
                if (i == 0) {
                    game = new CtlGame(true);
                    game.run();
                    viewPrincipal.setVisible(false);
                }
            } else {
                JOptionPane.showMessageDialog(this.viewPrincipal, "No s'ha trobat cap partida registrada");
            }
//Boto mostrar ranking
        } else if (a.getActionCommand().equals("Ranking")) {
            try {
                ranking = new CtlRanking(true);
                viewPrincipal.setVisible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
//Boto jugar, amb control de text buit
        } else {
            if (viewPrincipal.getNomUsuari().getText().equals("")) {
                JOptionPane.showMessageDialog(viewPrincipal, "Has de posar el nom del jugador!");
            } else {
                begin();
            }
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
