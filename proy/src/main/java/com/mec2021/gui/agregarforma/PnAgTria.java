package com.mec2021.gui.agregarforma;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.event.ActionListener;

import javax.swing.JLayeredPane;

import java.awt.RenderingHints;
import java.awt.event.ActionEvent;

import com.mec2021.Ctrl;
import com.mec2021.gui.PnPlano;
import com.mec2021.plano.objetos.formas.FrTria;

/**
 * PnAgTria
 */
public class PnAgTria extends PnAgregarForma{

    public PnAgTria(PnPlano plano) {
        super(plano);

        TFNombre.setText("Triangulo " + FrTria.ID);

        setBounds(340, getY(), getWidth(), 160);

        PnAgTria PG = this;

        BtAceptar.setBounds(20, getHeight() - 50,getWidth()-35,30);
        BtAceptar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //CREAR NUEVO TRIANGULO
                FrTria NuevoRect = new FrTria(PosIni.x - 2.5f*plano.Escala,PosIni.y - 2.5f*plano.Escala,0,5.0f*plano.Escala,2.5f*plano.Escala,0,5.0f*plano.Escala,5.0f*plano.Escala, false, plano);
                
                if(TFNombre.getText() != "")
                    NuevoRect.Nombre = TFNombre.getText();

                Plano.add(NuevoRect, JLayeredPane.DRAG_LAYER);
                Plano.moveToFront(NuevoRect);

                Plano.remove(PG);
                Plano.PnAgActivo = null;
                Plano.repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        
        g2.setFont(Ctrl.Fnt1);
        g2.drawString("Agregar Triangulo", 20, 45);
    }

}