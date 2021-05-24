package PryMecanica.Plano.Objetos;

import PryMecanica.Arrastrable;
import java.awt.Graphics;

/**Define un objeto generico con coordenadas relativas al Origen */
public abstract class Objeto2D extends Arrastrable{
    /**Coordenada X relativa al origen */
    public int X = 0;

    /**Coordenada Y relativa al origen */
    public int Y = 0;

    public static int Escala = 40;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /**Actualiza las coordenadas de la figura */
    public abstract void ActualizarCoordenadas();
}
