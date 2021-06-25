package com.mec2021.gui.propiedades;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.mec2021.Ctrl;
import com.mec2021.gui.PnPlano;
import com.mec2021.plano.objetos.Objeto2D;
import com.mec2021.plano.objetos.formas.Forma;
import com.mec2021.plano.objetos.formas.FrRect;

/**Panel de propiedades para un Rectangulo*/
public class PropRect extends PnPropiedades{

    private JTextField TFX = new JTextField();
    private JTextField TFY = new JTextField();
    private JTextField TFAn = new JTextField();
    private JTextField TFAl = new JTextField();

    public PropRect(Objeto2D obj, PnPlano plano) {
        super(obj, plano);
         
        setBounds(getX(), getY(), getWidth(), 350);

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

        //ANCHO
        JLabel LbAn = new JLabel("Ancho");
        LbAn.setBounds(10, 55, 50, 10);
        
        TFAn.setBounds(60, 50, 50, 20);
        TFAn.addKeyListener(TextCont);

        //ALTO
        JLabel LbAl = new JLabel("Alto");
        LbAl.setBounds(10, 85, 50, 10);
        
        TFAl.setBounds(60, 80, 50, 20);
        TFAl.addKeyListener(TextCont);
        
        //PROPIEDADES
        LbArea.setBounds(10, 130, 200, 10);
        LbCentX.setBounds(10, 155, 200, 10);
        LbCentY.setBounds(10, 180, 200, 10);

        LbInX.setBounds(10, 215, 200, 10);
        LbInY.setBounds(10, 240, 200, 10);

        CBHueco.setBounds(8, 270, 200, 30);

        PnCont.add(LbX);
        PnCont.add(TFX);        
        PnCont.add(LbY);
        PnCont.add(TFY);
        PnCont.add(LbAn);
        PnCont.add(TFAn);
        PnCont.add(LbAl);
        PnCont.add(TFAl);
        PnCont.add(LbArea);
        PnCont.add(LbCentX);
        PnCont.add(LbCentY);
        PnCont.add(CBHueco);

        
        actualizarDatos();
    }

    @Override
    public void actualizarDatos() {
        FrRect Rect = ((FrRect)ObjRef);
        DecimalFormat f = new DecimalFormat("#0.00");

        TFX.setText("" + f.format(Ctrl.aplicarEscalaLn(Rect.X)));
        TFY.setText("" + f.format(Ctrl.aplicarEscalaLn(-Rect.Y)));
        TFAn.setText(""+ f.format(Rect.Ancho));
        TFAl.setText(""+ f.format(Rect.Alto));
        
        LbArea.setText("Area:                      " +f.format(Ctrl.aplicarEscalaAr(Rect.calcularArea())));
        LbCentX.setText("Centroide en x:     " + f.format(Ctrl.aplicarEscalaLn(Rect.centroideX())));
        LbCentY.setText("Centroide en Y:     " + f.format(Ctrl.aplicarEscalaLn(Rect.centroideY())));

        LbInX.setText("Inercia en X:     "+f.format(Rect.inerciaCentEjeX()));
        LbInY.setText("Inercia en Y:     "+f.format(Rect.inerciaCentEjeY()));
        
        CBHueco.setSelected(((Forma)ObjRef).Hueco);
    }

    @Override
    public void actualizarForma() {
        FrRect Rec = (FrRect)ObjRef;

        Rec.setBounds(Math.round(Plano.PtOrigen.x) + Math.round(Ctrl.aplicarEscalaLnInv(Float.parseFloat((TFX.getText().isEmpty() || TFX.getText().equals("-") ? "0" : TFX.getText())))), 
                      Math.round(Plano.PtOrigen.y) + Math.round(Ctrl.aplicarEscalaLnInv(-Float.parseFloat((TFY.getText().isEmpty() || TFY.getText().equals("-") ? "0" : TFY.getText())))), 
                      Math.round(Ctrl.aplicarEscalaLnInv(Float.parseFloat((TFAn.getText().isEmpty() || TFAn.getText().equals("-") ? "0" : TFAn.getText())))), 
                      Math.round(Ctrl.aplicarEscalaLnInv(Float.parseFloat((TFAl.getText().isEmpty() || TFAl.getText().equals("-") ? "0" : TFAl.getText())))));
                      
        Rec.Hueco = CBHueco.isSelected();

        Rec.ActualizarCoordenadas();
        Rec.ActualizarPines();

        Plano.repaint();
    }
    
}
