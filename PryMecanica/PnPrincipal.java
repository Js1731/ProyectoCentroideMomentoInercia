package PryMecanica;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

import PryMecanica.Plano.Punto;
import PryMecanica.Plano.Objetos.Grupo;
import PryMecanica.Plano.Objetos.Objeto2D;
import PryMecanica.Plano.Objetos.Formas.Forma;
import PryMecanica.Plano.Objetos.Formas.FrCirc;
import PryMecanica.Plano.Objetos.Formas.FrRect;
import PryMecanica.Plano.Objetos.Formas.FrTria;

public class PnPrincipal extends JLayeredPane implements MouseInputListener{

    public static PnPrincipal PnPrinc;

    public static Punto Escala = new Punto(1,1);
    public static Punto PtOrigen = new Punto(0,0);

    Punto PtInicioSel = new Punto();
    Punto PtFinSel = new Punto();

    boolean Arrastrando = false;

    public Forma FrSel = null;

    public ArrayList<Objeto2D> LstObjetos = new ArrayList<Objeto2D>(); 

    Color ColSel = new Color(200, 200, 200, 100);

    public static Grupo GrupoSel = null;

    public PnPrincipal(){
        setLayout(null);

        addMouseMotionListener(this);
        addMouseListener(this);

        PnPrinc = this;

        Forma R = new FrRect(10,10,50,50);
        Forma T = new FrCirc(70,10,50);
        Forma C = new FrTria(120,10);

        add(R, JLayeredPane.DRAG_LAYER);
        add(T, JLayeredPane.DRAG_LAYER);
        add(C, JLayeredPane.DRAG_LAYER);     

        add(new Origen());
    }

    public void SelForma(Forma Fr){
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

        if(Arrastrando){
            g.setColor(ColSel);
            g.fillRect(Math.round(PtInicioSel.x), 
                       Math.round(PtInicioSel.y),
                       Math.round( PtFinSel.x - PtInicioSel.x),
                       Math.round( PtFinSel.y - PtInicioSel.y));
        }

    }

    public void mousePressed(MouseEvent e) {

        if(GrupoSel != null){
            moveToFront(GrupoSel);
            GrupoSel.MoviendoFormas = false;
            GrupoSel = null;
        }

        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, this);

        PtInicioSel.x = Pos.x;
        PtInicioSel.y = Pos.y;
    }
    
    public void mouseDragged(MouseEvent e) {
        Point Pos = e.getLocationOnScreen();
        SwingUtilities.convertPointFromScreen(Pos, this);

        PtFinSel.x = Pos.x;
        PtFinSel.y = Pos.y;

        Arrastrando = true;

        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        Arrastrando = false;

        Grupo NuevoGrupo = null;

        for (Objeto2D objeto2d : LstObjetos) {
            if(objeto2d instanceof Forma){
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
        
                if(objeto2d.getX() >= xIni && 
                   objeto2d.getY() >= yIni &&
                   (objeto2d.getX() + objeto2d.getWidth()) <= xFin && 
                   (objeto2d.getY() + objeto2d.getHeight()) <= yFin){

                    if(NuevoGrupo == null){
                        NuevoGrupo = new Grupo();
                        add(NuevoGrupo, JLayeredPane.DRAG_LAYER);
                        moveToFront(NuevoGrupo);
                        NuevoGrupo.MoviendoFormas = true;
                        
                    }

                    if(((Forma)objeto2d).Grp != null){
                        ((Forma)objeto2d).Grp.sacarForma((Forma)objeto2d);
                    }
                    NuevoGrupo.agregarForma((Forma)objeto2d);
                }
            }
        }

        if(NuevoGrupo != null){
            LstObjetos.add(NuevoGrupo);
        }

        repaint();
    }
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}
