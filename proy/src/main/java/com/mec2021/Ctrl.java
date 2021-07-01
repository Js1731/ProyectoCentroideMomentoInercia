package com.mec2021;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import com.mec2021.gui.PnPrincipal;

import java.awt.Color;

public class Ctrl {

    public static PnPrincipal PnPrinc = null;

    //CONSTANTES PARA LAS FUENTES
    public static Font Fnt0;
    public static Font Fnt1;
    public static Font Fnt2;

    //CONSTANTES PARA COLORES
    public static final Color ClGrisClaro = new Color(196, 196, 196);
    public static final Color ClGrisClaro2 = new Color(220, 220, 220);
    public static final Color ClGrisClaro3 = new Color(240, 240, 240);
    public static final Color ClGris = new Color(136, 136, 136);
    public static final Color ClGris2 = new Color(172, 172, 172);

    public static void importarArchivos(){
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Roboto-Regular.ttf");
        InputStream stream1 = ClassLoader.getSystemClassLoader().getResourceAsStream("Roboto-Regular.ttf");
        InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("Roboto-Bold.ttf");
        try {
            Fnt0 = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(9f);
            Fnt1 = Font.createFont(Font.TRUETYPE_FONT, stream1).deriveFont(15f);
            Fnt2 = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(20f);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transforma un valor lineal de Pixeles a Unidad
     * @param Valor
     * @return
     */
    public static Float aplicarEscalaLnPixU(float Valor){
        return Valor*((float)PnPrinc.PlanoActual.Escala/PnPrinc.PlanoActual.EscalaPix);
    }

    /**
     * Transforma un valor de area de Pixeles a Unidad
     * @param Valor
     * @return
     */
    public static Float aplicarEscalaAr(float Valor){
        return  Valor*((float)(PnPrinc.PlanoActual.Escala*PnPrinc.PlanoActual.Escala)/(PnPrinc.PlanoActual.EscalaPix*PnPrinc.PlanoActual.EscalaPix));
    }

    /**
     * Transforma un valor lineal de Unidades a Pixeles
     * @param Valor
     * @return
     */
    public static Float aplicarEscalaUPix(float Valor){
        return Valor*((float)PnPrinc.PlanoActual.EscalaPix/PnPrinc.PlanoActual.Escala);
    }

    /**
     * Transforma un valor de area de Unidades a Pixeles
     * @param Valor
     * @return
     */
    public static Float aplicarEscalaArInv(float Valor){
        return  Valor*((float)(PnPrinc.PlanoActual.EscalaPix*PnPrinc.PlanoActual.EscalaPix)/(PnPrinc.PlanoActual.Escala*PnPrinc.PlanoActual.Escala));
    }

    /**Ajustar un valor entre un minimo y un maximo 
     * 
     * @param a Valor
     * @param min Valor Minimo
     * @param max Valor Maximo
     * @return Valor ajustado
     */
    public static float clamp(float a, float min, float max){
        return a < max ? a > min ? a : min : max; 
    }

    /**
     * Elimina todos los caracteres no numericos en una cadena
     * @param txt
     * @return
     */
    public static String eliminarLetras(String txt){
        
        String StrFin = "";
        
        for (int i = 0; i < txt.length(); i++) {
            char c = txt.charAt(i);
            
            if(c >= 48 && c <= 57 || c == 45)
                StrFin += c;
        
            if(c == 44 || c == 46)
                StrFin += '.';
        }

        return StrFin;
    }

}