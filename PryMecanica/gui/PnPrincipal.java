package PryMecanica.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import PryMecanica.PnPlano;
import PryMecanica.plano.objetos.formas.FrCirc;
import PryMecanica.plano.objetos.formas.FrRect;
import PryMecanica.plano.objetos.formas.FrTria;

public class PnPrincipal extends JPanel{

    public static PnPrincipal PanelPrinc;

    public PnPrincipal(){

        PanelPrinc = this;

        setBackground(Color.WHITE);

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

        JFormattedTextField JFTScale = new JFormattedTextField(10);
        JFTScale.setBounds(0, 0, 40, 25);
        JFTScale.setPreferredSize(new Dimension(40, 25));
        JFTScale.getDocument().addDocumentListener(new DocumentListener(){

            private void actualizarEscala(){
                PnPlano.Escala = (int)JFTScale.getValue();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarEscala();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarEscala();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarEscala();
            }
        });

        BtRect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrimerPlano.add(new FrRect(), JLayeredPane.DRAG_LAYER);
            }
        });

        BtCirc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrimerPlano.add(new FrCirc(), JLayeredPane.DRAG_LAYER);
            }
        });

        BtTria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrimerPlano.add(new FrTria(), JLayeredPane.DRAG_LAYER);
            }
        });

        PnBarraSup.add(BtRect);
        PnBarraSup.add(BtCirc);
        PnBarraSup.add(BtTria);
        PnBarraSup.add(JFTScale);

        Ly.putConstraint(SpringLayout.WEST, PrimerPlano, 0, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.NORTH, PrimerPlano, 0, SpringLayout.SOUTH, PnBarraSup);
        Ly.putConstraint(SpringLayout.SOUTH, PrimerPlano, 0, SpringLayout.SOUTH, this);
        Ly.putConstraint(SpringLayout.EAST, PrimerPlano, 0, SpringLayout.EAST, this);
        
        
        Ly.putConstraint(SpringLayout.NORTH, PnBarraSup, 0, SpringLayout.NORTH, this);
        Ly.putConstraint(SpringLayout.WEST, PnBarraSup, 0, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.EAST, PnBarraSup, 0, SpringLayout.EAST, this);
        

        add(PnBarraSup);
        add(PrimerPlano);
    }
}
