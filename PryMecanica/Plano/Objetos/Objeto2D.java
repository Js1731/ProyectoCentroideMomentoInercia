package PryMecanica.Plano.Objetos;

import PryMecanica.Arrastrable;
import java.awt.Graphics;
import java.awt.Color;

/**Define un objeto generico con coordenadas relativas al Origen */
public abstract class Objeto2D extends Arrastrable{
    /**Coordenada X relativa al origen */
    public int X = 0;

    /**Coordenada Y relativa al origen */
    public int Y = 0;



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // g.setColor(new Color(200,200,200,50));

        // g.fillRect(0, 0, 100, 70);

        // g.setColor(Color.WHITE);

        // g.drawString(X + ", " + (-Y),10, 20);

        // g.setColor(Color.DARK_GRAY);
    }



    /**Actualiza las coordenadas de la figura */
    public abstract void ActualizarCoordenadas();
}
