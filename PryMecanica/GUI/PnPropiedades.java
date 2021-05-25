package PryMecanica.GUI;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.CardLayout;

import javax.swing.JPanel;

import PryMecanica.Plano.Objetos.Formas.Forma;
import PryMecanica.Plano.Objetos.Formas.FrRect;

public class PnPropiedades extends JPanel{

    public CardLayout Expositor = new CardLayout();
    public PropRect PR = new PropRect();

    public PnPropiedades(){
        setLayout(Expositor);
        setBackground(Color.DARK_GRAY);

        setPreferredSize(new Dimension(300, getHeight()));

        add("ola",PR);
    }

    public void mostrarPropiedades(Forma Fr){
        if(Fr instanceof FrRect){
            Expositor.show(this, "ola");
            PR.cambiarRectangulo((FrRect)Fr);
        }
    }
}
