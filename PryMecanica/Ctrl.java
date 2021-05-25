package PryMecanica;

public class Ctrl {
    
    public static class Utils{
        public static float clamp(float a, float min, float max){
            return a < max ? a > min ? a : min : max; 
        }
    }
}
