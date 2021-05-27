package PryMecanica.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import PryMecanica.Ctrl;
import PryMecanica.PnPlano;
import PryMecanica.plano.objetos.Grupo;
import PryMecanica.plano.objetos.formas.Forma;
import PryMecanica.plano.objetos.formas.FrCirc;
import PryMecanica.plano.objetos.formas.FrRect;
import PryMecanica.plano.objetos.formas.FrTria;
import PryMecanica.plano.objetos.Objeto2D;

import PryMecanica.gui.propiedades.*;

/**Arbol usado para representar todos los {@link Objeto2D} que estan dentro de un plano*/
public class ArbolObjetos extends JPanel{
    
    /**Lista de los objetos que el arbol debe representar */
    ArrayList<Objeto2D> LstObj = new ArrayList<Objeto2D>();

    /**Lista de todos los {@link PnNodo} presentes en el arbol*/
    ArrayList<PnNodo> LstPaneles = new ArrayList<PnNodo>();

    /**Estructura de datos de los objetos*/
    Nodo Arbol = new Nodo();

    public static int Altura = 30;
    
    public ArbolObjetos(ArrayList<Objeto2D> lst){
        setLayout(null);

        LstObj = lst;

        setOpaque(false);

        setBackground(Color.GRAY);
        setBounds(-5, -5, 200,300);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ArrayList<Nodo> LstSinVisitar = new ArrayList<Nodo>();
        LstSinVisitar.add(Arbol);
        Arbol.Altura = 0;

        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Ctrl.ClGrisClaro2);
        g2.setStroke(new BasicStroke(1.25f));

        //DIBUJAR LAS LINEAS DEL ARBOL
        int i = 0;
        while(LstSinVisitar.size() > 0){
            Nodo NodoVis = LstSinVisitar.get(0);
            LstSinVisitar.remove(NodoVis);

            //NO DIBUJAR LA LINEA DE LA RAIZ DEL ARBOL
            if(i != 0){
                g2.drawLine(35 + (NodoVis.Altura - 1)*30, 45 + (i - 1)*Altura, 35 + (NodoVis.Altura - 1)*30, 45 + i*Altura);          
                g2.drawLine(35 + (NodoVis.Altura - 1)*30, 45 + i*Altura,45 + (NodoVis.Altura - 1)*30 , 45 + i*Altura);       
            }   

            for(int e = NodoVis.Hijos.size() - 1; e >= 0; e--){
                
                Nodo nodo = NodoVis.Hijos.get(e);

                nodo.Altura = NodoVis.Altura + 1;
                LstSinVisitar.add(0,nodo);
            }
            i++;
        }

        if(i != 1){
            try{
                g2.drawLine(35 , 70 , 35 , 40 + (i - Arbol.Hijos.get(Arbol.Hijos.size() - 1).Hijos.size() - 1)*Altura);
            }catch(ArrayIndexOutOfBoundsException e){}
        }

        setBounds(-5, -5, 200, 40 + i*Altura);
    }

    /**Actualiza todos los {@link PnNodo} dentro del panel*/
    public void actualizarVisualizacion(){
        ArrayList<Nodo> LstSinVisitar = new ArrayList<Nodo>();
        LstSinVisitar.add(Arbol);
        Arbol.Altura = 0;

        //ELIMINAR TODOS LOS PANELES DENTRO DEL ARBOL
        for (PnNodo nd : LstPaneles) {
            remove(nd);
        }
        LstPaneles.clear();

        //CREAR TODOS LOS PANELES DEL ARBOL
        int i = 0;
        while(LstSinVisitar.size() > 0){
            Nodo NodoVis = LstSinVisitar.get(0);
            LstSinVisitar.remove(NodoVis);

            PnNodo Nd = new PnNodo(NodoVis.Altura, i, NodoVis); 
            add(Nd);
            LstPaneles.add(Nd);
            

            for(int e = NodoVis.Hijos.size() - 1; e >= 0; e--){
                
                Nodo nodo = NodoVis.Hijos.get(e);

                nodo.Altura = NodoVis.Altura + 1;
                LstSinVisitar.add(0,nodo);
            }
            i++;
        }

        revalidate();
        repaint();
    }

    /**Genera un arbol usando la {@code LstObj} del arbol*/
    public void generarArbol(){

        Arbol = new Nodo();

        for (Objeto2D objeto2d : LstObj) {
            if(objeto2d instanceof Grupo){

                /** 
                 * Si el objeto es un grupo:
                 * Se agrega como hijo de la raiz si no es un grupo temporal 
                 * y si no esta presente dentro del arbol todavia
                */

                if(!((Grupo)objeto2d).GrupoTemporal)
                    if(Nodo.buscarObjeto(Arbol, objeto2d) == null)
                        Arbol.Hijos.add(new Nodo(objeto2d));
            }else{
                /**
                 * Si el Objeto es una Forma:
                 * Si el objeto pertenece a un grupo entonces se busca el nodo dentro del arbol
                 * y se agrega el objeto como nodo hijo del grupo
                 * Si no pertenece a un grupo entonces se agrega como nodo hijo de la raiz
                 */
                if(((Forma)objeto2d).Grp == null){
                    Arbol.Hijos.add(new Nodo(objeto2d));
                }else{
                    Nodo NdGrupo = Nodo.buscarObjeto(Arbol, ((Forma)objeto2d).Grp);

                    if(NdGrupo == null){
                        NdGrupo = new Nodo(((Forma)objeto2d).Grp);
                        Arbol.Hijos.add(Arbol.Hijos.size(),NdGrupo);
                    }
                    NdGrupo.Hijos.add(new Nodo(objeto2d));
                }
            }
        }
    }
}

