package PryMecanica.GUI;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

import PryMecanica.PnPlano;
import PryMecanica.Plano.Objetos.Grupo;
import PryMecanica.Plano.Objetos.Formas.Forma;
import PryMecanica.Plano.Objetos.Formas.FrCirc;
import PryMecanica.Plano.Objetos.Formas.FrRect;
import PryMecanica.Plano.Objetos.Formas.FrTria;
import PryMecanica.Plano.Objetos.Objeto2D;


public class ArbolObjetos extends JPanel{
    
    ArrayList<Objeto2D> LstObj = new ArrayList<Objeto2D>();
    ArrayList<PnNodo> LstPaneles = new ArrayList<PnNodo>();
    Nodo Arbol = new Nodo();
    
    public ArbolObjetos(ArrayList<Objeto2D> lst){
        setLayout(null);

        LstObj = lst;

        setOpaque(false);

        setBackground(Color.GRAY);
        setBounds(20, 20, 200,300);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ArrayList<Nodo> LstSinVisitar = new ArrayList<Nodo>();
        LstSinVisitar.add(Arbol);
        Arbol.Altura = 0;

        int i = 0;
        while(LstSinVisitar.size() > 0){
            Nodo NodoVis = LstSinVisitar.get(0);
            LstSinVisitar.remove(NodoVis);

            if(i != 0){
                g.drawLine(35 + (NodoVis.Altura - 1)*30, 40 + (i - 1)*30, 35 + (NodoVis.Altura - 1)*30, 40 + i*30);          
                g.drawLine(35 + (NodoVis.Altura - 1)*30, 40 + i*30, 45 + (NodoVis.Altura - 1)*30, 40 + i*30);       
            }   

            for (Nodo nodo : NodoVis.Hijos){
                nodo.Altura = NodoVis.Altura + 1;
                LstSinVisitar.add(0,nodo);
            }
            i++;
        }
    }

    public void actualizarVisualizacion(){
        ArrayList<Nodo> LstSinVisitar = new ArrayList<Nodo>();
        LstSinVisitar.add(Arbol);
        Arbol.Altura = 0;

        for (PnNodo nd : LstPaneles) {
            remove(nd);
        }
        LstPaneles.clear();

        int i = 0;
        while(LstSinVisitar.size() > 0){
            Nodo NodoVis = LstSinVisitar.get(0);
            LstSinVisitar.remove(NodoVis);

            PnNodo Nd = new PnNodo(NodoVis.Altura, i, NodoVis); 
            add(Nd);
            LstPaneles.add(Nd);
            

            for (Nodo nodo : NodoVis.Hijos){
                nodo.Altura = NodoVis.Altura + 1;
                LstSinVisitar.add(0,nodo);
            }
            i++;
        }

        revalidate();
        repaint();
    }

    public void generarArbol(){

        Arbol = new Nodo();

        for (Objeto2D objeto2d : LstObj) {
            if(objeto2d instanceof Grupo){
                if(!((Grupo)objeto2d).GrupoTemporal)
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

class PnNodo extends JPanel implements MouseListener{
    
    Nodo Nd;
    
    public PnNodo(int Alt, int Pos, Nodo nd){

        if(nd.Objeto == null){
            setBackground(Color.lightGray);
            setBounds(20,10, 200, 40);
        }else{
            setBackground(Color.WHITE);
            setBounds(20 + 30*Alt,30 + Pos*30, 100, 20);
        }
        
        addMouseListener(this);

        Nd = nd;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(Nd.Objeto instanceof FrRect){
            g.fillRect(2,2, 16, 16);
        }else if(Nd.Objeto instanceof FrCirc){
            g.fillOval(2,2,16,16);
        }else if(Nd.Objeto instanceof FrTria){
            int[] xs = {2,10,17};
            int[] ys = {16,2,16};

            g.fillPolygon(xs, ys, 3);
        }

        if(Nd.Objeto != null)
            if(Nd.Objeto instanceof Forma)
                g.drawString(Nd.Objeto.Nombre,25,15);
            else
                g.drawString(Nd.Objeto.Nombre,5,15);
        else
            g.drawString("Objetos",10,15);
    }

    public void mousePressed(MouseEvent e) {
        if(Nd.Objeto != null){
        
            if(PnPlano.PlPrinc.PnPropActual != null){
                PnPlano.PlPrinc.remove(PnPlano.PlPrinc.PnPropActual);
                PnPlano.PlPrinc.PnPropActual = null;
                PnPlano.PlPrinc.repaint();
            }
        
            if(Nd.Objeto instanceof FrRect){
                PnPlano.PlPrinc.add(PnPlano.PlPrinc.PnPropActual = new PropRect(Nd.Objeto));
            }else if(Nd.Objeto instanceof FrCirc){
                PnPlano.PlPrinc.add(PnPlano.PlPrinc.PnPropActual = new PropCirc(Nd.Objeto));
            }else if(Nd.Objeto instanceof FrTria){
                PnPlano.PlPrinc.add(PnPlano.PlPrinc.PnPropActual = new PropTria(Nd.Objeto));
            }else if(Nd.Objeto instanceof Grupo){
                PnPlano.PlPrinc.add(PnPlano.PlPrinc.PnPropActual = new PropGrupo(Nd.Objeto));
            }
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
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