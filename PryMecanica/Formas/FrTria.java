package PryMecanica.Formas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import PryMecanica.PnPrincipal;

/**
 * Triangulo definido por tres vertices representados por {@link Punto}. Siempre esta dividido en 2 trianglos rectangulo para facilitar calculos
 */
public class FrTria extends Forma{

    //VERTICES
    public Punto Ver1 = new Punto();
    public Punto Ver2 = new Punto();
    public Punto Ver3 = new Punto();

    //POLIGONO PARA REPRESENTAR EL TRIANGULO
    private Polygon Poligono = new Polygon();

    /** 
    * Arreglo de ecuaciones para representar 2 tipos de rectas:
    * <p><b>Lados del Triangulo</b>
    * <PRE>
    *    <b   >Index           </b   >   <b>Ecuacion de la recta   </b>
    *    <code>0</code>:  <code>Recta entre Ver1 y Ver2</code>
    *    <code>1</code>:  <code>Recta entre Ver1 y Ver3</code>
    *    <code>2</code>:  <code>Recta entre Ver2 y Ver3</code>
    * </PRE>
    * <p><b>Rectas perependiculares a los lados</b>
    * <PRE>
    *    <b   >Index           </b   >   <b>Ecuacion de la recta   </b>
    *    <code>3</code>:  <code>perpendicular a la ecuacion {@code 0} que pasa por Ver3</code>
    *    <code>4</code>:  <code>perpendicular a la ecuacion {@code 1} que pasa por Ver2</code>
    *    <code>5</code>:  <code>perpendicular a la ecuacion {@code 2} que pasa por Ver1</code>
    * </PRE>
    */
    public EcuacionRecta[] Ecuaciones = new EcuacionRecta[6];

    /**
     * Crea un tringulo en el origen de 50x50
     */
    public FrTria(){
        this(0,0);
    }

    /**
     * Crea un tringulo en las coordenadas especificadas
     */
    public FrTria(int X, int Y){

        Pines = new Pin[3];

        Ver1.x = X;
        Ver1.y = Y + 50;

        Ver2.x = X + 25;
        Ver2.y = Y;

        Ver3.x = X + 50;
        Ver3.y = Y + 50;

        setOpaque(false);
        IniciarEcuaciones();
        ActualizarBordes();
    }

    /**
     * Iniciar ecuaciones del triangulo
     */
    public void IniciarEcuaciones(){
        //RECTA DE VER1 A VER2
        Ecuaciones[0] = new EcuacionRecta(Ver1, Ver2);
        
        //RECTA DE VER1 A VER3
        Ecuaciones[1] = new EcuacionRecta(Ver1, Ver3);

        //RECTA DE VER2 A VER3
        Ecuaciones[2] = new EcuacionRecta(Ver2, Ver3);

        //RECTA PERPENDICULAR A ECUACION 0 QUE PASA POR VER3
        Ecuaciones[3] = new EcuacionRecta(Ecuaciones[0], Ver3);

        //RECTA PERPENDICULAR A ECUACION 1 QUE PASA POR VER2
        Ecuaciones[4] = new EcuacionRecta(Ecuaciones[1], Ver2);

        //RECTA PERPENDICULAR A ECUACION 2 QUE PASA POR VER1
        Ecuaciones[5] = new EcuacionRecta(Ecuaciones[2], Ver1);
    }

    @Override
    public void ActualizarPines() {}

