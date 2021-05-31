package com.mec2021.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**Boton personalizable, se le puede agregar un {@link ActionListener} como un JButton*/
public abstract class BotonGenerico extends JPanel implements MouseListener {

    /**Indica si el mouse esta encima del boton */
    protected boolean MouseEncima = false;

    /**Indica si el boton esta siendo presionado */
    protected boolean Presionado = false;

    /**Listener para escuchar si se presiona el boton */
    protected ActionListener List;

    public BotonGenerico() {

        setBounds(0, 0, 20, 20);
        setBackground(Color.GRAY);

        addMouseListener(this);
    }

    public void addActionListener(ActionListener List){
        this.List = List;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        Presionado = true;
        
        //NOTIFICAR EVENTO
        if(List != null)
            List.actionPerformed(new ActionEvent(this, 0, "Hola soi nuebo"));

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Presionado = false;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        MouseEncima = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        MouseEncima = false;
        repaint();
    }
}
