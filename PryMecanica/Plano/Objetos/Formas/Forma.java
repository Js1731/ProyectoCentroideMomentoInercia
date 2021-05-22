package PryMecanica.Plano.Objetos.Formas;

import java.awt.event.MouseEvent;

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



    public Forma(){
        PnPrincipal.PnPrinc.LstObjetos.add(this);
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
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        if(Grp != null)
            Grp.ActualizarBordes();
        ActualizarCoordenadas();
    }
}
