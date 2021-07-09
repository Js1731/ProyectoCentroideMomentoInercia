package com.mec2021.plano;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import com.mec2021.Ctrl;
import com.mec2021.gui.PnPlano;
import com.mec2021.gui.PnPrincipal;


/**
 * PnCentroide
 */
public class PnCentroide extends JPanel{
    public PnPlano Plano;

    public float CX = 0;
    public float CY = 0;
    public float IY = 0;
    public float IX = 0;

    public Color Fondo = new Color(0,0,0,100);

    public PnCentroide(PnPlano plano){
        Plano = plano;

        setOpaque(false);
        setBounds(10, 10, 100, 100);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.setFont(Ctrl.Fnt1);

        int PosX = 5;
        int PosY = 15;

        
        g2.setColor(Color.BLACK);

        if(Plano.LstObjetos.size() > 0)
            g2.fillOval(getWidth()/2 - 3, getHeight() - 6, 6, 6);
        
        if(!PnPrincipal.VerCentroide)
            return;
            
        g2.setColor(Fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight() - 8, 10, 10);

        g2.setColor(Color.WHITE);

        DecimalFormat f = new DecimalFormat("#0.00");

        g2.drawString("Centroide", PosX, PosY);
        g2.drawString("x " + f.format(CX), PosX, PosY + 20);
        g2.drawString("y " + f.format(-CY), PosX, PosY + 35);

        g2.drawString("Inercia", PosX, PosY + 55);
        g2.drawString("x " + f.format(IX), PosX, PosY + 70);
        g2.drawString("y " + f.format(IY), PosX, PosY + 85);


    }
}