    public void ActualizarBordes(){

        int XMen = Math.min(Math.round(Ver1.x), Math.min(Math.round(Ver2.x), Math.round(Ver3.x)));
        int YMen = Math.min(Math.round(Ver1.y), Math.min(Math.round(Ver2.y), Math.round(Ver3.y)));

        int XMax = Math.max(Math.round(Ver1.x), Math.max(Math.round(Ver2.x), Math.round(Ver3.x)));
        int YMax = Math.max(Math.round(Ver1.y), Math.max(Math.round(Ver2.y), Math.round(Ver3.y)));

        setBounds(XMen, YMen, XMax - XMen, YMax - YMen);
        
        for (EcuacionRecta ecuacionRecta : Ecuaciones) 
            ecuacionRecta.Actualizar();
        
        repaint();


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

        for (EcuacionRecta ecuacionRecta : Ecuaciones) 
            ecuacionRecta.Actualizar();
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Poligono.reset();

        //VERTICE 1
        Point PtTemp = new Point(Math.round(Ver1.x), Math.round(Ver1.y));
        SwingUtilities.convertPoint(PnPrincipal.PnPrinc, PtTemp, this);
        
        Poligono.addPoint(PtTemp.x - getX(), PtTemp.y - getY());
        
        //VERTICE 2
        PtTemp = new Point(Math.round(Ver2.x), Math.round(Ver2.y));
        SwingUtilities.convertPoint(PnPrincipal.PnPrinc, PtTemp, this);
        
        Poligono.addPoint(PtTemp.x - getX(), PtTemp.y - getY());
        
        //VERTICE 3
        PtTemp = new Point(Math.round(Ver3.x), Math.round(Ver3.y));
        SwingUtilities.convertPoint(PnPrincipal.PnPrinc, PtTemp, this);
        
        Poligono.addPoint(PtTemp.x - getX(), PtTemp.y - getY());

        g.fillPolygon(Poligono);
        
        Point PtTemp2;
        
        g.setColor(Color.WHITE);

        for(int i = 3; i < 6;i++){
            //VERTICE 3
            PtTemp = new Point(Math.round(Ecuaciones[i].A.x), Math.round(Ecuaciones[i].A.y));
            PtTemp2 = new Point(Math.round(Ecuaciones[i].B.x),Math.round(Ecuaciones[i].B.y));

            SwingUtilities.convertPoint(PnPrincipal.PnPrinc, PtTemp, this);
            SwingUtilities.convertPoint(PnPrincipal.PnPrinc, PtTemp2, this);

            g.drawLine(PtTemp.x - getX(), PtTemp.y - getY(), PtTemp2.x - getX(), PtTemp2.y - getY());
        }
        

    }

    /**
     * Representacion de la ecuacion de la recta.
     */
    class EcuacionRecta{
        /**Punto Inicial */
        public Punto A = new Punto();
        
        /**Punto Final */
        public Punto B = new Punto();
    
        /**Pendiente de la recta */
        public float m;

        /**Interseccion con y */
        public float b;

        /**Ecuacion perpendicular */
        public EcuacionRecta EcuacionPerp;
    
        /**Define una nueva ecuacion de la recta que es perpendicular a la recta {@code EcOr} y que pasa por {@code a} 
         * @param EcOr Ecuacion de la recta
         * @param a Punto que pasa por la recta
        */
        public EcuacionRecta(EcuacionRecta EcOr, Punto a){
            A = a;
            EcuacionPerp = EcOr;

            Actualizar();
        }

        /**
         * Define una nueva ecuacion de la recta que pasa por {@code a} y {@code b}
         * @param a
         * @param bp
         */
        public EcuacionRecta(Punto a, Punto bp){
            A = a;
            B = bp;
    
            Actualizar();
        }

        public boolean VerPuntoEnRecta(float x, float y){
            return y == m*x + b;
        }

        /**
         * Actualiza los valores de le ecuaciones
         */
        public void Actualizar(){
            if(EcuacionPerp == null){
                m = (float)(B.y - A.y)/(B.x - A.x);
                b = A.y - m*A.x;
            }else{
                m = -(1/EcuacionPerp.m);
                b = A.y - m*A.x;
    
                B.x = Math.round((EcuacionPerp.b - b)/(m - EcuacionPerp.m));
                B.y = Math.round( m*B.x + b);
            }
        }
    }
}
