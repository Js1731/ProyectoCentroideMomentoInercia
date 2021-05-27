package PryMecanica.plano.objetos;

import PryMecanica.Arrastrable;
import PryMecanica.PnPlano;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**Define un objeto generico con coordenadas relativas al Origen */
public abstract class Objeto2D extends Arrastrable{
    /**Coordenada X relativa al origen */
    public int X = 0;

    /**Coordenada Y relativa al origen */
    public int Y = 0;

    /**Lista de las coordenadas X a las que la figura puede ajustarse */
    public ArrayList<Integer> SnapXs = new ArrayList<Integer>(); 
    
    /**Lista de las coordenadas Y a las que la figura puede ajustarse */
    public ArrayList<Integer> SnapYs = new ArrayList<Integer>(); 

    public String Nombre = "Nuebo";

    /**Escala de unidad : Pixel */
    public static int Escala = 50;

    /**Ajustar argumento al valor mas cercano dentro de la lista de valores 
     * @param c Valor
     * @param lst Lista de Valores
     * @return Valor Ajustado
     */
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

        //BUSCAR COORDENADAS PARA AJUSTARSE
        SnapXs.removeAll(SnapXs);
        SnapYs.removeAll(SnapYs);

        SnapXs.add(Math.round(PnPlano.PtOrigen.x));
        SnapYs.add(Math.round(PnPlano.PtOrigen.y));

        for (Objeto2D obj : PnPlano.PlPrinc.LstObjetos) {
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

        PnPlano.PlPrinc.notificarCambios(1);
    }
}
