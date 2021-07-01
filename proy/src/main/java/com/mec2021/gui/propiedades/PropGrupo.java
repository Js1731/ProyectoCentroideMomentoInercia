package com.mec2021.gui.propiedades;

import com.mec2021.Ctrl;
import com.mec2021.gui.PnPlano;
import com.mec2021.plano.objetos.Grupo;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.mec2021.plano.objetos.Objeto2D;
import com.mec2021.plano.objetos.formas.FrCirc;
import com.mec2021.plano.objetos.formas.FrRect;
import com.mec2021.plano.objetos.formas.FrTria;

/**Panel de propiedades para un Grupo*/
public class PropGrupo extends PnPropiedades{

    private JTextField TFX = new JTextField();
    private JTextField TFY = new JTextField();

    private JLabel LbCentX = new JLabel("Centroide en X:");
    private JLabel LbCentY = new JLabel("Centroide en Y:");

    public PropGrupo(Objeto2D obj, PnPlano plano) {
        super(obj, plano);

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

        LbInX.setBounds(10, 130, 200, 10);
        LbInY.setBounds(10, 160, 200, 10);

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
        TFX.setText("" + ObjRef.X);
        TFY.setText("" + (-ObjRef.Y));

        DecimalFormat f = new DecimalFormat("#0.00");

        float a = ObjRef.getY();
        float b = Plano.PtOrigen.y;
        float c = Ctrl.aplicarEscalaLnPixU(b-a);
        float d = (((Grupo)ObjRef).centroideY());

        LbCentX.setText("Centroide en x:     "+f.format((((Grupo)ObjRef).centroideX())));
        LbCentY.setText("Centroide en Y:     "+f.format(c-d));

        LbInX.setText("Inercia en x:     " + f.format(((Grupo)ObjRef).inerciaCentEjeX()));
        LbInY.setText("Inercia en Y:     " + f.format(((Grupo)ObjRef).inerciaCentEjeY()));
    }

    @Override
    public void actualizarForma() {


        float PosX = Float.parseFloat((TFX.getText().isEmpty() || TFX.getText().equals("-") ? "0" : TFX.getText()));
        float PosY = -Float.parseFloat((TFY.getText().isEmpty() || TFY.getText().equals("-") ? "0" : TFY.getText()));

        int ValX = Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(PosX));
        int ValY = Math.round(Plano.PtOrigen.y + Ctrl.aplicarEscalaUPix(PosY));

        float DifX = PosX - ObjRef.X;
        float DifY = PosY - ObjRef.Y;

        ObjRef.setBounds(ValX, ValY, ObjRef.getWidth(), ObjRef.getHeight());

        //ACTUALIZAR POSICION DE TODAS LAS FORMAS DENTRO DEL GRUPO
        for (Objeto2D fr : ((Grupo)ObjRef).LstForma) {

            if(fr instanceof FrRect){
                FrRect Rec = (FrRect)fr;

                Rec.X += DifX;
                Rec.Y += DifY;

                Rec.actualizarDimensiones();
                Rec.actualizarPines();
            }else if(fr instanceof FrCirc){
                FrCirc Circ = (FrCirc)fr;

                Circ.X += DifX;
                Circ.Y += DifY;

                Circ.actualizarDimensiones();
                Circ.actualizarPines();
            }else if(fr instanceof FrTria){
                FrTria Tria = (FrTria)fr;

                Tria.X += DifX;
                Tria.Y += DifY;
                
                Tria.moverVertices( DifX, DifY);

                Tria.actualizarDimensiones();
                Tria.actualizarPines();
            }
            Plano.repaint();
        }


    }
    
}
