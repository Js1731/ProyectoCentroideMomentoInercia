package com.mec2021.EstructIndex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Estructura basada en un arbol de nodos. Esta compuesto por dos tipos de nodos.
 * <p>Se puede acceder a una hoja del arbol o extraer una seccion de este
 * <p>Partes
 * <ol>
 * <li>{@link SeccionEI}
 * <li>{@link HojaEI}
 * </ol>
 * 
 */
final public class EstructuraInd {
    /**
     * Genera una nueva estructura indexada usando como base un String con toda la informacion
     * @param Info
     * @return Seccion con la raiz de la estructura
     */
    public static SeccionEI generarEstructuraInd(String Info){
        
        Scanner Sc = new Scanner(Info);
        String Linea = Sc.nextLine();
        
        SeccionEI NuevaEI = new SeccionEI(extraerValor(Linea, 1));
        int Limite = Integer.parseInt(extraerValor(Linea, 2));

        iniciarSeccion(NuevaEI, Sc, Limite);
        
        return NuevaEI;
    }

    /**
     * Genera una nueva estructura indexada usando como base un Archivo del que se tiene que obtner un texto
     * @param Fl Archivo que se tiene que buscar
     * @return
     */
    public static SeccionEI generarEstructuraInd(File Fl){
        
        Scanner Sc;
        String Linea = "";
        try {
            Sc = new Scanner(Fl);
            Linea = Sc.nextLine();
            SeccionEI NuevaEI = new SeccionEI(extraerValor(Linea, 1));
            int Limite = Integer.parseInt(extraerValor(Linea, 2));
            iniciarSeccion(NuevaEI, Sc, Limite);

            return NuevaEI;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public static SeccionEI generarEstructuraIndVacia(String Nombre){
        return new SeccionEI(Nombre);
    }

    /**
     * Obtiene todos los nodos de una seccion y los agrega a la seccion especificada
     * @param Sec Seccion que se va a inicializar
     * @param Sc Scanner para leer la informacion
     * @param Limite Cantidad de nodos hijos de la seccion
     */
    public static void iniciarSeccion(SeccionEI Sec, Scanner Sc, int Limite){
        for (int i = 0; i < Limite; i++) {
            String Linea = Sc.nextLine();
            char Tipo = Linea.charAt(0);

            NodoEI NuevoNodo;

            if(Tipo == 'n'){
                NuevoNodo = new SeccionEI(extraerValor(Linea, 1));
                int Lim = Integer.parseInt(extraerValor(Linea, 2));

                iniciarSeccion((SeccionEI)NuevoNodo, Sc, Lim);
            }else{
                NuevoNodo = new HojaEI(extraerValor(Linea, 1), extraerValor(Linea, 2));
            }

            Sec.Hijos.add(NuevoNodo);
        }

    }

    /**
     * Transforma una estructura a un String
     * @param Arbol
     * @return
     */
    public static String transformarEstrString(SeccionEI Arbol){
        String Res = "";
        ArrayList<NodoEI> Nodos = new ArrayList<NodoEI>();

        Nodos.add(Arbol);

        do{
            NodoEI Nd = Nodos.get(0);
            Nodos.remove(0);
    
            if(Nd instanceof SeccionEI){
                Res += "n|"+Nd.Nombre+"|"+((SeccionEI)Nd).Hijos.size()+"\n";    
                Nodos.addAll(0, ((SeccionEI)Nd).Hijos);
            }else{
                Res += "h|"+Nd.Nombre+"|"+((HojaEI)Nd).Valor+"\n";    
            }
        }while(Nodos.size() > 0);

        return Res;
    }

    /**
     * Extrae una seccion de una string estrucurado como:
     * <p> Dato1|Dato2|Dato3
     *  Pos 0 = Dato1
     *  Pos 1 = Dato2
     *  Pos 2 = Dato3
     * 
     * @param Str String estructurado
     * @param Pos Posicion dentro de la estructura
     * @return
     */
    public static String extraerValor(String Str, int Pos){
        String Res = "";
        int p = 0;
        int sec = 0;
        boolean EnSeccion = Pos == 0;

        //MOVER HASTA LA SECCION ESPECIFICADA
        while(p < Str.length()){
            if(! EnSeccion){
                if(Str.charAt(p) == '|')
                    sec++;
                if(sec == Pos)
                    EnSeccion = true;
            }else{
                char car = Str.charAt(p); 
                if(car == '|')
                    break;
                
                Res += car;
            }
            p++;
        }

        return Res;
    }
}