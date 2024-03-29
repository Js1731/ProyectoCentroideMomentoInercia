package com.mec2021;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.mec2021.gui.PnPrincipal;
import com.mec2021.plano.objetos.formas.Forma;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.UnsupportedLookAndFeelException;

class DKE implements KeyEventDispatcher{
    boolean Presionado = false;

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if(!Presionado){
            
            if(e.isControlDown() && PnPrincipal.PanelPrinc.PlanoActual != null)
                if(e.getKeyCode() == KeyEvent.VK_V){
                    
                    Clipboard CB =Toolkit.getDefaultToolkit().getSystemClipboard();
                    String Datos;
                    try {
                        Datos = (String)CB.getData(DataFlavor.stringFlavor);
                        String Llave = Datos.substring(0, 5);

                        if(Llave.equals("<|||>")){
                            Datos = Datos.substring(5, Datos.length());
                            PnPrincipal.PanelPrinc.PlanoActual.crearObjeto2D(Datos);
                        }

                    } catch (UnsupportedFlavorException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    Presionado = true;
                }else if(e.getKeyCode() == KeyEvent.VK_C){

                    Forma FrS = PnPrincipal.PanelPrinc.PlanoActual.FrSel;
                    String Data = "<|||>" + FrS.generarDataString();

                    Clipboard CB =Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection SL = new StringSelection(Data);
                    CB.setContents(SL, null);
                    
                    Presionado = true;
                    
                }
        }

        if(e.getID() == KeyEvent.KEY_RELEASED)
            Presionado = false;
        

        return false;
    }
    
}

public class Main {

    public static JFrame VentPrinc;

    public static void main(String[] args){
        JFrame Vent = VentPrinc = new JFrame();    

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        Ctrl.importarArchivos();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new DKE());

        
        Vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Vent.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50);
        Vent.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Vent.add(new PnPrincipal());
        Vent.setVisible(true);
        
    }
}

