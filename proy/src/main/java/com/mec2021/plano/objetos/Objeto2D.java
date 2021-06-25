package com.mec2021.plano.objetos;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.mec2021.Arrastrable;
import com.mec2021.gui.PnPlano;

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

    protected final static int EjeX = 0;
    protected final static int EjeY = 0;

    /**Ajustar argumento al valor mas cercano dentro de la lista de valores 
     * @param c Valor
     * @param lst Lista de Valores
     * @param Eje Eje donde se va a ajustar (0 : X, 1 : Y)
     * @return Valor Ajustado
     */

    private int snap(int c, ArrayList<Integer> lst){
        for (int snap : lst) {
            if(Math.abs(c - snap) < 30){
                return snap;
            }
        }
        return c;
    }

    public int snapX(int c){
        int snp = snap(c, SnapXs);

        if(snp != c){
            Plano.SnapActivoX = true;
            Plano.SnapX = snp;
            Plano.repaint();
        }
        
        return snp;
    }
    
    
    public int snapY(int c){
        int snp = snap(c, SnapYs);
        
        if(snp != c){
            Plano.SnapActivoY = true;
            Plano.SnapY = snp;
            Plano.repaint();
        }

        return snp;
    }

    public Objeto2D(PnPlano plano){
        super(plano);
        Plano = plano;
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

        SnapXs.add(Math.round(Plano.PtOrigen.x));
        SnapYs.add(Math.round(Plano.PtOrigen.y));

        for (Objeto2D obj : Plano.LstObjetos) {
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

        setLocation(snapX(getX()), snapY(getY()));
        setLocation(snapX(getX() + getWidth()) - getWidth(),
                    snapY(getY() + getHeight()) - getHeight());

        Plano.notificarCambios(1);
        Plano.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);

        Plano.repaint();
    }
}
