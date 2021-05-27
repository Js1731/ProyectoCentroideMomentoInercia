package PryMecanica.gui.propiedades;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import PryMecanica.PnPlano;
import PryMecanica.plano.objetos.Objeto2D;
import PryMecanica.plano.objetos.formas.Forma;
import PryMecanica.plano.objetos.formas.FrCirc;

/**Panel de propiedades para un Circulo*/
public class PropCirc extends PnPropiedades{
    
    private JLabel LbArea = new JLabel("Area:");
    private JLabel LbCentX = new JLabel("Centroide en X:");
    private JLabel LbCentY = new JLabel("Centroide en Y:");

    private JTextField TFX = new JTextField();
    private JTextField TFY = new JTextField();
    private JTextField TFRadio = new JTextField();
    private JTextField TFAngIni = new JTextField();
    private JTextField TFExt = new JTextField();

    public PropCirc(Objeto2D obj) {
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

        CBHueco.setBounds(8, 230 , 120, 30);


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
        TFX.setText(""+PnPlano.Escala*(float)ObjRef.X/Forma.Escala);
        TFY.setText(""+PnPlano.Escala*(float)-ObjRef.Y/Forma.Escala);
        TFRadio.setText(""+PnPlano.Escala*(float)(((FrCirc)ObjRef).Diametro/2)/Forma.Escala);

        DecimalFormat f = new DecimalFormat("#0.00");

        TFAngIni.setText(""+f.format(((FrCirc)ObjRef).Sector.start));
        TFExt.setText(""+f.format(((FrCirc)ObjRef).Sector.extent));

        LbArea.setText("Area:                      "+f.format(PnPlano.Escala*(float)((FrCirc)ObjRef).calcularArea()/(Forma.Escala * Forma.Escala)));
        LbCentX.setText("Centroide en x:     "+f.format(PnPlano.Escala*(float)((FrCirc)ObjRef).centroideX()/Forma.Escala));
        LbCentY.setText("Centroide en Y:     "+f.format(PnPlano.Escala*(float)(((FrCirc)ObjRef).getHeight() - ((FrCirc)ObjRef).centroideY())/Forma.Escala));

        CBHueco.setSelected(((Forma)ObjRef).Hueco);
    }

    @Override
    public void actualizarForma() {
        FrCirc Circ = (FrCirc)ObjRef;

        Circ.setBounds(Math.round(PnPlano.PtOrigen.x) + Math.round(Float.parseFloat((TFX.getText().isEmpty() || TFX.getText().equals("-") ? "0" : TFX.getText()))*Forma.Escala/PnPlano.Escala), 
                      Math.round(PnPlano.PtOrigen.y) + Math.round(-Float.parseFloat((TFY.getText().isEmpty() || TFY.getText().equals("-") ? "0" : TFY.getText()))*Forma.Escala/PnPlano.Escala), 
                      2*Math.round(Float.parseFloat((TFRadio.getText().isEmpty() || TFRadio.getText().equals("-") ? "0" : TFRadio.getText()))*Forma.Escala/PnPlano.Escala), 
                      2*Math.round(Float.parseFloat((TFRadio.getText().isEmpty() || TFRadio.getText().equals("-") ? "0" : TFRadio.getText()))*Forma.Escala/PnPlano.Escala));

        Circ.Sector.start = Float.parseFloat(TFAngIni.getText().isEmpty() || TFAngIni.getText().equals("-") ? "0" : TFAngIni.getText());
        Circ.Sector.extent = Float.parseFloat(TFExt.getText().isEmpty() || TFExt.getText().equals("-") ? "0" : TFExt.getText());

        Circ.Hueco = CBHueco.isSelected();

        Circ.ActualizarCoordenadas();
        Circ.ActualizarPines();

        PnPlano.PlPrinc.repaint();
    }
}
