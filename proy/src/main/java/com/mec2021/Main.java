package com.mec2021;
import javax.swing.JFrame;

import com.mec2021.gui.PnPrincipal;

public class Main {

    public static JFrame VentPrinc;

    public static void main(String[] args){
        JFrame Vent = VentPrinc = new JFrame();    

        Ctrl.importarArchivos();

        Vent.add(new PnPrincipal());

        Vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Vent.setSize(500, 500);
        Vent.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Vent.setVisible(true);
    }
}

