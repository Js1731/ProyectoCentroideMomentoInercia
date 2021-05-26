package PryMecanica.Plano.Objetos.Formas;

import java.awt.Color;
import java.awt.event.MouseEvent;

import PryMecanica.PnPlano;
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

    public boolean Hueco = false;

    public Forma(){
        PnPlano.PlPrinc.LstObjetos.add(this);

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
                PnPlano.PlPrinc.remove(Pines[i]);
                Pines[i] = null;
                
            }
            PnPlano.PlPrinc.repaint();
        }
    }



    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        requestFocus();

        //SELECCIONA LA FORMA
        PnPlano.PlPrinc.moveToFront(this);

        if(Pines[0] != null){
            for (Pin pin : Pines) 
                PnPlano.PlPrinc.moveToFront(pin);
        }

        if(PnPlano.PlPrinc.FrSel != this)
            PnPlano.PlPrinc.SelForma(this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        if(Grp != null)
            Grp.ActualizarBordes();
        ActualizarCoordenadas();
    }
}
