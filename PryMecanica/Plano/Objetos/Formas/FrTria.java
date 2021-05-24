package PryMecanica.Plano.Objetos.Formas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.event.MouseEvent;


import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import PryMecanica.PnPrincipal;
import PryMecanica.Plano.Punto;
import PryMecanica.Plano.Objetos.Pin;

/**
 * Triangulo definido por tres vertices representados por {@link Punto}.
 */
public class FrTria extends Forma{

    //VERTICES
    public Punto Ver1 = new Punto();
    public Punto Ver2 = new Punto();
    public Punto Ver3 = new Punto();

    //POLIGONO PARA REPRESENTAR EL TRIANGULO
    private Polygon Poligono = new Polygon();

    /**
     * Crea un tringulo en el origen de 50x50
     */
    public FrTria(){
        this(0,0,0,50,25,0,50,50);
    }

    /**
     * Crea un tringulo en las coordenadas especificadas
     * @param X X del panel que contiene a la figura
     * @param Y Y del panel que contiene a la figura
     * @param x1 X del Vertice 1 local al panel
     * @param y1 Y del Vertice 1 local al panel
     * @param x2 X del Vertice 2 local al panel
     * @param y2 Y del Vertice 2 local al panel
     * @param x3 X del Vertice 3 local al panel
     * @param y3 Y del Vertice 3 local al panel
     */
    public FrTria(float X, float Y,float x1, float y1, float x2, float y2, float x3, float y3){

        Pines = new Pin[3];

        Ver1.x = Math.round(PnPrincipal.PtOrigen.x) + Math.round(X*Escala) + Math.round(x1*Escala);
        Ver1.y = Math.round(PnPrincipal.PtOrigen.y) + Math.round(Y*Escala) + Math.round(y1*Escala);

        Ver2.x = Math.round(PnPrincipal.PtOrigen.x) + Math.round(X*Escala) + Math.round(x2*Escala);
        Ver2.y = Math.round(PnPrincipal.PtOrigen.y) + Math.round(Y*Escala) + Math.round(y2*Escala);

        Ver3.x = Math.round(PnPrincipal.PtOrigen.x) + Math.round(X*Escala) + Math.round(x3*Escala);
        Ver3.y = Math.round(PnPrincipal.PtOrigen.y) + Math.round(Y*Escala) + Math.round(y3*Escala);

        setOpaque(false);
        ActualizarBordes();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Poligono.reset();

        //VERTICE 1
        Point TmpVer1 = new Point(Math.round(Ver1.x), Math.round(Ver1.y));
        SwingUtilities.convertPoint(PnPrincipal.PnPrinc, TmpVer1, this);
        
        Poligono.addPoint(TmpVer1.x - getX(), TmpVer1.y - getY());
        
        //VERTICE 2
        Point TmpVer2 = new Point(Math.round(Ver2.x), Math.round(Ver2.y));
        SwingUtilities.convertPoint(PnPrincipal.PnPrinc, TmpVer2, this);
        
        Poligono.addPoint(TmpVer2.x - getX(), TmpVer2.y - getY());
        
        //VERTICE 3
        Point TmpVer3 = new Point(Math.round(Ver3.x), Math.round(Ver3.y));
        SwingUtilities.convertPoint(PnPrincipal.PnPrinc, TmpVer3, this);
        
        Poligono.addPoint(TmpVer3.x - getX(), TmpVer3.y - getY());

        g.fillPolygon(Poligono);
        
        g.setColor(Color.WHITE);

        g.drawString("Area " + calcularArea(), 50, 50);

        int x = Math.round(centroideX());
        int y = Math.round(centroideY());

        g.setColor(Color.RED);

        g.fillOval(x-3,y-3,6,6);
    }



    @Override
    public void ActualizarPines() {
        Pines[0].setLocation(Math.round(Ver1.x), Math.round(Ver1.y));
        Pines[1].setLocation(Math.round(Ver2.x), Math.round(Ver2.y));
        Pines[2].setLocation(Math.round(Ver3.x), Math.round(Ver3.y));
    }

