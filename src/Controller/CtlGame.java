package Controller;

import Model.Player;
import Model.Board;
import View.ViewGame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author adrian
 */
public class CtlGame extends MouseAdapter implements ActionListener, WindowListener, java.io.Serializable {

    CtlViewPrincipal principal;
    CtlDifficulty dificultat;
    CtlRanking ranking;
    File fitxer;
    String nFitxer, jugador;
    JButton btm[][];
    Board tablero;
    ViewGame view;
    int dif, ban, bomb, fila, puntuacio = 0;
    boolean fiPartida = false, minaExplota = false;
    ImageIcon bomba = new ImageIcon("bomba.png"), banderin = new ImageIcon("banderin.png");

    /**
     * Constructor per carregar partida
     *
     * @param carregarPartida
     */
    public CtlGame(boolean carregarPartida) {
        tablero = loadGame();
        view = new ViewGame();
        fila = tablero.getRow();
        jugador = tablero.getPlayer();
        bomb = tablero.getBomb();
        carregarBtm(fila);
        paintBox();
        mirarMinesMarcades();
        view.getkBtmGuardar().addActionListener(this);
        infoLabel();
        
    }

    /**
     * Constructor de joc nou
     *
     * @param jugador1
     * @param dificultat
     */
    public CtlGame(String jugador1, int dificultat) {
        switch (dificultat) {
            //Opció facil
            case 1:
                fila = 8;
                bomb = 8;
                view = new ViewGame();
                infoTaulell(jugador1, dificultat, fila, bomb);
//                carregarBtm(fila);
//                view.getkBtmGuardar().addActionListener(this);
                infoLabel();
                break;
            //Opció Mitg
            case 2:
                fila = 9;
                bomb = 9;
                view = new ViewGame();
                infoTaulell(jugador1, dificultat, fila, bomb);
                infoLabel();
                break;
            //Opció Dificil
            case 3:
                fila = 10;
                bomb = 10;
                view = new ViewGame();
                infoTaulell(jugador1, dificultat, fila, bomb);
                
//                carregarBtm(fila);
//                view.getkBtmGuardar().addActionListener(this);
                infoLabel();
                break;
            default:
                System.out.println("No es pot jugar");
        }
    }

    /**
     * Iniciem vista
     */
    public void run() {
        view.setVisible(true);
        view.setTitle("Buscaminas");
        view.setLocationRelativeTo(null);
    }
    
    public void infoTaulell(String jugador1, int dificultat, int fila, int bomb){
        jugador = jugador1;
        tablero = new Board(fila, bomb);
        tablero.setPlayer(jugador);
        tablero.setDifficulty(dificultat);
        tablero.setRow(fila);
        tablero.setBomb(bomb);
        carregarBtm(fila);
        view.getkBtmGuardar().addActionListener(this);
    }
    
    public void infoLabel(){
        view.txtBanderin.setText(ban + " / " + bomb);
        view.txtJugador.setText(jugador);
        view.txtJugador.setForeground(Color.white);
        view.labelJugador.setForeground(Color.white);
        view.txtBanderin.setForeground(Color.white);
        view.jLabel1.setForeground(Color.white);
    }

