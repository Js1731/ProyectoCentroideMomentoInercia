package PryMecanica.Plano.Objetos.Formas;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import PryMecanica.Main;
import PryMecanica.PnPrincipal;
import PryMecanica.Plano.Punto;
import PryMecanica.Plano.Objetos.Pin;

/**
 * Circulo que con diametro deformable y que se puede moficiar el arco
 */
public class FrCirc extends Forma{

    public Arc2D.Double Sector;

    public int Diametro = 50;



    /**
     * Crea un circulo de diametro de 50 en el origen {@code (0, 0)}
     */
    public FrCirc(){
        this(0, 0, 50);
    }

    /**
     * Crea un circulo definiendo el diametro y la posicion de la esquina superior izquierda
     * @param X
     * @param Y
     */
    public FrCirc(int X, int Y, int dia){
        setOpaque(false);
        Pines = new Pin[3];
        Diametro = dia;
        setBounds(PnPrincipal.PnPrinc.getX() + X, PnPrincipal.PnPrinc.getY() +  Y, Diametro, Diametro);
        Sector = new Arc2D.Double(0,0, getWidth(), getHeight(), 90, 360, Arc2D.PIE);
    }
    


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D G2 = (Graphics2D)g;

        G2.fill(Sector);

        int x = Math.round(centroideX());
        int y = Math.round(centroideY());

        g.setColor(Color.RED);

        g.fillOval(getWidth()/2 + x-3,getHeight()/2 + y-3,6,6);
    }



    @Override
    public void ActualizarPines() {

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
        X = Math.round(getX() + getWidth()/2 - PnPrincipal.PtOrigen.x);
        Y = Math.round(getY() + getHeight()/2 - PnPrincipal.PtOrigen.y);
    }

    @Override
    public float calcularArea() {
        return (float)(Diametro*Diametro*Math.toRadians(Sector.extent))/8;
    }

    private float areaSector(float r, float a){
        return (float)(r*r*Math.toRadians(a))/2;
    }

    private float distSect(float r, float a){
        float Dist = (float)(4*r*Math.pow(Math.sin(a/2),3))/(float)(3*(a - Math.sin(a))) - r*(float)Math.cos(a/2);
        
        return Dist;
    }


    @Override
    public float centroideX() {
        //BUSCAR RADIO DEL CENTROIDE
        float Radio = Diametro/2;
        float Ang = (float)Math.toRadians(Main.clamp((float)Sector.extent, 0, 180));
        float AngSec = (float)Math.toRadians(Main.clamp(((float)Sector.extent - 180), 0, 180)/2);

        //DISTANCIA DE SECTOR PRINCIPAL
        float distSectPrinc = distSect(Radio, Ang);

        //DISTANCIAS DE SECTORES SECUNDARIOS
        float distSectSec = distSect(Radio, AngSec);

        float CentXP = (float)Math.cos(Math.toRadians(Sector.extent/2 + Sector.start)) * distSectPrinc;
        float CentXS1 = (float)Math.cos(Math.toRadians(Sector.start + 90) - AngSec/2) * distSectSec;
        float CentXS2 = (float)Math.cos(Math.toRadians(Sector.start - 90) + AngSec/2) * distSectSec;

        float AreaP = areaSector(Radio, Ang);
        float AreaS = areaSector(Radio, AngSec);

        float SumaAreaPorX = CentXP*AreaP + CentXS1*AreaS + CentXS2*AreaS;
        
        return SumaAreaPorX/(AreaP + AreaS);
    }

    @Override
    public float centroideY() {
        //BUSCAR RADIO DEL CENTROIDE
        float Radio = Diametro/2;
        float Ang = (float)Math.toRadians(Main.clamp((float)Sector.extent, 0, 180));
        float AngSec = (float)Math.toRadians(Main.clamp(((float)Sector.extent - 180), 0, 180)/2);

        //DISTANCIA DE SECTOR PRINCIPAL
        float distSectPrinc = distSect(Radio, Ang);

        //DISTANCIAS DE SECTORES SECUNDARIOS
        float distSectSec = distSect(Radio, AngSec);

        float CentXP = -(float)Math.sin(Math.toRadians(Sector.extent/2 + Sector.start)) * distSectPrinc;
        float CentXS1 = -(float)Math.sin(Math.toRadians(Sector.start + 90) - AngSec/2) * distSectSec;
        float CentXS2 = -(float)Math.sin(Math.toRadians(Sector.start - 90) + AngSec/2) * distSectSec;

        float AreaP = areaSector(Radio, Ang);
        float AreaS = areaSector(Radio, AngSec);

        float SumaAreaPorX = CentXP*AreaP + CentXS1*AreaS + CentXS2*AreaS;
        
        return SumaAreaPorX/(AreaP + 2*AreaS);
    }



    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        //CREAR PINES
        if(Pines[0] == null){

            int PinX = getX() + getWidth()/2 + Diametro/2;
            int PinY = getY() + getHeight()/2;

            //PIN DE DIAMETRO
            Pines[0] = new Pin(this, PinX, PinY){
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point Pos = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(Pos, PnPrincipal.PnPrinc);
            
                    setBounds(Pos.x - PtOffset.x, Fr.getY() + Diametro/2, getWidth(), getHeight());
                    
                    Diametro = (Fr.getWidth() + getX()) - (Fr.getX() + Fr.getWidth());
                    
                    if(Fr.Grp != null)
                        Fr.Grp.ActualizarBordes();
                    ActualizarPines();
                }
            };

            //PIN PARA MODIFICAR SECTOR
            PinX = getX() + getWidth()/2 + Math.round((float)(Diametro/4*Math.cos(Sector.start)));

            Pines[1] = new Pin(this, PinX, PinY){
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point Pos = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(Pos, PnPrincipal.PnPrinc);
        
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
                    float angInv = Main.angulo(0f, 0f,VectDir.x,-VectDir.y);

                    /* Restar el area del sector del circulo cuando se ajuste el arco
                     * EXTFIN = EXTACT - EL DOBLE DEL AREA SOMBREADA REMOVIDA
                     */
                    Sector.extent = Sector.extent - 2*(Math.toDegrees(angInv) - Sector.start);

                    //AJUSTAR NUEVO ANGULO INICIAL
                    Sector.start = Math.toDegrees(angInv);

                    ActualizarPines();
                    Fr.repaint();
                }
            };


            //PIN PARA AJUSTAR LA ROTACION DEL CIRCULO
            Pines[2] = new Pin(this, PinX, PinY){
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point Pos = e.getLocationOnScreen();
                    SwingUtilities.convertPointFromScreen(Pos, PnPrincipal.PnPrinc);
        
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
                    float angInv = Main.angulo(0f, 0f,VectDir.x,-VectDir.y);

                    //AJUSTAR NUEVO ANGULO INICIAL
                    Sector.start = Math.toDegrees(angInv);

                    ActualizarPines();
                    Fr.repaint();
                }
            };

            //AGREGAR PINES
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
