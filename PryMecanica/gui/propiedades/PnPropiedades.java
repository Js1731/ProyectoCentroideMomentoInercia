package PryMecanica.gui.propiedades;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import PryMecanica.Ctrl;
import PryMecanica.plano.objetos.formas.Forma;
import PryMecanica.PnPlano;
import PryMecanica.gui.BotonGenerico;
import PryMecanica.plano.objetos.Objeto2D;

/**Panel generico para mostrar las propiedades de un {@link Objeto2D} */
public abstract class PnPropiedades extends JPanel implements MouseListener{
    public JLabel LbNombre = new JLabel("Nombre");

    /**Referencia al Objeto al que pertenecen las propiedades */
    public Objeto2D ObjRef;

    /**Panel con las propiedades del objeto */
    public JPanel PnCont = new JPanel();

    /**Indica si la forma esta hueca */
    JCheckBox CBHueco = new JCheckBox("Hueco");

    /**Controlador para manejar la entrada, se encarga de eliminar caracteres invalidos y notificar los cambios en un JTextField*/
    KeyListener TextCont = new KeyListener(){

        /**Texto antes de realizar cambios al JTextField */
        private String TextoPrev = "";

        @Override
        public void keyTyped(KeyEvent e) {
            //GUARDAR TEXTO INICIAL DEL JTEXTFIELD
            JTextField JTF = (JTextField)e.getSource();
            TextoPrev = JTF.getText();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            
            JTextField JTF = (JTextField)e.getSource();
            
            //SI SE INGRESA UN CARACTER QUE NO SEA NUMERICO, ENTONCESE SE ELIMINA LOS CARACTERES INVALIDOS
            if(!Character.isDigit(e.getKeyChar()) && e.getKeyChar() != KeyEvent.CHAR_UNDEFINED && e.getKeyChar() != KeyEvent.VK_BACK_SPACE)
                try{
                    JTF.setText(""+Ctrl.Utils.eliminarLetras(JTF.getText()));
                    Float.parseFloat(JTF.getText());
                    
                }catch(NumberFormatException ex){
                    //SI LO QUE SE HA INGRESADO NO SE PUEDE CORREGIR, ENTONCES SE REVIERTE EL TEXTO AL TEXTO INICIAL
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


        //PANEL SUPERIOR CON EL TITULO
        JPanel PnSup = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawLine(7, getHeight() - 1, getWidth() - 7, getHeight() - 1);
            }
        };
        PnSup.setLayout(null);


        //BOTON PARA CERRAR EL PANEL
        PnPropiedades PP = this;
        BotonGenerico BtCerrar = new BotonGenerico(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                g.drawLine(4, 4, 16, 16);
                g.drawLine(16, 4, 4, 16);
            }
        };

        

        BtCerrar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PnPlano.PlPrinc.remove(PP);
                PnPlano.PlPrinc.repaint();
            }
            
        });
        BtCerrar.setBounds(getWidth()-27, 7,20,20);
        
        CBHueco.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                ((Forma)ObjRef).Hueco = CBHueco.isSelected();
                PnPlano.PlPrinc.repaint();
            }

        });        
        LbNombre.setBounds(10,0,100,35);

        PnSup.setPreferredSize(new Dimension(getWidth(),35));

        PnSup.add(LbNombre);
        PnSup.add(BtCerrar);
        
        add(PnSup, BorderLayout.NORTH);
        add(PnCont, BorderLayout.CENTER);
    }

    /**Actualiza las propiedades del panel a las del objeto */
    public abstract void actualizarDatos();

    /**Actualiza al objeto con las propiedades del Panel */
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
