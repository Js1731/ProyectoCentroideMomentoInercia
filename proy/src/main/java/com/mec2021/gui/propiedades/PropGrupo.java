package com.mec2021.gui.propiedades;

import com.mec2021.plano.objetos.Grupo;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.mec2021.plano.objetos.Objeto2D;
import com.mec2021.plano.objetos.formas.Forma;
import com.mec2021.plano.objetos.formas.FrCirc;
import com.mec2021.plano.objetos.formas.FrRect;
import com.mec2021.plano.objetos.formas.FrTria;
import com.mec2021.PnPlano;

/**Panel de propiedades para un Grupo*/
public class PropGrupo extends PnPropiedades{

    private JTextField TFX = new JTextField();
    private JTextField TFY = new JTextField();

    private JLabel LbCentX = new JLabel("Centroide en X:");
    private JLabel LbCentY = new JLabel("Centroide en Y:");

    public PropGrupo(Objeto2D obj) {
        super(obj);

        //POSICION X
        JLabel LbX = new JLabel("X");
        LbX.setBounds(10, 15, 50, 10);

        TFX.setBounds(30, 10, 50, 20);
        TFX.addKeyListener(TextCont);
        
        //POSICION Y
        JLabel LbY = new JLabel("Y");
        LbY.setBounds(100, 15, 50, 10);
        
        TFY.setBounds(130, 10, 50, 20);
        TFY.addKeyListener(TextCont);

        //PROPIEDADES
        LbCentX.setBounds(10, 60, 200, 10);
        LbCentY.setBounds(10, 90, 200, 10);

        PnCont.add(LbX);
        PnCont.add(TFX);        
        PnCont.add(LbY);
        PnCont.add(TFY);
        PnCont.add(LbCentX);
        PnCont.add(LbCentY);

        actualizarDatos();
    }

    @Override
    public void actualizarDatos() {
        TFX.setText(""+PnPlano.Escala*(float)ObjRef.X/Forma.Escala);
        TFY.setText(""+PnPlano.Escala*(float)-ObjRef.Y/Forma.Escala);

        DecimalFormat f = new DecimalFormat("#0.00");

        LbCentX.setText("Centroide en x:     "+f.format(PnPlano.Escala*(float)(((Grupo)ObjRef).centroideX() + ((Grupo)ObjRef).X)/Forma.Escala));
        LbCentY.setText("Centroide en Y:     "+f.format(-PnPlano.Escala*(float)(((Grupo)ObjRef).centroideY() + ((Grupo)ObjRef).Y)/Forma.Escala));
    }

    @Override
    public void actualizarForma() {

        int ValX = Math.round(PnPlano.PtOrigen.x) +  Math.round(Float.parseFloat((TFX.getText().isEmpty() || TFX.getText().equals("-") ? "0" : TFX.getText()))*Forma.Escala)/PnPlano.Escala;
        int ValY = Math.round(PnPlano.PtOrigen.y) -  Math.round(Float.parseFloat((TFY.getText().isEmpty() || TFY.getText().equals("-") ? "0" : TFY.getText()))*Forma.Escala)/PnPlano.Escala;

        int DifX = ValX - Math.round(PnPlano.PtOrigen.x) - ObjRef.X;
        int DifY = ValY - Math.round(PnPlano.PtOrigen.y) - ObjRef.Y;

        ObjRef.setBounds(ValX, ValY, ObjRef.getWidth(), ObjRef.getHeight());

        //ACTUALIZAR POSICION DE TODAS LAS FORMAS DENTRO DEL GRUPO
        for (Objeto2D fr : ((Grupo)ObjRef).LstForma) {

            if(fr instanceof FrRect){
                FrRect Rec = (FrRect)fr;

                Rec.setBounds(Rec.getX() + DifX, 
                              Rec.getY() + DifY, 
                              Rec.getWidth(),
                              Rec.getHeight());

                Rec.ActualizarCoordenadas();
                Rec.ActualizarPines();
            }else if(fr instanceof FrCirc){
                FrCirc Circ = (FrCirc)fr;

                Circ.setBounds(Circ.getX() + DifX, 
                               Circ.getY() + DifY, 
                               getWidth(),
                               getHeight());

                Circ.ActualizarCoordenadas();
                Circ.ActualizarPines();
            }else if(fr instanceof FrTria){
                FrTria Tria = (FrTria)fr;

                Tria.setBounds(Tria.getX() + DifX, 
                               Tria.getY() + DifY, 
                               Tria.getWidth(), 
                               Tria.getHeight());
                
                Tria.moverVertices( DifX, DifY);

                Tria.Hueco = CBHueco.isSelected();

                Tria.ActualizarCoordenadas();
                Tria.ActualizarPines();
            }
            PnPlano.PlPrinc.repaint();
        }


    }
    
}