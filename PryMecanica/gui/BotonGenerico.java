package PryMecanica.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class BotonGenerico extends JPanel implements MouseListener {

    protected boolean MouseEncima = false;
    protected boolean Presionado = false;
    protected ActionListener List;

    protected int Sec[] = new int[10];

    public BotonGenerico() {
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
