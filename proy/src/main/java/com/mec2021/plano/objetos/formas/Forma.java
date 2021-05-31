package com.mec2021.plano.objetos.formas;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import com.mec2021.gui.ListaOpciones;
import com.mec2021.gui.Opcion;
import com.mec2021.plano.objetos.Grupo;
import com.mec2021.plano.objetos.Objeto2D;
import com.mec2021.plano.objetos.Pin;
import com.mec2021.PnPlano;

/**Define una forma generica que se puede arrastrar y deformar usando un conjuntos de {@link Pin}
*/
public abstract class Forma extends Objeto2D{
    
    protected Color ColFig;

    /**Hace referencia al grupo al que pertenece la figura (Si pertenece a uno) */
    public Grupo Grp;

    /**Conjunto de pines para deformar esta forma */
    public Pin[] Pines;

    public boolean Hueco = false;

    public Forma(){
        int Val = 220 - (new Random()).nextInt(90);
        ColFig = new Color(Val, Val, Val);

        PnPlano.PlPrinc.LstObjetos.add(this);

        setBackground(Color.LIGHT_GRAY);
    }



    /**Calcula la coordenada X del centroide (Es local a la forma)*/
    public abstract float centroideX();

    /**Calcula la coordenada Y del centroide (Es local a la forma)*/
    public abstract float centroideY();

    /**Calcula el area de esta forma, si la figura esta hueca el area sera negativa */
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
            PnPlano.PlPrinc.seleccionarForma(this);

        if(SwingUtilities.isRightMouseButton(e)){
            ListaOpciones Lo = new ListaOpciones(getX() + e.getX(), getY() +  e.getY());

            Forma Fr = this;
            Opcion Op = new Opcion("Eliminar"){
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);

                    PnPlano.PlPrinc.eliminarForma(Fr);
                    PnPlano.PlPrinc.remove(Lo);
                }
            };

            Lo.agregarOpcion(Op);

            PnPlano.PlPrinc.add(Lo, JLayeredPane.DRAG_LAYER);
            PnPlano.PlPrinc.moveToFront(Lo);
            Lo.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        if(Grp != null)
            Grp.ActualizarBordes();
        ActualizarCoordenadas();
    }
}
