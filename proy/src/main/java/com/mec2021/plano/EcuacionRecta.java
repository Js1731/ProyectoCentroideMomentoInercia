package com.mec2021.plano;

import com.mec2021.gui.PnPlano;


/**Representacion de la ecuacion de la recta de dos {@link Punto}s dados */
public class EcuacionRecta {

    /**Indica el tipo de ecuacion que es esta recta
    * <PRE>
    *      <b>id </b   >      <b>Ecuacion</b>
    *    <code>0</code>:  <code>Recta normal</code>
    *    <code>1</code>:  <code>Recta Horizontal</code>
    *    <code>2</code>:  <code>Recta Vertical</code>
    * </PRE>
    */
    public int Tipo = 0;

    /**Primer punto de la recta */
    public Punto A;

    /**Segundo Punto de la recta */
    public Punto B;

    /**Pendiente de la recta, si es 0 la recta es Horizontal o Vertical */
    public float m = 0;

    /**Interseccion con el eje Y */
    public float b = 0;

    public PnPlano Plano;

    /**
     * Define una nueva ecuacion de la recta que pasa por los puntos {@code A} y {@code B}
     * @param ai Punto A
     * @param bi Punto B
     */
    public EcuacionRecta(Punto ai, Punto bi, PnPlano plano){
        A = ai;
        B = bi;

        Plano = plano;

        actualizarDatos();
    }

    public void actualizarDatos(){
        if(B.y == A.y)
            Tipo = 1;
        else if(B.x == A.x)
            Tipo = 2;
        else{
            Tipo = 0;
            m = (B.y - A.y)/(B.x - A.x);
            b = A.y*((float)Plano.Escala/Plano.Escala) - m*A.x*((float)Plano.Escala/Plano.Escala);
        }
    }

    /**
     * Evalua la ecuacion de la recta en funcion de X
     * @param x
     * @return y
     */
    public float fx(float x){
        if(Tipo == 0)
            return m*x + b;
        else if(Tipo == 1)
            return A.y;
        else
            return 0;
    }

    /**
     * Evalua la ecuacion de la recta en funcion de y
     * @param y
     * @return x
     */
    public float fy(float y){
        if(Tipo == 0)
            return (y - b)/m;
        else if(Tipo == 2)
            return A.x;
        else
            return 0;
    }

    /**
     * Integra la ecuacion dada para encontrar la inercia con respecto al eje Y
     * @param Ec Ecuacion a integrar
     * @param a Limite inferior de integracion
     * @param b Limite superior de integracion
     * @return Inercia con respecto a Y
     */
    public static float integrar_fy(EcuacionRecta Ec, float a, float b, PnPlano plano){


        if (Ec.Tipo == 0)
            return (-4*Ec.b*((b*b*b)/3 - (a*a*a)/3) + (b*b*b*b) - (a*a*a*a))/(4*Ec.m);
        else if (Ec.Tipo == 2)
            return Ec.A.x*((float)plano.Escala/plano.Escala)*((b*b*b)/3 - (a*a*a)/3);
        else
            return 0;
    }

    /**
     * Integra la ecuacion dada para encontrar la inercia con respecto al eje X
     * @param Ec Ecuacion a integrar
     * @param a Limite inferior de integracion
     * @param b Limite superior de integracion
     * @return Inercia con respecto a X
     */
    public static float integrar_fx(EcuacionRecta Ec, float a, float b, PnPlano plano){
        if (Ec.Tipo == 0)
            return (-3*a*a*a*a*Ec.m + 3*b*b*b*b*Ec.m -4*a*a*a*Ec.b + 4*b*b*b*Ec.b)/12;
        else if (Ec.Tipo == 1)
            return Ec.A.y*((float)plano.Escala/plano.Escala)*((b*b*b)/3 - (a*a*a)/3);
        else
            return 0;
    }
}
