package com.mec2021.plano.objetos.formas;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import com.mec2021.plano.Punto;
import com.mec2021.plano.objetos.Pin;
import com.mec2021.Ctrl;
import com.mec2021.gui.PnPlano;

/**
 * Circulo que con diametro deformable y que se puede moficiar el arco
 * <p> El circulo esta dividido en 3, {@code Sector Principal} que mide como maximo 180 grados
 * y dos {@code Sectores Secundarios} que aparecen cuando el {@code Sector Principal} mide mas de 180 grados
 * <p>Para calcular el centroide del circulo, se utilizan estos tres sectores
 */
public class FrCirc extends Forma{


    /**Objeto para representar un sector Circular */
    public Arc2D.Double Sector;

    public float Radio = 2.5f;

    /**ID unico para cada circulo */
    public static int ID = 1;

    /**
     * Crea un circulo de diametro de 50 en el origen {@code (0, 0)}
     */
    public FrCirc(PnPlano plano){
        this(0*plano.Escala, -5*plano.Escala, 2.5f*plano.Escala, 90, 360, plano);
    }

    /**
     * Crea un circulo definiendo el diametro y la posicion de la esquina superior izquierda
     * @param X
     * @param Y
     * @param AngIni Angulo inicial del sector
     * @param Ext Angulo del sector
     */
    public FrCirc(float x, float y, float rad, int AngIni, int Ext, PnPlano plano){
        super(plano);
        
        
        Nombre = "Circulo " + (ID++);
        
        X = x;
        Y = y;
        Radio = rad;
        
        Plano.moveToFront(this);
        
        Pines = new Pin[3];
        
        setBounds(Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(X)), 
                             Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(Y)), 
                             2*Math.round(Ctrl.aplicarEscalaUPix(Radio)), 
                             2*Math.round(Ctrl.aplicarEscalaUPix(Radio)));
        Sector = new Arc2D.Double(0,0, getWidth(), getHeight(), AngIni, Ext, Arc2D.PIE);
        
        //setOpaque(false);

        Plano.notificarCambios(0);
    }
    


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D G2 = (Graphics2D)g;
        g.setColor(ColForma);

        if(Hueco){
            g.setColor(Color.WHITE);
            G2.fill(Sector);
            g.setColor(Color.DARK_GRAY);

            if(Sector.extent != 360)
                G2.draw(Sector);
            else
                G2.drawOval(0, 0, Math.round(Ctrl.aplicarEscalaUPix(Radio)) - 1, Math.round(Ctrl.aplicarEscalaUPix(Radio)) - 1);
        }else
            G2.fill(Sector);

        //POSICION DEL CIRCULO EN EL PLANO
        int x = Math.round(Ctrl.aplicarEscalaUPix(centroideX()));
        int y = Math.round(Ctrl.aplicarEscalaUPix(centroideY()));

        g.setColor(Color.RED);

        g.fillOval(x-3,y-3,6,6);
    }


    @Override
    public void actualizarDimensiones(){
        setBounds(Math.round( Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(X)),
                  Math.round( Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(Y)), 
                  2*Math.round(Ctrl.aplicarEscalaUPix(Radio)), 
                  2*Math.round(Ctrl.aplicarEscalaUPix(Radio)));

        Sector.width = 2*Ctrl.aplicarEscalaUPix(Radio);
        Sector.height = 2*Ctrl.aplicarEscalaUPix(Radio);

        actualizarPines();
        repaint();
    }


    @Override
    public void actualizarCoordenadas() {
        X = Ctrl.aplicarEscalaLnPixU(getX() - Plano.PtOrigen.x);
        Y = Ctrl.aplicarEscalaLnPixU(getY() - Plano.PtOrigen.y);

        Radio = Ctrl.aplicarEscalaLnPixU(getWidth()/2);
    }


    @Override
    public float calcularArea() {
        return (Hueco ? -1 : 1)*areaSector(Radio, (float)Math.toRadians(Sector.extent));
    }


    @Override
    public float centroideX() {

        //AMPLITUD DEL SECTOR PRINCIPAL
        float a = (float)Math.toRadians(Ctrl.clamp((float)Sector.extent, 0f, 180f)/2);
        //AMPLITUD DEL SECTOR SECUNDARIO
        float a2 = (float)Math.toRadians(Ctrl.clamp((float)(Sector.extent - 180)/2, 0f, 180f)/2);

        //DISTANCIA DE LOS CENTROIDES DEL SECTOR
        float DistP = distSect(Radio, a);
        float DistS = distSect(Radio, a2);

        //POSICION X DE LOS TRES SECTORES
        float xP = (float)Math.cos(Math.toRadians(Sector.extent/2 + Sector.start)) * DistP;
        float xS1 = (float)Math.cos(Math.toRadians(Sector.extent/2 + Sector.start) - Math.PI/2 - a2) * DistS;
        float xS2 = (float)Math.cos(Math.toRadians(Sector.extent/2 + Sector.start) + Math.PI/2 + a2) * DistS;

        //AREA DE LOS TRES SECTORES
        float AreaP = areaSector(Radio, a);
        float AreaS = areaSector(Radio, a2);

        //CALCULAR AX
        float Axp = AreaP*xP;
        float AxS1 = AreaS*xS1;
        float AxS2 = AreaS*xS2;

        //CALCULAR CENTROIDE DE TODOS LOS SECTORES
        float SumaAreas = AreaP + 2*AreaS;
        float SumaAreasPorX = Axp + AxS1 + AxS2;

        return SumaAreasPorX/SumaAreas + Radio;
    }


    @Override
    public float centroideY() {

        //AMPLITUD DEL SECTOR PRINCIPAL
        float a = (float)Math.toRadians(Ctrl.clamp((float)Sector.extent, 0, 180)/2);
        //AMPLITUD DEL SECTOR SECUNDARIO
        float a2 = (float)Math.toRadians(Ctrl.clamp((float)(Sector.extent - 180)/2, 0, 180)/2);

        //DISTANCIA DE LOS CENTROIDES DEL SECTOR
        float DistP = distSect(Radio, a);
        float DistS = distSect(Radio, a2);

        //POSICION Y DE LOS TRES SECTORES
        float yP = (float)Math.sin(Math.toRadians(Sector.extent/2 + Sector.start)) * DistP;
        float yS1 = (float)Math.sin(Math.toRadians(Sector.extent/2 + Sector.start) - Math.PI/2 - a2) * DistS;
        float yS2 = (float)Math.sin(Math.toRadians(Sector.extent/2 + Sector.start) + Math.PI/2 + a2) * DistS;

        //AREA DE LOS TRES SECTORES
        float AreaP = areaSector(Radio, a);
        float AreaS = areaSector(Radio, a2);

        //CALCULAR AY
        float Ayp = AreaP*yP;
        float AyS1 = AreaS*yS1;
        float AyS2 = AreaS*yS2;

        //CALCULAR CENTROIDE DE TODOS LOS SECTORES
        float SumaAreas = AreaP + 2*AreaS;
        float SumaAreasPorY = Ayp + AyS1 + AyS2;

        return -SumaAreasPorY/SumaAreas + Radio;
    }


    /**
     * Calcula el area de un sector
     * @param r Radio
     * @param a Amplitud del sector
     * @return Area del sector
     */
    private float areaSector(float r, float a){
        return (r*r*a)/2;
    }

    /**
     * Calcula la distancia deste el centro del circulo hasta el centroide del sector
     * @param r Radio
     * @param a Amplitud del sector
     * @return Distancia
     */
    private float distSect(float r, float a){

        //Referencia
        //https://calcresource.com/geom-circularsector.html

        if(a != 0)
            return (float)(2*r*Math.sin(a))/(3*a);
        else
            return 0;
    }


    @Override
    public float inerciaCentEjeX(){
        //Referencia
        //https://www.efunda.com/math/areas/CircularSection.cfm
        
        if(Grp != null){
            float r = Radio;
            float a = (float)Math.toRadians(Sector.extent)/2f;

            float ix = ((r*r*r*r)/4)*(a+((float)Math.sin(2*a)/2)) - (4*r*r*r*r*(float)Math.sin(a)*(float)Math.sin(a))/(9*a);
            
            float dy = (-Y - centroideY()) - (Ctrl.aplicarEscalaLnPixU(Plano.PtOrigen.y - Grp.getY()) - Grp.centroideY());

            float Ix = ix + Math.abs(calcularArea())*dy*dy;

            return (Hueco ? -1 : 1)*Ix;
        }else 
            return 0;
    }

    @Override
    public float inerciaCentEjeY(){
        //Referencia
        //https://www.efunda.com/math/areas/CircularSection.cfm
        
        if(Grp != null){
            float r = Radio;
            float a = (float)Math.toRadians(Sector.extent)/2f;

            float ix = ((r*r*r*r)/4)*(a-((float)Math.sin(2*a)/2));

            float dy = (centroideX() + X) - Grp.centroideX();

            float Ix = ix + Math.abs(calcularArea())*dy*dy;

            return (Hueco ? -1 : 1)*Ix;
        }else 
            return 0;
    }



    @Override
    public void mostrarPines() {
        
        //CREAR PINES
        if(Pines[0] == null){

            int Pin0X = getX() + getWidth();
            int Pin0Y = getY() + getWidth()/2;

            //PIN DE RADIO
            Pines[0] = new Pin(this, Pin0X, Pin0Y, Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point Pos = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(Pos, Plano);
            
                    setBounds(Pos.x - PtOffset.x, Fr.getY() + getHeight()/2, getWidth(), getHeight());

                    //ACTUALIZAR RADIO
                    int DiaPix = getX() - Fr.getX();
                    Radio = Ctrl.aplicarEscalaLnPixU(DiaPix/2f);

                    //ACTUALIZAR CIRCULO
                    Fr.setBounds(Fr.getX(), Fr.getY(), DiaPix, DiaPix);
                    Sector.width = DiaPix;
                    Sector.height = DiaPix;

                    if(Fr.Grp != null)
                        Fr.Grp.ActualizarBordes();
                    
                    actualizarPines();
                    
                    Plano.repaint();
                    Plano.notificarCambios(1);
                }
            };

            //PIN PARA MODIFICAR LA AMPLITUD DEL SECTOR
            int Pin1X = getX() + getWidth()/2 + Math.round((float)(Ctrl.aplicarEscalaUPix(Radio) * Math.cos(Math.toRadians(-Sector.start))));
            int Pin1Y = getY() + getHeight()/2 + Math.round((float)(Ctrl.aplicarEscalaUPix(Radio)*Math.sin(Math.toRadians(-Sector.start))));

            Pines[1] = new Pin(this, Pin1X, Pin1Y, Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point Pos = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(Pos, Plano);
        
                    //CALCULAR ANGULO DESDE EL CENTRO DEL CIRCULO A ESTE PIN
                    float ang = Punto.calcularDirection(Fr.getX() + Fr.getWidth()/2,
                                                        Fr.getY() + Fr.getHeight()/2,
                                                        Pos.x,
                                                        Pos.y);

                    //CREAR REPRESENTACION VECTORIAL DEL ANGULO
                    Punto VectDir = new Punto((float)Math.cos(ang), (float)Math.sin(ang));


                    //AJUSTAR EL PIN A LA CIRCUNFERENCIA
                    setBounds(Math.round(Fr.getX() + Ctrl.aplicarEscalaUPix(Radio) + (float)(Ctrl.aplicarEscalaUPix(Radio)*VectDir.x)),
                              Math.round(Fr.getY() + Ctrl.aplicarEscalaUPix(Radio) + (float)(Ctrl.aplicarEscalaUPix(Radio)*VectDir.y)), 
                              getWidth(), 
                              getHeight());
                    
                    /*Como las coordenadas estan invertidas en y, para poder realizar calculos correctos
                    *se busca un angulo usando las coordenadas del primero pero con -y
                    */
                    float angInv = Punto.calcularDirection(0f, 0f,VectDir.x,-VectDir.y);

                    /* ACTUALIZAR LA AMPLITUD DEL SECTOR
                     */
                    Sector.extent = Sector.extent - 2*(Math.toDegrees(angInv) - Sector.start);

                    //AJUSTAR NUEVO ANGULO INICIAL
                    Sector.start = Math.toDegrees(angInv);

                    actualizarPines();
                    Plano.notificarCambios(1);
                    Fr.repaint();
                }
            };


            //PIN PARA AJUSTAR EL ANGULO INCIAL
            int Pin2X = getX() + getWidth()/2 + Math.round((float)(Ctrl.aplicarEscalaUPix(Radio)/2 * Math.cos(Math.toRadians(-Sector.start))));
            int Pin2Y = getY() + getHeight()/2 + Math.round((float)(Ctrl.aplicarEscalaUPix(Radio)/2*Math.sin(Math.toRadians(-Sector.start))));
            Pines[2] = new Pin(this, Pin2X, Pin2Y, Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point Pos = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(Pos, Plano);
        
                    //CALCULAR ANGULO DESDE EL CENTRO DEL CIRCULO A ESTE PIN
                    float ang = Punto.calcularDirection(Fr.getX() + Fr.getWidth()/2,
                                                        Fr.getY() + Fr.getHeight()/2,
                                                        Pos.x,
                                                        Pos.y);

                    //CREAR REPRESENTACION VECTORIAL DEL ANGULO
                    Punto VectDir = new Punto((float)Math.cos(ang), (float)Math.sin(ang));

                    //AJUSTAR EL PIN A LA CIRCUNFERENCIA
                    setBounds(Math.round(Fr.getX() + Ctrl.aplicarEscalaUPix(Radio) + (float)(Ctrl.aplicarEscalaUPix(Radio)/2*VectDir.x)),
                              Math.round(Fr.getY() + Ctrl.aplicarEscalaUPix(Radio) + (float)(Ctrl.aplicarEscalaUPix(Radio)/2*VectDir.y)), 
                              getWidth(), 
                              getHeight());
                    
                    /*Como las coordenadas estan invertidas en y, para poder realizar calculos correctos
                    *se busca un angulo usando las coordenadas del primero pero con -y
                    */
                    float angInv = Punto.calcularDirection(0f, 0f,VectDir.x,-VectDir.y);

                    //AJUSTAR NUEVO ANGULO INICIAL
                    Sector.start = Math.toDegrees(angInv);

                    actualizarPines();
                    Plano.notificarCambios(1);
                    Fr.repaint();
                }
            };

            //AGREGAR PINES
            for (Pin pin : Pines) {
                Plano.add(pin, JLayeredPane.DRAG_LAYER);
                Plano.moveToFront(pin);
            }

            Plano.repaint();
        }
    }

    @Override
    public void actualizarPines() {

        if(Pines[0] == null)
            return;

        //PIN DE DIAMETRO
        int PinX = getX() + getWidth();
        int PinY = getY() + getHeight()/2;

        Pines[0].setLocation(PinX, PinY);

        //PIN PARA MODIFICAR ARCO
        PinX = getX() + getWidth()/2 + Math.round((float)((Math.round(Ctrl.aplicarEscalaUPix(Radio)))*Math.cos(Math.toRadians(-Sector.start))));
        PinY = getY() + getHeight()/2 + Math.round((float)((Math.round(Ctrl.aplicarEscalaUPix(Radio)))*Math.sin(Math.toRadians(-Sector.start))));

        Pines[1].setLocation(PinX, PinY);

        //PIN PARA MODIFICAR LA ROTACION DEL CIRCULO
        PinX = getX() + getWidth()/2 + Math.round((float)((Math.round(Ctrl.aplicarEscalaUPix(Radio))/2)*Math.cos(Math.toRadians(-Sector.start))));
        PinY = getY() + getHeight()/2 + Math.round((float)((Math.round(Ctrl.aplicarEscalaUPix(Radio))/2)*Math.sin(Math.toRadians(-Sector.start))));

        Pines[2].setLocation(PinX, PinY);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        actualizarPines();
    }
}
