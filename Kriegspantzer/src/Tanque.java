public class Tanque implements Monitor {
    private String color;
    private int [] posTanque;
    private boolean estado_tanque;
        
    public Tanque (String color, int[] posTanque){
        this.color = color;
        this.posTanque = posTanque;
    }

    public boolean disparo(double velocidad, double angulo){
        return false;
    }

    public int[] getPos(){
        return posTanque;
    }
}

