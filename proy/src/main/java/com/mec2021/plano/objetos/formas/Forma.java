package com.mec2021.plano.objetos.formas;

import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import com.mec2021.gui.ListaOpciones;
import com.mec2021.gui.Opcion;
import com.mec2021.gui.PnPlano;
import com.mec2021.gui.propiedades.PropCirc;
import com.mec2021.gui.propiedades.PropRect;
import com.mec2021.gui.propiedades.PropTria;
import com.mec2021.plano.objetos.Grupo;
import com.mec2021.plano.objetos.Objeto2D;
import com.mec2021.plano.objetos.Pin;

/**Define una forma generica que se puede arrastrar y deformar usando un conjuntos de {@link Pin}
*/
public abstract class Forma extends Objeto2D{
    
    /**Contador de formas creadas */
    public static int FrCount = 0;

    /**Color de la forma */
    protected Color ColForma;

    /**Hace referencia al grupo al que pertenece la figura (Si pertenece a uno) */
    public Grupo Grp;

    /**Conjunto de pines para deformar esta forma */
    public Pin[] Pines;

    public boolean Hueco = false;

    

    public Forma(PnPlano plano){
        super(plano);

        Plano = plano;
        FrCount++;

        //ESCOGE UN COLOR PARA LA FIGURA
        int Val = 230 - Math.round((float)(40*(Math.cos(Math.toRadians(FrCount*60)) + 1)/2));
        ColForma = new Color(Val, Val, Val);

        Plano.LstObjetos.add(this);
    }

    /**Calcula el area de esta forma, si la figura esta hueca el area sera negativa */
    public abstract float calcularArea();

    /**Calcula la coordenada X del centroide (Es local a la forma)*/
    public abstract float centroideX();

    /**Calcula la coordenada Y del centroide (Es local a la forma)*/
    public abstract float centroideY();


    /**Calcula la inercia con respecto al centroide X para esta Forma 
     *<p>Si la forma no pertence a un grupo, no se podra calcular la inercia y se retornara 0.
    */
    public abstract float inerciaCentEjeX();
    
    /**Calcula la inercia con respecto al centroide Y para esta Forma
     * <p>Si la forma no pertence a un grupo, no se podra calcular la inercia y se retornara 0.
     */
    public abstract float inerciaCentEjeY();

    /**
     * Actualiza la posicion de los pines de la figura
     */
    public abstract void actualizarPines();

    /**
     * Mostrar los Pines de la Forma
     */
    public abstract void mostrarPines();

    /** 
     * Elimina todos los pines activos de la figura
    */
    public void eliminarPines(){
        if(Pines[0] != null){
            for (int i = 0; i < Pines.length; i++) {
                Plano.remove(Pines[i]);
                Pines[i] = null;
                
            }
            Plano.repaint();
        }
    }



    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        actualizarCoordenadas();

        //ABRIR PANEL DE PROPIEDADES
        if(e.getClickCount() > 1){
            //ELIMINAR OTROS PANELES ACTIVOS
            if(Plano.PnPropActual != null){
                Plano.remove(Plano.PnPropActual);
                Plano.PnPropActual = null;
                Plano.repaint();
            }
        
            //ABRIR EL PANEL DE PROPIEDADES DE LA FORMA
            if(this instanceof FrRect){
                Plano.add(Plano.PnPropActual = new PropRect(this, Plano), JLayeredPane.DRAG_LAYER);
            }else if(this instanceof FrCirc){
                Plano.add(Plano.PnPropActual = new PropCirc(this, Plano), JLayeredPane.DRAG_LAYER);
            }else if(this instanceof FrTria){
                Plano.add(Plano.PnPropActual = new PropTria(this, Plano), JLayeredPane.DRAG_LAYER);
            }

            Plano.moveToFront(Plano.PnPropActual);
        }
        

        if(Plano.FrSel != this)
            Plano.seleccionarForma(this);

        //CREA UN MENU CONTEXTUAL PARA LA FORMA
        if(SwingUtilities.isRightMouseButton(e)){
            ListaOpciones Lo = new ListaOpciones(getX() + e.getX(), getY() +  e.getY(), Plano);

            Forma Fr = this;
            //OPCION PARA ELIMINAR LA FORMA
            Opcion OpEliminar = new Opcion("Eliminar"){
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);

                    Plano.eliminarForma(Fr);
                    Plano.remove(Lo);
                }
            };

            //OPCION PARA ENVIAR LA FORMA AL FRENTE DEL PLANO
            Opcion OpEnvFrente = new Opcion("Enviar al Frente"){
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);

                    Plano.moveToFront(Fr);
                    Plano.remove(Lo);
                }
            };

            //OPCION PARA ENVIAR LA FORMA DETRAS DE TODAS LAS FORMAS DEL PLANO
            Opcion OpEnvFondo = new Opcion("Enviar al Fondo"){
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);

                    Plano.moveToBack(Fr);
                    Plano.remove(Lo);
                }
            };

            Lo.agregarOpcion(OpEliminar);
            Lo.agregarOpcion(OpEnvFrente);
            Lo.agregarOpcion(OpEnvFondo);

            Plano.add(Lo, JLayeredPane.DRAG_LAYER);
            Plano.moveToFront(Lo);
            Lo.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        if(Grp != null)
            Grp.ActualizarBordes();
        actualizarCoordenadas();
    }
}
