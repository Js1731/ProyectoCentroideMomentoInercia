package com.mec2021.EstructIndex;

/**
 * Representa una hoja dentro de un arbol de una estructura indexada.
 * <p>Se representa asi:
 * <p>n-Nombre-Valor
 */
public class HojaEI extends NodoEI{

    String Valor = "none";

    public HojaEI(String Nom, String val) {
        super(Nom);

        Valor = val;
    }

}