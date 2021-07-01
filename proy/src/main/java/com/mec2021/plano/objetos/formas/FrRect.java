package com.mec2021.plano.objetos.formas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;

import com.mec2021.Ctrl;
import com.mec2021.gui.PnPlano;
import com.mec2021.plano.objetos.Pin;

/**
 * Rectangulo al que se le puede editar su ancho y altura
 */
public class FrRect extends Forma{

    /**Ancho del rectangulo */
    public float Ancho = 50;

    /**Alto del rectangulo */
    public float Alto = 50;

    /**ID unico de cada rectangulo */
    public static int ID = 0;

    /**
     * Crea un rectangulo de 50x50 en el origen {@code (0, 0)}
     */
    public FrRect(PnPlano plano){
        this(0*plano.Escala,-5*plano.Escala,5*plano.Escala, 5*plano.Escala, plano);
    }

    /**
     * Crea un rectangulo en una posicion y tamaño definido
     * @param x X
     * @param y Y
     * @param an Ancho
     * @param al Alto
     */
    public FrRect(float x, float y, float an, float al, PnPlano plano){
        super(plano);

        X = x;
        Y = y;
        Ancho = an;
        Alto = al;

        Nombre = "Rectangulo " + (ID++);
        
        Pines = new Pin[4];
        
        setBounds(Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(x)), 
                  Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(y)), 
                  Math.round(Ctrl.aplicarEscalaUPix(an)), 
                  Math.round(Ctrl.aplicarEscalaUPix(al)));
        
        setBackground(Color.DARK_GRAY);
        setOpaque(false);

        Plano.notificarCambios(0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //POSICION DE LA FORMA EN EL PLANO
        int x = Math.round(Ctrl.aplicarEscalaUPix(centroideX()));
        int y = Math.round(Ctrl.aplicarEscalaUPix(centroideY()));
        
        g.setColor(ColForma);
        
        if(Hueco){
            g.setColor(Color.WHITE);
            g.fillRect(0,0,getWidth(),getHeight());
            g.setColor(Color.DARK_GRAY);
            g.drawRect(0,0,getWidth()-1,getHeight()-1);
        }else
            g.fillRect(0,0,getWidth(),getHeight());

        g.setColor(Color.RED);


        g.fillOval(x-3,y-3,6,6);
    }


    @Override
    public void actualizarDimensiones(){

        setBounds(Math.round( Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(X)),
                  Math.round( Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(Y)), 
                  Math.round(Ctrl.aplicarEscalaUPix(Ancho)), 
                  Math.round(Ctrl.aplicarEscalaUPix(Alto)));

        actualizarPines();
        repaint();
    }

    

    @Override
    public void actualizarCoordenadas() {
        X = Ctrl.aplicarEscalaLnPixU(getX() - Plano.PtOrigen.x);
        Y = Ctrl.aplicarEscalaLnPixU(getY() - Plano.PtOrigen.y);

        Ancho = Ctrl.aplicarEscalaLnPixU(getWidth());
        Alto = Ctrl.aplicarEscalaLnPixU(getHeight());
    }

    
    @Override
    public float calcularArea() {
        return (Hueco ? -1 : 1)*Ancho*Alto;
    }

    @Override
    public float centroideX() {
        return Ancho/2;
    }

    @Override
    public float centroideY() {
        return Alto/2;
    }

    @Override
    public float inerciaCentEjeX(){

        if(Grp != null){
            float ix = (Ancho*(Alto*Alto*Alto))/12f;
            float dy = (-Y - centroideY()) - (Ctrl.aplicarEscalaLnPixU(Plano.PtOrigen.y - Grp.getY()) - Grp.centroideY());

            float Ix = ix + Math.abs(calcularArea())*dy*dy;

            return (Hueco ? -1 : 1)*Ix;
        }else{
            return 0;
        }
    }

    @Override
    public float inerciaCentEjeY(){

        if(Grp != null){
            float iy = (Alto*(Ancho*Ancho*Ancho))/12;
            float dx = (X + centroideX()) - Grp.centroideX();

            float Iy = iy + Math.abs(calcularArea())*dx*dx;

            return (Hueco ? -1 : 1)*Iy;
        }else{
            return 0;
        }
    }

    @Override
    public void mostrarPines(){
        //CREAR PINES DE DEFORMACION
        if(Pines[0] == null){

            //PIN PARA LA ESQUINA SUPERIOR IZQUIERDA
            Pines[0] = new Pin(this, getX() - 15, getY() - 15, Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    //AJUSTAR A OTROS OBJETOS
                    setLocation(snapX(getX() + 15) - 15, snapY(getY() + 15) - 15);

                    int DifX = getX() + 15 - Fr.getX();
                    int DifY = getY() + 15 - Fr.getY();
                    
                    Fr.setBounds(getX() + 15, getY() + 15, Fr.getWidth() - DifX, Fr.getHeight() - DifY);
                    Fr.actualizarPines();
                    Fr.actualizarCoordenadas();
                }
            };

            //PIN PARA LA ESQUINA INFERIOR IZQUIERDA
            Pines[1] = new Pin(this, getX() - 15, getY() + getHeight() + 15, Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    //AJUSTAR A OTROS OBJETOS
                    setLocation(snapX(getX() + 15) - 15, snapY(getY() + 15) - 15);

                    int DifX = getX() + 15 - Fr.getX();
                    int DifY = (getY() - 15) - (Fr.getY() + Fr.getHeight());
                    
                    Fr.setBounds(getX() + 15, Fr.getY(), Fr.getWidth() - DifX, Fr.getHeight() + DifY);
                    Fr.actualizarPines();
                    Fr.actualizarCoordenadas();
                }
            };

            //PIN PARA LA ESQUINA SUPERIOR DERECHA
            Pines[2] = new Pin(this, getX() + getWidth() + 15, getY() - 15, Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    //AJUSTAR A OTROS OBJETOS
                    setLocation(snapX(getX() - 15) + 15, snapY(getY() + 15) - 15);

                    int DifX = (getX() - 15) - (Fr.getX() + Fr.getWidth());
                    int DifY = getY() + 15 - Fr.getY();
                    
                    Fr.setBounds(Fr.getX(), getY() + 15, Fr.getWidth() + DifX, Fr.getHeight() - DifY);
                    Fr.actualizarPines();
                    Fr.actualizarCoordenadas();
                }
            };

            //PIN PARA LA ESQUINA INFERIOR DERECHA
            Pines[3] = new Pin(this, getX() + getWidth() + 15, getY() + getHeight() + 15, Plano){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    //AJUSTAR A OTROS OBJETOS
                    setLocation(snapX(getX() - 15) + 15, snapY(getY() - 15) + 15);

                    int DifX = (getX() - 15) - (Fr.getX() + Fr.getWidth());
                    int DifY = (getY() - 15) - (Fr.getY() + Fr.getHeight());
                    
                    Fr.setBounds(Fr.getX(), Fr.getY(), Fr.getWidth() + DifX, Fr.getHeight() + DifY);
                    Fr.actualizarPines();
                    Fr.actualizarCoordenadas();
                }
            };

            //AGREGAR PINES AL PANEL PRINCIPAL
            for (Pin pin : Pines) {
                Plano.add(pin, JLayeredPane.DRAG_LAYER);
                Plano.moveToFront(pin);
            }

            Plano.repaint();
        }
    }

    @Override
    public void actualizarPines(){
        if(Pines[0] != null){
            Pines[0].setLocation(Math.round(Ctrl.aplicarEscalaUPix(X) - 15 + Plano.PtOrigen.x), Math.round(Ctrl.aplicarEscalaUPix(Y) - 15 + Plano.PtOrigen.y));
            Pines[1].setLocation(Math.round(Ctrl.aplicarEscalaUPix(X) - 15 + Plano.PtOrigen.x), Math.round(Ctrl.aplicarEscalaUPix(Y) + getHeight() + 15 + Plano.PtOrigen.y));
            Pines[2].setLocation(Math.round(Ctrl.aplicarEscalaUPix(X) + getWidth() + 15 + Plano.PtOrigen.x), Math.round(Ctrl.aplicarEscalaUPix(Y) - 15 + Plano.PtOrigen.y));
            Pines[3].setLocation(Math.round(Ctrl.aplicarEscalaUPix(X) + getWidth() + 15 + Plano.PtOrigen.x),Math.round(Ctrl.aplicarEscalaUPix(Y) + getHeight() + 15 + Plano.PtOrigen.y));

            Plano.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        actualizarPines();
    }
}
