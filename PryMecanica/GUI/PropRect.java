package PryMecanica.GUI;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import PryMecanica.Plano.Objetos.Objeto2D;
import PryMecanica.Plano.Objetos.Formas.Forma;
import PryMecanica.Plano.Objetos.Formas.FrRect;

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

        //POSICION Y
        JLabel LbY = new JLabel("Y");
        LbY.setBounds(100, 15, 50, 10);
        
        TFY.setBounds(130, 10, 50, 20);

        //ANCHO
        JLabel LbAn = new JLabel("Ancho");
        LbAn.setBounds(10, 55, 50, 10);
        
        TFAn.setBounds(60, 50, 50, 20);

        //ALTO
        JLabel LbAl = new JLabel("Alto");
        LbAl.setBounds(10, 85, 50, 10);
        
        TFAl.setBounds(60, 80, 50, 20);

        //PROPIEDADES
        LbArea.setBounds(10, 130, 200, 10);
        LbCentX.setBounds(10, 155, 200, 10);
        LbCentY.setBounds(10, 180, 200, 10);

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

        actualizarDatos();
    }

    @Override
    public void actualizarDatos() {
        TFX.setText(""+(float)ObjRef.X/Forma.Escala);
        TFY.setText(""+(float)-ObjRef.Y/Forma.Escala);
        TFAn.setText(""+(float)((FrRect)ObjRef).Ancho/Forma.Escala);
        TFAl.setText(""+(float)((FrRect)ObjRef).Alto/Forma.Escala);

        DecimalFormat f = new DecimalFormat("#0.00");

        LbArea.setText("Area:                      "+f.format((float)((FrRect)ObjRef).calcularArea()/(Forma.Escala * Forma.Escala)));
        LbCentX.setText("Centroide en x:     "+f.format((float)((FrRect)ObjRef).centroideX()/Forma.Escala));
        LbCentY.setText("Centroide en Y:     "+f.format((float)((FrRect)ObjRef).centroideY()/Forma.Escala));
    }
    
}
