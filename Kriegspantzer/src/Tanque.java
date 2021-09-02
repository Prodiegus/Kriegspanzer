public class Tanque implements Monitor {
    private String color;
    private int [] pos_tanque;
    private int barra_vida;
    private boolean estado_tanque;
        
    public Tanque (String color, int[] pos_tanque){
        this.color = color;
        this.pos_tanque = pos_tanque;
    }

    public boolean Disparo(double velocidad, double angulo,int sentido){
        return false;
    }

}

