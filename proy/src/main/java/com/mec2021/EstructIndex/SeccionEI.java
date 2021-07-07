package com.mec2021.EstructIndex;

import java.util.ArrayList;

/**
 * Representa una seccion de una estructura indexada.
 * <p>Se puede acceder y extraer una seccion o hoja que sea hijo directo del padre. Por lo que no se puede acceder a un hijo de un hijo de una sola llamada.
 * Se necesita extraer el hijo y luego extraer el hijo de ese hijo.
 */
public class SeccionEI extends NodoEI{

    public ArrayList<NodoEI> Hijos = new ArrayList<NodoEI>();
 
    public SeccionEI(String Nom) {
        super(Nom);
    }

    /**
     * Extrae una seccion con todos sus hijos
     * @param Nombre Nombre de la seccion para extraer
     * @return
     */
    public SeccionEI extraerSeccion(String Nombre){
        for (NodoEI nodo : Hijos) {
            if(nodo.Nombre.equals(Nombre) && nodo instanceof SeccionEI)
                return (SeccionEI)nodo;
        }

        return null;
    }

    /**
     * Obtiene el valor de un nodo hijo de una seccion
     * @param Nombre Nombre del nodo a extraer
     * @return
     */
    public String extraerValorHoja(String Nombre){
        for (NodoEI nodo : Hijos) {
            if(nodo.Nombre.equals(Nombre) && nodo instanceof HojaEI)
                return ((HojaEI)nodo).Valor;
        }

        return null;
    }

    /**
     * Agrega una nueva seccion a esta seccion
     * @param Nombre Nombre de la nueva seccion
     */
    public SeccionEI agregarSeccion(String Nombre){
        
        SeccionEI Sec = new SeccionEI(Nombre); 
        
        Hijos.add(Sec);
        return Sec;
    }

    /**
     * Agrega una seccion existente a esta seccion
     * @param Nombre Nombre de la nueva seccion
     */
    public void agregarSeccion(SeccionEI sec){
        Hijos.add(sec);
    }

    /**
     * Agrega una nueva hoja a esta seccion
     * @param Nombre Nombre de la hoja
     * @param Valor Valor de la hoja
     */
    public void agregarHoja(String Nombre, String Valor){
        Hijos.add(new HojaEI(Nombre, Valor));
    }
}