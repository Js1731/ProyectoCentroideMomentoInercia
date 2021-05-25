package PryMecanica.Plano.Objetos;

import PryMecanica.Arrastrable;
import PryMecanica.PnPlano;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**Define un objeto generico con coordenadas relativas al Origen */
public abstract class Objeto2D extends Arrastrable{
    /**Coordenada X relativa al origen */
    public int X = 0;

    public ArrayList<Integer> SnapXs = new ArrayList<Integer>(); 
    public ArrayList<Integer> SnapYs = new ArrayList<Integer>(); 

    /**Coordenada Y relativa al origen */
    public int Y = 0;

    public static int Escala = 40;

    public int snap(int c, ArrayList<Integer> lst){
        for (int snap : lst) {
            if(Math.abs(c - snap) < 30){
                return snap;
            }
        }
        return c;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /**Actualiza las coordenadas de la figura */
    public abstract void ActualizarCoordenadas();

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        //BUSCAR X PARA AJUSTARSE
        SnapXs.removeAll(SnapXs);
        SnapYs.removeAll(SnapYs);

        SnapXs.add(Math.round(PnPlano.PtOrigen.x));
        SnapYs.add(Math.round(PnPlano.PtOrigen.y));

        for (Objeto2D obj : PnPlano.PnPrinc.LstObjetos) {
            if(obj != this){
                SnapXs.add(obj.getX());
                SnapXs.add(obj.getX() + obj.getWidth());

                SnapYs.add(obj.getY());
                SnapYs.add(obj.getY() + obj.getHeight());
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        setLocation(snap(getX(), SnapXs), snap(getY(), SnapYs));
        setLocation(snap(getX() + getWidth(), SnapXs) - getWidth(),
                    snap(getY() + getHeight(), SnapYs) - getHeight());
    }
}
