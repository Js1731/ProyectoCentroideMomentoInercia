package PryMecanica;

import java.awt.Font;
import java.awt.Color;

public class Ctrl {
    
    public static Font Fnt0 = new Font("Roboto", Font.PLAIN, 9);
    public static Font Fnt1 = new Font("Roboto", Font.PLAIN, 12);
    public static Font Fnt2 = new Font("Roboto", Font.PLAIN, 20);

    public static Color ClGrisClaro = new Color(196, 196, 196);
    public static Color ClGrisClaro2 = new Color(220, 220, 220);
    public static Color ClGris = new Color(136, 136, 136);

    public static class Utils{
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
}