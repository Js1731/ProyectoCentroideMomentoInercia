package PryMecanica.Plano.Objetos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import PryMecanica.PnPrincipal;
import PryMecanica.Plano.Objetos.Formas.Forma;
import PryMecanica.Plano.Objetos.Formas.FrTria;

/**Define un {@link Objeto2D} que permite agrupar un conjunto de {@link Forma}s.
 * <p> El area de arrastrado del grupo se define por las formas que estan dentro del mismo
*/
public class Grupo extends Objeto2D{

    /**Lista de todas las formas del grupo */
    public ArrayList<Forma> LstForma = new ArrayList<Forma>();

    public boolean MoviendoFormas = false;

    /**Crea un nuevo grupo vacio en el origen*/
    public Grupo(){
        setOpaque(false);
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
            float Area = forma.calcularArea();
            SumaAreas += Area;

            float CentX = forma.X + forma.centroideX();
            float CentY = forma.Y + forma.centroideY();

            SumaAreasPorX += CentX*Area;
            SumaAreasPorY += CentY*Area;
        }

        int x = Math.round(PnPrincipal.PtOrigen.x - getX() + SumaAreasPorX/SumaAreas);
        int y = Math.round(PnPrincipal.PtOrigen.y - getY() + SumaAreasPorY/SumaAreas);

        g.setColor(Color.GREEN);

        g.fillOval(x-3,y-3,6,6);
        g.drawString((PnPrincipal.PtOrigen.x - getX() + SumaAreasPorX/SumaAreas)/Escala + ", " + (PnPrincipal.PtOrigen.y - getY() + SumaAreasPorY/SumaAreas)/Escala,x-3,y-3);
    }

    public void agregarForma(Forma Fr){
        Fr.Grp = this;
        LstForma.add(Fr);
        ActualizarBordes();
        repaint();
        
    }

    public void sacarForma(Forma Fr){
        LstForma.remove(Fr);
        ActualizarBordes();

        if(LstForma.size() == 0){
            PnPrincipal.PnPrinc.remove(this);
        }
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
        X = Math.round(getX() - PnPrincipal.PtOrigen.x);
        Y = Math.round(getY() - PnPrincipal.PtOrigen.y);

        repaint();
    }



    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        
        //SELECCIONA GRUPO
        PnPrincipal.GrupoSel = this;

        if(e.getClickCount() > 1){
            MoviendoFormas = true;
            PnPrincipal.PnPrinc.moveToBack(this);
        }

        //DESELECCIONAR FIGURA ACTUAL
        PnPrincipal.PnPrinc.SelForma(null);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //ARRASTRAR OBJETO
        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, PnPrincipal.PnPrinc);

        //DISTANCIA ENTRE POSICION INICIAL Y FINAL
        int DifX = Pos.x - (PtOffset.x + getX());
        int DifY = Pos.y - (PtOffset.y + getY());

        setBounds(Pos.x - PtOffset.x, Pos.y - PtOffset.y, getWidth(), getHeight());
        
        //ACTUALIZAR POSICIONES DE LAS FORMAS
        for (Forma Fr : LstForma) {
            Fr.setBounds(Fr.getX() + DifX, Fr.getY() + DifY, Fr.getWidth(), Fr.getHeight());
            Fr.ActualizarCoordenadas();
            
            if(Fr instanceof FrTria){
                //ACTUALIZAR VERTICES
                ((FrTria)Fr).Ver1.x += DifX;
                ((FrTria)Fr).Ver1.y += DifY;
                
                ((FrTria)Fr).Ver2.x += DifX;
                ((FrTria)Fr).Ver2.y += DifY;
                
                ((FrTria)Fr).Ver3.x += DifX;
                ((FrTria)Fr).Ver3.y += DifY;
            }
        }
        
        ActualizarCoordenadas();
        PnPrincipal.PnPrinc.repaint();
    }
}
