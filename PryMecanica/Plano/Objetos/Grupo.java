package PryMecanica.Plano.Objetos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import PryMecanica.PnPlano;
import PryMecanica.GUI.ListaOpciones;
import PryMecanica.GUI.Opcion;
import PryMecanica.Plano.Objetos.Formas.Forma;
import PryMecanica.Plano.Objetos.Formas.FrTria;

/**Define un {@link Objeto2D} que permite agrupar un conjunto de {@link Forma}s.
 * <p> El area de arrastrado del grupo se define por las formas que estan dentro del mismo
*/
public class Grupo extends Objeto2D{

    /**Lista de todas las formas del grupo */
    public ArrayList<Forma> LstForma = new ArrayList<Forma>();

    public boolean MoviendoFormas = false;

    public boolean GrupoTemporal = true;

    /**Crea un nuevo grupo vacio en el origen*/
    public Grupo(){
        setOpaque(false);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);

        if(MoviendoFormas)
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        float SumaAreas = 0;
        float SumaAreasPorX = 0;
        float SumaAreasPorY = 0;

        for (Forma forma : LstForma) {
            forma.ActualizarCoordenadas();
            float Area = forma.calcularArea();
            SumaAreas += Area;

            float CentX = forma.X + forma.centroideX();
            float CentY = forma.Y + forma.centroideY();

            SumaAreasPorX += CentX*Area;
            SumaAreasPorY += CentY*Area;
        }

        int x = Math.round(PnPlano.PtOrigen.x - getX() + SumaAreasPorX/SumaAreas);
        int y = Math.round(PnPlano.PtOrigen.y - getY() + SumaAreasPorY/SumaAreas);

        g.setColor(Color.GREEN);

        g.fillOval(x-3,y-3,6,6);
        ActualizarCoordenadas();
        g.drawString( (PnPlano.PtOrigen.x - getX() + SumaAreasPorX/SumaAreas + X)/Escala+", " +  (-(PnPlano.PtOrigen.y - getY() + SumaAreasPorY/SumaAreas + Y)/Escala),
                      x-3,
                      y-3);
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
            PnPlano.PlPrinc.LstObjetos.remove(this);
            PnPlano.PlPrinc.remove(this);
        }
    }

    public void vaciarGrupo(){
        for (Forma fr : LstForma) {
            fr.Grp = null;
        }
    }

    public void eliminarGrupo(){
        vaciarGrupo();
        PnPlano.PlPrinc.LstObjetos.remove(this);
        PnPlano.PlPrinc.remove(this);
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
        X = Math.round(getX() - PnPlano.PtOrigen.x);
        Y = Math.round(getY() - PnPlano.PtOrigen.y);

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
        PnPlano.GrupoSel = this;



        if(e.getClickCount() > 1){
            MoviendoFormas = true;
            PnPlano.PlPrinc.moveToBack(this);
        }

        if(SwingUtilities.isRightMouseButton(e)){
            ListaOpciones Lo = new ListaOpciones(getX() + e.getX(), getY() +  e.getY());
            Grupo Gp = this;
            Opcion Agrupar = new Opcion(GrupoTemporal ? "Agrupar" : "Desagrupar"){
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    Gp.GrupoTemporal = !Gp.GrupoTemporal;

                    if(Gp.GrupoTemporal){
                        Gp.eliminarGrupo();
                    }

                    PnPlano.PlPrinc.remove(Lo);
                }
            };
            Lo.agregarOpcion(Agrupar);

            PnPlano.PlPrinc.add(Lo, JLayeredPane.DRAG_LAYER);
            PnPlano.PlPrinc.moveToFront(Lo);
            Lo.repaint();
        }

        //DESELECCIONAR FIGURA ACTUAL
        PnPlano.PlPrinc.SelForma(null);

        //BUSCAR X PARA AJUSTARSE
        SnapXs.removeAll(SnapXs);
        SnapYs.removeAll(SnapYs);

        SnapXs.add(Math.round(PnPlano.PtOrigen.x));
        SnapYs.add(Math.round(PnPlano.PtOrigen.y));

        for (Objeto2D obj : PnPlano.PlPrinc.LstObjetos) {
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
        //ARRASTRAR OBJETO
        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, PnPlano.PlPrinc);

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        int DifX = Pos.x - (PtOffset.x + getX());
        int DifY = Pos.y - (PtOffset.y + getY());

        setBounds(Pos.x - PtOffset.x, Pos.y - PtOffset.y, getWidth(), getHeight());

        //ACTUALIZAR POSICIONES DE LAS FORMAS
        moverFormas(DifX, DifY);

        //AUTOAJUSTAR A FORMAS CERCANAS
        Point PtPrev = getLocation();

        setLocation(snap(getX(), SnapXs), snap(getY(), SnapYs));
        setLocation(snap(getX() + getWidth(), SnapXs) - getWidth(),
                    snap(getY() + getHeight(), SnapYs) - getHeight());

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        DifX = getX() - PtPrev.x;
        DifY = getY() - PtPrev.y;

        //ACTUALIZAR POSICIONES DE LAS FORMAS
        moverFormas(DifX, DifY);
        
        ActualizarCoordenadas();
        PnPlano.PlPrinc.repaint();
    }
}
