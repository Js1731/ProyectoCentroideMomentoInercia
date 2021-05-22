package PryMecanica;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

/**
 * Controlador que permite que un panel se pueda arrastrar
 */
public class Arrastrable extends JPanel implements MouseInputListener{

    /**Distancia entre la esquina superior izquierda del componente al punto en el que el mouse esta arrastrando */
    protected Point PtOffset = new Point(0,0);

    public Arrastrable(){
        setBounds(10, 10, 50, 50);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void mousePressed(MouseEvent e) {
        PtOffset = e.getPoint();
    }
    
    public void mouseDragged(MouseEvent e) {
        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, PnPrincipal.PnPrinc);

        setBounds(Pos.x - PtOffset.x, Pos.y - PtOffset.y, getWidth(), getHeight());
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}
