package com.mec2021.gui.agregarforma;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mec2021.Ctrl;
import com.mec2021.gui.BotonGenerico;
import com.mec2021.gui.PnPlano;


import java.awt.Polygon;
import java.awt.BasicStroke;


/**Panel para agregar una forma */
public abstract class PnAgregarForma extends JPanel{



    public JLabel LbNombre = new JLabel("Nombre");
    public BotonGenerico BtAceptar;
    public JTextField TFNombre = new JTextField();

    public PnPlano Plano;

    /**
     * Crea un nuevo panel para agregar una forma. Toma como parametro la clase de la forma que debe crear
     * @param R Clase de la forma
     */
    public PnAgregarForma(PnPlano plano){
        setLayout(null);
        setBounds(10, 10, 250, 200);
        setOpaque(false);

        Plano = plano;

        Plano.PnAgActivo = this;

        JLabel LbNombre = new JLabel("Nombre");
        LbNombre.setFont(Ctrl.Fnt1);
        LbNombre.setBounds(30, 70, 100, 20);

        TFNombre.setBounds(100, 70, 100,20);

        BtAceptar = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        


                if(MouseEncima)
                    g2.setColor(Ctrl.ClGris2);
                else
                    g2.setColor(Ctrl.ClGris);

                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.setFont(Ctrl.Fnt1);
                g2.setColor(Color.WHITE);

                g2.drawString("Aceptar", getWidth()/2 - 30, 20);
            }
        };
        BtAceptar.setBounds(20, getHeight() - 50,getWidth()-35,30);
        BtAceptar.setOpaque(false);

        add(LbNombre);
        add(TFNombre);
        add(BtAceptar);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        
        
        g2.setFont(Ctrl.Fnt0);
        
        g2.setColor(Ctrl.ClGrisClaro3);
        
        Polygon Flecha = new Polygon();
        Flecha.addPoint(10, 20);
        Flecha.addPoint(20, 0);
        Flecha.addPoint(30, 20);
        
        g2.fillPolygon(Flecha);
        
        g2.fillRoundRect(0, 20, getWidth(), getHeight() - 20, 10, 10);
        
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(Ctrl.Fnt1);
    
        
        g2.setStroke(new BasicStroke(1));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);        
        g2.drawLine(20, 55, getWidth()-20, 55);
    }
}
