package com.mec2021.plano.objetos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

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

        g.setColor(Color.DARK_GRAY);

        if(MoviendoFormas)
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        g.setColor(Color.darkGray);

        int x = centroideX();
        int y = centroideY();

        g.fillOval(x-3,y-3,6,6);
        ActualizarCoordenadas();
        g.drawString( (float)(x + X)/Escala + ", " +  -(float)(y + Y)/Escala,
                      x-3,
                      y-3);
    }

    /**Calcula la posicion X del centroide con respecto al origen */
    public int centroideX(){
        float SumaAreas = 0;
        float SumaAreasPorX = 0;

        for (Forma forma : LstForma) {
            forma.ActualizarCoordenadas();
            float Area = forma.calcularArea();
            SumaAreas += Area;

            float CentX = forma.X + forma.centroideX();

            SumaAreasPorX += CentX*Area;
        }

        int x = Math.round(Plano.PtOrigen.x - getX() + SumaAreasPorX/SumaAreas);

        return x;
    }

    /**Calcula la posicion Y del centroide con respecto al origen */
    public int centroideY(){
        float SumaAreas = 0;
        float SumaAreasPorY = 0;

        for (Forma forma : LstForma) {
            forma.ActualizarCoordenadas();
            float Area = forma.calcularArea();
            SumaAreas += Area;

            float CentY = forma.Y + forma.centroideY();

            SumaAreasPorY += CentY*Area;
        }

        int y = Math.round(Plano.PtOrigen.y - getY() + SumaAreasPorY/SumaAreas);

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

    public void vaciarGrupo(){
        for (Forma fr : LstForma) {
            fr.Grp = null;
        }
    }

    public void eliminarGrupo(){
        vaciarGrupo();
        Plano.LstObjetos.remove(this);
        Plano.remove(this);
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
    public void ActualizarCoordenadas() {
        X = Math.round(getX() - Plano.PtOrigen.x);
        Y = Math.round(getY() - Plano.PtOrigen.y);

        repaint();
    }

    private void moverFormas(int distx, int disty) {
        //ACTUALIZAR POSICIONES DE LAS FORMAS
        for (Forma Fr : LstForma) {
            Fr.setBounds(Fr.getX() + distx, Fr.getY() + disty, Fr.getWidth(), Fr.getHeight());
            Fr.ActualizarCoordenadas();
            
            if(Fr instanceof FrTria){
                //ACTUALIZAR VERTICES
                ((FrTria)Fr).Ver1.x += distx;
                ((FrTria)Fr).Ver1.y += disty;
                
                ((FrTria)Fr).Ver2.x += distx;
                ((FrTria)Fr).Ver2.y += disty;
                
                ((FrTria)Fr).Ver3.x += distx;
                ((FrTria)Fr).Ver3.y += disty;
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
        
        ActualizarCoordenadas();
        Plano.repaint();
    }
}
