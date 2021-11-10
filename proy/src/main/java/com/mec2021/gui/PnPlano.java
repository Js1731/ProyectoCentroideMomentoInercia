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
import java.io.IOException;

import com.mec2021.Ctrl;
import com.mec2021.EstructIndex.EstructuraInd;
import com.mec2021.EstructIndex.NodoEI;
import com.mec2021.EstructIndex.SeccionEI;
import com.mec2021.gui.agregarforma.PnAgCirc;
import com.mec2021.gui.agregarforma.PnAgRect;
import com.mec2021.gui.agregarforma.PnAgTria;
import com.mec2021.gui.agregarforma.PnAgregarForma;
import com.mec2021.gui.propiedades.PnPropiedades;
import com.mec2021.plano.PnCentroide;
import com.mec2021.plano.Punto;
import com.mec2021.plano.objetos.Grupo;
import com.mec2021.plano.objetos.Objeto2D;
import com.mec2021.plano.objetos.formas.Forma;
import com.mec2021.plano.objetos.formas.FrCirc;
import com.mec2021.plano.objetos.formas.FrRect;
import com.mec2021.plano.objetos.formas.FrTria;


import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

/**Area de trabajo, aqui se colocan todas las formas */
public class PnPlano extends JLayeredPane implements MouseInputListener, MouseWheelListener{

    /**Escala de las coordenadas del plano */
    public int Escala = 1;

    /**Escala de unidad : Pixel */
    public int EscalaPix = 50;

    public float EscalaVieja = Escala/EscalaPix;

    /**Posicion del origen dentro del Panel del plano */
    public Punto PtOrigen = new Punto(0,0);

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

    public PnAgregarForma PnAgActivo = null;

    private Point PtOffset = new Point(0,0);

    public ArbolObjetos AB;

    public PnPropiedades PnPropActual = null;

    public boolean SnapActivoX = false;
    public boolean SnapActivoY = false;

    public int SnapX = 0;
    public int SnapY = 0;

    public PnCentroide PnCent = new PnCentroide(this);

    public BotonGenerico BtMostrarCent;

    public PnPlano(){
        setLayout(null);
        revalidate();

        addMouseMotionListener(this);
        addMouseListener(this);
        addMouseWheelListener(this);

        setBackground(Color.WHITE);
        setOpaque(true);

        AB = new ArbolObjetos(LstObjetos, this);
        
        LOP.setVisible(false);


        BtMostrarCent = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D)g;
                
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if(PnPrincipal.VerCentroide){
                    g2.setColor(Color.red);
                    g2.fillOval(20, 20, 3, 3);
                    if(MouseEncima)
                        g2.setColor(Ctrl.ClGrisClaro);
                    else
                        g2.setColor(Ctrl.ClGris);
                }else
                    if(MouseEncima)
                        g2.setColor(Ctrl.ClGris2);
                    else    
                        g2.setColor(Ctrl.ClGrisClaro);

                g2.fillOval(0, 0, getWidth(), getHeight());
                
                if(PnPrincipal.VerCentroide){
                    g2.setColor(Ctrl.ClGrisClaro);
                    g2.fillOval(17, 17, 6, 6);
                }

