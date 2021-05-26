package PryMecanica.GUI;

import javax.swing.JLabel;
import javax.swing.JTextField;

import PryMecanica.Plano.Objetos.Objeto2D;

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

        //POSICION Y
        JLabel LbY = new JLabel("Y");
        LbY.setBounds(100, 15, 50, 10);
        
        TFY.setBounds(130, 10, 50, 20);

        //Radio
        JLabel LbRad = new JLabel("Radio");
        LbRad.setBounds(10, 55, 50, 10);
        
        TFRadio.setBounds(110, 50, 50, 20);

        //ANGULO INI
        JLabel LbAngIni = new JLabel("Angulo Inicial");
        LbAngIni.setBounds(10, 85, 100, 10);
        
        TFAngIni.setBounds(110, 80, 50, 20);

        //Extension
        JLabel LbExt = new JLabel("Extension");
        LbExt.setBounds(10, 115, 100, 10);
        
        TFExt.setBounds(110, 110, 50, 20);

        //PROPIEDADES
        LbArea.setBounds(10, 160, 100, 10);
        LbCentX.setBounds(10, 185, 100, 10);
        LbCentY.setBounds(10, 210, 100, 10);

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
    }

    @Override
    public void actualizarDatos() {
        // TODO Auto-generated method stub
        
    }
    
}
