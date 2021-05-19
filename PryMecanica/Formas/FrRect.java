package PryMecanica.Formas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;

import PryMecanica.PnPrincipal;

/**
 * Rectangulo al que se le puede editar su ancho y altura
 */
public class FrRect extends Forma{
    public int Ancho = 50;
    public int Alto = 50;

    /**
     * Crea un rectangulo de 50x50 en el origen {@code (0, 0)}
     */
    public FrRect(){
        this(0,0,50, 50);
    }

    /**
     * Crea un rectangulo en una posicion y tamaño definido
     * @param x X
     * @param y Y
     * @param an Ancho
     * @param al Alto
     */
    public FrRect(int x, int y, int an, int al){
        Ancho = an;
        Alto = al;

        Pines = new Pin[4];

        setBounds(x, y, Ancho, Alto);
        setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(200,200,200,50));

        g.fillRect(0, 0, 100, 70);

        g.setColor(Color.WHITE);

        g.drawString(getX() + ", " + getY(),10, 20);
        g.drawString("Ancho " + Ancho, 10, 40);
        g.drawString("Alto " + Alto, 10, 50);
    }

    public void ActualizarPines(){
        if(Pines[0] != null){
            Pines[0].setLocation(getX() - 15, getY() -15);
            Pines[1].setLocation(getX() - 15, getY() + getHeight() + 15);
            Pines[2].setLocation(getX() + getWidth() + 15, getY() - 15);
            Pines[3].setLocation(getX() + getWidth() + 15, getY() + getHeight() + 15);

            PnPrincipal.PnPrinc.repaint();

            Ancho = getWidth();
            Alto = getHeight();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);

        //CREAR PINES DE DEFORMACION
        if(Pines[0] == null){

            //REDIM ESQUINA SUPERIOR IZQUIERDA
            Pines[0] = new Pin(this, getX() - 15, getY() -15){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    int DifX = getX() + 15 - Fr.getX();
                    int DifY = getY() + 15 - Fr.getY();
                    
                    Fr.setBounds(getX() + 15, getY() + 15, Fr.getWidth() - DifX, Fr.getHeight() - DifY);
                    Fr.ActualizarPines();
                }
            };

            //REDIM ESQUINA INFERIOR IZQUIERDA
            Pines[1] = new Pin(this, getX() - 15, getY() + getHeight() + 15){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    int DifX = getX() + 15 - Fr.getX();
                    int DifY = (getY() - 15) - (Fr.getY() + Fr.getHeight());
                    
                    Fr.setBounds(getX() + 15, Fr.getY(), Fr.getWidth() - DifX, Fr.getHeight() + DifY);
                    Fr.ActualizarPines();
                }
            };

            //REDIM ESQUINA SUPERIOR DERECHA
            Pines[2] = new Pin(this, getX() + getWidth() + 15, getY() - 15){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    int DifX = (getX() - 15) - (Fr.getX() + Fr.getWidth());
                    int DifY = getY() + 15 - Fr.getY();
                    
                    Fr.setBounds(Fr.getX(), getY() + 15, Fr.getWidth() + DifX, Fr.getHeight() - DifY);
                    Fr.ActualizarPines();
                }
            };

            //REDIM ESQUINA INFERIOR DERECHA
            Pines[3] = new Pin(this, getX() + getWidth() + 15, getY() + getHeight() + 15){
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);

                    int DifX = (getX() - 15) - (Fr.getX() + Fr.getWidth());
                    int DifY = (getY() - 15) - (Fr.getY() + Fr.getHeight());
                    
                    Fr.setBounds(Fr.getX(), Fr.getY(), Fr.getWidth() + DifX, Fr.getHeight() + DifY);
                    Fr.ActualizarPines();
                }
            };

            //AGREGAR PINES AL PANEL PRINCIPAL
            for (Pin pin : Pines) {
                PnPrincipal.PnPrinc.add(pin, JLayeredPane.DRAG_LAYER);
                PnPrincipal.PnPrinc.moveToFront(pin);
            }

            PnPrincipal.PnPrinc.repaint();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);

        ActualizarPines();
    }
}
