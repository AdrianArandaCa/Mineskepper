/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author adrian
 */
public class Player implements java.io.Serializable{
    private String name;
    private int punctuation;
    private int dificultat;
    
    public Player(){};

    public Player(String nom) {
        this.name = nom;
    }
    
    public Player(String nom, int puntuacio) {
        this.name = nom;
        this.punctuation = puntuacio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPunctuation() {
        return punctuation;
    }

    public void setPunctuation(int punctuation) {
        this.punctuation = punctuation;
    }

    public int getDificultat() {
        return dificultat;
    }

    public void setDificultat(int dificultat) {
            this.dificultat = dificultat;
        
    }   
}
