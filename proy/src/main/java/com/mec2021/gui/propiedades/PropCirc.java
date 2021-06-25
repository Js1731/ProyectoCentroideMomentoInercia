package com.mec2021.gui.propiedades;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.mec2021.Ctrl;
import com.mec2021.gui.PnPlano;
import com.mec2021.plano.objetos.Objeto2D;
import com.mec2021.plano.objetos.formas.Forma;
import com.mec2021.plano.objetos.formas.FrCirc;

/**Panel de propiedades para un Circulo*/
public class PropCirc extends PnPropiedades{
    
    private JTextField TFX = new JTextField();
    private JTextField TFY = new JTextField();
    private JTextField TFRadio = new JTextField();
    private JTextField TFAngIni = new JTextField();
    private JTextField TFExt = new JTextField();

    public PropCirc(Objeto2D obj, PnPlano plano) {
        super(obj, plano);

        setBounds(getX(), getY(), getWidth(), 400);

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

        //Radio
        JLabel LbRad = new JLabel("Radio");
        LbRad.setBounds(10, 55, 50, 10);
        
        TFRadio.setBounds(110, 50, 80, 20);
        TFRadio.addKeyListener(TextCont);

        //ANGULO INI
        JLabel LbAngIni = new JLabel("Angulo Inicial");
        LbAngIni.setBounds(10, 85, 100, 10);
        
        TFAngIni.setBounds(110, 80, 80, 20);
        TFAngIni.addKeyListener(TextCont);

        //Extension
        JLabel LbExt = new JLabel("Extension");
        LbExt.setBounds(10, 115, 100, 10);
        
        TFExt.setBounds(110, 110, 80, 20);
        TFExt.addKeyListener(TextCont);

        //PROPIEDADES
        LbArea.setBounds(10, 160, 200, 10);
        LbCentX.setBounds(10, 185, 200, 10);
        LbCentY.setBounds(10, 210, 200, 10);

        LbInX.setBounds(10, 245, 200, 10);
        LbInY.setBounds(10, 270, 200, 10);

        CBHueco.setBounds(8, 300 , 120, 30);


        PnCont.add(LbX);
        PnCont.add(TFX);        
        PnCont.add(LbY);
        PnCont.add(TFY);
        PnCont.add(LbRad);
        PnCont.add(TFRadio);
        PnCont.add(LbAngIni);
        PnCont.add(TFAngIni);
        PnCont.add(LbExt);
        PnCont.add(TFExt);
        PnCont.add(LbArea);
        PnCont.add(LbCentX);
        PnCont.add(LbCentY);

        PnCont.add(CBHueco);

        actualizarDatos();
    }

    @Override
    public void actualizarDatos() {

        DecimalFormat f = new DecimalFormat("#0.00");
        FrCirc Circulo = (FrCirc)ObjRef;

        TFX.setText("" + f.format(Ctrl.aplicarEscalaLn(Circulo.X)));
        TFY.setText("" + f.format(Ctrl.aplicarEscalaLn(-Circulo.Y)));
        TFRadio.setText("" + f.format(Circulo.Diametro/2));

        

        TFAngIni.setText("" + f.format(Circulo.Sector.start));
        TFExt.setText("" + f.format(Circulo.Sector.extent));

        Float t = Circulo.calcularArea();

        LbArea.setText("Area:                      " + f.format(Ctrl.aplicarEscalaAr(Circulo.calcularArea())));
        LbCentX.setText("Centroide en x:     " + f.format(Ctrl.aplicarEscalaLn(-Circulo.getWidth()/2f + Circulo.centroideX())));
        LbCentY.setText("Centroide en Y:     " + f.format(Ctrl.aplicarEscalaLn(Circulo.getHeight()/2f - Circulo.centroideY())));

        LbInX.setText("Inercia en x:     " + f.format(Circulo.inerciaCentEjeX()));
        LbInY.setText("Inercia en Y:     " + f.format(Circulo.inerciaCentEjeY()));

        CBHueco.setSelected(((Forma)ObjRef).Hueco);
    }

    @Override
    public void actualizarForma() {
        FrCirc Circ = (FrCirc)ObjRef;

        Circ.setBounds(Math.round(Plano.PtOrigen.x) + Math.round(Ctrl.aplicarEscalaLnInv(Float.parseFloat((TFX.getText().isEmpty() || TFX.getText().equals("-") ? "0" : TFX.getText())))), 
                      Math.round(Plano.PtOrigen.y) + Math.round(Ctrl.aplicarEscalaLnInv(-Float.parseFloat((TFY.getText().isEmpty() || TFY.getText().equals("-") ? "0" : TFY.getText())))), 
                      2*Math.round(Ctrl.aplicarEscalaLnInv(Float.parseFloat((TFRadio.getText().isEmpty() || TFRadio.getText().equals("-") ? "0" : TFRadio.getText())))), 
                      2*Math.round(Ctrl.aplicarEscalaLnInv(Float.parseFloat((TFRadio.getText().isEmpty() || TFRadio.getText().equals("-") ? "0" : TFRadio.getText())))));

        //Circ.Diametro = 2*Math.round(Ctrl.aplicarEscalaLnInv(Float.parseFloat((TFRadio.getText().isEmpty() || TFRadio.getText().equals("-") ? "0" : TFRadio.getText()))));
        Circ.Sector.start = Float.parseFloat(TFAngIni.getText().isEmpty() || TFAngIni.getText().equals("-") ? "0" : TFAngIni.getText());
        Circ.Sector.extent = Float.parseFloat(TFExt.getText().isEmpty() || TFExt.getText().equals("-") ? "0" : TFExt.getText());
        Circ.Sector.width = 2*Math.round(Ctrl.aplicarEscalaLnInv(Float.parseFloat((TFRadio.getText().isEmpty() || TFRadio.getText().equals("-") ? "0" : TFRadio.getText()))));
        Circ.Sector.height = 2*Math.round(Ctrl.aplicarEscalaLnInv(Float.parseFloat((TFRadio.getText().isEmpty() || TFRadio.getText().equals("-") ? "0" : TFRadio.getText()))));

        Circ.Hueco = CBHueco.isSelected();

        Circ.ActualizarCoordenadas();
        Circ.ActualizarPines();

        Circ.repaint();

        Plano.repaint();
    }
}
