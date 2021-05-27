package PryMecanica.gui.propiedades;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import PryMecanica.PnPlano;
import PryMecanica.plano.objetos.Objeto2D;
import PryMecanica.plano.objetos.formas.Forma;
import PryMecanica.plano.objetos.formas.FrRect;

/**Panel de propiedades para un Rectangulo*/
public class PropRect extends PnPropiedades{

    private JTextField TFX = new JTextField();
    private JTextField TFY = new JTextField();
    private JTextField TFAn = new JTextField();
    private JTextField TFAl = new JTextField();

    private JLabel LbArea = new JLabel("Area:");
    private JLabel LbCentX = new JLabel("Centroide en X:");
    private JLabel LbCentY = new JLabel("Centroide en Y:");

    public PropRect(Objeto2D obj) {
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

        CBHueco.setBounds(8, 220, 200, 30);

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
        TFX.setText(""+PnPlano.Escala*(float)ObjRef.X/Forma.Escala);
        TFY.setText(""+PnPlano.Escala*(float)-ObjRef.Y/Forma.Escala);
        TFAn.setText(""+PnPlano.Escala*(float)((FrRect)ObjRef).Ancho/Forma.Escala);
        TFAl.setText(""+PnPlano.Escala*(float)((FrRect)ObjRef).Alto/Forma.Escala);

        DecimalFormat f = new DecimalFormat("#0.00");
        
        LbArea.setText("Area:                      "+f.format(PnPlano.Escala*PnPlano.Escala*(float)((FrRect)ObjRef).calcularArea()/(Forma.Escala * Forma.Escala)));
        LbCentX.setText("Centroide en x:     "+f.format(PnPlano.Escala*(float)((FrRect)ObjRef).centroideX()/Forma.Escala));
        LbCentY.setText("Centroide en Y:     "+f.format(PnPlano.Escala*(float)((FrRect)ObjRef).centroideY()/Forma.Escala));
        
        CBHueco.setSelected(((Forma)ObjRef).Hueco);
    }

    @Override
    public void actualizarForma() {
        FrRect Rec = (FrRect)ObjRef;

        Rec.setBounds(Math.round(PnPlano.PtOrigen.x) + Math.round(Float.parseFloat((TFX.getText().isEmpty() || TFX.getText().equals("-") ? "0" : TFX.getText()))*Forma.Escala/PnPlano.Escala), 
                      Math.round(PnPlano.PtOrigen.y) + Math.round(-Float.parseFloat((TFY.getText().isEmpty() || TFY.getText().equals("-") ? "0" : TFY.getText()))*Forma.Escala/PnPlano.Escala), 
                      Math.round(Float.parseFloat((TFAn.getText().isEmpty() || TFAn.getText().equals("-") ? "0" : TFAn.getText()))*Forma.Escala/PnPlano.Escala), 
                      Math.round(Float.parseFloat((TFAl.getText().isEmpty() || TFAl.getText().equals("-") ? "0" : TFAl.getText()))*Forma.Escala/PnPlano.Escala));
                      
        Rec.Hueco = CBHueco.isSelected();

        Rec.ActualizarCoordenadas();
        Rec.ActualizarPines();

        PnPlano.PlPrinc.repaint();
    }
    
}
