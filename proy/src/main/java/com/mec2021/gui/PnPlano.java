package com.mec2021.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.awt.BasicStroke;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseWheelListener;

import com.mec2021.Ctrl;
import com.mec2021.gui.propiedades.PnPropiedades;
import com.mec2021.plano.Punto;
import com.mec2021.plano.objetos.Grupo;
import com.mec2021.plano.objetos.Objeto2D;
import com.mec2021.plano.objetos.formas.Forma;
import com.mec2021.plano.objetos.formas.FrTria;

/**Area de trabajo, aqui se colocan todas las formas */
public class PnPlano extends JLayeredPane implements MouseInputListener, MouseWheelListener{

    /**Escala de las coordenadas del plano */
    public int Escala = 1;

    /**Escala de unidad : Pixel */
    public int EscalaPix = 50;

    public float EscalaVieja = Escala/EscalaPix;

    /**Posicion del origen dentro del Panel del plano */
    public Punto PtOrigen = new Punto(500,500);

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
    public Grupo GrupoSel = null;

    /**Menu contextual */
    public ListaOpciones LOP = new ListaOpciones(100, 100, this);

    private Point PtOffset = new Point(0,0);

    public ArbolObjetos AB;

    public PnPropiedades PnPropActual = null;

    public boolean SnapActivoX = false;
    public boolean SnapActivoY = false;

    public int SnapX = 0;
    public int SnapY = 0;

    public PnPlano(){
        setLayout(null);

        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);

        setBackground(Color.WHITE);
        setOpaque(true);

        AB = new ArbolObjetos(LstObjetos, this);
        
        LOP.setVisible(false);

        notificarCambios(0);

        // Grupo NuevoGrupo = new Grupo(this);

        
        // FrRect Rec = new FrRect(0,-80,120,80,this);
        // FrTria Tria = new FrTria(0,0,0,0,0,60,120f,0,this);
        // FrCirc Cir1 = new FrCirc(0,-140,120,0,180,this);
        // FrCirc Cir2 = new FrCirc(20,-120,80,0,360,this);
        
        
        // add(Rec, JLayeredPane.DRAG_LAYER);
        // add(Tria, JLayeredPane.DRAG_LAYER);
        // add(Cir1, JLayeredPane.DRAG_LAYER);
        // add(Cir2, JLayeredPane.DRAG_LAYER);
        
        // moveToFront(Cir2);

        // NuevoGrupo.agregarForma(Rec);
        // NuevoGrupo.agregarForma(Tria);
        // NuevoGrupo.agregarForma(Cir1);
        // NuevoGrupo.agregarForma(Cir2);

        // add(NuevoGrupo, JLayeredPane.DRAG_LAYER);
        // moveToFront(NuevoGrupo);
        // LstObjetos.add(NuevoGrupo);

