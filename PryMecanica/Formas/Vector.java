package PryMecanica.Formas;

/**
 * Vector entre el Punto {@code A y B}, se trabaja con numeros flotantes
 */
public class Vector {
    
    /**Punto Inicial del Vector */
    public Punto A;

    /**Punto Final del Vector */
    public Punto B;

    /**Crea un vector nulo, punto A en 0 y punto B en 0 */
    public Vector(){
        this(new Punto(0, 0), new Punto(0, 0));
    }

    /**
     * Crea un vector definiendo {@code a} como punto inicial y {@code b} como punto final 
     * @param a Punto inicial
     * @param b Punto final
     */
    public Vector(Punto a, Punto b){
        A = a;
        B = b;
    }

    /**
     * Calcula y devuelve la direccion del vector en radianes
     * @return Direccion del vector en {@code radianes}
     */
    public float direccion(){
        return Punto.calcularDirection(A.x, A.y, B.x, B.y);
    }
    
    /**
     * Calcula la distancia entre los puntos del vector
     * @return Distancia
     */
    public float distancia(){
        return Punto.distanciaEntre(A.x, A.y, B.x, B.y);
    }

}
