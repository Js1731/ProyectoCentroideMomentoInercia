package PryMecanica.Formas;

import java.awt.Color;

import PryMecanica.Arrastrable;

public class Pin extends Arrastrable {
    public Forma Fr;

    public Pin(Forma fr, int x, int y){
        Fr = fr;
        setBackground(Color.BLUE);
        setBounds(x,y,10,10);
    }
}
