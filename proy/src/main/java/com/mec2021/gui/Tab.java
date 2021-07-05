package com.mec2021.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import java.awt.RenderingHints;

import javax.swing.JLabel;

import com.mec2021.Ctrl;

public class Tab extends BotonGenerico{

    String Nombre;
    PnPlano Plano;

    public Tab(String Nombre, PnPlano plano){
        setLayout(null);

        this.Nombre = Nombre;
        Plano = plano;

        setBackground(Ctrl.ClGrisClaro2);
        JLabel LbNombre = new JLabel(Nombre);
        LbNombre.setForeground(Color.GRAY);
        LbNombre.setFont(Ctrl.Fnt1);
        LbNombre.setBounds(5, 0, 100, 30);

        Tab tb = this;

        BotonGenerico BtCerrar = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if(MouseEncima){
                    g.setColor(Ctrl.ClGrisClaro);
                    g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
                }

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                g2.setColor(Ctrl.ClGris);
                g2.drawLine(5, 5, 13, 13);
                g2.drawLine(13, 5, 5, 13);

            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                PnPrincipal.PanelPrinc.PnAreaTrabajo.remove(Plano);
                PnPrincipal.PanelPrinc.BarraTabs.remove(tb);

                PnPrincipal.PanelPrinc.PnAreaTrabajo.repaint();
                PnPrincipal.PanelPrinc.BarraTabs.revalidate();
                PnPrincipal.PanelPrinc.BarraTabs.repaint();

                if(PnPrincipal.TabSel == tb && PnPrincipal.PanelPrinc.BarraTabs.getComponentCount() > 1){
                    Tab PrimTab = (Tab)PnPrincipal.PanelPrinc.BarraTabs.getComponent(0);
                    PnPrincipal.PanelPrinc.PlanoActual = PrimTab.Plano;
                    PnPrincipal.TabSel = PrimTab;
                    PnPrincipal.PanelPrinc.Expositor.show(PnPrincipal.PanelPrinc.PnAreaTrabajo, PrimTab.Nombre);
                }
            }
        };

        BtCerrar.setBounds(120, 5, 20, 20);
        BtCerrar.setOpaque(false);


        add(LbNombre, BorderLayout.WEST);
        add(BtCerrar, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GRAY);

        if(PnPrincipal.TabSel == this){
            setBackground(Ctrl.ClGrisClaro2);
        }else{
            
            g.drawLine(getWidth() - 1, 0, getWidth() - 1 , getHeight());

            if(MouseEncima){
                setBackground(Ctrl.ClGrisClaro);
            }else{
                setBackground(Ctrl.ClGris2);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        PnPrincipal.TabSel = this;
        PnPrincipal.PanelPrinc.BarraTabs.repaint();

        PnPrincipal.PanelPrinc.Expositor.show(PnPrincipal.PanelPrinc.PnAreaTrabajo, Nombre);
        PnPrincipal.PanelPrinc.PlanoActual = Plano;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(150,30);
    }
}