    /**Acualiza las dimensiones del panel que contiene la forma */
    public void ActualizarBordes(){

        int XMen = Math.min(Math.round(Ver1.x), Math.min(Math.round(Ver2.x), Math.round(Ver3.x)));
        int YMen = Math.min(Math.round(Ver1.y), Math.min(Math.round(Ver2.y), Math.round(Ver3.y)));

        int XMax = Math.max(Math.round(Ver1.x), Math.max(Math.round(Ver2.x), Math.round(Ver3.x)));
        int YMax = Math.max(Math.round(Ver1.y), Math.max(Math.round(Ver2.y), Math.round(Ver3.y)));

        setBounds(XMen, YMen, XMax - XMen, YMax - YMen);
        
        repaint();
    }

    @Override
    public void ActualizarCoordenadas() {
        X = Math.round(getX() - PnPrincipal.PtOrigen.x);
        Y = Math.round(getY() - PnPrincipal.PtOrigen.y);
    }

    @Override
    public float calcularArea() {
        float LadoA = Punto.distanciaEntre(Ver1.x, Ver1.y, Ver2.x, Ver2.y);
        float LadoB = Punto.distanciaEntre(Ver3.x, Ver3.y, Ver2.x, Ver2.y);
        float LadoC = Punto.distanciaEntre(Ver1.x, Ver1.y, Ver3.x, Ver3.y);

        float s = (LadoA + LadoB + LadoC)/2;

        return (float)Math.round(Math.sqrt(s * (s - LadoA) * (s - LadoB) * (s - LadoC)));
    }

    @Override
    public float centroideX() {
        return (Ver1.x + Ver2.x + Ver3.x)/3 - getX();
    }

    @Override
    public float centroideY() {
        return (Ver1.y + Ver2.y + Ver3.y)/3 - getY();
    }



    
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        if(Pines[0] == null){

            //VER1
            Pines[0] = new Pin(this, getX(), getY() + getHeight()){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    setLocation(snap(getX(), SnapXs), snap(getY(), SnapYs));

                    Ver1.x = getX();
                    Ver1.y = getY();

                    ActualizarBordes();
                }
            };

            //VER2
            Pines[1] = new Pin(this, getX() + 25, getY()){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    setLocation(snap(getX(), SnapXs), snap(getY(), SnapYs));

                    Ver2.x = getX();
                    Ver2.y = getY();

                    ActualizarBordes();
                }
            };

            //VER3
            Pines[2] = new Pin(this, getX() + getWidth(), getY() + getHeight()){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    setLocation(snap(getX(), SnapXs), snap(getY(), SnapYs));

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

            ActualizarPines();

            PnPrincipal.PnPrinc.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, PnPrincipal.PnPrinc);

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        int DifX = Pos.x - (PtOffset.x + getX());
        int DifY = Pos.y - (PtOffset.y + getY());

        //ACTUALIZAR VERTICES
        Ver1.x += DifX;
        Ver1.y += DifY;

        Ver2.x += DifX;
        Ver2.y += DifY;

        Ver3.x += DifX;
        Ver3.y += DifY;

        setBounds(Pos.x - PtOffset.x, Pos.y - PtOffset.y, getWidth(), getHeight());
        ActualizarPines();
        
        if(Grp != null)
            Grp.ActualizarBordes();
        
        Point PtPrev = getLocation();

        setLocation(snap(getX(), SnapXs), snap(getY(), SnapYs));
        setLocation(snap(getX() + getWidth(), SnapXs) - getWidth(),
                    snap(getY() + getHeight(), SnapYs) - getHeight());

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        DifX = getX() - PtPrev.x;
        DifY = getY() - PtPrev.y;

        //ACTUALIZAR VERTICES
        Ver1.x += DifX;
        Ver1.y += DifY;

        Ver2.x += DifX;
        Ver2.y += DifY;

        Ver3.x += DifX;
        Ver3.y += DifY;

        ActualizarPines();
        
    }
}
