package com.mec2021.plano.objetos.formas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.event.MouseEvent;


import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import com.mec2021.Ctrl;
import com.mec2021.gui.PnPlano;
import com.mec2021.plano.EcuacionRecta;
import com.mec2021.plano.Punto;
import com.mec2021.plano.objetos.Pin;

/**
 * Triangulo definido por tres vertices representados por {@link Punto}.
 */
public class FrTria extends Forma{

    //VERTICES
    /**Vertice 1 que es local al origen */
    public Punto Ver1 = new Punto();
    /**Vertice 2 que es local al origen */
    public Punto Ver2 = new Punto();
    /**Vertice 3 que es local al origen */
    public Punto Ver3 = new Punto();

    /**Centroide del triangulo */
    public Punto Cent = new Punto();

    /**Vertice 1 que es local al centroide */
    public Punto Ver1C = new Punto();
    /**Vertice 2 que es local al centroide */
    public Punto Ver2C = new Punto();
    /**Vertice 3 que es local al centroide */
    public Punto Ver3C = new Punto();


    /**Conjunto de rectas locales al centroide 
    * <PRE>
    *      <b>id </b   >      <b>Ecuacion</b>
    *<code>0</code>:  <code>Recta que pasa por Ver1C a Ver2C</code>
    *<code>1</code>:  <code>Recta que pasa por Ver2C a Ver3C</code>
    *<code>2</code>:  <code>Recta que pasa por Ver3C a Ver1C</code>
    * </PRE>
    */
    public EcuacionRecta Ec[] = new EcuacionRecta[3];

    //POLIGONO PARA REPRESENTAR EL TRIANGULO
    private Polygon Poligono = new Polygon();

    /**ID unico para cada triangulo */
    public static int ID = 1;

    /**
     * Crea un tringulo en el origen de 50x50
     */
    public FrTria(PnPlano plano,  boolean hueco){
        this(-2.5f*plano.Escala,-2.5f*plano.Escala,0,5.0f*plano.Escala,2.5f*plano.Escala,0,5.0f*plano.Escala,5.0f*plano.Escala, hueco, plano);
    }

