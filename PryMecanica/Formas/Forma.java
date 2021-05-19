package PryMecanica.Formas;

import PryMecanica.Arrastrable;

/**Define una forma generica que se puede arrastrar y deformar usando un conjuntos de {@link Pin}
 * 
*/
public abstract class Forma extends Arrastrable{
    public int X = 0;
    public int Y = 0;

    /**Conjunto de pines para deformar esta forma */
    public Pin[] Pines;

    /**
     * Actualiza la posicion de los pines de la figura
     */
    public abstract void ActualizarPines();
}
