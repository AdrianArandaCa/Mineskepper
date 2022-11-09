package Principal;

import Controller.CtlGame;
import Controller.CtlViewPrincipal;
import Model.Board;
import java.util.Scanner;
import Util.BDUtil;

/**
 * @author David
 */
public class Minesweeper {

    public static void main(String[] args) {
        try {
            //BDUtil.cleanTables();
            BDUtil.createStructure();
            CtlViewPrincipal ctl = new CtlViewPrincipal();
            ctl.run();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        
    }
}
