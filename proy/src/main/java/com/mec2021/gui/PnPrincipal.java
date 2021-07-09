package com.mec2021.gui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.FlowLayout;
import java.awt.CardLayout;
import java.awt.Polygon;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.mec2021.plano.objetos.formas.Forma;
import com.mec2021.plano.objetos.formas.FrTria;

import javafx.scene.shape.Line;

import com.mec2021.Ctrl;
import com.mec2021.EstructIndex.EstructuraInd;
import com.mec2021.EstructIndex.SeccionEI;
import com.mec2021.gui.agregarforma.PnAgCirc;
import com.mec2021.gui.agregarforma.PnAgRect;
import com.mec2021.gui.agregarforma.PnAgTria;
import javax.swing.JFileChooser;

public class PnPrincipal extends JPanel{

    public CardLayout Expositor = new CardLayout(); 
    BotonGenerico BtAgregarTab;

    public static PnPrincipal PanelPrinc;
    public JPanel PnBarraSup = new JPanel(){
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Ctrl.ClGrisClaro);
            g.drawLine(115, 5, 115, 35);
        }
    };
    public JPanel BarraTabs = new JPanel();
    public PnPlano PlanoActual;
    public JPanel PnAreaTrabajo = new JPanel();

    public static int TabsCount = 1;

    public final Polygon Disquete = new Polygon();
    public final Polygon Carpeta = new Polygon();

    public static Tab TabSel;

    public PnPrincipal(){

        PanelPrinc = this;
        Ctrl.PnPrinc = this;


        int posx[] = {10, 10, 30, 30, 25};
        int posy[] = {10, 30, 30, 15, 10};

        Disquete.npoints = 5;
        Disquete.xpoints = posx;
        Disquete.ypoints = posy;

        
        int posx2[] = {10, 20, 23, 30, 30, 10};
        int posy2[] = {10, 10, 15, 15, 30, 30};

        Carpeta.npoints = 6;
        Carpeta.xpoints = posx2;
        Carpeta.ypoints = posy2;



        setBackground(Ctrl.ClGris2);

        //INICIAR BARRA SUPERIOR
        iniciarBarraSuperior();

        //INICIAR BARRA PARA TABS
        BarraTabs.setBackground(Ctrl.ClGris2);
        BarraTabs.setPreferredSize(new Dimension(getWidth(), 30));
        BarraTabs.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));

        PnAreaTrabajo.setLayout(Expositor);

        TabSel = agregarTab();

        BtAgregarTab = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if(MouseEncima){
                    g.setColor(Ctrl.ClGrisClaro);
                }else{
                    g.setColor(Ctrl.ClGris2);
                }

                g.fillRect(2, 2, 26,26);

                g.setColor(Ctrl.ClGrisClaro3);
                g.drawLine(15, 8, 15, 22);
                g.drawLine(8, 15, 22, 15);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                agregarTab().seleccionarTab();
                BarraTabs.setComponentZOrder(this, BarraTabs.getComponentCount()-1);
            }
        };
        BtAgregarTab.setPreferredSize(new Dimension(30,30));
        BtAgregarTab.setOpaque(false);

        BarraTabs.add(BtAgregarTab);

        //ORDENAR COMPONENTES DE LA GUI
        SpringLayout Ly = new SpringLayout();
        setLayout(Ly);

        Ly.putConstraint(SpringLayout.NORTH, PnBarraSup, 0, SpringLayout.NORTH, this);
        Ly.putConstraint(SpringLayout.WEST, PnBarraSup, 0, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.EAST, PnBarraSup, 0, SpringLayout.EAST, this);

        Ly.putConstraint(SpringLayout.WEST, BarraTabs, 20, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.NORTH, BarraTabs, 0, SpringLayout.SOUTH, PnBarraSup);
        Ly.putConstraint(SpringLayout.EAST, BarraTabs, -20, SpringLayout.EAST, this);

        Ly.putConstraint(SpringLayout.WEST, PnAreaTrabajo, 0, SpringLayout.WEST, this);
        Ly.putConstraint(SpringLayout.NORTH, PnAreaTrabajo, 0, SpringLayout.SOUTH, BarraTabs);
        Ly.putConstraint(SpringLayout.SOUTH, PnAreaTrabajo, 0, SpringLayout.SOUTH, this);
        Ly.putConstraint(SpringLayout.EAST, PnAreaTrabajo, 0, SpringLayout.EAST, this);

        add(PnBarraSup);
        add(BarraTabs);
        add(PnAreaTrabajo);

        Expositor.show(PnAreaTrabajo, "Figura 1");
        PnAreaTrabajo.repaint();
    }
    
    public static Tab agregarTab(){
        return agregarTab("Figura " + TabsCount);
    }

    public static Tab agregarTab(String nom){

        String Nom = nom;

        PnPlano Plano = new PnPlano();
        PnPrincipal.PanelPrinc.PnAreaTrabajo.add(Nom, Plano);

        if(TabsCount == 1){
            PnPrincipal.PanelPrinc.PlanoActual = Plano;
        }
        
        Tab tab = new Tab(Nom, Plano);
        PnPrincipal.PanelPrinc.BarraTabs.add(tab);        
        PnPrincipal.PanelPrinc.BarraTabs.revalidate();

        TabsCount ++;

        return tab;
    }

    public void iniciarBarraSuperior(){
        SpringLayout Ly2 = new SpringLayout();

        PnBarraSup.setLayout(Ly2);
        PnBarraSup.setBackground(Color.gray);
        PnBarraSup.setPreferredSize(new Dimension(getWidth(), 40));
        PnBarraSup.setAlignmentX(JPanel.CENTER_ALIGNMENT);

    
        BotonGenerico BtGuardar = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(Ctrl.ClGrisClaro);



                if(MouseEncima){
                    setBackground(Color.darkGray);

                    g2.drawPolygon(Disquete);
                    g2.drawOval(15, 18, 10, 10);

                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

                    g2.drawRect(11, 11, 9, 4);
                }else{
                    setBackground(Color.GRAY);

                    g2.fillPolygon(Disquete);
                    g2.setColor(Color.GRAY);
                    g2.fillOval(15, 18, 10, 10);
                    g.fillRect(11, 11, 9, 4);
                }
            }
        };


        BotonGenerico BtCargar = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(Ctrl.ClGrisClaro);

                if(MouseEncima){
                    setBackground(Color.darkGray);
                    g2.drawPolygon(Carpeta);
                }else{
                    setBackground(Color.GRAY);                    
                    g2.fillPolygon(Carpeta);
                }
            }
        };


        BotonGenerico BtRect = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(Ctrl.ClGrisClaro);

                

                if(MouseEncima){
                    g2.drawRect(10, 10, 20, 20);
                    setBackground(Color.darkGray);
                }else{
                    g2.fillRect(10, 10, 20, 20);
                    setBackground(Color.GRAY);
                }
            }
        };

        BotonGenerico BtCirc = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(Ctrl.ClGrisClaro);

                

                if(MouseEncima){
                    g2.drawOval(10, 10, 20, 20);
                    setBackground(Color.darkGray);
                }else{
                    setBackground(Color.GRAY);
                    g2.fillOval(10, 10, 20, 20);
                }
            }
        };

        BotonGenerico BtTria = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(Ctrl.ClGrisClaro);

                int[] xs = {10,20,30};
                int[] ys = {30,10,30};
    
                

                if(MouseEncima){
                    g2.drawPolygon(xs, ys, 3);
                    setBackground(Color.darkGray);
                }else{
                    setBackground(Color.GRAY);
                    g2.fillPolygon(xs, ys, 3);
                }
            }
        };

        BtGuardar.setPreferredSize(new Dimension(40, 40));
        BtCargar.setPreferredSize(new Dimension(40, 40));
        BtRect.setPreferredSize(new Dimension(40, 40));
        BtCirc.setPreferredSize(new Dimension(40, 40));
        BtTria.setPreferredSize(new Dimension(40, 40));


        BtGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if(PlanoActual == null)
                    return;
                
                File Archivo = new File("");
                
                if(Ctrl.ExploradorArch.showSaveDialog(Ctrl.ExploradorArch) == JFileChooser.APPROVE_OPTION){
                    Archivo = Ctrl.ExploradorArch.getSelectedFile().getAbsoluteFile();


                    try {
                        FileWriter FW = new FileWriter(Archivo, false);
                        BufferedWriter BW = new BufferedWriter(FW);
                        BW.write(EstructuraInd.transformarEstrString(PlanoActual.generarData()));

                        BW.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    
                }
            }
        });

        BtCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File Archivo = new File("");
                
                if(Ctrl.ExploradorArch.showOpenDialog(Ctrl.ExploradorArch) == JFileChooser.APPROVE_OPTION){
                    Archivo = Ctrl.ExploradorArch.getSelectedFile().getAbsoluteFile();


                    try {

                        Scanner Sc = new Scanner(Archivo);
                        String Linea = Sc.nextLine();

                        String Nombre = EstructuraInd.extraerValor(Linea, 1);
                        SeccionEI Raiz = new SeccionEI(Nombre);
                        int Limite = Integer.parseInt(EstructuraInd.extraerValor(Linea, 2));

                        EstructuraInd.iniciarSeccion(Raiz, Sc, Limite);

                        Tab NuevaTab = agregarTab(Nombre);
                        NuevaTab.seleccionarTab();
                        NuevaTab.Plano.crearObjeto2D(Raiz);
                        BarraTabs.setComponentZOrder(BtAgregarTab, BarraTabs.getComponentCount()-1);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    
                }
            }
        });

        BtRect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(PlanoActual.PnAgActivo != null){
                    PlanoActual.remove(PlanoActual.PnAgActivo);
                    PlanoActual.PnAgActivo = null;
                }

                PnAgRect PG = new PnAgRect(PlanoActual);
                PlanoActual.add(PG, JLayeredPane.DRAG_LAYER);
                PlanoActual.moveToFront(PG);
                PG.TFNombre.requestFocus();
            }
        });

        BtCirc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(PlanoActual.PnAgActivo != null){
                    PlanoActual.remove(PlanoActual.PnAgActivo);
                    PlanoActual.PnAgActivo = null;
                }

                PnAgCirc PG = new PnAgCirc(PlanoActual);
                PlanoActual.add(PG, JLayeredPane.DRAG_LAYER);
                PlanoActual.moveToFront(PG);
                PG.TFNombre.requestFocus();
            }
        });

        BtTria.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if(PlanoActual.PnAgActivo != null){
                    PlanoActual.remove(PlanoActual.PnAgActivo);
                    PlanoActual.PnAgActivo = null;
                }

                PnAgTria PG = new PnAgTria(PlanoActual);
                PlanoActual.add(PG, JLayeredPane.DRAG_LAYER);
                PlanoActual.moveToFront(PG);
                PG.TFNombre.requestFocus();
            }
        });

        JLabel LbIngresarFr = new JLabel("Agregar Forma");
        LbIngresarFr.setForeground(Color.WHITE);
        LbIngresarFr.setFont(Ctrl.Fnt1);
        
        JLabel LbTitulo = new JLabel("Centroide y Momento de Inercia");
        LbTitulo.setFont(Ctrl.Fnt2);
        LbTitulo.setForeground(Color.WHITE);
        LbTitulo.setHorizontalAlignment(JLabel.CENTER);


        Ly2.putConstraint(SpringLayout.WEST, BtGuardar, 20, SpringLayout.WEST, PnBarraSup);
        Ly2.putConstraint(SpringLayout.NORTH,BtGuardar, 0, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH,BtGuardar, 0, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.WEST, BtCargar, 0, SpringLayout.EAST, BtGuardar);
        Ly2.putConstraint(SpringLayout.NORTH, BtCargar, 0, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, BtCargar, 0, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.WEST, LbIngresarFr, 40, SpringLayout.EAST, BtCargar);
        Ly2.putConstraint(SpringLayout.NORTH, LbIngresarFr, 0, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, LbIngresarFr, 0, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.WEST, BtRect, 20, SpringLayout.EAST, LbIngresarFr);
        Ly2.putConstraint(SpringLayout.NORTH, BtRect, 0, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, BtRect, 0, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.WEST, BtCirc, 0, SpringLayout.EAST, BtRect);
        Ly2.putConstraint(SpringLayout.NORTH, BtCirc, 0, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, BtCirc, 0, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.WEST, BtTria, 0, SpringLayout.EAST, BtCirc);
        Ly2.putConstraint(SpringLayout.NORTH, BtTria, 0, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, BtTria, 0, SpringLayout.SOUTH, PnBarraSup);

        Ly2.putConstraint(SpringLayout.EAST, LbTitulo, -10, SpringLayout.EAST, PnBarraSup);
        Ly2.putConstraint(SpringLayout.NORTH, LbTitulo, 10, SpringLayout.NORTH, PnBarraSup);
        Ly2.putConstraint(SpringLayout.SOUTH, LbTitulo, -10, SpringLayout.SOUTH, PnBarraSup);

        PnBarraSup.add(BtGuardar);
        PnBarraSup.add(BtCargar);
        PnBarraSup.add(BtRect);
        PnBarraSup.add(BtCirc);
        PnBarraSup.add(BtTria);
        PnBarraSup.add(LbIngresarFr);
        PnBarraSup.add(LbTitulo);
    }
}
