package PryMecanica.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import PryMecanica.PnPlano;
import PryMecanica.Plano.Objetos.Objeto2D;

public abstract class PnPropiedades extends JPanel {
    public JLabel LbNombre = new JLabel("Nombre");
    public Objeto2D ObjRef;

    public JPanel PnCont = new JPanel();

    public PnPropiedades(Objeto2D obj) {
        setLayout(new BorderLayout());

        PnCont.setLayout(null);

        setBounds(250, 50, 200, 300);

        ObjRef = obj;

        LbNombre.setText(obj.Nombre);
        LbNombre.setVerticalAlignment(JLabel.CENTER);


        JPanel PnSup = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawLine(7, getHeight() - 1, getWidth() - 7, getHeight() - 1);
            }
        };
        PnSup.setLayout(null);


        
        BotonGenerico BtCerrar = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                g.drawLine(4, 4, 16, 16);
                g.drawLine(16, 4, 4, 16);
            }
        };

        PnPropiedades PP = this;

        BtCerrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PnPlano.PlPrinc.remove(PP);
                PnPlano.PlPrinc.repaint();
            }
            
        });
        
        BtCerrar.setBounds(getWidth()-27, 7,20,20);

        LbNombre.setBounds(10,0,100,35);

        PnSup.setPreferredSize(new Dimension(getWidth(),35));

        PnSup.add(LbNombre);
        PnSup.add(BtCerrar);
        
        add(PnSup, BorderLayout.NORTH);
        add(PnCont, BorderLayout.CENTER);
    }

    public abstract void actualizarDatos();
}
