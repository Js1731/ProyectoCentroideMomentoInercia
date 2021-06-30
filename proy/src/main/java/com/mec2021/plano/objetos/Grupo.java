package com.mec2021.plano.objetos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import com.mec2021.Ctrl;
import com.mec2021.gui.ListaOpciones;
import com.mec2021.gui.Opcion;
import com.mec2021.gui.PnPlano;
import com.mec2021.plano.objetos.formas.Forma;
import com.mec2021.plano.objetos.formas.FrTria;

/**Define un {@link Objeto2D} que permite agrupar un conjunto de {@link Forma}s.
 * <p> El area de arrastrado del grupo se define por las formas que estan dentro del mismo
 * <p> Se puede calcular el centroide de todas la formas que estan dentro del grupo
*/
public class Grupo extends Objeto2D{
    /**Lista de todas las formas del grupo */
    public ArrayList<Forma> LstForma = new ArrayList<Forma>();

    public boolean MoviendoFormas = false;

    public boolean GrupoTemporal = true;

    public static int ID = 1;

    /**Crea un nuevo grupo vacio en el origen*/
    public Grupo(PnPlano plano){
        super(plano);
        setOpaque(false);
        Nombre = "Grupo " + (ID++);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        
        g2.setColor(Color.DARK_GRAY);

        g.setColor(Color.DARK_GRAY);

        if(MoviendoFormas)
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g.setColor(Color.darkGray);

        float xi = centroideX();
        float yi = centroideY();
        float x = Ctrl.aplicarEscalaUPix(xi);
        float y = Ctrl.aplicarEscalaUPix(yi);

        g.fillOval(Math.round(x-3),Math.round(y-3),6,6);
        actualizarCoordenadas();

        DecimalFormat f = new DecimalFormat("#0.00");

        String Texto =  "" + f.format(xi + Ctrl.aplicarEscalaLnPixU(getX() - Plano.PtOrigen.x)) + ",   " + f.format(Ctrl.aplicarEscalaLnPixU(Plano.PtOrigen.y - getY()) - yi);

        g2.setColor(Color.darkGray);
        g2.fillRoundRect(Math.round(x - 7) - Texto.length()*7/2, Math.round(y - 50), Texto.length()*7, 40, 9, 9);
        
        g2.setColor(Color.white);

        g2.drawString("Centroide",
                      Math.round(x + 6) - Texto.length()*7/2,
                      Math.round(y-35));

        g2.drawString(Texto,
                      Math.round(x + 6) - Texto.length()*7/2,
                      Math.round(y-15));
    }

    @Override
    public float inerciaCentEjeX(){

        float SumaIx = 0;

        for (Forma forma : LstForma)
            SumaIx += forma.inerciaCentEjeX();
        
        return SumaIx;
    }

    @Override
    public float inerciaCentEjeY(){

        float SumaIy = 0;

        for (Forma forma : LstForma)
            SumaIy += forma.inerciaCentEjeY();
        
        return SumaIy;
    }

    /**Calcula la posicion X del centroide con respecto al origen */
    public float centroideX(){
        float SumaAreas = 0;
        float SumaAreasPorX = 0;

        for (Forma forma : LstForma) {

            float Area = forma.calcularArea();
            SumaAreas += Area;

            float CentX = forma.X + forma.centroideX();

            SumaAreasPorX += CentX*Area;
        }

        float x = Ctrl.aplicarEscalaLnPixU(Plano.PtOrigen.x - getX()) + (SumaAreasPorX/SumaAreas);

        return x;
    }

    /**Calcula la posicion Y del centroide con respecto al origen */
    public float centroideY(){
        float SumaAreas = 0;
        float SumaAreasPorY = 0;

        for (Forma forma : LstForma) {
            //forma.ActualizarCoordenadas();
            float Area = forma.calcularArea();
            SumaAreas += Area;

            float CentY = forma.Y + forma.centroideY();

            SumaAreasPorY += CentY*Area;
        }

        float y = Ctrl.aplicarEscalaLnPixU(Plano.PtOrigen.y - getY()) + SumaAreasPorY/SumaAreas;

        return y;
    }

    public void agregarForma(Forma Fr){
        Fr.Grp = this;
        LstForma.add(Fr);
        ActualizarBordes();
        repaint();
        
    }

    public void sacarForma(Forma Fr){
        LstForma.remove(Fr);
        Fr.Grp = null;
        ActualizarBordes();

        if(LstForma.size() == 0){
            Plano.LstObjetos.remove(this);
            Plano.remove(this);
        }
    }

    public void eliminarGrupo(){
        for (Forma fr : LstForma) {
            fr.Grp = null;
        }

        Plano.LstObjetos.remove(this);
        Plano.remove(this);
    }

    @Override
    public void actualizarDimensiones() {
        ActualizarBordes();
    }

