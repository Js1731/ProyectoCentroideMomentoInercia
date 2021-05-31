package com.mec2021.plano.objetos.formas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;

import com.mec2021.plano.objetos.Pin;
import com.mec2021.PnPlano;

/**
 * Rectangulo al que se le puede editar su ancho y altura
 */
public class FrRect extends Forma{
    
    public int Ancho = 50;
    public int Alto = 50;

    public static int ID = 0;

    /**
     * Crea un rectangulo de 50x50 en el origen {@code (0, 0)}
     */
    public FrRect(){
        this(0,-5,5, 5);
    }

    /**
     * Crea un rectangulo en una posicion y tamaño definido
     * @param x X
     * @param y Y
     * @param an Ancho
     * @param al Alto
     */
    public FrRect(float x, float y, float an, float al){
        Ancho = Math.round(an*1/PnPlano.Escala*Escala);
        Alto = Math.round(al*1/PnPlano.Escala*Escala);

        Nombre = "Rectangulo " + (ID++);
        PnPlano.PlPrinc.notificarCambios(0);

        Pines = new Pin[4];

        setOpaque(false);


        
        setBounds(Math.round(PnPlano.PtOrigen.x) + Math.round(x*1/PnPlano.Escala*Escala), Math.round(PnPlano.PtOrigen.y) + Math.round(y*1/PnPlano.Escala*Escala), Ancho, Alto);
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
    public void ActualizarPines(){
        if(Pines[0] != null){
            Pines[0].setLocation(getX() - 15, getY() - 15);
            Pines[1].setLocation(getX() - 15, getY() + getHeight() + 15);
            Pines[2].setLocation(getX() + getWidth() + 15, getY() - 15);
            Pines[3].setLocation(getX() + getWidth() + 15, getY() + getHeight() + 15);

            PnPlano.PlPrinc.repaint();

            Ancho = getWidth();
            Alto = getHeight();
        }
    }

    @Override
    public void ActualizarCoordenadas() {
        X = Math.round(getX() - PnPlano.PtOrigen.x);
        Y = Math.round(getY() - PnPlano.PtOrigen.y);

        Ancho = getWidth();
        Alto = getHeight();
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
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        

        //CREAR PINES DE DEFORMACION
        if(Pines[0] == null){

            //PIN PARA LA ESQUINA SUPERIOR IZQUIERDA
            Pines[0] = new Pin(this, getX() - 15, getY() - 15){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    //AJUSTAR A OTROS OBJETOS
                    setLocation(snap(getX() + 15, SnapXs) - 15, snap(getY() + 15, SnapYs) - 15);

                    int DifX = getX() + 15 - Fr.getX();
                    int DifY = getY() + 15 - Fr.getY();
                    
                    Fr.setBounds(getX() + 15, getY() + 15, Fr.getWidth() - DifX, Fr.getHeight() - DifY);
                    Fr.ActualizarPines();
                    Fr.ActualizarCoordenadas();
                }
            };

            //PIN PARA LA ESQUINA INFERIOR IZQUIERDA
            Pines[1] = new Pin(this, getX() - 15, getY() + getHeight() + 15){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    //AJUSTAR A OTROS OBJETOS
                    setLocation(snap(getX() + 15, SnapXs) - 15, snap(getY() + 15, SnapYs) - 15);

                    int DifX = getX() + 15 - Fr.getX();
                    int DifY = (getY() - 15) - (Fr.getY() + Fr.getHeight());
                    
                    Fr.setBounds(getX() + 15, Fr.getY(), Fr.getWidth() - DifX, Fr.getHeight() + DifY);
                    Fr.ActualizarPines();
                    Fr.ActualizarCoordenadas();
                }
            };

            //PIN PARA LA ESQUINA SUPERIOR DERECHA
            Pines[2] = new Pin(this, getX() + getWidth() + 15, getY() - 15){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    //AJUSTAR A OTROS OBJETOS
                    setLocation(snap(getX() - 15, SnapXs) + 15, snap(getY() - 15, SnapYs) + 15);

                    int DifX = (getX() - 15) - (Fr.getX() + Fr.getWidth());
                    int DifY = getY() + 15 - Fr.getY();
                    
                    Fr.setBounds(Fr.getX(), getY() + 15, Fr.getWidth() + DifX, Fr.getHeight() - DifY);
                    Fr.ActualizarPines();
                    Fr.ActualizarCoordenadas();
                }
            };

            //PIN PARA LA ESQUINA INFERIOR DERECHA
            Pines[3] = new Pin(this, getX() + getWidth() + 15, getY() + getHeight() + 15){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    //AJUSTAR A OTROS OBJETOS
                    setLocation(snap(getX() - 15, SnapXs) + 15, snap(getY() - 15, SnapYs) + 15);

                    int DifX = (getX() - 15) - (Fr.getX() + Fr.getWidth());
                    int DifY = (getY() - 15) - (Fr.getY() + Fr.getHeight());
                    
                    Fr.setBounds(Fr.getX(), Fr.getY(), Fr.getWidth() + DifX, Fr.getHeight() + DifY);
                    Fr.ActualizarPines();
                    Fr.ActualizarCoordenadas();
                }
            };

            //AGREGAR PINES AL PANEL PRINCIPAL
            for (Pin pin : Pines) {
                PnPlano.PlPrinc.add(pin, JLayeredPane.DRAG_LAYER);
                PnPlano.PlPrinc.moveToFront(pin);
            }

            PnPlano.PlPrinc.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        ActualizarPines();
    }
}