package PryMecanica;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import PryMecanica.Plano.Objetos.Objeto2D;
import PryMecanica.Plano.Objetos.Formas.FrTria;

/** (0, 0) de un plano de coordenadas, todas las Formas son relativas a este objeto */
public class Origen extends Arrastrable{
    public Origen(){
        setBackground(Color.RED);
        setBounds(Math.round(PnPrincipal.PtOrigen.x), Math.round(PnPrincipal.PtOrigen.y), 10, 10);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        PnPrincipal.PnPrinc.SelForma(null);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, PnPrincipal.PnPrinc);

        int DifX = Pos.x - PtOffset.x - getX();
        int DifY = Pos.y - PtOffset.y - getY();

        setBounds(Pos.x - PtOffset.x, Pos.y - PtOffset.y, getWidth(), getHeight());

        PnPrincipal.PtOrigen.x = Pos.x - PtOffset.x;
        PnPrincipal.PtOrigen.y = Pos.y - PtOffset.y;

        for (Objeto2D obj : PnPrincipal.PnPrinc.LstObjetos) {
            obj.setBounds(obj.getX() + DifX, obj.getY() + DifY, obj.getWidth(), obj.getHeight());
            if(obj instanceof FrTria){
                //ACTUALIZAR VERTICES
                ((FrTria)obj).Ver1.x += DifX;
                ((FrTria)obj).Ver1.y += DifY;

                ((FrTria)obj).Ver2.x += DifX;
                ((FrTria)obj).Ver2.y += DifY;

                ((FrTria)obj).Ver3.x += DifX;
                ((FrTria)obj).Ver3.y += DifY;
            }
        }

        PnPrincipal.PnPrinc.repaint();
    }
}
