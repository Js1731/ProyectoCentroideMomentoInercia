package com.mec2021;
import javax.swing.JFrame;

import com.mec2021.gui.PnPrincipal;
import com.mec2021.plano.objetos.formas.Forma;

import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;

class DKE implements KeyEventDispatcher{
    boolean Presionado = false;

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if(!Presionado){
            
            if(e.isControlDown())
                if(e.getKeyCode() == KeyEvent.VK_V){
                    
                    System.out.println("Soi nuebo");

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
                    System.out.println("Soi nuebo");

                    Forma FrS = PnPrincipal.PanelPrinc.PlanoActual.FrSel;
                    String Data = "<|||>" + FrS.generarData();

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

        Ctrl.importarArchivos();

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new DKE());

        Vent.add(new PnPrincipal());

        Vent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Vent.setSize(500, 500);
        Vent.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Vent.setVisible(true);
    }
}

