package com.mec2021.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;


/**Representa una Opcion para el {@link ListaOpciones}*/
public class Opcion extends JPanel implements MouseListener{

    JLabel LbTexto = new JLabel();

    public Opcion(String Txt){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(LbTexto);

        LbTexto.setText(Txt);

        addMouseListener(this);

        setBackground(Color.lightGray);
        setMaximumSize(getPreferredSize());
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 30);
    }
    
    public void mouseEntered(MouseEvent e) {
        setBackground(Color.gray);
        repaint();
    }
    
    public void mouseExited(MouseEvent e) {
        setBackground(Color.lightGray);
        repaint();
    }
    
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
}