        add(LOP, JLayeredPane.DRAG_LAYER);
        add(AB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        
        g2.setColor(Color.DARK_GRAY);

        g2.drawLine(0, Math.round(PtOrigen.y), getWidth(), Math.round(PtOrigen.y));
        g2.drawLine(Math.round(PtOrigen.x), 0,Math.round(PtOrigen.x), getHeight());
        g2.setFont(Ctrl.Fnt0);

        //COORDENADAS X
        for(int ux = -100; ux < 100; ux++){
            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(Math.round(PtOrigen.x + ux*EscalaPix), Math.round(PtOrigen.y - 2), Math.round(PtOrigen.x + ux*EscalaPix), Math.round(PtOrigen.y + 2));
            g2.setColor(Ctrl.ClGris);
            if(ux != 0)
                g2.drawString(""+(ux*Escala),Math.round(PtOrigen.x + ux*EscalaPix), Math.round(PtOrigen.y + 20));
        }

        //COORDENADAS Y
        for(int uy = -100; uy < 100; uy++){
            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(Math.round(PtOrigen.x - 2),Math.round(PtOrigen.y + uy*EscalaPix), Math.round(PtOrigen.x + 2), Math.round(PtOrigen.y + uy*EscalaPix));
            g2.setColor(Ctrl.ClGris);
            if(uy != 0)
                g2.drawString(""+(-uy*Escala),Math.round(PtOrigen.x - 20),Math.round(PtOrigen.y + uy*EscalaPix));
        }

        g2.drawString("0",Math.round(PtOrigen.x - 18),Math.round(PtOrigen.y + 20));

        //DIBUJAR AREA DE SELECCION
        if(Seleccinando){
            g2.setColor(ColSel);

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

            g2.fillRect(Math.round(xIni), 
                       Math.round(yIni),
                       Math.round((float)Math.abs(xFin - xIni)),
                       Math.round((float)Math.abs(yFin- yIni)));
        }

        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(1.5f));
        if(SnapActivoY){
            g2.drawLine(0, SnapY, getWidth(), SnapY);
            SnapActivoY = false;
        }
        if(SnapActivoX){
            g2.drawLine(SnapX, 0,SnapX, getHeight());
            SnapActivoX = false;
        }

    }

    /** 
    * Notifica al Panel los eventos que suceden
    * <p><b>Tipos de ventos</b>
    * <PRE>
    *    <b   >id           </b   >   <b>Evento   </b>
    *    <code>0</code>:  <code>Se ha agregado o eliminado un objeto</code>
    *    <code>1</code>:  <code>Se ha movido o modificado algun objeto</code>
    *    <code>2</code>:  <code>Se ha cambiado la escala</code>
    * </PRE>
    *
     * @param tipo de evento
     */
    public void notificarCambios(int tipo){
        

        switch(tipo){
            case 0:{
                AB.generarArbol();
                AB.actualizarVisualizacion();
                break;
            }

            case 1:{
                if(PnPropActual != null)
                    PnPropActual.actualizarDatos();

                if(PnPropActual != null)
                    moveToFront(PnPropActual);
                break;
            }

            case 2:{
                for (Objeto2D objeto2d : LstObjetos) 
                    objeto2d.actualizarDimensiones();
                
                repaint();
                break;
            }
        }


    }


    public void seleccionarForma(Forma Fr){
        if(FrSel != null){
            FrSel.eliminarPines();
        }

        FrSel = Fr;

        if(Fr != null)
            Fr.mostrarPines();
    }
    
    /**
     * Elimina una forma de los objetos actuales
     * <p> si la forma esta en un grupo, entonces el grupo se elimina
     * @param Fr
     */
    public void eliminarForma(Forma Fr){
        if(Fr.Grp != null){
            Fr.Grp.eliminarGrupo();
        }

        Fr.eliminarPines();

        if(PnPropActual != null)
            if(PnPropActual.ObjRef == Fr)
                remove(PnPropActual);

        LstObjetos.remove(Fr);
        remove(Fr);

        notificarCambios(0);
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
            // LOP.setLocation(e.getX(), e.getY());
            // moveToFront(LOP);
            // LOP.setVisible(true);
            // repaint();
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
    
            PtOrigen.x = Pos.x + PtOffset.x;
            PtOrigen.y = Pos.y + PtOffset.y;
    
            for (Objeto2D obj : LstObjetos) {
                obj.setBounds(obj.getX() + DifX, obj.getY() + DifY, obj.getWidth(), obj.getHeight());
                if(obj instanceof FrTria){
                    //ACTUALIZAR VERTICES
                    // ((FrTria)obj).Ver1.x += DifX;
                    // ((FrTria)obj).Ver1.y += DifY;
    
                    // ((FrTria)obj).Ver2.x += DifX;
                    // ((FrTria)obj).Ver2.y += DifY;
    
                    // ((FrTria)obj).Ver3.x += DifX;
                    // ((FrTria)obj).Ver3.y += DifY;
                }
            }
    
            repaint();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //MODIFICAR ZOOM
        EscalaVieja = (float)Escala/EscalaPix;
        if (e.getWheelRotation() > 0 && Escala < 10000)
            EscalaPix -= 5;
        else if (e.getWheelRotation() < 0)
            EscalaPix += 5;
        
        if(EscalaPix <= 25){
            EscalaPix = 50;
            Escala *= 5;
        }
        
        if(EscalaPix >= 100){
            EscalaPix = 50;
            Escala /= 5;
        }

        notificarCambios(2);
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
                        NuevoGrupo = new Grupo(this);
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
                NuevoGrupo.eliminarGrupo();
            }
        }

        repaint();
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}


}
