package PryMecanica;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import PryMecanica.gui.ArbolObjetos;
import PryMecanica.gui.ListaOpciones;
import PryMecanica.gui.propiedades.PnPropiedades;
import PryMecanica.plano.Punto;
import PryMecanica.plano.objetos.Grupo;
import PryMecanica.plano.objetos.Objeto2D;
import PryMecanica.plano.objetos.formas.Forma;
import PryMecanica.plano.objetos.formas.FrTria;

/**Area de trabajo, aqui se colocan todas las formas */
public class PnPlano extends JLayeredPane implements MouseInputListener{

    public static PnPlano PlPrinc;

    /**Escala de las coordenadas del plano */
    public static int Escala = 10;

    /**Posicion del origen dentro del Panel del plano */
    public static Punto PtOrigen = new Punto(500,500);

    /**Posicion inicial de la seleccion */
    Punto PtInicioSel = new Punto();

    /**Posicion final de la seleccion s*/
    Punto PtFinSel = new Punto();

    /**Indica si se esta arrastrando el mouse para hacer una seleccion */
    boolean Seleccinando = false;

    /**Indica la forma seleccionada actualmente */
    public Forma FrSel = null;

    /**Lista de todos los objetos dentro del plano */
    public ArrayList<Objeto2D> LstObjetos = new ArrayList<Objeto2D>(); 

    /**Color del area de seleccion */
    Color ColSel = new Color(200, 200, 200, 100);

    /**Indica el grupo seleccionado */
    public static Grupo GrupoSel = null;

    /**Menu contextual */
    public ListaOpciones LOP = new ListaOpciones(100, 100);

    private Point PtOffset = new Point(0,0);

    public ArbolObjetos AB;

    public PnPropiedades PnPropActual = null;

    public PnPlano(){
        setLayout(null);

        addMouseMotionListener(this);
        addMouseListener(this);

        

        PlPrinc = this;

        AB = new ArbolObjetos(LstObjetos);
        
        LOP.setVisible(false);;
        add(LOP, JLayeredPane.DRAG_LAYER);
        add(AB);
    }

    public void seleccionarForma(Forma Fr){
        if(FrSel != null){
            FrSel.eliminarPines();
        }

        FrSel = Fr;
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        
        g.setColor(Color.DARK_GRAY);

        g.drawLine(0, Math.round(PtOrigen.y), getWidth(), Math.round(PtOrigen.y));
        g.drawLine(Math.round(PtOrigen.x), 0,Math.round(PtOrigen.x), getHeight());

        //COORDENADAS X
        for(int ux = -100; ux < 100; ux++){
            g.drawLine(Math.round(PtOrigen.x + ux*Forma.Escala), Math.round(PtOrigen.y - 5), Math.round(PtOrigen.x + ux*Forma.Escala), Math.round(PtOrigen.y + 5));
            g.drawString(""+(ux),Math.round(PtOrigen.x + ux*Forma.Escala), Math.round(PtOrigen.y + 20));
        }

        //COORDENADAS Y
        for(int uy = -100; uy < 100; uy++){
            g.drawLine(Math.round(PtOrigen.x - 5),Math.round(PtOrigen.y + uy*Forma.Escala), Math.round(PtOrigen.x + 5), Math.round(PtOrigen.y + uy*Forma.Escala));
            g.drawString(""+(-uy),Math.round(PtOrigen.x - 20),Math.round(PtOrigen.y + uy*Forma.Escala));
        }

        //DIBUJAR AREA DE SELECCION
        if(Seleccinando){
            g.setColor(ColSel);

            float xIni = PtInicioSel.x;
            float xFin = PtFinSel.x;
            float yIni = PtInicioSel.y;
            float yFin = PtFinSel.y;
    
            if(PtInicioSel.y > PtFinSel.y){
                yIni = PtFinSel.y;
                yFin = PtInicioSel.y;
            }
    
            if(PtInicioSel.x > PtFinSel.x){
                xIni = PtFinSel.x;
                xFin = PtInicioSel.x;
            }

            g.fillRect(Math.round(xIni), 
                       Math.round(yIni),
                       Math.round((float)Math.abs(xFin - xIni)),
                       Math.round((float)Math.abs(yFin- yIni)));
        }

    }


    /** 
    * Notifica al Panel los eventos que suceden
    * <p><b>Tipos de ventos</b>
    * <PRE>
    *    <b   >id           </b   >   <b>Evento   </b>
    *    <code>0</code>:  <code>Se ha agregado o eliminado un objeto</code>
    *    <code>1</code>:  <code>Se ha movido o modificado algun objeto</code>

    * </PRE>
    *
     * @param tipo de evento
     */
    public void notificarCambios(int tipo){
        

        switch(tipo){
            case 0:{
                AB.generarArbol();
                AB.actualizarVisualizacion();
                //System.out.println("Se ha agregado/ Eliminado un objeto");
            }

            case 1:{
                if(PnPropActual != null)
                    PnPropActual.actualizarDatos();

                if(PnPropActual != null)
                    PnPlano.PlPrinc.moveToFront(PnPlano.PlPrinc.PnPropActual);
                //System.out.println("Se han modificado los objetos del plano");
            }
        }


    }



