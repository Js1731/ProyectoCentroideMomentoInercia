package PryMecanica.GUI;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;

import PryMecanica.Plano.Objetos.Grupo;
import PryMecanica.Plano.Objetos.Formas.Forma;
import PryMecanica.Plano.Objetos.Objeto2D;


public class ArbolObjetos extends JPanel{
    
    ArrayList<Objeto2D> LstObj = new ArrayList<Objeto2D>();
    Nodo Arbol = new Nodo();
    
    public ArbolObjetos(ArrayList<Objeto2D> lst){
        setLayout(null);

        LstObj = lst;

        setBackground(Color.GRAY);
        setBounds(20, 20, 200,300);

        generarArbol();
        actualizarVisualizacion();
    }

    public void actualizarVisualizacion(){
        ArrayList<Nodo> LstSinVisitar = new ArrayList<Nodo>();
        LstSinVisitar.add(Arbol);
        Arbol.Altura = 0;

        int i = 0;
        while(LstSinVisitar.size() > 0){
            Nodo NodoVis = LstSinVisitar.get(0);
            LstSinVisitar.remove(NodoVis);

            add(new PnNodo(NodoVis.Altura, i));

            for (Nodo nodo : NodoVis.Hijos){
                nodo.Altura = NodoVis.Altura + 1;
                LstSinVisitar.add(0,nodo);
            }
            i++;
        }

        repaint();
    }

    public void generarArbol(){

        Arbol = new Nodo();

        for (Objeto2D objeto2d : LstObj) {
            if(objeto2d instanceof Grupo){
                if(Nodo.buscarObjeto(Arbol, objeto2d) == null)
                    Arbol.Hijos.add(new Nodo(objeto2d));
            }else{
                if(((Forma)objeto2d).Grp == null){
                    Arbol.Hijos.add(new Nodo(objeto2d));
                }else{
                    Nodo NdGrupo = Nodo.buscarObjeto(Arbol, ((Forma)objeto2d).Grp);

                    if(NdGrupo == null){
                        NdGrupo = new Nodo(((Forma)objeto2d).Grp);
                        Arbol.Hijos.add(NdGrupo);
                    }
                    NdGrupo.Hijos.add(new Nodo(objeto2d));
                }
            }
        }
    }
}

class PnNodo extends JPanel{
    public PnNodo(int Alt, int Pos){
        setBackground(Color.DARK_GRAY);
        setBounds(20 + 20*Alt,30 + Pos*30, 100, 20);
    }
}

class Nodo{
    Objeto2D Objeto = null;
    ArrayList<Nodo> Hijos = new ArrayList<Nodo>();
    int Altura = 0;

    public Nodo(){
        this(null);
    }

    public Nodo(Objeto2D objeto){
        Objeto = objeto;
    }

        
    //https://es.wikipedia.org/wiki/Algoritmos_de_b%C3%BAsqueda_en_grafos#Pseudoc%C3%B3digo_de_b%C3%BAsqueda    
    /**Buscar un objeto en el arbol 
     * 
     * @param raiz
     * @param obj
     * @return
    */
    public static Nodo buscarObjeto(Nodo raiz, Objeto2D obj){
        ArrayList<Nodo> LstSinVisitar = new ArrayList<Nodo>();
        LstSinVisitar.add(raiz);

        while(LstSinVisitar.size() > 0){
            Nodo NodoVis = LstSinVisitar.get(0);
            LstSinVisitar.remove(NodoVis);

            if(NodoVis.Objeto == obj)
                return NodoVis;
            else
                for (Nodo nodo : NodoVis.Hijos) 
                    LstSinVisitar.add(nodo);
        }

        return null;
    }
}