    /**
     * Crea un tringulo en las coordenadas especificadas
     * @param X X del panel que contiene a la figura
     * @param Y Y del panel que contiene a la figura
     * @param y1 Y del Vertice 1 local a la figura
     * @param x2 X del Vertice 2 local a la figura
     * @param y2 Y del Vertice 2 local a la figura
     * @param x1 X del Vertice 1 local a la figura
     * @param x3 X del Vertice 3 local a la figura
     * @param y3 Y del Vertice 3 local a la figura
     */
    public FrTria(float X, float Y,float x1, float y1, float x2, float y2, float x3, float y3,  boolean hueco, PnPlano plano){
        super(plano, hueco);
        Pines = new Pin[3];

        Nombre = "Triangulo " + (ID++);
        
        Ver1.x = X + x1;
        Ver1.y = Y + y1;
        
        Ver2.x = X + x2;
        Ver2.y = Y + y2;
        
        Ver3.x = X + x3;
        Ver3.y = Y + y3;
        
        
        //DEFINIR ECUACIONES DE LA RECTA LOCALES AL CENTROIDE
        Ec[0] = new EcuacionRecta(Ver1C, Ver2C, Plano);
        Ec[1] = new EcuacionRecta(Ver2C, Ver3C, Plano);
        Ec[2] = new EcuacionRecta(Ver3C, Ver1C, Plano);
        
        
        setOpaque(false);
        ActualizarBordes();
        actualizarCoordenadas();

        Plano.notificarCambios(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Poligono.reset();

        //VERTICE 1
        Point TmpVer1 = new Point(Math.round(Ctrl.aplicarEscalaUPix(Ver1.x)), Math.round(Ctrl.aplicarEscalaUPix(Ver1.y)));
        TmpVer1.x -= getX() - Plano.PtOrigen.x;         
        TmpVer1.y -= getY() - Plano.PtOrigen.y;         

        Poligono.addPoint(TmpVer1.x,TmpVer1.y);
        
        //VERTICE 2
        Point TmpVer2 = new Point(Math.round(Ctrl.aplicarEscalaUPix(Ver2.x)), Math.round(Ctrl.aplicarEscalaUPix(Ver2.y)));
        TmpVer2.x -= getX() - Plano.PtOrigen.x;         
        TmpVer2.y -= getY() - Plano.PtOrigen.y;  
        
        Poligono.addPoint(TmpVer2.x,TmpVer2.y);
        
        //VERTICE 3
        Point TmpVer3 = new Point(Math.round(Ctrl.aplicarEscalaUPix(Ver3.x)), Math.round(Ctrl.aplicarEscalaUPix(Ver3.y)));
        TmpVer3.x -= getX() - Plano.PtOrigen.x;         
        TmpVer3.y -= getY() - Plano.PtOrigen.y;  
        
        Poligono.addPoint(TmpVer3.x, TmpVer3.y);

        g.setColor(ColForma);

        if(Hueco){
            g.setColor(Color.WHITE);
            g.fillPolygon(Poligono);
            g.setColor(Color.DARK_GRAY);
            g.drawPolygon(Poligono);
        }else
            g.fillPolygon(Poligono);
        
        g.setColor(Color.WHITE);

        int x = Math.round(Ctrl.aplicarEscalaUPix(centroideX()));
        int y = Math.round(Ctrl.aplicarEscalaUPix(centroideY()));

        g.setColor(Color.RED);

        g.fillOval(x-3,y-3,6,6);
    }


    /**Actualiza las ecuaciones de la recta de la aristas del triangulo locales al centroide */
    public void actualizarEcuaciones(){
        for (EcuacionRecta ec : Ec) {
            ec.actualizarDatos();
        }
    }


    @Override
    public void actualizarDimensiones(){

        int XMen = Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(Math.min(Ver1.x, Math.min(Ver2.x, Ver3.x))));
        int YMen = Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(Math.min(Ver1.y, Math.min(Ver2.y, Ver3.y))));

