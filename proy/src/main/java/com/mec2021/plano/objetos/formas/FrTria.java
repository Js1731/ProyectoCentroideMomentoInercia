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

    public static int ID = 1;

    /**
     * Crea un tringulo en el origen de 50x50
     */
    public FrTria(PnPlano plano){
        this(0,-5,0,5.0f,2.5f,0,5.0f,5.0f, plano);
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
    public FrTria(float X, float Y,float x1, float y1, float x2, float y2, float x3, float y3, PnPlano plano){
        super(plano);
        Pines = new Pin[3];

        Nombre = "Triangulo " + (ID++);
        Plano.notificarCambios(0);

        Ver1.x = Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaLnInv(X) + Ctrl.aplicarEscalaLnInv(x1));
        Ver1.y = Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaLnInv(Y) + Ctrl.aplicarEscalaLnInv(y1));

        Ver2.x = Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaLnInv(X) + Ctrl.aplicarEscalaLnInv(x2));
        Ver2.y = Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaLnInv(Y) + Ctrl.aplicarEscalaLnInv(y2));

        Ver3.x = Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaLnInv(X) + Ctrl.aplicarEscalaLnInv(x3));
        Ver3.y = Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaLnInv(Y) + Ctrl.aplicarEscalaLnInv(y3));

        //CALCULAR CENTROIDE
        centroideX();
        centroideY();

        //CALCULAR VERTICES LOCALES AL CENTROIDE
        Ver1C.x = Ver1.x - Cent.x - getX();
        Ver1C.y = Ver1.y - Cent.y - getY();

        Ver2C.x = Ver2.x - Cent.x - getX();
        Ver2C.y = Ver2.y - Cent.y - getY();

        Ver3C.x = Ver3.x - Cent.x - getX();
        Ver3C.y = Ver3.y - Cent.y - getY();

        //DEFINIR ECUACIONES DE LA RECTA LOCALES AL CENTROIDE
        Ec[0] = new EcuacionRecta(Ver1C, Ver2C);
        Ec[1] = new EcuacionRecta(Ver2C, Ver3C);
        Ec[2] = new EcuacionRecta(Ver3C, Ver1C);


        //setOpaque(false);
        ActualizarBordes();
        ActualizarCoordenadas();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Poligono.reset();

        //VERTICE 1
        Point TmpVer1 = new Point(Math.round(Ver1.x), Math.round(Ver1.y));
        SwingUtilities.convertPoint(Plano, TmpVer1, this);
        
        Poligono.addPoint(TmpVer1.x - getX(), TmpVer1.y - getY());
        
        //VERTICE 2
        Point TmpVer2 = new Point(Math.round(Ver2.x), Math.round(Ver2.y));
        SwingUtilities.convertPoint(Plano, TmpVer2, this);
        
        Poligono.addPoint(TmpVer2.x - getX(), TmpVer2.y - getY());
        
        //VERTICE 3
        Point TmpVer3 = new Point(Math.round(Ver3.x), Math.round(Ver3.y));
        SwingUtilities.convertPoint(Plano, TmpVer3, this);
        
        Poligono.addPoint(TmpVer3.x - getX(), TmpVer3.y - getY());

        g.setColor(ColFig);

        if(Hueco){
            g.setColor(Color.WHITE);
            g.fillPolygon(Poligono);
            g.setColor(Color.DARK_GRAY);
            g.drawPolygon(Poligono);
        }else
            g.fillPolygon(Poligono);
        
        g.setColor(Color.WHITE);

        int x = Math.round(centroideX());
        int y = Math.round(centroideY());

        g.setColor(Color.RED);

        g.fillOval(x-3,y-3,6,6);
    }

    @Override
    public void actualizarEscala(){

        float NuevaEsc = ( (float)Plano.EscalaPix/Plano.Escala) * Plano.EscalaVieja;

        setBounds(Math.round(Plano.PtOrigen.x + X*NuevaEsc), 
                  Math.round(Plano.PtOrigen.y + Y*NuevaEsc), 
                  Math.round(getWidth()*NuevaEsc), 
                  Math.round(getHeight()*NuevaEsc));
        
        Ver1.x = Math.round(Plano.PtOrigen.x + NuevaEsc*(Ver1.x - Plano.PtOrigen.x));
        Ver1.y = Math.round(Plano.PtOrigen.y + NuevaEsc*(Ver1.y - Plano.PtOrigen.y));

        Ver2.x = Math.round(Plano.PtOrigen.x + NuevaEsc*(Ver2.x - Plano.PtOrigen.x));
        Ver2.y = Math.round(Plano.PtOrigen.y + NuevaEsc*(Ver2.y - Plano.PtOrigen.y));

        Ver3.x = Math.round(Plano.PtOrigen.x + NuevaEsc*(Ver3.x - Plano.PtOrigen.x));
        Ver3.y = Math.round(Plano.PtOrigen.y + NuevaEsc*(Ver3.y - Plano.PtOrigen.y));

        ActualizarCoordenadas();
        ActualizarPines();
        actualizarEcuaciones();
    }

    public void actualizarEcuaciones(){
        for (EcuacionRecta ec : Ec) {
            ec.actualizarDatos();
        }
    }

    @Override
    public void ActualizarPines() {
        if(Pines[0] == null)
            return;

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
        X = Math.round(getX() - Plano.PtOrigen.x);
        Y = Math.round(getY() - Plano.PtOrigen.y);

        //CALCULAR CENTROIDE
        centroideX();
        centroideY();

        //CALCULAR VERTICES LOCALES AL CENTROIDE
        Ver1C.x = Ver1.x - Cent.x - getX();
        Ver1C.y = Ver1.y - Cent.y - getY();

        Ver2C.x = Ver2.x - Cent.x - getX();
        Ver2C.y = Ver2.y - Cent.y - getY();

        Ver3C.x = Ver3.x - Cent.x - getX();
        Ver3C.y = Ver3.y - Cent.y - getY();

        actualizarEcuaciones();
    }

    /**
     * Actualiza los vertices del triangulo
     * @param distx Distancia en X
     * @param disty Distancia en Y
     */
    public void moverVertices(int distx, int disty){
        //ACTUALIZAR VERTICES
        Ver1.x += distx;
        Ver1.y += disty;

        Ver2.x += distx;
        Ver2.y += disty;

        Ver3.x += distx;
        Ver3.y += disty;
    }

    @Override
    public float calcularArea() {
        float LadoA = Punto.distanciaEntre(Ver1.x, Ver1.y, Ver2.x, Ver2.y);
        float LadoB = Punto.distanciaEntre(Ver3.x, Ver3.y, Ver2.x, Ver2.y);
        float LadoC = Punto.distanciaEntre(Ver1.x, Ver1.y, Ver3.x, Ver3.y);

        float s = (LadoA + LadoB + LadoC)/2;

        return (Hueco ? -1 : 1)*(float)Math.round(Math.sqrt(s * (s - LadoA) * (s - LadoB) * (s - LadoC)));
    }

    @Override
    public float inerciaCentEjeX(){

        float a = Ver3C.y*((float)PnPlano.Escala/PnPlano.EscalaPix);
        float b = Ver1C.y*((float)PnPlano.Escala/PnPlano.EscalaPix);
        float c = Ver2C.y*((float)PnPlano.Escala/PnPlano.EscalaPix);
        
        float I1 = EcuacionRecta.integrar_fy(Ec[1], a, b);
        float I2 = EcuacionRecta.integrar_fy(Ec[2], a, b);
        float I3 = EcuacionRecta.integrar_fy(Ec[1], b, c);
        float I4 = EcuacionRecta.integrar_fy(Ec[0], b, c);

        return -(I1-I2+I3-I4);
    }

    @Override
    public float inerciaCentEjeY(){
        float a = Ver1C.x*((float)PnPlano.Escala/PnPlano.EscalaPix);
        float b = Ver2C.x*((float)PnPlano.Escala/PnPlano.EscalaPix);
        float c = Ver3C.x*((float)PnPlano.Escala/PnPlano.EscalaPix);
        
        float I1 = EcuacionRecta.integrar_fx(Ec[0], a, b);
        float I2 = EcuacionRecta.integrar_fx(Ec[2], a, b);
        float I3 = EcuacionRecta.integrar_fx(Ec[1], b, c);
        float I4 = EcuacionRecta.integrar_fx(Ec[2], b, c);

        return -(I1-I2+I3-I4);
    }

    @Override
    public float centroideX() {
        return Cent.x = (Ver1.x + Ver2.x + Ver3.x)/3 - getX();
    }

    @Override
    public float centroideY() {
        return Cent.y = ((Ver1.y + Ver2.y + Ver3.y)/3 - getY());
    }



    
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        if(Pines[0] == null){

            //VER1
            Pines[0] = new Pin(this, getX(), getY() + getHeight(), Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    setLocation(snapX(getX()), snapY(getY()));

                    Ver1.x = getX();
                    Ver1.y = getY();

                    ActualizarBordes();
                    ActualizarCoordenadas();
                }
            };

            //VER2
            Pines[1] = new Pin(this, getX() + 25, getY(), Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    setLocation(snapX(getX()), snapY(getY()));

                    Ver2.x = getX();
                    Ver2.y = getY();

                    ActualizarBordes();
                    ActualizarCoordenadas();
                }
            };

            //VER3
            Pines[2] = new Pin(this, getX() + getWidth(), getY() + getHeight(), Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    setLocation(snapX(getX()), snapY(getY()));

                    Ver3.x = getX();
                    Ver3.y = getY();

                    ActualizarBordes();
                    ActualizarCoordenadas();
                    
                }
            };

            //AGREGAR PINES AL PANEL PRINCIPAL
            for (Pin pin : Pines) {
                Plano.add(pin, JLayeredPane.DRAG_LAYER);
                Plano.moveToFront(pin);
            }

            ActualizarPines();

            Plano.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        Plano.notificarCambios(1);

        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, Plano);

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        int DifX = Pos.x - (PtOffset.x + getX());
        int DifY = Pos.y - (PtOffset.y + getY());

        moverVertices(DifX, DifY);

        setBounds(Pos.x - PtOffset.x, Pos.y - PtOffset.y, getWidth(), getHeight());
        ActualizarPines();
        
        if(Grp != null)
            Grp.ActualizarBordes();
        
        Point PtPrev = getLocation();

        //AUTOAJUSTAR A OTRAS FORMAS
        setLocation(snapX(getX()), snapY(getY()));
        setLocation(snapX(getX() + getWidth()) - getWidth(),
                    snapY(getY() + getHeight()) - getHeight());

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        DifX = getX() - PtPrev.x;
        DifY = getY() - PtPrev.y;

        //ACTUALIZAR VERTICES
        moverVertices(DifX, DifY);

        ActualizarPines();
        ActualizarCoordenadas();
        Plano.repaint();
    }
}
