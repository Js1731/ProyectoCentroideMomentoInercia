package PryMecanica.GUI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import PryMecanica.PnPlano;

import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;

public class ListaOpciones extends JPanel{

    public int CantOp = 0;

    public ListaOpciones(int X, int Y) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setBounds(X, Y, 130, 200);

        setBackground(Color.GRAY);

        ListaOpciones LP = this;

        AWTEventListener Listener = new AWTEventListener(){

            @Override
            public void eventDispatched(AWTEvent event) {
                if ( event.getID () == MouseEvent.MOUSE_PRESSED ){
                    try{
                        if(!LP.contains(SwingUtilities.convertPoint((JComponent)event.getSource(), ((MouseEvent)event).getPoint(), LP))){
                            LP.setVisible(false);
                            PnPlano.PnPrinc.repaint();
                        }
                    }catch(ClassCastException e){}
                }
                
            }
        };

        Toolkit.getDefaultToolkit().addAWTEventListener(Listener, AWTEvent.MOUSE_EVENT_MASK);

        agregarOpcion(new Opcion("Copiar"));
        agregarOpcion(new Opcion("Cortar"));
        agregarOpcion(new Opcion("Pegar"));
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GRAY);
        g.fillRect(0,0,getWidth(),getHeight());
    }

    public void agregarOpcion(Opcion op){
        CantOp++;
        setBounds(getX(),getY(),getWidth(),30*CantOp);
        add(op);
        repaint();
    }


}
