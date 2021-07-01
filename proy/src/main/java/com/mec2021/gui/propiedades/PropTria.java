package com.mec2021.gui.propiedades;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import com.mec2021.plano.objetos.Objeto2D;
import com.mec2021.plano.objetos.formas.Forma;
import com.mec2021.plano.objetos.formas.FrTria;
import com.mec2021.Ctrl;
import com.mec2021.gui.PnPlano;

/**Panel de propiedades para un Triangulo*/
public class PropTria extends PnPropiedades{

    private JTextField TFX = new JTextField();
    private JTextField TFY = new JTextField();

    private JTextField TFX1 = new JTextField();
    private JTextField TFY1 = new JTextField();

    private JTextField TFX2 = new JTextField();
    private JTextField TFY2 = new JTextField();

    private JTextField TFX3 = new JTextField();
    private JTextField TFY3 = new JTextField();


    KeyListener KL2 = new KeyListener(){

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
                JTF.setText(""+Ctrl.eliminarLetras(JTF.getText()));
                Float.parseFloat(JTF.getText());

            }catch(NumberFormatException ex){
                if(!JTF.getText().equals("-"))
                    JTF.setText(TextoPrev);
            }

            actualizarPosTriangulo();
        }

        public void keyPressed(KeyEvent e) {}
    };

    public PropTria(Objeto2D obj, PnPlano plano) {
        super(obj, plano);

        setBounds(getX(), getY(), getWidth(), 490);

        int Y = 1;
        int Esp = 30;

        //POSICION X
        JLabel LbX = new JLabel("X");
        LbX.setBounds(10, 15, 50, 10);

        TFX.setBounds(30, 10, 50, 20);
        TFX.addKeyListener(KL2);


        //POSICION Y
        JLabel LbY = new JLabel("Y");
        LbY.setBounds(100, 15, 50, 10);

        TFY.setBounds(130, 10, 50, 20);
        TFY.addKeyListener(KL2);

        //VERTICE 1
        JLabel LbVer1 = new JLabel("Vertice 1");
        LbVer1.setBounds(10, 25 + Y*Esp, 100, 10);
        Y++;

        //POSICION X
        JLabel LbX1 = new JLabel("X");
        LbX1.setBounds(10, 25 + Y*Esp, 50, 10);

        TFX1.setBounds(30, 20 + Y*Esp, 50, 20);
        TFX1.addKeyListener(TextCont);

        //POSICION Y
        JLabel LbY1 = new JLabel("Y");
        LbY1.setBounds(100, 25 + Y*Esp, 50, 10);

        TFY1.setBounds(130, 20 + Y*Esp, 50, 20);
        TFY1.addKeyListener(TextCont);
        Y++;

        //VERTICE 2
        JLabel LbVer2 = new JLabel("Vertice 2");
        LbVer2.setBounds(10, 25 + Y*Esp, 100, 10);
        Y++;

        //POSICION X
        JLabel LbX2 = new JLabel("X");
        LbX2.setBounds(10, 25 + Y*Esp, 50, 10);

        TFX2.setBounds(30, 20 + Y*Esp, 50, 20);
        TFX2.addKeyListener(TextCont);

        //POSICION Y
        JLabel LbY2 = new JLabel("Y");
        LbY2.setBounds(100, 25 + Y*Esp, 50, 10);

        TFY2.setBounds(130, 20 + Y*Esp, 50, 20);
        TFY2.addKeyListener(TextCont);
        Y++;

        //VERTICE 1
        JLabel LbVer3 = new JLabel("Vertice 3");
        LbVer3.setBounds(10, 25 + Y*Esp, 100, 10);
        Y++;

        //POSICION X
        JLabel LbX3 = new JLabel("X");
        LbX3.setBounds(10, 25 + Y*Esp, 50, 10);

        TFX3.setBounds(30, 20 + Y*Esp, 50, 20);
        TFX3.addKeyListener(TextCont);
        //POSICION Y
        JLabel LbY3 = new JLabel("Y");
        LbY3.setBounds(100, 25 + Y*Esp, 50, 10);

        TFY3.setBounds(130, 20 + Y*Esp, 50, 20);
        TFX3.addKeyListener(TextCont);
        Y++;

        //PROPIEDADES
        LbArea.setBounds(10, 40 + Y*Esp, 200, 10);
        LbCentX.setBounds(10, 70 + Y*Esp, 200, 10);
        LbCentY.setBounds(10, 100 + Y*Esp, 200, 10);

        LbInX.setBounds(10, 140 + Y*Esp, 200, 10);
        LbInY.setBounds(10, 170 + Y*Esp, 200, 10);

        CBHueco.setBounds(8, 200  + Y*Esp, 120, 30);


        PnCont.add(LbX);
        PnCont.add(TFX);
        PnCont.add(LbY);
        PnCont.add(TFY);

        PnCont.add(LbVer1);

        PnCont.add(LbX1);
        PnCont.add(TFX1);
        PnCont.add(LbY1);
        PnCont.add(TFY1);

        PnCont.add(LbVer2);

        PnCont.add(LbX2);
        PnCont.add(TFX2);
        PnCont.add(LbY2);
        PnCont.add(TFY2);

        PnCont.add(LbVer3);

        PnCont.add(LbX3);
        PnCont.add(TFX3);
        PnCont.add(LbY3);
        PnCont.add(TFY3);

        PnCont.add(LbArea);
        PnCont.add(LbCentX);
        PnCont.add(LbCentY);

        PnCont.add(CBHueco);

        actualizarDatos();
    }

    @Override
    public void actualizarDatos() {

        FrTria Tria = (FrTria)ObjRef;
        DecimalFormat f = new DecimalFormat("#0.00");

        TFX.setText("" + f.format(Tria.X));
        TFY.setText("" + f.format(-Tria.Y));

        TFX1.setText("" + f.format(Tria.Ver1.x));
        TFY1.setText("" + f.format(-Tria.Ver1.y));

        TFX2.setText("" + f.format(Tria.Ver2.x));
        TFY2.setText("" + f.format(-Tria.Ver2.y));

        TFX3.setText("" + f.format(Tria.Ver3.x));
        TFY3.setText("" + f.format(-Tria.Ver3.y));

        

        LbArea.setText("Area:                      " + f.format(Tria.calcularArea()));
        LbCentX.setText("Centroide en x:     " + f.format(Tria.centroideX()));
        LbCentY.setText("Centroide en Y:     " + f.format(Ctrl.aplicarEscalaLnPixU(ObjRef.getHeight()) - Tria.centroideY()));

        LbInX.setText("Inercia en x:     " + f.format(Tria.inerciaCentEjeX()));
        LbInY.setText("Inercia en Y:     " + f.format(Tria.inerciaCentEjeY()));

        CBHueco.setSelected(((Forma)ObjRef).Hueco);
    }

    @Override
    public void actualizarForma() {
        FrTria Tria = (FrTria)ObjRef;

        Tria.Ver1.x = Float.parseFloat((TFX1.getText().isEmpty() || TFX1.getText().equals("-") ? "0" : TFX1.getText()));
        Tria.Ver1.y = -Float.parseFloat((TFY1.getText().isEmpty() || TFY1.getText().equals("-") ? "0" : TFY1.getText()));

        Tria.Ver2.x = Float.parseFloat((TFX2.getText().isEmpty() || TFX2.getText().equals("-") ? "0" : TFX2.getText()));
        Tria.Ver2.y = -Float.parseFloat((TFY2.getText().isEmpty() || TFY2.getText().equals("-") ? "0" : TFY2.getText()));

        Tria.Ver3.x = Float.parseFloat((TFX3.getText().isEmpty() || TFX3.getText().equals("-") ? "0" : TFX3.getText()));
        Tria.Ver3.y = -Float.parseFloat((TFY3.getText().isEmpty() || TFY3.getText().equals("-") ? "0" : TFY3.getText()));

        Tria.Hueco = CBHueco.isSelected();

        Tria.ActualizarBordes();
        Tria.actualizarPines();

        Plano.repaint();
    }

    /**Actualiza los vertices en base a la posicion X y Y */
    public void actualizarPosTriangulo() {
        FrTria Tria = (FrTria)ObjRef;

        int DifX = Math.round(Ctrl.aplicarEscalaUPix(Float.parseFloat((TFX.getText().isEmpty() || TFX.getText().equals("-") ? "0" : TFX.getText())))  - Tria.X);
        int DifY = -Math.round(Ctrl.aplicarEscalaUPix(Float.parseFloat((TFY.getText().isEmpty() || TFY.getText().equals("-") ? "0" : TFY.getText()))) - Tria.Y);

        Tria.setBounds(Math.round(Plano.PtOrigen.x + Ctrl.aplicarEscalaUPix(Float.parseFloat((TFX.getText().isEmpty() || TFX.getText().equals("-") ? "0" : TFX.getText())))),
                      Math.round(Plano.PtOrigen.y - Ctrl.aplicarEscalaUPix(Float.parseFloat((TFY.getText().isEmpty() || TFY.getText().equals("-") ? "0" : TFY.getText())))),
                      Tria.getWidth(),
                      Tria.getHeight());

        Tria.moverVertices( DifX, DifY);

        Tria.Hueco = CBHueco.isSelected();

        Tria.actualizarCoordenadas();
        Tria.actualizarPines();
        Plano.repaint();

        //ACTUALIZAR CAMPOS
        TFX1.setText("" + Ctrl.aplicarEscalaUPix(Tria.Ver1.x - Plano.PtOrigen.x));
        TFY1.setText("" + Ctrl.aplicarEscalaUPix(-Tria.Ver1.y + Plano.PtOrigen.y));

        TFX2.setText("" + Ctrl.aplicarEscalaUPix(Tria.Ver2.x - Plano.PtOrigen.x));
        TFY2.setText("" + Ctrl.aplicarEscalaUPix(-Tria.Ver2.y + Plano.PtOrigen.y));

        TFX3.setText("" + Ctrl.aplicarEscalaUPix(Tria.Ver3.x - Plano.PtOrigen.x));
        TFY3.setText("" + Ctrl.aplicarEscalaUPix(-Tria.Ver3.y + Plano.PtOrigen.y));
    }

}
