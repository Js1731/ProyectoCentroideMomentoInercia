package PryMecanica.Plano.Objetos.Formas;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import PryMecanica.PnPrincipal;
import PryMecanica.Plano.Objetos.Grupo;
import PryMecanica.Plano.Objetos.Objeto2D;
import PryMecanica.Plano.Objetos.Pin;

/**Define una forma generica que se puede arrastrar y deformar usando un conjuntos de {@link Pin}
*/
public abstract class Forma extends Objeto2D{
    
    /**Hace referencia al grupo al que pertenece la figura (Si pertenece a uno) */
    public Grupo Grp;

    /**Conjunto de pines para deformar esta forma */
    public Pin[] Pines;

    public ArrayList<Integer> SnapXs = new ArrayList<Integer>(); 
    public ArrayList<Integer> SnapYs = new ArrayList<Integer>(); 

    public Forma(){
        PnPrincipal.PnPrinc.LstObjetos.add(this);
        setBackground(Color.LIGHT_GRAY);
    }



    /**Calcula la coordenada X del centroide (Es local a la forma)*/
    public abstract float centroideX();

    /**Calcula la coordenada Y del centroide (Es local a la forma)*/
    public abstract float centroideY();

    public abstract float calcularArea();

    /**
     * Actualiza la posicion de los pines de la figura
     */
    public abstract void ActualizarPines();

    /** 
     * Elimina todos los pines activos de la figura
    */
    public void eliminarPines(){
        if(Pines[0] != null){
            for (int i = 0; i < Pines.length; i++) {
                PnPrincipal.PnPrinc.remove(Pines[i]);
                Pines[i] = null;
                
            }
            PnPrincipal.PnPrinc.repaint();
        }
    }

    public int snap(int c, ArrayList<Integer> lst){
        for (int snap : lst) {
            if(Math.abs(c - snap) < 30){
                return snap;
            }
        }
        return c;
    }


    
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        //SELECCIONA LA FORMA
        PnPrincipal.PnPrinc.moveToFront(this);

        if(Pines[0] != null){
            for (Pin pin : Pines) 
                PnPrincipal.PnPrinc.moveToFront(pin);
        }

        if(PnPrincipal.PnPrinc.FrSel != this)
            PnPrincipal.PnPrinc.SelForma(this);

        //BUSCAR X PARA AJUSTARSE
        SnapXs.removeAll(SnapXs);
        SnapYs.removeAll(SnapYs);
        for (Objeto2D obj : PnPrincipal.PnPrinc.LstObjetos) {
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

        if(Grp != null)
            Grp.ActualizarBordes();
        ActualizarCoordenadas();

        setLocation(snap(getX(), SnapXs), snap(getY(), SnapYs));
        setLocation(snap(getX() + getWidth(), SnapXs) - getWidth(),
                    snap(getY() + getHeight(), SnapYs) - getHeight());
    }
}
