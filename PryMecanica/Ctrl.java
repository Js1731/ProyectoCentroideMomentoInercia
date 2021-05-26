package PryMecanica;

public class Ctrl {
    
    public static class Utils{
        public static float clamp(float a, float min, float max){
            return a < max ? a > min ? a : min : max; 
        }

        public static String eliminarNumeros(String txt){
            
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