/**Panel para la representacion de un nodo del arbol */
class PnNodo extends JPanel implements MouseListener{
    
    /**Nodo del arbol */
    Nodo Nd;
    
    /**
     * Crea un nuevo panel para representar
     * @param Alt Altura del nodo dentro del arbol
     * @param Pos Posicion vertical del nodo
     * @param nd Nodo que se va a representar
     */
    public PnNodo(int Alt, int Pos, Nodo nd){

        if(nd.Objeto == null){
            setBackground(Ctrl.ClGris);
            setBounds(20,30, 200, 30);
        }else{
            setBackground(Color.WHITE);
            setBounds(20 + 30*Alt,30 + Pos*ArbolObjetos.Altura, 150, ArbolObjetos.Altura);
        }
        
        addMouseListener(this);

        Nd = nd;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Dibujar Icono para representar la forma
        g2.setColor(Ctrl.ClGrisClaro);
        if(Nd.Objeto instanceof FrRect){
            g2.fillRect(5, 5, 20, 20);
        }else if(Nd.Objeto instanceof FrCirc){
            g2.fillOval(5, 5, 20, 20);
        }else if(Nd.Objeto instanceof FrTria){
            int[] xs = {5,15,25};
            int[] ys = {25,5,25};

            g2.fillPolygon(xs, ys, 3);
        }

        g2.setFont(Ctrl.Fnt1);
        g2.setColor(Color.BLACK);
        //Representar Grupo
        if(Nd.Objeto != null)
            if(Nd.Objeto instanceof Forma)
                g2.drawString(Nd.Objeto.Nombre,35,20);
            else
                g2.drawString(Nd.Objeto.Nombre,5,20);
        else{
            g2.setColor(Color.WHITE);
            g2.setFont(Ctrl.Fnt2);
            g2.drawString("Objetos",10,22);
        }
    }

    public void mousePressed(MouseEvent e) {
        if(Nd.Objeto != null){
        
            //ELIMINAR OTROS PANELES ACTIVOS
            if(PnPlano.PlPrinc.PnPropActual != null){
                PnPlano.PlPrinc.remove(PnPlano.PlPrinc.PnPropActual);
                PnPlano.PlPrinc.PnPropActual = null;
                PnPlano.PlPrinc.repaint();
            }
        
            //ABRIR EL PANEL DE PROPIEDADES DE LA FORMA
            if(Nd.Objeto instanceof FrRect){
                PnPlano.PlPrinc.add(PnPlano.PlPrinc.PnPropActual = new PropRect(Nd.Objeto), JLayeredPane.DRAG_LAYER);
            }else if(Nd.Objeto instanceof FrCirc){
                PnPlano.PlPrinc.add(PnPlano.PlPrinc.PnPropActual = new PropCirc(Nd.Objeto), JLayeredPane.DRAG_LAYER);
            }else if(Nd.Objeto instanceof FrTria){
                PnPlano.PlPrinc.add(PnPlano.PlPrinc.PnPropActual = new PropTria(Nd.Objeto), JLayeredPane.DRAG_LAYER);
            }else if(Nd.Objeto instanceof Grupo){
                PnPlano.PlPrinc.add(PnPlano.PlPrinc.PnPropActual = new PropGrupo(Nd.Objeto), JLayeredPane.DRAG_LAYER);
            }

            PnPlano.PlPrinc.moveToFront(PnPlano.PlPrinc.PnPropActual);
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