    /**Acualiza las dimensiones del panel del grupo */
    public void ActualizarBordes(){

        if(LstForma.size() == 0)
            return;

        //BUSCA LA COORDENADA X y Y QUE ESTEN MAS A LA IZQUIERDA SUPERIOR PARA EL INICIO DEL AREA DEL GRUPO Y
        //LAS QUE ESTEN MAS A LA ESQUINA INFERIOR DERECHA PARA EL ANCHO

        int XMen = LstForma.get(0).getX();
        int YMen = LstForma.get(0).getY();

        int XMax = LstForma.get(0).getX();
        int YMax = LstForma.get(0).getY();

        for (Forma forma : LstForma) {
            XMen = Math.min(XMen, forma.getX());
            YMen = Math.min(YMen, forma.getY());

            XMax = Math.max(XMax, forma.getX() + forma.getWidth());
            YMax = Math.max(YMax, forma.getY() + forma.getHeight());
        }

        X = XMen;
        Y = YMen;

        setBounds(XMen, YMen, XMax - XMen, YMax - YMen);
        
        repaint();
    }

    @Override
    public void actualizarCoordenadas() {
        X = Ctrl.aplicarEscalaLnPixU(getX() - Plano.PtOrigen.x);
        Y = Ctrl.aplicarEscalaLnPixU(getY() - Plano.PtOrigen.y);

        repaint();
    }

    private void moverFormas(int distx, int disty) {
        //ACTUALIZAR POSICIONES DE LAS FORMAS
        for (Forma Fr : LstForma) {
            Fr.setBounds(Fr.getX() + distx, Fr.getY() + disty, Fr.getWidth(), Fr.getHeight());
            Fr.actualizarCoordenadas();
            
            if(Fr instanceof FrTria){
                //ACTUALIZAR VERTICES
                ((FrTria)Fr).Ver1.x += Ctrl.aplicarEscalaLnPixU(distx);
                ((FrTria)Fr).Ver1.y += Ctrl.aplicarEscalaLnPixU(disty);
                
                ((FrTria)Fr).Ver2.x += Ctrl.aplicarEscalaLnPixU(distx);
                ((FrTria)Fr).Ver2.y += Ctrl.aplicarEscalaLnPixU(disty);
                
                ((FrTria)Fr).Ver3.x += Ctrl.aplicarEscalaLnPixU(distx);
                ((FrTria)Fr).Ver3.y += Ctrl.aplicarEscalaLnPixU(disty);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        //SELECCIONA GRUPO
        Plano.GrupoSel = this;

        if(e.getClickCount() > 1){
            MoviendoFormas = true;
            Plano.moveToBack(this);
        }

        if(SwingUtilities.isRightMouseButton(e)){
            ListaOpciones Lo = new ListaOpciones(getX() + e.getX(), getY() +  e.getY(), Plano);
            Grupo Gp = this;
            Opcion Agrupar = new Opcion(GrupoTemporal ? "Agrupar" : "Desagrupar"){
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    Gp.GrupoTemporal = !Gp.GrupoTemporal;

                    if(Gp.GrupoTemporal){
                        Gp.eliminarGrupo();
                    }
                        
                    Plano.notificarCambios(0);
                
                    Plano.remove(Lo);
                }
            };

            Opcion Op = new Opcion("Eliminar"){
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);

                    for (Forma fr : LstForma) {
                        Plano.eliminarForma(fr);
                    }
                    Plano.remove(Lo);
                }
            };


            Lo.agregarOpcion(Agrupar);
            Lo.agregarOpcion(Op);

            Plano.add(Lo, JLayeredPane.DRAG_LAYER);
            Plano.moveToFront(Lo);
            Lo.repaint();
        }

        //DESELECCIONAR FIGURA ACTUAL
        Plano.seleccionarForma(null);

        //BUSCAR X PARA AJUSTARSE
        SnapXs.removeAll(SnapXs);
        SnapYs.removeAll(SnapYs);

        SnapXs.add(Math.round(Plano.PtOrigen.x));
        SnapYs.add(Math.round(Plano.PtOrigen.y));

        for (Objeto2D obj : Plano.LstObjetos) {
            if(obj != this && !LstForma.contains(obj)){
                SnapXs.add(obj.getX());
                SnapXs.add(obj.getX() + obj.getWidth());

                SnapYs.add(obj.getY());
                SnapYs.add(obj.getY() + obj.getHeight());
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        Plano.notificarCambios(1);

        //ARRASTRAR OBJETO
        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, Plano);

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        int DifX = Pos.x - (PtOffset.x + getX());
        int DifY = Pos.y - (PtOffset.y + getY());

        setBounds(Pos.x - PtOffset.x, Pos.y - PtOffset.y, getWidth(), getHeight());

        //ACTUALIZAR POSICIONES DE LAS FORMAS
        moverFormas(DifX, DifY);

        //AUTOAJUSTAR A FORMAS CERCANAS
        Point PtPrev = getLocation();

        setLocation(snapX(getX()), snapY(getY()));
        setLocation(snapX(getX() + getWidth()) - getWidth(),
                    snapY(getY() + getHeight()) - getHeight());

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        DifX = getX() - PtPrev.x;
        DifY = getY() - PtPrev.y;

        //ACTUALIZAR POSICIONES DE LAS FORMAS
        moverFormas(DifX, DifY);
        
        actualizarCoordenadas();
        Plano.repaint();
    }
}
