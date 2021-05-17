package PryMecanica.Formas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import PryMecanica.Main;
import PryMecanica.PnPrincipal;

public class FrCirc extends Forma{
    public Arc2D.Double Sector;

    public int Diametro = 50;
    public Pin[] Pines = new Pin[3];

    public FrCirc(){
        setOpaque(false);
        setBounds(10, 10, Diametro, Diametro);
        Sector = new Arc2D.Double(0,0, getWidth(), getHeight(), 90, 360, Arc2D.PIE);
        
    }

    public FrCirc(int X, int Y){
        setOpaque(false);
        setBounds(X, Y, Diametro, Diametro);
        Sector = new Arc2D.Double(0,0, getWidth(), getHeight(), 90, 360, Arc2D.PIE);
    }

    public void ActCirculo(){
        setBounds(getX(), getY(), Diametro, Diametro);
        Sector.width = getWidth();
        Sector.height = getHeight();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D G2 = (Graphics2D)g;

        G2.fill(Sector);
    }

    @Override
    public void ActualizarPines() {
        int PinX = getX() + getWidth()/2  + Diametro/2;
        int PinY = getY() + getWidth()/2;

        Pines[0].setLocation(PinX, PinY);

        PinX = getX() + getWidth()/2 + Math.round((float)((Diametro/2.5)*Math.cos(Math.toRadians(-Sector.start))));
        PinY = getY() + getHeight()/2 + Math.round((float)((Diametro/2.5)*Math.sin(Math.toRadians(-Sector.start))));

        Pines[1].setLocation(PinX, PinY);

        PinX = getX() + getWidth()/2 + Math.round((float)((Diametro/5)*Math.cos(Math.toRadians(-Sector.start))));
        PinY = getY() + getHeight()/2 + Math.round((float)((Diametro/5)*Math.sin(Math.toRadians(-Sector.start))));

        Pines[2].setLocation(PinX, PinY);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        if(Pines[0] == null){

            int PinX = getX() + getWidth()/2 + Diametro/2;
            int PinY = getY() + getHeight()/2;

            Pines[0] = new Pin(this, PinX, PinY){
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point Pos = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(Pos, PnPrincipal.PnPrinc);
            
                    setBounds(Pos.x - PtOffset.x, Fr.getY() + Diametro/2, getWidth(), getHeight());
                    
                    Diametro = Fr.getWidth() + getX() - (Fr.getX() + Fr.getWidth());
                    ActCirculo();
                    ActualizarPines();
                }
            };

            PinX = getX() + getWidth()/2 + Math.round((float)(Diametro/4*Math.cos(Sector.start)));

            Pines[1] = new Pin(this, PinX, PinY){
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point Pos = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(Pos, PnPrincipal.PnPrinc);
        
                    Point2D Pt = Main.direccion(Fr.getX() + Fr.getWidth()/2, Fr.getY() + Fr.getHeight()/2, Pos.x, Pos.y);

                    setBounds(Math.round(Fr.getX() + Diametro/2 + (float)(Diametro/2*Pt.getX())), Math.round(Fr.getY() + Diametro/2 + (float)(Diametro/2*Pt.getY())), getWidth(), getHeight());
                    
                    float ang = Main.angulo(0f, 0f,(float) Pt.getX(),(float) -Pt.getY());

                    Sector.extent = Sector.extent - 2*(Math.toDegrees(ang) - Sector.start);
                    Sector.start = Math.toDegrees(ang);

                    System.out.println(Math.toDegrees(ang));
                    ActualizarPines();
                    ActCirculo();
                    Fr.repaint();
                }
            };

            Pines[2] = new Pin(this, PinX, PinY){
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point Pos = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(Pos, PnPrincipal.PnPrinc);
        
                    Point2D Pt = Main.direccion(Fr.getX() + Fr.getWidth()/2, Fr.getY() + Fr.getHeight()/2, Pos.x, Pos.y);

                    setBounds(Math.round(Fr.getX() + Diametro/2 + (float)(Diametro/3*Pt.getX())), Math.round(Fr.getY() + Diametro/2 + (float)(Diametro/3*Pt.getY())), getWidth(), getHeight());
                    
                    float ang = Main.angulo(0f, 0f,(float) Pt.getX(),(float) -Pt.getY());

                    Sector.start = Math.toDegrees(ang);

                    ActualizarPines();
                    ActCirculo();
                    Fr.repaint();
                }
            };

            for (Pin pin : Pines) {
                PnPrincipal.PnPrinc.add(pin, JLayeredPane.DRAG_LAYER);
                PnPrincipal.PnPrinc.moveToFront(pin);
            }

            ActualizarPines();


        }
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        ActualizarPines();
    }
}
