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

    public Arc2D.Double Sector;

    public int Diametro = 5;

    public static int ID = 1;

    /**
     * Crea un circulo de diametro de 50 en el origen {@code (0, 0)}
     */
    public FrCirc(PnPlano plano){
        this(0, -5, 5, 90, 360, plano);
    }

    /**
     * Crea un circulo definiendo el diametro y la posicion de la esquina superior izquierda
     * @param X
     * @param Y
     * @param AngIni Angulo inicial del sector
     * @param Ext Angulo del sector
     */
    public FrCirc(float X, float Y, float dia, int AngIni, int Ext, PnPlano plano){
        super(plano);
        
        setOpaque(false);

        Nombre = "Circulo " + (ID++);

        Plano.notificarCambios(0);

        Plano.moveToFront(this);

        Pines = new Pin[3];
        Diametro = Math.round(dia*Escala);
        setBounds(Math.round(Plano.PtOrigen.x) + Math.round(X*Escala), Math.round(Plano.PtOrigen.y) +  Math.round(Y*Escala), Diametro, Diametro);
        Sector = new Arc2D.Double(0,0, getWidth(), getHeight(), AngIni, Ext, Arc2D.PIE);
        ActualizarCoordenadas();

        
    }
    


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D G2 = (Graphics2D)g;
        g.setColor(ColFig);

        if(Hueco){
            g.setColor(Color.WHITE);
            G2.fill(Sector);
            g.setColor(Color.DARK_GRAY);
            G2.draw(Sector);
        }else
            G2.fill(Sector);

        int x = Math.round(centroideX());
        int y = Math.round(centroideY());

        g.setColor(Color.RED);

        g.fillOval(x-3,y-3,6,6);
    }



    @Override
    public void ActualizarPines() {

        if(Pines[0] == null)
            return;

        //ACTUALIZAR CIRCULO
        setBounds(getX(), getY(), Diametro, Diametro);
        Sector.width = getWidth();
        Sector.height = getHeight();

        //ACTUALIZAR PINES

        //PIN DE DIAMETRO
        int PinX = getX() + getWidth()/2  + Diametro/2;
        int PinY = getY() + getWidth()/2;

        Pines[0].setLocation(PinX, PinY);

        //PIN PARA MODIFICAR ARCO
        PinX = getX() + getWidth()/2 + Math.round((float)((Diametro/2.5)*Math.cos(Math.toRadians(-Sector.start))));
        PinY = getY() + getHeight()/2 + Math.round((float)((Diametro/2.5)*Math.sin(Math.toRadians(-Sector.start))));

        Pines[1].setLocation(PinX, PinY);

        //PIN PARA MODIFICAR LA ROTACION DEL CIRCULO
        PinX = getX() + getWidth()/2 + Math.round((float)((Diametro/5)*Math.cos(Math.toRadians(-Sector.start))));
        PinY = getY() + getHeight()/2 + Math.round((float)((Diametro/5)*Math.sin(Math.toRadians(-Sector.start))));

        Pines[2].setLocation(PinX, PinY);
    }

    @Override
    public void ActualizarCoordenadas() {
        X = Math.round(getX() - Plano.PtOrigen.x);
        Y = Math.round(getY() - Plano.PtOrigen.y);

        Diametro = getWidth();
    }

    @Override
    public float calcularArea() {
        return (Hueco ? -1 : 1)*areaSector(Diametro/2f, (float)Math.toRadians(Sector.extent));
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
        if(a != 0)
            return (float)(2*r*Math.sin(a))/(3*a);
        else
            return 0;
    }


    @Override
    public float centroideX() {

        float Radio = Diametro/2;

        //AMPLITUD DEL SECTOR PRINCIPAL
        float a = (float)Math.toRadians(Ctrl.Utils.clamp((float)Sector.extent, 0f, 180f)/2);
        //AMPLITUD DEL SECTOR SECUNDARIO
        float a2 = (float)Math.toRadians(Ctrl.Utils.clamp((float)(Sector.extent - 180)/2, 0f, 180f)/2);

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

        float SumaAreas = AreaP + 2*AreaS;
        float SumaAreasPorX = Axp + AxS1 + AxS2;

        return SumaAreasPorX/SumaAreas + Diametro/2;
    }

    @Override
    public float centroideY() {
        float Radio = Diametro/2;

        //AMPLITUD DEL SECTOR PRINCIPAL
        float a = (float)Math.toRadians(Ctrl.Utils.clamp((float)Sector.extent, 0, 180)/2);
        //AMPLITUD DEL SECTOR SECUNDARIO
        float a2 = (float)Math.toRadians(Ctrl.Utils.clamp((float)(Sector.extent - 180)/2, 0, 180)/2);

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

        float SumaAreas = AreaP + 2*AreaS;
        float SumaAreasPorX = Ayp + AyS1 + AyS2;

        return -SumaAreasPorX/SumaAreas + Diametro/2;
    }



    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        //CREAR PINES
        if(Pines[0] == null){

            int PinX = getX() + getWidth()/2 + Diametro/2;
            int PinY = getY() + getHeight()/2;

            //PIN DE DIAMETRO
            Pines[0] = new Pin(this, PinX, PinY, Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point Pos = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(Pos, Plano);
            
                    setBounds(Pos.x - PtOffset.x, Fr.getY() + Diametro/2, getWidth(), getHeight());
                    
                    Diametro = (Fr.getWidth() + getX()) - (Fr.getX() + Fr.getWidth());
                    
                    if(Fr.Grp != null)
                        Fr.Grp.ActualizarBordes();
                    ActualizarPines();
                    ActualizarCoordenadas();
                    Plano.notificarCambios(1);
                }
            };

            //PIN PARA MODIFICAR LA AMPLITUD DEL SECTOR
            PinX = getX() + getWidth()/2 + Math.round((float)(Diametro/4*Math.cos(Sector.start)));

            Pines[1] = new Pin(this, PinX, PinY, Plano){
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
                    setBounds(Math.round(Fr.getX() + Diametro/2 + (float)(Diametro/2*VectDir.x)),
                              Math.round(Fr.getY() + Diametro/2 + (float)(Diametro/2*VectDir.y)), 
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

                    ActualizarPines();
                    ActualizarCoordenadas();
                    Plano.notificarCambios(1);
                    Fr.repaint();
                }
            };


            //PIN PARA AJUSTAR EL ANGULO INCIAL
            Pines[2] = new Pin(this, PinX, PinY, Plano){
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
                    setBounds(Math.round(Fr.getX() + Diametro/2 + (float)(Diametro/2*VectDir.x)),
                              Math.round(Fr.getY() + Diametro/2 + (float)(Diametro/2*VectDir.y)), 
                              getWidth(), 
                              getHeight());
                    
                    /*Como las coordenadas estan invertidas en y, para poder realizar calculos correctos
                    *se busca un angulo usando las coordenadas del primero pero con -y
                    */
                    float angInv = Punto.calcularDirection(0f, 0f,VectDir.x,-VectDir.y);

                    //AJUSTAR NUEVO ANGULO INICIAL
                    Sector.start = Math.toDegrees(angInv);

                    ActualizarPines();
                    ActualizarCoordenadas();
                    Plano.notificarCambios(1);
                    Fr.repaint();
                }
            };

            //AGREGAR PINES
            for (Pin pin : Pines) {
                Plano.add(pin, JLayeredPane.DRAG_LAYER);
                Plano.moveToFront(pin);
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
