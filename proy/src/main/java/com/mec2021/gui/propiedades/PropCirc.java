package com.mec2021.gui.propiedades;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

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

        TFX.setText("" + f.format(Circulo.X));
        TFY.setText("" + f.format(-Circulo.Y));
        TFRadio.setText("" + f.format(Circulo.Diametro/2));
    
        TFAngIni.setText("" + f.format(Circulo.Sector.start));
        TFExt.setText("" + f.format(Circulo.Sector.extent));

        LbArea.setText("Area:                      " + f.format(Circulo.calcularArea()));
        LbCentX.setText("Centroide en x:     " + f.format(Circulo.centroideX()));
        LbCentY.setText("Centroide en Y:     " + f.format(Circulo.centroideY()));

        LbInX.setText("Inercia en x:     " + f.format(Circulo.inerciaCentEjeX()));
        LbInY.setText("Inercia en Y:     " + f.format(Circulo.inerciaCentEjeY()));

        CBHueco.setSelected(((Forma)ObjRef).Hueco);
    }

    @Override
    public void actualizarForma() {
        FrCirc Circ = (FrCirc)ObjRef;

        //PROPIEDADES NUEVAS
        float PosX = Float.parseFloat((TFX.getText().isEmpty() || TFX.getText().equals("-") ? "0" : TFX.getText()));
        float PosY = -Float.parseFloat((TFY.getText().isEmpty() || TFY.getText().equals("-") ? "0" : TFY.getText()));
        float Radio = Float.parseFloat((TFRadio.getText().isEmpty() || TFRadio.getText().equals("-") ? "0" : TFRadio.getText()));

        float AngIni = Float.parseFloat(TFAngIni.getText().isEmpty() || TFAngIni.getText().equals("-") ? "0" : TFAngIni.getText());
        float AngExt = Float.parseFloat(TFExt.getText().isEmpty() || TFExt.getText().equals("-") ? "0" : TFExt.getText());

        //APLICAR PROPIEDADES
        Circ.X = PosX;
        Circ.Y = PosY;
        Circ.Diametro = Radio*2;

        Circ.Sector.start = AngIni;
        Circ.Sector.extent = AngExt;

        Circ.Hueco = CBHueco.isSelected();

        Circ.actualizarDimensiones();
        Circ.ActualizarPines();

        Circ.repaint();
    }
}
