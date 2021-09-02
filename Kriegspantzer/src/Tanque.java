public class Tanque implements Monitor {
    private String color;
    private int [] pos_tanque;
    private int barra_vida;
    private boolean estado_tanque;
        
    public Tanque (String color, int[] posTanque){
        this.color = color;
        this.posTanque = posTanque;
    }

    public boolean Disparo(double velocidad, double angulo,int sentido){
        return false;
    }

    public int[] getPos(){
        return posTanque;
    }
}

