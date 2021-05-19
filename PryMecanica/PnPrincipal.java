package PryMecanica;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import PryMecanica.Formas.FrCirc;
import PryMecanica.Formas.FrRect;
import PryMecanica.Formas.FrTria;

public class PnPrincipal extends JLayeredPane{

    public static PnPrincipal PnPrinc;

    public PnPrincipal(){
        setLayout(null);

        PnPrinc = this;

        add(new FrRect(10,10,50,50));
        add(new FrCirc(70,10,50));
        add(new FrTria(120,10));
        
        JButton BtRect = new JButton("Rect");
        JButton BtTrian = new JButton("Triangulo");
        JButton BtCirc = new JButton("Circulo");
        
    }
}
