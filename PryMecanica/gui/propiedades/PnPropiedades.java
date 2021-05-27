package PryMecanica.gui.propiedades;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import PryMecanica.Ctrl;
import PryMecanica.PnPlano;
import PryMecanica.gui.BotonGenerico;
import PryMecanica.plano.objetos.Objeto2D;

public abstract class PnPropiedades extends JPanel implements MouseListener{
    public JLabel LbNombre = new JLabel("Nombre");
    public Objeto2D ObjRef;

    public JPanel PnCont = new JPanel();

    KeyListener KL = new KeyListener(){

        String TextoPrev = "";

        @Override
        public void keyTyped(KeyEvent e) {
            JTextField JTF = (JTextField)e.getSource();
            TextoPrev = JTF.getText();
        }

        public void keyReleased(KeyEvent e) {
            
            JTextField JTF = (JTextField)e.getSource();
            

            if(!Character.isDigit(e.getKeyChar()) && e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && e.getKeyChar() != KeyEvent.VK_BACK_SPACE)
            try{
                JTF.setText(""+Ctrl.Utils.eliminarNumeros(JTF.getText()));
                Float.parseFloat(JTF.getText());
                
            }catch(NumberFormatException ex){
                if(!JTF.getText().equals("-"))
                    JTF.setText(TextoPrev);
            }
            
            actualizarForma();
        }
        
        public void keyPressed(KeyEvent e) {}
    };


    public PnPropiedades(Objeto2D obj) {
        setLayout(new BorderLayout());

        addMouseListener(this);

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

    public abstract void actualizarForma();

    @Override
    public void mousePressed(MouseEvent e) {
        PnPlano.PlPrinc.moveToFront(this);
        
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
