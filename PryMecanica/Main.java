package PryMecanica;
import javax.swing.JFrame;

import java.awt.geom.Point2D;

public class Main {

    public static JFrame VentPrinc;

    public static void main(String[] args){
        JFrame Vent = VentPrinc = new JFrame();    

        Vent.add(new PnPrincipal());

        Vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Vent.setSize(500, 500);
        Vent.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Vent.setVisible(true);
    }

    public static float clamp(float a, float min, float max){
        return a < max ? a > min ? a : min : max; 
    }

    public static Point2D direccion(int x1, int y1, int x2, int y2){
        float ang = angulo(x1, y1, x2, y2);

        return new Point2D.Double(Math.cos(ang), Math.sin(ang));
    }

    public static float angulo(float x1, float y1, float x2, float y2){
        float ang = (float) Math.atan2(y2 - y1, x2 - x1);

        //System.out.println(ang);

        if((y2 - y1) < 0)
            return ((float)Math.PI * 2) + ang;
        else
            return ang;
        
    }
}

