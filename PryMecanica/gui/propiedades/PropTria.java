package PryMecanica.gui.propiedades;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTextField;

import PryMecanica.Ctrl;
import PryMecanica.PnPlano;
import PryMecanica.plano.objetos.Objeto2D;
import PryMecanica.plano.objetos.formas.Forma;
import PryMecanica.plano.objetos.formas.FrTria;

/**Panel de propiedades para un Triangulo*/
public class PropTria extends PnPropiedades{

    private JLabel LbArea = new JLabel("Area:");
    private JLabel LbCentX = new JLabel("Centroide en X:");
    private JLabel LbCentY = new JLabel("Centroide en Y:");

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
                JTF.setText(""+Ctrl.Utils.eliminarLetras(JTF.getText()));
                Float.parseFloat(JTF.getText());

            }catch(NumberFormatException ex){
                if(!JTF.getText().equals("-"))
                    JTF.setText(TextoPrev);
            }

            actualizarPosTriangulo();
        }

        public void keyPressed(KeyEvent e) {}
    };

    public PropTria(Objeto2D obj) {
        super(obj);

        setBounds(getX(), getY(), getWidth(), 400);

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

        CBHueco.setBounds(8, 120  + Y*Esp, 120, 30);


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
        TFX.setText(""+PnPlano.Escala*(float)ObjRef.X/Forma.Escala);
        TFY.setText(""+PnPlano.Escala*(float)-ObjRef.Y/Forma.Escala);

        TFX1.setText(""+PnPlano.Escala*(float)(((FrTria)ObjRef).Ver1.x  - PnPlano.PtOrigen.x)/Forma.Escala);
        TFY1.setText(""+PnPlano.Escala*(float)-(((FrTria)ObjRef).Ver1.y  - PnPlano.PtOrigen.y)/Forma.Escala);

        TFX2.setText(""+PnPlano.Escala*(float)(((FrTria)ObjRef).Ver2.x  - PnPlano.PtOrigen.x)/Forma.Escala);
        TFY2.setText(""+PnPlano.Escala*(float)-(((FrTria)ObjRef).Ver2.y  - PnPlano.PtOrigen.y)/Forma.Escala);

        TFX3.setText(""+PnPlano.Escala*(float)(((FrTria)ObjRef).Ver3.x  - PnPlano.PtOrigen.x)/Forma.Escala);
        TFY3.setText(""+PnPlano.Escala*(float)-(((FrTria)ObjRef).Ver3.y  - PnPlano.PtOrigen.y)/Forma.Escala);

        DecimalFormat f = new DecimalFormat("#0.00");

        LbArea.setText("Area:                      "+f.format(PnPlano.Escala*(float)((FrTria)ObjRef).calcularArea()/(Forma.Escala * Forma.Escala)));
        LbCentX.setText("Centroide en x:     "+f.format(PnPlano.Escala*(float)((FrTria)ObjRef).centroideX()/Forma.Escala));
        LbCentY.setText("Centroide en Y:     "+f.format(-(PnPlano.Escala*(float)((FrTria)ObjRef).centroideY() - ((FrTria)ObjRef).getHeight())/Forma.Escala));

        CBHueco.setSelected(((Forma)ObjRef).Hueco);
    }

    @Override
    public void actualizarForma() {
        FrTria Tria = (FrTria)ObjRef;

        Tria.Ver1.x = (Float.parseFloat((TFX1.getText().isEmpty() || TFX1.getText().equals("-") ? "0" : TFX1.getText())) )*Forma.Escala/PnPlano.Escala + PnPlano.PtOrigen.x;
        Tria.Ver1.y = -(Float.parseFloat((TFY1.getText().isEmpty() || TFY1.getText().equals("-") ? "0" : TFY1.getText())) )*Forma.Escala/PnPlano.Escala + PnPlano.PtOrigen.y;

        Tria.Ver2.x = (Float.parseFloat((TFX2.getText().isEmpty() || TFX2.getText().equals("-") ? "0" : TFX2.getText())))*Forma.Escala/PnPlano.Escala  + PnPlano.PtOrigen.x;
        Tria.Ver2.y = -(Float.parseFloat((TFY2.getText().isEmpty() || TFY2.getText().equals("-") ? "0" : TFY2.getText())))*Forma.Escala/PnPlano.Escala  + PnPlano.PtOrigen.y;

        Tria.Ver3.x = (Float.parseFloat((TFX3.getText().isEmpty() || TFX3.getText().equals("-") ? "0" : TFX3.getText())))*Forma.Escala/PnPlano.Escala  + PnPlano.PtOrigen.x;
        Tria.Ver3.y = -(Float.parseFloat((TFY3.getText().isEmpty() || TFY3.getText().equals("-") ? "0" : TFY3.getText())))*Forma.Escala/PnPlano.Escala  + PnPlano.PtOrigen.y;

        Tria.Hueco = CBHueco.isSelected();

        Tria.ActualizarCoordenadas();
        Tria.ActualizarBordes();
        Tria.ActualizarPines();

        PnPlano.PlPrinc.repaint();
    }

    /**Actualiza los vertices en base a la posicion X y Y */
    public void actualizarPosTriangulo() {
        FrTria Tria = (FrTria)ObjRef;

        int DifX = Math.round(Float.parseFloat((TFX.getText().isEmpty() || TFX.getText().equals("-") ? "0" : TFX.getText()))*Forma.Escala)/PnPlano.Escala  - Tria.X;
        int DifY = -Math.round(Float.parseFloat((TFY.getText().isEmpty() || TFY.getText().equals("-") ? "0" : TFY.getText()))*Forma.Escala)/PnPlano.Escala - Tria.Y;

        Tria.setBounds(Math.round(PnPlano.PtOrigen.x + Float.parseFloat((TFX.getText().isEmpty() || TFX.getText().equals("-") ? "0" : TFX.getText()))*Forma.Escala/PnPlano.Escala),
                      Math.round(PnPlano.PtOrigen.y - Float.parseFloat((TFY.getText().isEmpty() || TFY.getText().equals("-") ? "0" : TFY.getText()))*Forma.Escala/PnPlano.Escala),
                      Tria.getWidth(),
                      Tria.getHeight());

        Tria.moverVertices( DifX, DifY);

        Tria.Hueco = CBHueco.isSelected();

        Tria.ActualizarCoordenadas();
        Tria.ActualizarPines();
        PnPlano.PlPrinc.repaint();

        //ACTUALIZAR CAMPOS
        TFX1.setText(""+PnPlano.Escala*(float)(((FrTria)ObjRef).Ver1.x  - PnPlano.PtOrigen.x)/Forma.Escala);
        TFY1.setText(""+PnPlano.Escala*(float)-(((FrTria)ObjRef).Ver1.y  - PnPlano.PtOrigen.y)/Forma.Escala);

        TFX2.setText(""+PnPlano.Escala*(float)(((FrTria)ObjRef).Ver2.x  - PnPlano.PtOrigen.x)/Forma.Escala);
        TFY2.setText(""+PnPlano.Escala*(float)-(((FrTria)ObjRef).Ver2.y  - PnPlano.PtOrigen.y)/Forma.Escala);

        TFX3.setText(""+PnPlano.Escala*(float)(((FrTria)ObjRef).Ver3.x  - PnPlano.PtOrigen.x)/Forma.Escala);
        TFY3.setText(""+PnPlano.Escala*(float)-(((FrTria)ObjRef).Ver3.y  - PnPlano.PtOrigen.y)/Forma.Escala);
    }

}