    public void mousePressed(MouseEvent e) {

        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, this);
        
        PtOffset.x = Math.round(PtOrigen.x - Pos.x);
        PtOffset.y = Math.round(PtOrigen.y - Pos.y);

        if(SwingUtilities.isLeftMouseButton(e)){

            //DESELECCIONAR CUALQUIER FORMA
            if(GrupoSel != null){
                moveToFront(GrupoSel);
                GrupoSel.MoviendoFormas = false;

                if(GrupoSel.GrupoTemporal)
                    GrupoSel.eliminarGrupo();
                
                GrupoSel = null;
            }

            seleccionarForma(null);

            Pos = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(Pos, this);

            PtInicioSel.x = Pos.x;
            PtInicioSel.y = Pos.y;

        }else if(SwingUtilities.isRightMouseButton(e)){
            //ABRIR MENU CONTEXTUAL
            LOP.setLocation(e.getX(), e.getY());
            moveToFront(LOP);
            LOP.setVisible(true);
            repaint();
        }
    }
    
    public void mouseDragged(MouseEvent e) {

        //SELECCIONAR
        if(SwingUtilities.isLeftMouseButton(e)){
            Point Pos = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(Pos, this);

            PtFinSel.x = Pos.x;
            PtFinSel.y = Pos.y;

            Seleccinando = true;

            repaint();
        }

        //MOVER EL AREA DEL PLANO
        if(SwingUtilities.isMiddleMouseButton(e)){
            Point Pos = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(Pos, this);

            seleccionarForma(null);

            int DifX = Math.round(Pos.x  - PtOrigen.x + PtOffset.x);
            int DifY = Math.round(Pos.y  - PtOrigen.y + PtOffset.y);
    
            PnPlano.PtOrigen.x = Pos.x + PtOffset.x;
            PnPlano.PtOrigen.y = Pos.y + PtOffset.y;
    
            for (Objeto2D obj : PnPlano.PlPrinc.LstObjetos) {
                obj.setBounds(obj.getX() + DifX, obj.getY() + DifY, obj.getWidth(), obj.getHeight());
                if(obj instanceof FrTria){
                    //ACTUALIZAR VERTICES
                    ((FrTria)obj).Ver1.x += DifX;
                    ((FrTria)obj).Ver1.y += DifY;
    
                    ((FrTria)obj).Ver2.x += DifX;
                    ((FrTria)obj).Ver2.y += DifY;
    
                    ((FrTria)obj).Ver3.x += DifX;
                    ((FrTria)obj).Ver3.y += DifY;
                }
            }
    
            PnPlano.PlPrinc.repaint();
        }
    }

    public void mouseReleased(MouseEvent e) {

        if(!Seleccinando)
            return;
        
        Seleccinando = false;

        Grupo NuevoGrupo = null;

        //AJUSTAR LA POSICION Y LAS DIMENSIONES DEL RECTANGULO DE SELECCION
        float xIni = PtInicioSel.x;
        float xFin = PtFinSel.x;
        float yIni = PtInicioSel.y;
        float yFin = PtFinSel.y;

        if(PtInicioSel.y > PtFinSel.y){
            yIni = PtFinSel.y;
            yFin = PtInicioSel.y;
        }

        if(PtInicioSel.x > PtFinSel.x){
            xIni = PtFinSel.x;
            xFin = PtInicioSel.x;
        }

        //AGRUPAR TODAS LAS FORMAS DENTRO DEL AREA DE SELECCION
        for(int i = 0; i < LstObjetos.size(); i++){
            
            Objeto2D objeto2d = LstObjetos.get(i);

            if(objeto2d instanceof Forma){

                //VERIFICAR SI LA FORMAR ESTA DENTRO DEL AREA DE SELECCION
                if(objeto2d.getX() >= xIni && 
                   objeto2d.getY() >= yIni &&
                   (objeto2d.getX() + objeto2d.getWidth()) <= xFin && 
                   (objeto2d.getY() + objeto2d.getHeight()) <= yFin){

                    //CREAR UN NUEVO GRUPO SI NO EXISTE
                    if(NuevoGrupo == null){
                        NuevoGrupo = new Grupo();
                        NuevoGrupo.MoviendoFormas = true;
                    }

                    //SI LA FORMA YA ESTA EN OTRO GRUPO, SE SACA LA FORMA DE ESE GRUPO Y SE AGREGA AL NUEVO
                    if(((Forma)objeto2d).Grp != null){
                        ((Forma)objeto2d).Grp.sacarForma((Forma)objeto2d);
                    }
                    NuevoGrupo.agregarForma((Forma)objeto2d);
                }
            }
        }



        //CREAR UN GRUPO NUEVO SI ESTE TIENE MAS DE UNA FORMA
        if(NuevoGrupo != null){        
            if(NuevoGrupo.LstForma.size() > 1){
                add(NuevoGrupo, JLayeredPane.DRAG_LAYER);
                moveToFront(NuevoGrupo);
                LstObjetos.add(NuevoGrupo);
            }else{
                NuevoGrupo.vaciarGrupo();
            }
        }

        repaint();
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}