                g2.setColor(Ctrl.ClGrisClaro3);
                g2.drawRect(10, 10, 20, 20);

            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                PnPrincipal.VerCentroide = !PnPrincipal.VerCentroide; 
            }
        };

        BtMostrarCent.setOpaque(false);
        BtMostrarCent.setBounds(getWidth() - 60, getHeight() - 60, 40, 40);


        notificarCambios(0);
        add(LOP, JLayeredPane.DRAG_LAYER);
        add(PnCent, JLayeredPane.DRAG_LAYER);
        add(AB);
        add(BtMostrarCent);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        float CX = centroideX();
        float CY = centroideY();
        float IY = inerciaCentEjeY();
        float IX = inerciaCentEjeX();

        PnCent.setBounds(Math.round(PtOrigen.x + Ctrl.aplicarEscalaUPix(CX)) - 60, 
                        Math.round(PtOrigen.y + Ctrl.aplicarEscalaUPix(CY)) - 117,
                        120,
                        120);
        
        BtMostrarCent.setBounds(getWidth() - 60, getHeight() - 60, 40, 40);

        PnCent.CX = CX;
        PnCent.CY = CY;
        PnCent.IX = IX;
        PnCent.IY = IY;

        moveToFront(PnCent);

        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        

        g2.setFont(Ctrl.Fnt0);

        //COORDENADAS X
        for(int ux = -100; ux < 100; ux++){
            g2.setColor(Color.DARK_GRAY);
            if(ux != 0){
                g2.setColor(Ctrl.ClGrisClaro2);
                g2.drawLine(Math.round(PtOrigen.x + ux*EscalaPix), 0, Math.round(PtOrigen.x + ux*EscalaPix), getHeight());
                g2.setColor(Ctrl.ClGris);
                g2.drawString(""+(ux*Escala),Math.round(PtOrigen.x + ux*EscalaPix), Math.round(PtOrigen.y + 20));
            }
        }

        //COORDENADAS Y
        for(int uy = -100; uy < 100; uy++){
            g2.setColor(Color.DARK_GRAY);
            if(uy != 0){
                g2.setColor(Ctrl.ClGrisClaro2);
                g2.drawLine(0,Math.round(PtOrigen.y + uy*EscalaPix), getWidth(), Math.round(PtOrigen.y + uy*EscalaPix));
                g2.setColor(Ctrl.ClGris);
                g2.drawString(""+(-uy*Escala),Math.round(PtOrigen.x - 20),Math.round(PtOrigen.y + uy*EscalaPix));
            }
        }

        g2.setColor(Color.DARK_GRAY);

        g2.drawLine(0, Math.round(PtOrigen.y), getWidth(), Math.round(PtOrigen.y));
        g2.drawLine(Math.round(PtOrigen.x), 0,Math.round(PtOrigen.x), getHeight());

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

    /**Calcula la posicion X del centroide con respecto al origen */
    public float centroideX(){
        float SumaAreas = 0;
        float SumaAreasPorX = 0;

        for (Objeto2D forma : LstObjetos) {

            if(forma instanceof Forma){
                Forma fr = (Forma)forma;

                float Area = fr.calcularArea();
                SumaAreas += Area;

                float CentX = fr.X + fr.centroideX();

                SumaAreasPorX += CentX*Area;
            }
        }

        float x = (SumaAreasPorX/SumaAreas);

        return x;
    }

    /**Calcula la posicion Y del centroide con respecto al origen */
    public float centroideY(){
        float SumaAreas = 0;
        float SumaAreasPorY = 0;

        for (Objeto2D forma : LstObjetos) {

            if(forma instanceof Forma){
                Forma fr = (Forma)forma;

                float Area = fr.calcularArea();
                SumaAreas += Area;

                float CentY = fr.Y + fr.centroideY();

                SumaAreasPorY += CentY*Area;
            }
        }

        float y = SumaAreasPorY/SumaAreas;

        return y;
    }

    public float inerciaCentEjeX(){

        float SumaIx = 0;

        for (Objeto2D forma : LstObjetos) 
            if(forma instanceof Forma){
                Forma fr = (Forma)forma;
                SumaIx += fr.inerciaCentEjeX();
            }
        
        return SumaIx;
    }

    public float inerciaCentEjeY(){

        float SumaIy = 0;

        for (Objeto2D forma : LstObjetos) 

            if(forma instanceof Forma){
                Forma fr = (Forma)forma;
                SumaIy += fr.inerciaCentEjeY();
            }
        
        return SumaIy;
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

    public SeccionEI generarData(){

        SeccionEI Raiz = new SeccionEI(PnPrincipal.TabSel.LbNombre.getText());
        Raiz.agregarHoja("t", "a");
        Raiz.agregarHoja("p", "" + EscalaPix);
        Raiz.agregarHoja("u", "" + Escala);

        for (Nodo nodo : AB.Arbol.Hijos) {
            SeccionEI DatosObj = nodo.Objeto.generarData();
            Raiz.agregarSeccion(DatosObj);
        }

        return Raiz;
    }

    public Objeto2D crearObjeto2D(String Data){
        return crearObjeto2D(EstructuraInd.generarEstructuraInd(Data));
    }

    public Objeto2D crearObjeto2D(SeccionEI DatosEI){
        Objeto2D NuevoObjeto = null;

        String Tipo = DatosEI.extraerValorHoja("t");        


        switch(Tipo){
            case "r":{
                float Xt = Float.parseFloat(DatosEI.extraerValorHoja("x"));
                float Yt = Float.parseFloat(DatosEI.extraerValorHoja("y"));
                boolean Hu = DatosEI.extraerValorHoja("u").equals("1");

                float ancho = Float.parseFloat(DatosEI.extraerValorHoja("w"));
                float alto = Float.parseFloat(DatosEI.extraerValorHoja("h"));

                NuevoObjeto = new FrRect(Xt, Yt, ancho, alto, Hu, this);
                break;
            }

            case "c":{
                float Xt = Float.parseFloat(DatosEI.extraerValorHoja("x"));
                float Yt = Float.parseFloat(DatosEI.extraerValorHoja("y"));
                boolean Hu = DatosEI.extraerValorHoja("u").equals("1");

                float radio = Float.parseFloat(DatosEI.extraerValorHoja("r"));
                int anguloIn = Math.round(Float.parseFloat(DatosEI.extraerValorHoja("a")));
                int ext = Math.round(Float.parseFloat(DatosEI.extraerValorHoja("e")));

                NuevoObjeto = new FrCirc(Xt, Yt, radio, anguloIn, ext, Hu, this);
                break;
            }

            case "t":{
                float Xt = Float.parseFloat(DatosEI.extraerValorHoja("x"));
                float Yt = Float.parseFloat(DatosEI.extraerValorHoja("y"));
                boolean Hu = DatosEI.extraerValorHoja("u").equals("1");

                SeccionEI Vertice1 = DatosEI.extraerSeccion("a");
                float v1x = Float.parseFloat(Vertice1.extraerValorHoja("x")) - Xt;
                float v1y = Float.parseFloat(Vertice1.extraerValorHoja("y")) - Yt;

                SeccionEI Vertice2 = DatosEI.extraerSeccion("b");
                float v2x = Float.parseFloat(Vertice2.extraerValorHoja("x")) - Xt;
                float v2y = Float.parseFloat(Vertice2.extraerValorHoja("y")) - Yt;

                SeccionEI Vertice3 = DatosEI.extraerSeccion("c");
                float v3x = Float.parseFloat(Vertice3.extraerValorHoja("x")) - Xt;
                float v3y = Float.parseFloat(Vertice3.extraerValorHoja("y")) - Yt;

                NuevoObjeto = new FrTria(Xt, Yt, v1x, v1y, v2x, v2y, v3x, v3y, Hu, this);

                break;
            }

            case "g":{
                
                NuevoObjeto = new Grupo(this);

                ArrayList<NodoEI> Objetos = new ArrayList<NodoEI>();
                Objetos.addAll(DatosEI.Hijos.subList(1, DatosEI.Hijos.size()));

                
                for (NodoEI nodoEI : Objetos) {
                    Objeto2D obj = crearObjeto2D((SeccionEI)nodoEI);
                    ((Grupo)NuevoObjeto).agregarForma((Forma)obj);
                }

                ((Grupo)NuevoObjeto).ActualizarBordes();

                ((Grupo)NuevoObjeto).MoviendoFormas = false;

                LstObjetos.add(NuevoObjeto);

                break;
            }

            case "a":{
                ArrayList<NodoEI> Objetos = new ArrayList<NodoEI>();
                Objetos.addAll(DatosEI.Hijos.subList(3, DatosEI.Hijos.size()));

                for (NodoEI nodoEI : Objetos) 
                    crearObjeto2D((SeccionEI)nodoEI);
                
                break;
            }
        }

        if(!Tipo.equals("a")){
            add(NuevoObjeto, JLayeredPane.DRAG_LAYER);
            moveToFront(NuevoObjeto);
        }

        notificarCambios(0);

        return NuevoObjeto;

    }


    public void mousePressed(MouseEvent e) {

        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, this);
        
        PtOffset.x = Math.round(PtOrigen.x - Pos.x);
        PtOffset.y = Math.round(PtOrigen.y - Pos.y);

        //CERRAR VENTANA PARA AGREGAR FIGURA
        if(PnAgActivo != null){
            remove(PnAgActivo);
            PnAgActivo = null;
        }

        if(SwingUtilities.isLeftMouseButton(e)){

            //DESELECCIONAR CUALQUIER FORMA
            if(GrupoSel != null){
                moveToFront(GrupoSel);
                GrupoSel.MoviendoFormas = false;

                if(GrupoSel.GrupoTemporal)
                    GrupoSel.eliminarGrupo();
                
                GrupoSel = null;
                notificarCambios(0);
            }

            seleccionarForma(null);

            Pos = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(Pos, this);

            PtInicioSel.x = Pos.x;
            PtInicioSel.y = Pos.y;

        }else if(SwingUtilities.isRightMouseButton(e)){

            int PosX = e.getX();
            int PosY =  e.getY();
            ListaOpciones Lo = new ListaOpciones(getX() + PosX, getY() +  PosY, this);

            PnPlano Pl = this;

            //OPCION PARA COPIAR FORMA
            Opcion OpPegar = new Opcion("Pegar"){
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);


                    Clipboard CB =Toolkit.getDefaultToolkit().getSystemClipboard();
                    String Datos;
                    try {
                        Datos = (String)CB.getData(DataFlavor.stringFlavor);
                        String Llave = Datos.substring(0, 5);

                        if(Llave.equals("<|||>")){
                            Datos = Datos.substring(5, Datos.length());
                            Pl.crearObjeto2D(Datos);
                        }

                    } catch (UnsupportedFlavorException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }


                    Pl.remove(Lo);
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    g.setColor(Ctrl.ClGris);
                    g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
                }
            };

            //OPCION PARA COPIAR FORMA
            Opcion OpRec = new Opcion("Crear Rectangulo"){
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);

                    if(Pl.PnAgActivo != null){
                        Pl.remove(Pl.PnAgActivo);
                        Pl.PnAgActivo = null;
                    }
    
                    PnAgRect PG = new PnAgRect(Pl);
                    Pl.add(PG, JLayeredPane.DRAG_LAYER);
                    Pl.moveToFront(PG);
                    PG.TFNombre.requestFocus();

                    Point PtMouse = new Point(Lo.getX(), Lo.getY());

                    PG.setBounds(PtMouse.x - 20, PtMouse.y, PG.getWidth(), PG.getHeight());
                    PG.PosIni.x = Ctrl.aplicarEscalaLnPixU(PtMouse.x - Pl.PtOrigen.x);
                    PG.PosIni.y = Ctrl.aplicarEscalaLnPixU(PtMouse.y - Pl.PtOrigen.y);

                    Pl.remove(Lo);
                }
            };


            

            //OPCION PARA COPIAR FORMA
            Opcion OpCirc = new Opcion("Crear Circulo"){
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);

                    if(Pl.PnAgActivo != null){
                        Pl.remove(Pl.PnAgActivo);
                        Pl.PnAgActivo = null;
                    }
    
                    PnAgCirc PG = new PnAgCirc(Pl);
                    Pl.add(PG, JLayeredPane.DRAG_LAYER);
                    Pl.moveToFront(PG);
                    PG.TFNombre.requestFocus();

                    Point PtMouse = new Point(Lo.getX(), Lo.getY());

                    PG.setBounds(PtMouse.x - 20, PtMouse.y, PG.getWidth(), PG.getHeight());
                    PG.PosIni.x = Ctrl.aplicarEscalaLnPixU(PtMouse.x - Pl.PtOrigen.x);
                    PG.PosIni.y = Ctrl.aplicarEscalaLnPixU(PtMouse.y - Pl.PtOrigen.y);

                    System.out.println(e.getX());

                    Pl.remove(Lo);
                }
            };


            //OPCION PARA COPIAR FORMA
            Opcion OpTria = new Opcion("Crear Triangulo"){
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);

                    if(Pl.PnAgActivo != null){
                        Pl.remove(Pl.PnAgActivo);
                        Pl.PnAgActivo = null;
                    }
    
                    PnAgTria PG = new PnAgTria(Pl);
                    Pl.add(PG, JLayeredPane.DRAG_LAYER);
                    Pl.moveToFront(PG);
                    PG.TFNombre.requestFocus();


                    Point PtMouse = new Point(Lo.getX(), Lo.getY());

                    PG.setBounds(PtMouse.x - 20, PtMouse.y, PG.getWidth(), PG.getHeight());
                    PG.PosIni.x = Ctrl.aplicarEscalaLnPixU(PtMouse.x - Pl.PtOrigen.x);
                    PG.PosIni.y = Ctrl.aplicarEscalaLnPixU(PtMouse.y - Pl.PtOrigen.y);

                    System.out.println(e.getX());

                    Pl.remove(Lo);
                }
            };
            


            Lo.agregarOpcion(OpPegar);
            Lo.agregarOpcion(OpRec);
            Lo.agregarOpcion(OpCirc);
            Lo.agregarOpcion(OpTria);


            add(Lo, JLayeredPane.DRAG_LAYER);
            moveToFront(Lo);
            Lo.repaint();
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
        else if (e.getWheelRotation() < 0 && Escala > 1)
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
                notificarCambios(0);
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
