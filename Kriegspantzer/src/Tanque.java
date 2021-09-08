public class Tanque implements Monitor {
    private String color;
    private int [] posTanque;
    private int barraVida;
    private boolean estadoTanque;
        
    public Tanque (String color, int[] posTanque){
        this.color = color;
        this.posTanque = posTanque;
    }

    public boolean disparo(double velocidad, double angulo,int sentido){
        return false;
    }

    public int[] getPos(){
        return posTanque;
    }
    public String getColor() {
        return color;
    }
    public void setPos(int x, int y){
        int [] pos = {x, y};
        posTanque = pos;
    }
}

