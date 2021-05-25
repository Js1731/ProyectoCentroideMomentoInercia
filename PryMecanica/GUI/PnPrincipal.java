package PryMecanica.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import PryMecanica.PnPlano;
import PryMecanica.Plano.Objetos.Formas.FrCirc;
import PryMecanica.Plano.Objetos.Formas.FrRect;
import PryMecanica.Plano.Objetos.Formas.FrTria;

public class PnPrincipal extends JPanel{

    public static PnPrincipal PanelPrinc;

    public PnPropiedades PnPropiedades = new PnPropiedades();

    public PnPrincipal(){

        PanelPrinc = this;

        SpringLayout Ly = new SpringLayout();
        setLayout(Ly);

        PnPlano PrimerPlano = new PnPlano();
        

        JPanel PnBarraSup = new JPanel();
        PnBarraSup.setLayout(new FlowLayout(FlowLayout.LEFT));
        PnBarraSup.setBackground(Color.gray);
        PnBarraSup.setPreferredSize(new Dimension(getWidth(), 35));

        JButton BtRect = new JButton("Rectangulo");
        JButton BtCirc = new JButton("Circulo");
        JButton BtTria = new JButton("Triangulo");


        BtRect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrimerPlano.add(new FrRect());
            }
        });

        BtCirc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrimerPlano.add(new FrCirc());
            }
        });

        BtTria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrimerPlano.add(new FrTria());
            }
        });

        PnBarraSup.add(BtRect);
        PnBarraSup.add(BtCirc);
        PnBarraSup.add(BtTria);

        Ly.putConstraint(SpringLayout.WEST, PrimerPlano, 0, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.NORTH, PrimerPlano, 0, SpringLayout.SOUTH, PnBarraSup);
        Ly.putConstraint(SpringLayout.SOUTH, PrimerPlano, 0, SpringLayout.SOUTH, this);
        Ly.putConstraint(SpringLayout.EAST, PrimerPlano, 0, SpringLayout.WEST, PnPropiedades);
        
        Ly.putConstraint(SpringLayout.EAST, PnPropiedades, 0, SpringLayout.EAST, this);
        Ly.putConstraint(SpringLayout.NORTH, PnPropiedades, 0, SpringLayout.SOUTH, PnBarraSup);
        Ly.putConstraint(SpringLayout.SOUTH, PnPropiedades, 0, SpringLayout.SOUTH, this);
        
        Ly.putConstraint(SpringLayout.NORTH, PnBarraSup, 0, SpringLayout.NORTH, this);
        Ly.putConstraint(SpringLayout.WEST, PnBarraSup, 0, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.EAST, PnBarraSup, 0, SpringLayout.EAST, this);
        

        add(PnBarraSup);
        add(PrimerPlano);
        add(PnPropiedades);
    }
}
