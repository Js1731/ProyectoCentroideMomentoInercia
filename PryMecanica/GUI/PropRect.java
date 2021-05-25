package PryMecanica.GUI;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import PryMecanica.Plano.Objetos.Formas.Forma;
import PryMecanica.Plano.Objetos.Formas.FrRect;

public class PropRect extends JPanel{

    FrRect Rect;

    private JLabel LbX = new JLabel("X");
    private JLabel LbY = new JLabel("Y");
    private JLabel LbAncho = new JLabel("Ancho");
    private JLabel LbAlto = new JLabel("Alto");
    private JLabel LbArea = new JLabel("Area:                         ");
    private JLabel LbCentX = new JLabel("Centroide X:                          ");
    private JLabel LbCentY = new JLabel("Centroide Y:                         ");

    private JTextField TFX = new JTextField();
    private JTextField TFY = new JTextField();
    private JTextField TFAn = new JTextField();
    private JTextField TFAl = new JTextField();

    public PropRect(){
        SpringLayout Ly = new SpringLayout();
        setLayout(Ly);

        setOpaque(false);


        LbX.setForeground(Color.WHITE);
        LbY.setForeground(Color.WHITE);
        LbAncho.setForeground(Color.WHITE);
        LbAlto.setForeground(Color.WHITE);
        LbArea.setForeground(Color.WHITE);
        LbCentX.setForeground(Color.WHITE);
        LbCentY.setForeground(Color.WHITE);


        TFX.setPreferredSize(new Dimension(50, 20));
        TFY.setPreferredSize(new Dimension(50, 20));
        TFAn.setPreferredSize(new Dimension(50, 20));
        TFAl.setPreferredSize(new Dimension(50, 20));

        //POSICION X
        Ly.putConstraint(SpringLayout.WEST, LbX, 30, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.NORTH, LbX, 50, SpringLayout.NORTH, this);
        Ly.putConstraint(SpringLayout.SOUTH, LbX, 0, SpringLayout.SOUTH, TFX);
        
        Ly.putConstraint(SpringLayout.NORTH, TFX, 50, SpringLayout.NORTH, this);
        Ly.putConstraint(SpringLayout.WEST, TFX, 60, SpringLayout.EAST, LbX);

        //PROPIEDAD Y
        Ly.putConstraint(SpringLayout.WEST, LbY, 30, SpringLayout.EAST, TFX);
        Ly.putConstraint(SpringLayout.NORTH, LbY, 50, SpringLayout.NORTH, this);
        Ly.putConstraint(SpringLayout.SOUTH, LbY, 0, SpringLayout.SOUTH, TFY);
        
        Ly.putConstraint(SpringLayout.NORTH, TFY, 50, SpringLayout.NORTH, this);
        Ly.putConstraint(SpringLayout.WEST, TFY, 30, SpringLayout.EAST, LbY);

        //ANCHO
        Ly.putConstraint(SpringLayout.WEST, LbAncho, 30, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.NORTH, LbAncho, 20, SpringLayout.SOUTH, LbX);
        Ly.putConstraint(SpringLayout.SOUTH, LbAncho, 0, SpringLayout.SOUTH, TFAn);
        
        Ly.putConstraint(SpringLayout.NORTH, TFAn, 20, SpringLayout.SOUTH, LbX);
        Ly.putConstraint(SpringLayout.WEST, TFAn, 0, SpringLayout.WEST, TFX);

        //ALTO
        Ly.putConstraint(SpringLayout.WEST, LbAlto, 30, SpringLayout.EAST, TFAn);
        Ly.putConstraint(SpringLayout.NORTH, LbAlto, 20, SpringLayout.SOUTH, LbY);
        Ly.putConstraint(SpringLayout.SOUTH, LbAlto, 0, SpringLayout.SOUTH, TFAn);
        
        Ly.putConstraint(SpringLayout.NORTH, TFAl, 20, SpringLayout.SOUTH, LbY);
        Ly.putConstraint(SpringLayout.WEST, TFAl, 0, SpringLayout.WEST, TFY);

        //PROPIEDADES
        Ly.putConstraint(SpringLayout.WEST, LbArea, 30, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.NORTH, LbArea, 20, SpringLayout.SOUTH, LbAncho);

        Ly.putConstraint(SpringLayout.WEST, LbCentX, 30, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.NORTH, LbCentX, 20, SpringLayout.SOUTH, LbArea);

        Ly.putConstraint(SpringLayout.WEST, LbCentY, 30, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.NORTH, LbCentY, 20, SpringLayout.SOUTH, LbCentX);


        add(LbX);
        add(TFX);
        add(LbY);
        add(TFY);
        add(LbAncho);
        add(LbAlto);
        add(TFAn);
        add(TFAl);

        add(LbArea);
        add(LbCentX);
        add(LbCentY);
    }

    public void cambiarRectangulo(FrRect fr) {
        Rect = fr;
        actualizarDatos();
    }

    public void actualizarDatos(){
        TFX.setText(""+((float)Rect.X/Forma.Escala));
        TFY.setText(""+(-(float)Rect.Y/Forma.Escala));
        TFAn.setText(""+((float)Rect.Ancho/Forma.Escala));
        TFAl.setText(""+((float)Rect.Alto/Forma.Escala));

        LbArea.setText("Area: " + Rect.calcularArea()/(Forma.Escala * Forma.Escala));
        LbCentX.setText("Centroide X: " + Rect.centroideX()/Forma.Escala);
        LbCentY.setText("Centroide Y: " + Rect.centroideY()/Forma.Escala);
    }
}