        int XMax = Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(Math.max(Ver1.x, Math.max(Ver2.x, Ver3.x))));
        int YMax = Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(Math.max(Ver1.y, Math.max(Ver2.y, Ver3.y))));

        setBounds(XMen, YMen, XMax - XMen, YMax - YMen);

        actualizarPines();
        actualizarEcuaciones();
    }


    @Override
    public void actualizarCoordenadas() {
        X = Ctrl.aplicarEscalaLnPixU(getX() - Plano.PtOrigen.x);
        Y = Ctrl.aplicarEscalaLnPixU(getY() - Plano.PtOrigen.y);

        //CALCULAR CENTROIDE
        centroideX();
        centroideY();

        //CALCULAR VERTICES LOCALES AL CENTROIDE
        Ver1C.x = Ver1.x - Cent.x;
        Ver1C.y = Ver1.y + Cent.y;

        Ver2C.x = Ver2.x - Cent.x;
        Ver2C.y = Ver2.y + Cent.y;

        Ver3C.x = Ver3.x - Cent.x;
        Ver3C.y = Ver3.y + Cent.y;

        actualizarEcuaciones();
    }


    /**Acualiza las dimensiones del panel que contiene la forma */
    public void ActualizarBordes(){

        float XMen = Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(Math.min(Ver1.x, Math.min(Ver2.x, Ver3.x)));
        float YMen = Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(Math.min(Ver1.y, Math.min(Ver2.y, Ver3.y)));

        float XMax = Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(Math.max(Ver1.x, Math.max(Ver2.x, Ver3.x)));
        float YMax = Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(Math.max(Ver1.y, Math.max(Ver2.y, Ver3.y)));

        X = Ctrl.aplicarEscalaLnPixU(XMen - Plano.PtOrigen.x);
        Y = Ctrl.aplicarEscalaLnPixU(YMen - Plano.PtOrigen.y);

        setBounds(Math.round(XMen), Math.round(YMen), Math.round(XMax) - Math.round(XMen), Math.round(YMax) - Math.round(YMen));
        
        repaint();
    }



    /**
     * Actualiza los vertices del triangulo
     * @param distx Distancia en X
     * @param disty Distancia en Y
     */
    public void moverVertices(float distx, float disty){
        //ACTUALIZAR VERTICES
        Ver1.x += distx;
        Ver1.y += disty;

        Ver2.x += distx;
        Ver2.y += disty;

        Ver3.x += distx;
        Ver3.y += disty;

        //CALCULAR CENTROIDE
        centroideX();
        centroideY();

        //CALCULAR VERTICES LOCALES AL CENTROIDE
        Ver1C.x = Ver1.x - Cent.x;
        Ver1C.y = Ver1.y + Cent.y;

        Ver2C.x = Ver2.x - Cent.x;
        Ver2C.y = Ver2.y + Cent.y;

        Ver3C.x = Ver3.x - Cent.x;
        Ver3C.y = Ver3.y + Cent.y;

        actualizarEcuaciones();
    }

    @Override
    public float calcularArea() {
        float LadoA = Punto.distanciaEntre(Ver1.x, Ver1.y, Ver2.x, Ver2.y);
        float LadoB = Punto.distanciaEntre(Ver3.x, Ver3.y, Ver2.x, Ver2.y);
        float LadoC = Punto.distanciaEntre(Ver1.x, Ver1.y, Ver3.x, Ver3.y);

        float s = (LadoA + LadoB + LadoC)/2;

        return (Hueco ? -1 : 1)*(float)Math.sqrt(s * (s - LadoA) * (s - LadoB) * (s - LadoC));
    }

    @Override
    public float centroideX() {
        float Cx = (Ver1.x + Ver2.x + Ver3.x)/3 - Ctrl.aplicarEscalaLnPixU(getX() - Plano.PtOrigen.x);
        Cent.x = Cx + X;

        return Cx;
    }

    
    @Override
    public float centroideY() {
        float Cy = (Ver1.y + Ver2.y + Ver3.y)/3 - Ctrl.aplicarEscalaLnPixU(getY() - Plano.PtOrigen.y);
        Cent.y = -Y - Cy;

        return Cy;
    }

    @Override
    public float inerciaCentEjeX(){

            //LIMITES DE INTEGRACION
            float a = Ver3C.y;
            float b = Ver1C.y;
            float c = Ver2C.y;
            
            //CALCULAR INERCIA DE LAS RECTAS
            float I1 = EcuacionRecta.integrar_fy(Ec[1], a, b, Plano);
            float I2 = EcuacionRecta.integrar_fy(Ec[2], a, b, Plano);
            float I3 = EcuacionRecta.integrar_fy(Ec[1], b, c, Plano);
            float I4 = EcuacionRecta.integrar_fy(Ec[0], b, c, Plano);


            float ix = Math.abs(I1-I2+I3-I4);
            float dy = (-Y - centroideY()) + Plano.centroideY();

            //TRASLADAR INERCIA AL CENTROIDE DE LA FIGURA
            float Ix = ix + Math.abs(calcularArea())*dy*dy;

            return (Hueco ? -1 : 1)*Ix;

    }

    @Override
    public float inerciaCentEjeY(){

            //LIMITES DE INTEGRACION
            float a = Ver1C.x;
            float b = Ver2C.x;
            float c = Ver3C.x;
            
            //CALCULAR INCERCIA DE LAS RECTAS
            float I1 = EcuacionRecta.integrar_fx(Ec[0], a, b, Plano);
            float I2 = EcuacionRecta.integrar_fx(Ec[2], a, b, Plano);
            float I3 = EcuacionRecta.integrar_fx(Ec[1], b, c, Plano);
            float I4 = EcuacionRecta.integrar_fx(Ec[2], b, c, Plano);

            //SUMAR INERCIAS
            float iy = Math.abs(I1-I2+I3-I4);

            float dx = (X + centroideX()) - Plano.centroideX();

            //TRASLADAR INERCIA AL CENTROIDE DE LA FORMA
            float Ix = iy + Math.abs(calcularArea())*dx*dx;
            
            return (Hueco ? -1 : 1)*Ix;

    }


    @Override
    public void mostrarPines() {
        
        if(Pines[0] == null){

            //VER1
            Pines[0] = new Pin(this, getX(), getY() + getHeight(), Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    setLocation(snapX(getX()), snapY(getY()));

                    Ver1.x = Ctrl.aplicarEscalaLnPixU(getX() - Plano.PtOrigen.x);
                    Ver1.y = Ctrl.aplicarEscalaLnPixU(getY() - Plano.PtOrigen.y);

                    ActualizarBordes();
                    actualizarCoordenadas();
                    Plano.repaint();
                }
            };

            //VER2
            Pines[1] = new Pin(this, getX() + 25, getY(), Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    setLocation(snapX(getX()), snapY(getY()));

                    Ver2.x = Ctrl.aplicarEscalaLnPixU(getX() - Plano.PtOrigen.x);
                    Ver2.y = Ctrl.aplicarEscalaLnPixU(getY() - Plano.PtOrigen.y);

                    ActualizarBordes();
                    actualizarCoordenadas();
                    Plano.repaint();
                }
            };

            //VER3
            Pines[2] = new Pin(this, getX() + getWidth(), getY() + getHeight(), Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    setLocation(snapX(getX()), snapY(getY()));

                    Ver3.x = Ctrl.aplicarEscalaLnPixU(getX() - Plano.PtOrigen.x);
                    Ver3.y = Ctrl.aplicarEscalaLnPixU(getY() - Plano.PtOrigen.y);

                    ActualizarBordes();
                    actualizarCoordenadas();
                    Plano.repaint();
                    
                }
            };

            //AGREGAR PINES AL PANEL PRINCIPAL
            for (Pin pin : Pines) {
                Plano.add(pin, JLayeredPane.DRAG_LAYER);
                Plano.moveToFront(pin);
            }

            actualizarPines();
            Plano.repaint();
        }
    }

    @Override
    public void actualizarPines() {
        if(Pines[0] == null)
            return;

        Pines[0].setLocation(Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(Ver1.x)), Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(Ver1.y)));
        Pines[1].setLocation(Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(Ver2.x)), Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(Ver2.y)));
        Pines[2].setLocation(Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(Ver3.x)), Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(Ver3.y)));
    }


    @Override
    public void mouseDragged(MouseEvent e) {

        Plano.notificarCambios(1);

        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, Plano);

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        float DifX = Ctrl.aplicarEscalaLnPixU(Pos.x - getX() - PtOffset.x);
        float DifY = Ctrl.aplicarEscalaLnPixU(Pos.y - getY() - PtOffset.y);

        moverVertices(DifX, DifY);

        setBounds(Pos.x - PtOffset.x, Pos.y - PtOffset.y, getWidth(), getHeight());
        actualizarPines();
        
        if(Grp != null)
            Grp.ActualizarBordes();
        
        Point PtPrev = getLocation();

        //AUTOAJUSTAR A OTRAS FORMAS
        setLocation(snapX(getX()), snapY(getY()));
        setLocation(snapX(getX() + getWidth()) - getWidth(),
                    snapY(getY() + getHeight()) - getHeight());

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        DifX = Ctrl.aplicarEscalaLnPixU(getX() - PtPrev.x);
        DifY = Ctrl.aplicarEscalaLnPixU(getY() - PtPrev.y);

        //ACTUALIZAR VERTICES
        moverVertices(DifX, DifY);

        actualizarPines();
        actualizarCoordenadas();

        Plano.repaint();
    }
}
