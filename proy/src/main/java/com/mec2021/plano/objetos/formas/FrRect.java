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
    
    public float Ancho = 50;
    public float Alto = 50;

    public static int ID = 0;

    /**
     * Crea un rectangulo de 50x50 en el origen {@code (0, 0)}
     */
    public FrRect(PnPlano plano){
        this(0,-5,5, 5, plano);
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
        Ancho = an;
        Alto = al;

        Nombre = "Rectangulo " + (ID++);
        Plano.notificarCambios(0);


        Pines = new Pin[4];
        setOpaque(false);

        setBounds(Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaLnInv(x)), 
                  Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaLnInv(y)), 
                  Math.round(Ctrl.aplicarEscalaLnInv(an)), 
                  Math.round(Ctrl.aplicarEscalaLnInv(al)));
        setBackground(Color.DARK_GRAY);
        ActualizarCoordenadas();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = Math.round(centroideX());
        int y = Math.round(centroideY());
        
        g.setColor(ColFig);
        
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
    public void actualizarEscala(){

        float NuevaEsc = ( (float)Plano.EscalaPix/Plano.Escala) * Plano.EscalaVieja;

        setBounds(Math.round( Plano.PtOrigen.x + X * NuevaEsc),
                  Math.round( Plano.PtOrigen.y + Y* NuevaEsc), 
                  Math.round(getWidth()* NuevaEsc), 
                  Math.round(getHeight()* NuevaEsc));
        ActualizarCoordenadas();
        ActualizarPines();
        repaint();
    }


    @Override
    public void ActualizarPines(){
        if(Pines[0] != null){
            Pines[0].setLocation(getX() - 15, getY() - 15);
            Pines[1].setLocation(getX() - 15, getY() + getHeight() + 15);
            Pines[2].setLocation(getX() + getWidth() + 15, getY() - 15);
            Pines[3].setLocation(getX() + getWidth() + 15, getY() + getHeight() + 15);

            Plano.repaint();

            Ancho = Ctrl.aplicarEscalaLn(getWidth());
            Alto = Ctrl.aplicarEscalaLn(getHeight());
        }
    }

    @Override
    public void ActualizarCoordenadas() {
        X = Math.round(getX() - Plano.PtOrigen.x);
        Y = Math.round(getY() - Plano.PtOrigen.y);

        Ancho = Ctrl.aplicarEscalaLn(getWidth());
        Alto = Ctrl.aplicarEscalaLn(getHeight());
    }

    
    @Override
    public float calcularArea() {
        return (Hueco ? -1 : 1)*Ctrl.aplicarEscalaLnInv(Ancho)*Ctrl.aplicarEscalaLnInv(Alto);
    }

    @Override
    public float inerciaCentEjeX(){
        float an = Ancho*((float)PnPlano.Escala/PnPlano.EscalaPix);
        float al = Alto*((float)PnPlano.Escala/PnPlano.EscalaPix);

        return (an*(al*al*al))/12;
    }

    @Override
    public float inerciaCentEjeY(){
        float an = Ancho*((float)PnPlano.Escala/PnPlano.EscalaPix);
        float al = Alto*((float)PnPlano.Escala/PnPlano.EscalaPix);

        return (al*(an*an*an))/12;
    }

    @Override
    public float centroideX() {
        return Ctrl.aplicarEscalaLnInv(Ancho)/2;
    }

    @Override
    public float centroideY() {
        return Ctrl.aplicarEscalaLnInv(Alto)/2;
    }



    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        

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
                    Fr.ActualizarPines();
                    Fr.ActualizarCoordenadas();
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
                    Fr.ActualizarPines();
                    Fr.ActualizarCoordenadas();
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
                    Fr.ActualizarPines();
                    Fr.ActualizarCoordenadas();
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
                    Fr.ActualizarPines();
                    Fr.ActualizarCoordenadas();
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
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        ActualizarPines();
    }
}
