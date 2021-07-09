package com.mec2021.plano.objetos;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.mec2021.Arrastrable;
import com.mec2021.EstructIndex.EstructuraInd;
import com.mec2021.EstructIndex.SeccionEI;
import com.mec2021.gui.PnPlano;
import com.mec2021.plano.objetos.formas.Forma;
import com.mec2021.plano.objetos.formas.FrCirc;
import com.mec2021.plano.objetos.formas.FrRect;
import com.mec2021.plano.objetos.formas.FrTria;

/**Define un objeto generico con coordenadas relativas al Origen que se puede arrastrar*/
public abstract class Objeto2D extends Arrastrable{
    /**Coordenada X relativa al origen */
    public float X = 0;

    /**Coordenada Y relativa al origen */
    public float Y = 0;

    /**Lista de las coordenadas X a las que la figura puede ajustarse */
    public ArrayList<Integer> SnapXs = new ArrayList<Integer>(); 
    
    /**Lista de las coordenadas Y a las que la figura puede ajustarse */
    public ArrayList<Integer> SnapYs = new ArrayList<Integer>(); 

    public String Nombre = "Nuebo";

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

    /**
     * Ajusta un valor de X a la X mas cercana de un objeto dentro del plano
     * @param c
     * @return
     */
    public int snapX(int c){
        int snp = snap(c, SnapXs);

        //SI SE AJUSTA A UN VALOR, INDICAR LA POSICION DEL AJUSTE EN EL PLANO
        if(snp != c){
            Plano.SnapActivoX = true;
            Plano.SnapX = snp;
            Plano.repaint();
        }
        
        return snp;
    }
    
    
    /**
     * Ajusta un valor de Y a la Y mas cercana de un objeto dentro del plano
     * @param c
     * @return
     */
    public int snapY(int c){
        int snp = snap(c, SnapYs);
        
        //SI SE AJUSTA A UN VALOR, INDICAR LA POSICION DEL AJUSTE EN EL PLANO
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

    /**Actualiza la posicion y tamano de la forma a la escala actual del lienzo */
    public abstract void actualizarDimensiones();

    /**Actualiza las coordenadas de la figura */
    public abstract void actualizarCoordenadas();

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

    public String generarDataString(){
        return EstructuraInd.transformarEstrString(generarData());
    }

    public SeccionEI generarData(){
        SeccionEI EstData = EstructuraInd.generarEstructuraIndVacia(Nombre);
        EstData.agregarHoja("t", (this instanceof FrRect) ? "r" : (this instanceof FrCirc) ? "c" : (this instanceof FrTria) ? "t" : "g");


        if(this instanceof FrRect){
            FrRect FrR = (FrRect)this;
            EstData.agregarHoja("x", "" + X);
            EstData.agregarHoja("y", "" + Y);

            EstData.agregarHoja("w", "" + FrR.Ancho);
            EstData.agregarHoja("h", "" + FrR.Alto);
            EstData.agregarHoja("u", "" + (FrR.Hueco ? 1:0));
        }else if(this instanceof FrTria){
            FrTria FrT = (FrTria)this;

            EstData.agregarHoja("x", "" + X);
            EstData.agregarHoja("y", "" + Y);

            //VERTICE 1
            SeccionEI V1 = EstData.agregarSeccion("a");
            V1.agregarHoja("x",""+ FrT.Ver1.x);
            V1.agregarHoja("y",""+ FrT.Ver1.y);

            SeccionEI V2 = EstData.agregarSeccion("b");
            V2.agregarHoja("x",""+ FrT.Ver2.x);
            V2.agregarHoja("y",""+ FrT.Ver2.y);

            SeccionEI V3 = EstData.agregarSeccion("c");
            V3.agregarHoja("x",""+ FrT.Ver3.x);
            V3.agregarHoja("y",""+ FrT.Ver3.y);

            EstData.agregarHoja("u", "" + (FrT.Hueco ? 1:0));

            
        }else if(this instanceof FrCirc){
            FrCirc FrC = (FrCirc)this;

            EstData.agregarHoja("x", "" + X);
            EstData.agregarHoja("y", "" + Y);

            EstData.agregarHoja("r","" + FrC.Radio);
            EstData.agregarHoja("a","" + FrC.Sector.start);
            EstData.agregarHoja("e","" + FrC.Sector.extent);

            EstData.agregarHoja("u", "" + (FrC.Hueco ? 1:0));
        }else{
            Grupo Gp = (Grupo)this;

            for (Forma fr : Gp.LstForma) {
                EstData.agregarSeccion(fr.generarData());
            }
        }

        return EstData;
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
