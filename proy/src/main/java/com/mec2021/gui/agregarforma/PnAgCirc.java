package com.mec2021.gui.agregarforma;

import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

import com.mec2021.Ctrl;
import com.mec2021.gui.BotonGenerico;
import com.mec2021.gui.PnPlano;
import com.mec2021.plano.objetos.formas.FrCirc;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

public class PnAgCirc extends PnAgregarForma{

    JTextField TFRadio = new JTextField("2.5");
    JLabel LbRadio = new JLabel("Radio");

    Arc2D.Double Sector = new Arc2D.Double();
    BotonGenerico BtSel = null;


    BotonGenerico BtCirNorm = new BotonGenerico(){
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D)g;

            if(BtSel == this){
                setBackground(Ctrl.ClGris);
            }else{
                if(MouseEncima)
                    setBackground(Ctrl.ClGrisClaro2);
                else
                    setBackground(Ctrl.ClGrisClaro);
            }

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        
            g2.setColor(Ctrl.ClGrisClaro3);

            g2.fillOval(10, 10, 30, 30);
            
        }
    };


    BotonGenerico BtCirSemi = new BotonGenerico(){
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D)g;


            if(BtSel == this){
                setBackground(Ctrl.ClGris);
            }else{
                if(MouseEncima)
                    setBackground(Ctrl.ClGrisClaro2);
                else
                    setBackground(Ctrl.ClGrisClaro);
            }


            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        
            g2.setColor(Ctrl.ClGrisClaro3);

            Sector.extent = 180;
            Sector.x = 10;
            Sector.y = 17.5;
            Sector.width = 30;
            Sector.height = 30;
            g2.fill(Sector);
            
        }
    };

    BotonGenerico BtCirCuarto = new BotonGenerico(){
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D)g;


            if(BtSel == this){
                setBackground(Ctrl.ClGris);
            }else{
                if(MouseEncima)
                    setBackground(Ctrl.ClGrisClaro2);
                else
                    setBackground(Ctrl.ClGrisClaro);
            }


            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);        
            g2.setColor(Ctrl.ClGrisClaro3);

            Sector.x = -2;
            Sector.y = 15.5;
            Sector.extent = 90;
            Sector.width = 40;
            Sector.height = 40;

            g2.fill(Sector);
        }
    };

    private void crearForma(){
        //PROPIEDADES DEL CIRCULO
        float Radio = Float.parseFloat((TFRadio.getText().isEmpty() || TFRadio.getText().equals("-") || TFRadio.getText().equals("1")? "1" : TFRadio.getText()));
        
        int Ext = 0;
        if(BtSel.equals(BtCirNorm)){
            Ext = 360;
            PosIni.x += -Radio;
            PosIni.y += Radio;
        }else if(BtSel.equals(BtCirSemi)){
            Ext = 180;
            PosIni.x += -Radio;
            PosIni.y += Radio;
        }else{
            Ext = 90;
            PosIni.x += -Radio;
            PosIni.y += Radio;
        }

        //CREAR NUEVO CIRCULO
        FrCirc NuevoCirc = new FrCirc(PosIni.x, PosIni.y - Radio*2, Radio, 0, Ext, false,Plano);
        
        if(TFNombre.getText() != "")
             NuevoCirc.Nombre = TFNombre.getText();

         Plano.add(NuevoCirc, JLayeredPane.DRAG_LAYER);
         Plano.moveToFront(NuevoCirc);

        Plano.remove(this);
        Plano.PnAgActivo = null;
        Plano.repaint();
    }

    public PnAgCirc(PnPlano plano) {
        super(plano);
        setBounds(300, getY(), getWidth(), 280);

        PnAgCirc PAg = this;

        Sector.x = 10;
        Sector.y = 10;
        Sector.width = 30;
        Sector.height = 30;
        Sector.start = 0;
        Sector.extent = 180;
        Sector.setArcType(Arc2D.PIE);

        TFNombre.setText("Circulo " + FrCirc.ID);

        LbRadio.setBounds(30, 105, 100, 20);
        LbRadio.setFont(Ctrl.Fnt1);
        
        TFRadio.setBounds(100, 105, 100, 20);
        TFRadio.setFont(Ctrl.Fnt1);


        BtCirNorm.setBounds(30, 150, 50, 50);
        BtCirNorm.setBackground(Ctrl.ClGris2);

 
        BtCirCuarto.setBounds(150, 150, 50, 50);
        BtCirCuarto.setBackground(Ctrl.ClGris2);



        BtCirSemi.setBounds(90, 150, 50, 50);
        BtCirSemi.setBackground(Ctrl.ClGris2);

        BtSel = BtCirNorm;

        BtCirNorm.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                BtSel = BtCirNorm;
                PAg.repaint();
            }
        });

        BtCirSemi.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                BtSel = BtCirSemi;
                PAg.repaint();
            }
        });

        BtCirCuarto.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                BtSel = BtCirCuarto;
                PAg.repaint();
            }
        });

        add(BtCirNorm);
        add(BtCirCuarto);
        add(BtCirSemi);
        add(TFRadio);
        add(LbRadio);

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
        g2.drawString("Agregar Circulo", 20, 45);
    }

}
