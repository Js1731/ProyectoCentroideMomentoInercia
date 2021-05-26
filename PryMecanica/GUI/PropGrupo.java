package PryMecanica.GUI;

import PryMecanica.Plano.Objetos.Grupo;

import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import PryMecanica.Plano.Punto;
import PryMecanica.Plano.Objetos.Objeto2D;
import PryMecanica.Plano.Objetos.Formas.Forma;

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

        //POSICION Y
        JLabel LbY = new JLabel("Y");
        LbY.setBounds(100, 15, 50, 10);

        TFY.setBounds(130, 10, 50, 20);

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
        TFX.setText(""+(float)ObjRef.X/Forma.Escala);
        TFY.setText(""+(float)-ObjRef.Y/Forma.Escala);

        DecimalFormat f = new DecimalFormat("#0.00");
        Punto Centroide = ((Grupo)ObjRef).centroide();

        LbCentX.setText("Centroide en x:     "+f.format((float)(Centroide.x + ((Grupo)ObjRef).X)/Forma.Escala));
        LbCentY.setText("Centroide en Y:     "+f.format(-(float)(Centroide.y + ((Grupo)ObjRef).Y)/Forma.Escala));
    }

    @Override
    public void actualizarForma() {
        System.out.println("ola soi rectangulo");
    }
    
}