    /**
     * Botons per jugar o guardar partida
     *
     * @param a
     */
    @Override
    public void actionPerformed(ActionEvent a) {

//Guardem partida
        if (a.getActionCommand().equals("Guardar")) {
            saveGame();

//Juguem al buscamines
        } else {

//Retorna posició del botó pres
            String nn = ((JButton) a.getSource()).getName();
            int fila = Integer.parseInt(nn.substring(0, 1));
            int col = Integer.parseInt(nn.substring(1, 2));

//Mirem si hi esta descoberta aquesta casella, si no esta, descobrim casella
            if (!tablero.descoberta(fila, col)) {
                try {
                    if (tablero.minaMarcada(fila, col)) {
                        System.out.println("===============entra=================");
                    } else {
                        uncoverBox(fila, col, a);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!fiPartida) {
//Si totes estan destapades sense bombes, guanyem el joc
                gameWon();
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        String nn = ((JButton) e.getSource()).getName();
        int fila = Integer.parseInt(nn.substring(0, 1));
        int col = Integer.parseInt(nn.substring(1, 2));

        //Detecta el botó dret de ratolí
        if ((e.getButton() == MouseEvent.BUTTON3)) {
            if (tablero.minaMarcada(fila, col)) {
                tablero.marcaMina(fila, col);
                btm[fila][col].setIcon(null);
                ban--;
                view.txtBanderin.setText(ban + " / " + bomb);
            } else if (!tablero.descoberta(fila, col) && ban < bomb) {
                tablero.marcaMina(fila, col);
                btm[fila][col].setIcon(banderin);
                ban++;
                view.txtBanderin.setText(ban + " / " + bomb);
            }
        }
    }

    /**
     * Calcula puntuació final per quantitat de caselles descubertes
     *
     * @return puntuacio del jugador
     */
    private int calcPuntuacio() {
        int puntuacio = 0;
        boolean desc, mina, marcada;
        for (int i = 0; i < btm.length; i++) {
            for (int j = 0; j < btm[0].length; j++) {
                desc = false;
                mina = false;
                marcada = false;
                desc = tablero.descoberta(i, j);
                mina = tablero.hiHaMina(i, j);
                marcada = tablero.minaMarcada(i, j);
                if (desc && !mina) {
                    puntuacio++;
                }
            }
        }
        return puntuacio;
    }

    /**
     * Carreguem els botons a la vista
     *
     * @param fila quantitat de files*columnes que volem a la nostra vista
     */
    public void carregarBtm(int fila) {
        view.getjPanel1().setLayout(new java.awt.GridLayout(fila, fila));
        btm = new JButton[fila][fila];
        for (int i = 0; i < btm.length; i++) {
            for (int j = 0; j < btm[0].length; j++) {
                btm[i][j] = new JButton();
                btm[i][j].addActionListener(this);
                btm[i][j].addMouseListener(this);
                btm[i][j].setName(Integer.toString(i) + Integer.toString(j));
                btm[i][j].setText("");
                btm[i][j].setBackground(Color.gray);
                btm[i][j].setFont(new java.awt.Font("Tahoma", 0, 36));
                view.getjPanel1().add(btm[i][j]);
            }
        }
    }

    /**
     * Descobrim caselles
     *
     * @param fila
     * @param col
     * @param a
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void uncoverBox(int fila, int col, ActionEvent a) throws SQLException, ClassNotFoundException {
        tablero.descobreixCasella(fila, col);
        if (tablero.hiHaMina(fila, col)) {
//Descobrim totes les mines i hem perdut la partida
            paintAll();
            mineExplodes(fila, col, a);
        } else {
//Pintem el botó i continuem
            paintBox();
        }
    }

    /**
     * Guanya la partida, s'acaba el joc
     */
    public void gameWon() {
        UIManager UI = new UIManager();
        UI.put("OptionPane.background", Color.darkGray);
        UI.put("Panel.background", Color.darkGray);
        UI.put("OptionPane.messageForeground", Color.white);
        Player j = new Player();
        if (tablero.descobert()) {
            fiPartida = true;
            tablero.toString();
            JOptionPane.showMessageDialog(view, "Has Ganado!\nPuntuación de " + jugador + ": " + calcPuntuacio());
            j.setName(jugador);
            j.setPunctuation(calcPuntuacio());
            j.setDificultat(tablero.getDifficulty());
//Guardem al ranking al jugador i la puntuació seva.
            try {
                ranking = new CtlRanking(false);
                if (!ranking.saveJugador(j)) {
                    JOptionPane.showMessageDialog(view, "El jugador " + jugador + " a tret menys puntació que la seva màxima");
                }
            } catch (Exception e) {
                e.toString();
            }
//Mostrem missatge per tornar a jugar
            int i = missatge();
            if (i == 0) {
                view.setVisible(false);
                dificultat = new CtlDifficulty(jugador);
                dificultat.run();
            } else {
                view.setVisible(false);
                principal = new CtlViewPrincipal();
                principal.run();
            }
        }
    }

    /**
     * Explota mina, s'acaba el joc
     *
     * @param fila
     * @param col
     * @param a
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void mineExplodes(int fila, int col, ActionEvent a) throws SQLException, ClassNotFoundException {
        UIManager UI = new UIManager();
        UI.put("OptionPane.background", Color.darkGray);
        UI.put("Panel.background", Color.darkGray);
        UI.put("OptionPane.messageForeground", Color.white);
        Player j = new Player();
        minaExplota = true;
        ((JButton) a.getSource()).setIcon(bomba);
        JOptionPane.showMessageDialog(view, "Has Perdido.\nPuntuación de " + jugador + ": " + calcPuntuacio());
        tablero.toString();
        j.setName(jugador);
        j.setPunctuation(calcPuntuacio());
        j.setDificultat(tablero.getDifficulty());
//Guardem al ranking al jugador i la puntuació seva.
        try {
            ranking = new CtlRanking(false);
            if (!ranking.saveJugador(j)) {
                JOptionPane.showMessageDialog(view, "El jugador " + jugador + " a tret menys puntació que la seva màxima");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//Mostrem missatge per tornar a jugar
        int i = missatge();
        if (i == 0) {
            view.setVisible(false);
            dificultat = new CtlDifficulty(jugador);
            dificultat.run();
        } else {
            view.setVisible(false);
            principal = new CtlViewPrincipal();
            principal.run();
        }

    }

    /**
     * Mostrem missatge si vol tornar a jugar
     *
     * @return i, es el botó que ha pres, si = 0 | no = 1
     */
    private int missatge() {
        final String[] botons = {"Si", "No"};
        UIManager UI = new UIManager();
        UI.put("OptionPane.background", Color.darkGray);
        UI.put("Panel.background", Color.darkGray);
        UI.put("OptionPane.messageForeground", Color.white);
        String missatge = "Vols tornar a jugar?";
        int i = JOptionPane.showOptionDialog(view, missatge, "Avís", JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, botons, botons[0]);
        return i;
    }

    /**
     * Pintem caselles e indiquem numero de bombes
     */
    public void paintBox() {
        Color naranja = new Color(255, 152, 0);
        for (int i = 0; i < btm.length; i++) {
            for (int j = 0; j < btm[0].length; j++) {
                if (tablero.descoberta(i, j)) {
                    actualitzarTaulell();
                    if (btm[i][j].getText().equals("")) {
                        btm[i][j].setBackground(naranja);
                    }
                }
            }
        }
    }

    public void mirarMinesMarcades() {
        for (int i = 0; i < btm.length; i++) {
            for (int j = 0; j < btm[0].length; j++) {
                if (tablero.minaMarcada(i, j)) {
                    btm[i][j].setIcon(banderin);
                    ban++;
                }
            }
        }
    }

    /**
     * Destapem totes les caselles
     */
    public void paintAll() {
        for (int i = 0; i < btm.length; i++) {
            for (int j = 0; j < btm[0].length; j++) {
                if (tablero.getT()[i][j].getEsMina()) {
                    btm[i][j].setIcon(bomba);
                }
            }
        }
    }

    /**
     * Actualitzem el taulell de joc, amb les caselles amb bombes al costat
     */
    public void actualitzarTaulell() {
        for (int i = 0; i < btm.length; i++) {
            for (int j = 0; j < btm[0].length; j++) {
                btm[i][j].setText(tablero.getT()[i][j].toString());
                if (btm[i][j].getText().equals("1")) {
                    btm[i][j].setForeground(Color.red);
                } else if (btm[i][j].getText().equals("2")) {
                    btm[i][j].setForeground(Color.blue);
                } else if (btm[i][j].getText().equals("3")) {
                    btm[i][j].setForeground(Color.green);
                } else if (tablero.getT()[i][j].toString().equals("*")) {
                    btm[i][j].setText("");
                } else {
                    btm[i][j].setForeground(Color.yellow);
                }
            }
        }
    }

    /**
     * Guardem el joc
     */
    public void saveGame() {
        UIManager UI = new UIManager();
        UI.put("OptionPane.background", Color.darkGray);
        UI.put("Panel.background", Color.darkGray);
        UI.put("OptionPane.messageForeground", Color.white);
        CtlViewPrincipal principal;
        nFitxer = tablero.getPlayer().toLowerCase() + ".bin";
        fitxer = new File("buscaminas.bin");
//Creem i omplim el fitxer amb la matriu lògica
        try {
            principal = new CtlViewPrincipal();
            ObjectOutputStream escritor = new ObjectOutputStream(new FileOutputStream(fitxer));
            escritor.writeObject(tablero);
            escritor.close();
            JOptionPane.showMessageDialog(view, "Fitxer guardat amb exit!");
            principal.run();
            view.setVisible(false);
        } catch (FileNotFoundException e) {
            System.out.println("No se encuentra fichero: buscaminas.bin");
        } catch (IOException e) {
            System.out.println("Problema de acceso a fichero: buscaminas.bin");
        }
    }

    /**
     * Carreguem partida
     *
     * @return tablero, taulell logic de la partida
     */
    public Board loadGame() {
        nFitxer = "buscaminas.bin";
        fitxer = new File(nFitxer);
//Retornem fitxer amb la matriu lògica per iniciar partida, si hi existeix
        try {
            ObjectInputStream lector = new ObjectInputStream(new FileInputStream("buscaminas.bin"));
            //Leemos los bytes del archivo y nos retornará un Object, al que haremos casting a matriz int[]
            tablero = (Board) lector.readObject();
            lector.close();
            fitxer.delete();

        } catch (FileNotFoundException e) {
            System.out.println("No se encuentra fichero: buscaminas.bin");
        } catch (IOException e) {
            System.out.println("Problema de acceso a fichero: buscaminas.bin");
        } catch (ClassNotFoundException e) {
            System.out.println("Los datos obtenidos no permiten recuperar la partida");
        }
        return tablero;
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
