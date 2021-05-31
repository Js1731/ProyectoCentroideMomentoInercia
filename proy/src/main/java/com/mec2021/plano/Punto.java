package com.mec2021.plano;

/** 
 * Define un punto usando numeros flotantes
*/
public class Punto {

    public float x = 0;
    public float y = 0;

    /**Crea un punto en el origen {@code (0, 0)} 
     * 
    */
    public Punto(){
        this(0,0);
    }

    /**
     * Crea un punto en (x, y)
     * @param x
     * @param y
     */
    public Punto(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calcula la direccion entre dos puntos y el resultado se da en radianes
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return Angulo en {@code radianes}
     */
    public static float calcularDirection(float x1, float y1, float x2, float y2){
        float ang = (float) Math.atan2(y2 - y1, x2 - x1);

        if((y2 - y1) < 0)
            return ((float)Math.PI * 2) + ang;
        else
            return ang;
        
    }

    /**
     * Calcula la distancia entre dos puntos
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return 
     */
    public static float distanciaEntre(float x1, float y1, float x2, float y2){
        return (float)Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
