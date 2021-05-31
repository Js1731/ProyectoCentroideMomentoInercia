package com.mec2021.plano.objetos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.mec2021.plano.objetos.formas.Forma;
import com.mec2021.Arrastrable;
import com.mec2021.PnPlano;

/**Componete Arrastrable para deformar una forma*/
public class Pin extends Arrastrable {
    public Forma Fr;

    public Pin(Forma fr, int x, int y){
        Fr = fr;
        setBackground(Color.BLUE);
        setBounds(x,y,10,10);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillOval(0,0,10,10);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        PnPlano.PlPrinc.notificarCambios(1);

        if(Fr.Grp != null)
            Fr.Grp.ActualizarBordes();
    }
}
