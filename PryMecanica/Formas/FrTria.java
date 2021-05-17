package PryMecanica.Formas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import PryMecanica.PnPrincipal;

public class FrTria extends Forma{

    public Point Ver1 = new Point(0,0);
    public Point Ver2 = new Point(0,0);
    public Point Ver3 = new Point(0,0);

    private Polygon Poligono = new Polygon();

    public Pin[] Pines = new Pin[3];

    public FrTria(){

        Ver1.x = 10;
        Ver1.y = 10 + 50;

        Ver2.x = 10 + 25;
        Ver2.y = 10;

        Ver3.x = 10 + 50;
        Ver3.y = 10 + 50;

        setOpaque(false);

        ActualizarBordes();
    }

    
    public FrTria(int X, int Y){

        Ver1.x = X;
        Ver1.y = Y + 50;

        Ver2.x = X + 25;
        Ver2.y = Y;

        Ver3.x = X + 50;
        Ver3.y = Y + 50;

        setOpaque(false);

        ActualizarBordes();
    }

    @Override
    public void ActualizarPines() {}

    public void ActualizarBordes(){

        int XMen = Math.min(Ver1.x, Math.min(Ver2.x, Ver3.x));
        int YMen = Math.min(Ver1.y, Math.min(Ver2.y, Ver3.y));

        int XMax = Math.max(Ver1.x, Math.max(Ver2.x, Ver3.x));
        int YMax = Math.max(Ver1.y, Math.max(Ver2.y, Ver3.y));

        setBounds(XMen, YMen, XMax - XMen, YMax - YMen);
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        if(Pines[0] == null){

            //REDIM VER1
            Pines[0] = new Pin(this, getX(), getY() + getHeight()){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    Ver1.x = getX();
                    Ver1.y = getY();

                    ActualizarBordes();
                }
            };

            //REDIM VER2
            Pines[1] = new Pin(this, getX() + 25, getY()){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    Ver2.x = getX();
                    Ver2.y = getY();

                    ActualizarBordes();
                }
            };

            //REDIM VER3
            Pines[2] = new Pin(this, getX() + getWidth(), getY() + getHeight()){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    Ver3.x = getX();
                    Ver3.y = getY();

                    ActualizarBordes();
                }
            };

            //AGREGAR PINES AL PANEL PRINCIPAL
            for (Pin pin : Pines) {
                PnPrincipal.PnPrinc.add(pin, JLayeredPane.DRAG_LAYER);
                PnPrincipal.PnPrinc.moveToFront(pin);
            }

            PnPrincipal.PnPrinc.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, PnPrincipal.PnPrinc);

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        int DifX = Pos.x - PtOffset.x - getX();
        int DifY = Pos.y - PtOffset.y - getY();

        //ACTUALIZAR PINES
        Pines[0].setLocation(Pines[0].getX() + DifX, Pines[0].getY() + DifY);
        Pines[1].setLocation(Pines[1].getX() + DifX, Pines[1].getY() + DifY);
        Pines[2].setLocation(Pines[2].getX() + DifX, Pines[2].getY() + DifY);

        //ACTUALIZAR VERTICES
        Ver1.x += DifX;
        Ver1.y += DifY;

        Ver2.x += DifX;
        Ver2.y += DifY;

        Ver3.x += DifX;
        Ver3.y += DifY;

        setBounds(Pos.x - PtOffset.x, Pos.y - PtOffset.y, getWidth(), getHeight());
        ActualizarPines();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Poligono.reset();

        //VERTICE 1
        Point PtTemp = new Point(Ver1.x, Ver1.y);
        SwingUtilities.convertPoint(PnPrincipal.PnPrinc, PtTemp, this);
        
        Poligono.addPoint(PtTemp.x - getX(), PtTemp.y - getY());
        
        //VERTICE 2
        PtTemp = new Point(Ver2.x, Ver2.y);
        SwingUtilities.convertPoint(PnPrincipal.PnPrinc, PtTemp, this);
        
        Poligono.addPoint(PtTemp.x - getX(), PtTemp.y - getY());
        
        //VERTICE 3
        PtTemp = new Point(Ver3.x, Ver3.y);
        SwingUtilities.convertPoint(PnPrincipal.PnPrinc, PtTemp, this);
        
        Poligono.addPoint(PtTemp.x - getX(), PtTemp.y - getY());

        g.fillPolygon(Poligono);
    }


}
