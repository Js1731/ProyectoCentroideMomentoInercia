package PryMecanica.GUI;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import PryMecanica.PnPlano;
import PryMecanica.Plano.Objetos.Objeto2D;
import PryMecanica.Plano.Objetos.Formas.Forma;
import PryMecanica.Plano.Objetos.Formas.FrTria;

public class PropTria extends PnPropiedades{

    private JLabel LbArea = new JLabel("Area:");
    private JLabel LbCentX = new JLabel("Centroide en X:");
    private JLabel LbCentY = new JLabel("Centroide en Y:");

    private JTextField TFX = new JTextField();
    private JTextField TFY = new JTextField();

    private JTextField TFX1 = new JTextField();
    private JTextField TFY1 = new JTextField();

    private JTextField TFX2 = new JTextField();
    private JTextField TFY2 = new JTextField();

    private JTextField TFX3 = new JTextField();
    private JTextField TFY3 = new JTextField();

    public PropTria(Objeto2D obj) {
        super(obj);

        setBounds(getX(), getY(), getWidth(), 400);

        int Y = 1;
        int Esp = 30;

        //POSICION X
        JLabel LbX = new JLabel("X");
        LbX.setBounds(10, 15, 50, 10);
        
        TFX.setBounds(30, 10, 50, 20);

        //POSICION Y
        JLabel LbY = new JLabel("Y");
        LbY.setBounds(100, 15, 50, 10);
        
        TFY.setBounds(130, 10, 50, 20);


        //VERTICE 1
        JLabel LbVer1 = new JLabel("Vertice 1");
        LbVer1.setBounds(10, 25 + Y*Esp, 100, 10);
        Y++;

        //POSICION X
        JLabel LbX1 = new JLabel("X");
        LbX1.setBounds(10, 25 + Y*Esp, 50, 10);
        
        TFX1.setBounds(30, 20 + Y*Esp, 50, 20);

        //POSICION Y
        JLabel LbY1 = new JLabel("Y");
        LbY1.setBounds(100, 25 + Y*Esp, 50, 10);
        
        TFY1.setBounds(130, 20 + Y*Esp, 50, 20);
        Y++;

        //VERTICE 2
        JLabel LbVer2 = new JLabel("Vertice 2");
        LbVer2.setBounds(10, 25 + Y*Esp, 100, 10);
        Y++;

        //POSICION X
        JLabel LbX2 = new JLabel("X");
        LbX2.setBounds(10, 25 + Y*Esp, 50, 10);
        
        TFX2.setBounds(30, 20 + Y*Esp, 50, 20);

        //POSICION Y
        JLabel LbY2 = new JLabel("Y");
        LbY2.setBounds(100, 25 + Y*Esp, 50, 10);
        
        TFY2.setBounds(130, 20 + Y*Esp, 50, 20);
        Y++;

        //VERTICE 1
        JLabel LbVer3 = new JLabel("Vertice 3");
        LbVer3.setBounds(10, 25 + Y*Esp, 100, 10);
        Y++;

        //POSICION X
        JLabel LbX3 = new JLabel("X");
        LbX3.setBounds(10, 25 + Y*Esp, 50, 10);
        
        TFX3.setBounds(30, 20 + Y*Esp, 50, 20);

        //POSICION Y
        JLabel LbY3 = new JLabel("Y");
        LbY3.setBounds(100, 25 + Y*Esp, 50, 10);
        
        TFY3.setBounds(130, 20 + Y*Esp, 50, 20);
        Y++;

        //PROPIEDADES
        LbArea.setBounds(10, 40 + Y*Esp, 200, 10);
        LbCentX.setBounds(10, 70 + Y*Esp, 200, 10);
        LbCentY.setBounds(10, 100 + Y*Esp, 200, 10);

        PnCont.add(LbX);
        PnCont.add(TFX);
        PnCont.add(LbY);
        PnCont.add(TFY);

        PnCont.add(LbVer1);

        PnCont.add(LbX1);
        PnCont.add(TFX1);
        PnCont.add(LbY1);
        PnCont.add(TFY1);

        PnCont.add(LbVer2);

        PnCont.add(LbX2);
        PnCont.add(TFX2);
        PnCont.add(LbY2);
        PnCont.add(TFY2);

        PnCont.add(LbVer3);

        PnCont.add(LbX3);
        PnCont.add(TFX3);
        PnCont.add(LbY3);
        PnCont.add(TFY3);

        PnCont.add(LbArea);
        PnCont.add(LbCentX);
        PnCont.add(LbCentY);

        actualizarDatos();
    }

    @Override
    public void actualizarDatos() {
        TFX.setText(""+(float)ObjRef.X/Forma.Escala);
        TFY.setText(""+(float)-ObjRef.Y/Forma.Escala);

        TFX1.setText(""+(float)(((FrTria)ObjRef).Ver1.x  - PnPlano.PtOrigen.x)/Forma.Escala);
        TFY1.setText(""+(float)-(((FrTria)ObjRef).Ver1.y  - PnPlano.PtOrigen.y)/Forma.Escala);

        TFX2.setText(""+(float)(((FrTria)ObjRef).Ver2.x  - PnPlano.PtOrigen.x)/Forma.Escala);
        TFY2.setText(""+(float)-(((FrTria)ObjRef).Ver2.y  - PnPlano.PtOrigen.y)/Forma.Escala);

        TFX3.setText(""+(float)(((FrTria)ObjRef).Ver3.x  - PnPlano.PtOrigen.x)/Forma.Escala);
        TFY3.setText(""+(float)-(((FrTria)ObjRef).Ver3.y  - PnPlano.PtOrigen.y)/Forma.Escala);

        DecimalFormat f = new DecimalFormat("#0.00");

        LbArea.setText("Area:                      "+f.format((float)((FrTria)ObjRef).calcularArea()/(Forma.Escala * Forma.Escala)));
        LbCentX.setText("Centroide en x:     "+f.format((float)((FrTria)ObjRef).centroideX()/Forma.Escala));
        LbCentY.setText("Centroide en Y:     "+f.format(-((float)((FrTria)ObjRef).centroideY() - ((FrTria)ObjRef).getHeight())/Forma.Escala));
    }
    
}
