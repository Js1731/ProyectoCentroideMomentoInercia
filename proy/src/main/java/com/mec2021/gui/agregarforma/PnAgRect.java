package com.mec2021.gui.agregarforma;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

import com.mec2021.Ctrl;
import com.mec2021.gui.PnPlano;
import com.mec2021.plano.objetos.formas.FrRect;

import javafx.geometry.Pos;


public class PnAgRect extends PnAgregarForma{


    JLabel LbAncho = new JLabel("Ancho");
    JLabel LbAlto = new JLabel("Alto");
    JTextField TFAlto = new JTextField("5");
    JTextField TFAncho = new JTextField("5");

    private void crearForma(){
        //PROPIEDADES DEL RECTANGULO
        float Ancho = Float.parseFloat((TFAncho.getText().isEmpty() || TFAncho.getText().equals("-") || TFAncho.getText().equals("1")? "1" : TFAncho.getText()));
        float Alto = Float.parseFloat((TFAlto.getText().isEmpty() || TFAlto.getText().equals("-") || TFAlto.getText().equals("1") ? "1" : TFAlto.getText()));
        
        //CREAR NUEVO RECTANGULO
        FrRect NuevoRect = new FrRect(PosIni.x - Ancho/2, PosIni.y - Alto/2, Ancho, Alto, false, Plano);
        
        if(TFNombre.getText() != "")
            NuevoRect.Nombre = TFNombre.getText();

        Plano.add(NuevoRect, JLayeredPane.DRAG_LAYER);
        Plano.moveToFront(NuevoRect);

        Plano.remove(this);
        Plano.PnAgActivo = null;
        Plano.repaint();
    }

    public PnAgRect(PnPlano plano) {
        super(plano);
        
        setBounds(140, getY(), getWidth(), 220);
        
        TFNombre.setText("Rectangulo " + FrRect.ID);
        TFNombre.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                TFAncho.requestFocusInWindow();                       
            }

        });

        TFAncho.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                TFAlto.requestFocusInWindow();                       
            }

        });

        TFAlto.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                crearForma();            
            }
        });

        LbAncho.setBounds(30, 105, 100, 20);
        LbAncho.setFont(Ctrl.Fnt1);

        
        TFAncho.setBounds(100, 105, 100, 20);
        TFAncho.setFont(Ctrl.Fnt1);


        LbAlto.setBounds(30, 130, 100, 20);
        LbAlto.setFont(Ctrl.Fnt1);

        TFAlto.setBounds(100, 130, 100, 20);
        TFAlto.setFont(Ctrl.Fnt1);

        add(LbAncho);
        add(TFAncho);        
        add(LbAlto);
        add(TFAlto);

        BtAceptar.setBounds(20, getHeight() - 50,getWidth()-35,30);
        BtAceptar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                crearForma();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        
        g2.setFont(Ctrl.Fnt1);
        g2.drawString("Agregar Rectangulo", 20, 45);
    }
}
