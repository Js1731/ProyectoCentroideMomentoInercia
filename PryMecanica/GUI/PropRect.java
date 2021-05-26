package PryMecanica.GUI;

import javax.swing.JLabel;
import javax.swing.JTextField;

import PryMecanica.Plano.Objetos.Objeto2D;
import PryMecanica.Plano.Objetos.Formas.Forma;

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
        LbArea.setBounds(10, 130, 100, 10);
        LbCentX.setBounds(10, 155, 100, 10);
        LbCentY.setBounds(10, 180, 100, 10);

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
    }
    
}
