package PryMecanica.Plano.Objetos;

import java.awt.Color;
import java.awt.event.MouseEvent;

import PryMecanica.Arrastrable;
import PryMecanica.Plano.Objetos.Formas.Forma;

/**Componete Arrastrable para deformar una forma*/
public class Pin extends Arrastrable {
    public Forma Fr;




    public Pin(Forma fr, int x, int y){
        Fr = fr;
        setBackground(Color.BLUE);
        setBounds(x,y,10,10);
    }




    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        if(Fr.Grp != null)
            Fr.Grp.ActualizarBordes();
    }
}
