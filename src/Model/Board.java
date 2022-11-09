package Model;

import java.util.Random;
import javax.swing.JButton;

/*
 @author David
 */
public class Board implements java.io.Serializable{

    private static Random rd = new Random();
    private int row;
    private int bomb;
    private String player;
    private Box t[][];
    private int difficulty;
    
    public Board(){};
    
    public Board(int n, int nm) {
        row = n;
        t = new Box[n][n];
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t.length; j++) {
                t[i][j] = new Box();
            }
        }
        posaMines(nm);
        comptaMines();
    }

    public int getBomb() {
        return bomb;
    }

    public void setBomb(int bomb) {
        this.bomb = bomb;
    }
    
    
    public int getRow(){
        return row;
    }
    
    public void setRow(int fila){
        this.row = fila;
    }
    
    public String getPlayer(){
        return player;
    }
    
    public void setPlayer(String jugador){
        this.player = jugador;
    }

    public Box[][] getT() {
        return t;
    }

    public void setT(Box[][] t) {
        this.t = t;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int dificultat) {
        this.difficulty = dificultat;
    }
    
    

    
    
    public void posaMines(int nm) {

        int i, j;

        while (nm > 0) {
            i = rd.nextInt(t.length);
            j = rd.nextInt(t.length);
            if (!t[i][j].getEsMina()) {
                t[i][j].setEsMina(true);
                nm--;
            }
        }
    }

    public void comptaMines() {

        int nm = 0;

        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t.length; j++) {

                nm = 0;
                if (!t[i][j].getEsMina()) {
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if (k >= 0 && k < t.length && l >= 0 && l < t.length) {
                                if (t[k][l].getEsMina()) {
                                    nm++;
                                }
                            }
                        }
                    }
                    t[i][j].setnMines(nm);

                }
            }
        }

    }
    
    

    public int comptaMines(int x, int y) {

        int nm = 0;

        if (!t[x][y].getEsMina()) {
            for (int k = x - 1; k <= y + 1; k++) {
                for (int l = x - 1; l <= y + 1; l++) {
                    if (k >= 0 && k < t.length && l >= 0 && l < t.length) {
                        if (t[k][l].getEsMina()) {
                            nm++;
                        }
                    }
                }
            }
        }
        return nm;

    }

    public boolean hiHaMina(int x, int y) {
        return t[x][y].getEsMina();
    }

    public int descobreixTauler() {
        int puntuacio = 0;
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t.length; j++) {
                t[i][j].setDescoberta(true);
                puntuacio++;
            }
        }
        return puntuacio;
    }

    public boolean descobert() {

        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t.length; j++) {
                if (!t[i][j].getEsMina()) {
                    if (!t[i][j].getDescoberta()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void descobreixCasella(int x, int y) {

        if (!t[x][y].getDescoberta()) {

            t[x][y].setDescoberta(true);

            if (!t[x][y].getEsMina() && !t[x][y].getMarcada() && t[x][y].getnMines() == 0) {

                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (i >= 0 && j >= 0 && i < t.length && j < t[i].length) {
                            descobreixCasella(i, j);
                        }
                    }
                }
            }
        }

    }

    public boolean descoberta(int x, int y) {
        return t[x][y].getDescoberta();
    }

    public void marcaMina(int x, int y) {
        if (t[x][y].getMarcada()) {
            t[x][y].setMarcada(false);
        } else {
            t[x][y].setMarcada(true);
        }
    }

    public boolean minaMarcada(int x, int y) {
        return t[x][y].getMarcada();
    }

    @Override
    public String toString() {

        int a = 'a';

        //Primera línia
        System.out.print(" ");
        for (int i = 0; i < t.length; i++) {
            System.out.print((char) a);
            a++;
        }

        //Resta de línies
        a = 'a';
        System.out.println("");
        for (int i = 0; i < t.length; i++) {
            System.out.print((char) a);
            for (int j = 0; j < t.length; j++) {
                System.out.print(t[i][j]);
            }
            System.out.println("");
            a++;
        }
        return "";
    }

}
