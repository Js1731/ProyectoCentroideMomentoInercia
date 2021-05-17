package PryMecanica.Formas;

import java.awt.Color;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;

import PryMecanica.PnPrincipal;

public class FrRect extends Forma{
    public int Ancho = 50;
    public int Alto = 50;

    public Pin[] Pines = new Pin[4];

    public FrRect(){
        setBounds(50, 50, Ancho, Alto);
        setBackground(Color.DARK_GRAY);
    }

    public FrRect(int x, int y, int an, int al){
        Ancho = an;
        Alto = al;
        setBounds(x, y, Ancho, Alto);
        setBackground(Color.DARK_GRAY);
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
