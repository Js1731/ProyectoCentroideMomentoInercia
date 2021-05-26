package PryMecanica;
import javax.swing.JFrame;

import PryMecanica.GUI.PnPrincipal;

public class Main {

    public static JFrame VentPrinc;


    public static void main(String[] args){
        JFrame Vent = VentPrinc = new JFrame();    

        Vent.add(new PnPrincipal());

        Vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Vent.setSize(500, 500);
        Vent.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Vent.setVisible(true);
    }
}

