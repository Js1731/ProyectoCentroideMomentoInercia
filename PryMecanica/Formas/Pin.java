package PryMecanica.Formas;

import java.awt.Color;

import PryMecanica.PnArrastrable;

public class Pin extends PnArrastrable {
    public Forma Fr;

    public Pin(Forma fr, int x, int y){
        Fr = fr;
        setBackground(Color.BLUE);
        setBounds(x,y,10,10);
    }
}
