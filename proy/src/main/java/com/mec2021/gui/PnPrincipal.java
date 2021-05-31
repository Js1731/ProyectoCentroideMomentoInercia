package com.mec2021.gui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mec2021.plano.objetos.formas.FrCirc;
import com.mec2021.plano.objetos.formas.FrRect;
import com.mec2021.plano.objetos.formas.FrTria;
import com.mec2021.Ctrl;
import com.mec2021.PnPlano;

public class PnPrincipal extends JPanel{

    public static PnPrincipal PanelPrinc;

    public PnPrincipal(){

        PanelPrinc = this;

        setBackground(Color.WHITE);

        SpringLayout Ly = new SpringLayout();
        setLayout(Ly);

        PnPlano PrimerPlano = new PnPlano();
        

        SpringLayout Ly2 = new SpringLayout();
        JPanel PnBarraSup = new JPanel();
        PnBarraSup.setLayout(Ly2);
        PnBarraSup.setBackground(Color.gray);
        PnBarraSup.setPreferredSize(new Dimension(getWidth(), 40));
        PnBarraSup.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        

        BotonGenerico BtRect = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(Ctrl.ClGrisClaro);

                

                if(MouseEncima){
                    g2.drawRect(10, 10, 20, 20);
                    setBackground(Color.darkGray);
                }else{
                    g2.fillRect(10, 10, 20, 20);
                    setBackground(Color.GRAY);
                }
            }
        };

        BotonGenerico BtCirc = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(Ctrl.ClGrisClaro);

                

                if(MouseEncima){
                    g2.drawOval(10, 10, 20, 20);
                    setBackground(Color.darkGray);
                }else{
                    setBackground(Color.GRAY);
                    g2.fillOval(10, 10, 20, 20);
                }
            }
        };

        BotonGenerico BtTria = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(Ctrl.ClGrisClaro);

                int[] xs = {10,20,30};
                int[] ys = {30,10,30};
    
                

                if(MouseEncima){
                    g2.drawPolygon(xs, ys, 3);
                    setBackground(Color.darkGray);
                }else{
                    setBackground(Color.GRAY);
                    g2.fillPolygon(xs, ys, 3);
                }
            }
        };

        BtRect.setPreferredSize(new Dimension(40, 40));
        BtCirc.setPreferredSize(new Dimension(40, 40));
        BtTria.setPreferredSize(new Dimension(40, 40));

        JFormattedTextField JFTScale = new JFormattedTextField(PnPlano.Escala);
        JFTScale.setBounds(0, 0, 40, 25);
        JFTScale.setPreferredSize(new Dimension(40, 25));
        JFTScale.setBorder(BorderFactory.createEmptyBorder());
        JFTScale.setHorizontalAlignment(JTextField.CENTER);
        JFTScale.addKeyListener(new KeyListener(){

            private void actualizarEscala(){
                PnPlano.Escala = (int)JFTScale.getValue();

            }

            @Override
            public void keyTyped(KeyEvent e) {
                actualizarEscala();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                actualizarEscala();
            }

            @Override
            public void keyReleased(KeyEvent e) {
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

        JLabel LbIngresarFr = new JLabel("Agregar Forma");
        LbIngresarFr.setForeground(Color.WHITE);
        LbIngresarFr.setFont(Ctrl.Fnt1);
        
        JLabel LbEscala = new JLabel("Escala");
        LbEscala.setFont(Ctrl.Fnt1);
        LbEscala.setForeground(Color.WHITE);

        JLabel LbTitulo = new JLabel("Titulo");
        LbTitulo.setFont(Ctrl.Fnt2);
        LbTitulo.setForeground(Color.WHITE);
        LbTitulo.setHorizontalAlignment(JLabel.CENTER);

        Ly2.putConstraint(SpringLayout.WEST, LbIngresarFr, 20, SpringLayout.WEST, PnBarraSup);
        Ly2.putConstraint(SpringLayout.NORTH, LbIngresarFr, 0, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, LbIngresarFr, 0, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.WEST, BtRect, 20, SpringLayout.EAST, LbIngresarFr);
        Ly2.putConstraint(SpringLayout.NORTH, BtRect, 0, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, BtRect, 0, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.WEST, BtCirc, 0, SpringLayout.EAST, BtRect);
        Ly2.putConstraint(SpringLayout.NORTH, BtCirc, 0, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, BtCirc, 0, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.WEST, BtTria, 0, SpringLayout.EAST, BtCirc);
        Ly2.putConstraint(SpringLayout.NORTH, BtTria, 0, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, BtTria, 0, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.WEST, LbTitulo, 10, SpringLayout.EAST, BtTria);
        Ly2.putConstraint(SpringLayout.EAST, LbTitulo, -150, SpringLayout.WEST, LbEscala);
        Ly2.putConstraint(SpringLayout.NORTH, LbTitulo, 10, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, LbTitulo, -10, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.EAST, LbEscala, -10, SpringLayout.WEST, JFTScale);
        Ly2.putConstraint(SpringLayout.NORTH, LbEscala, 10, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, LbEscala, -10, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.EAST, JFTScale, -10, SpringLayout.EAST, PnBarraSup);
        Ly2.putConstraint(SpringLayout.NORTH, JFTScale, 10, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, JFTScale, -10, SpringLayout.SOUTH, PnBarraSup);




        PnBarraSup.add(BtRect);
        PnBarraSup.add(BtCirc);
        PnBarraSup.add(BtTria);
        PnBarraSup.add(LbIngresarFr);
        PnBarraSup.add(JFTScale);
        PnBarraSup.add(LbEscala);
        PnBarraSup.add(LbTitulo);

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
