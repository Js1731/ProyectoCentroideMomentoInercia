package PryMecanica.GUI;

import javax.swing.JLabel;
import javax.swing.JTextField;

import PryMecanica.Plano.Objetos.Objeto2D;

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
        LbCentX.setBounds(10, 60, 100, 10);
        LbCentY.setBounds(10, 90, 100, 10);

        PnCont.add(LbX);
        PnCont.add(TFX);        
        PnCont.add(LbY);
        PnCont.add(TFY);
        PnCont.add(LbCentX);
        PnCont.add(LbCentY);
    }

    @Override
    public void actualizarDatos() {
        // TODO Auto-generated method stub
        
    }
    
}
