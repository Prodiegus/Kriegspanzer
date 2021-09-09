public class Tanque implements Monitor {
    private String color;
    private int [] posTanque;
    private int barraVida;
    private boolean estadoTanque;
    private Bala bala;
        
    public Tanque (String color, int[] posTanque, Bala bala){
        this.color = color;
        this.posTanque = posTanque;
        this.bala=bala;
    }

    public boolean disparo(double velocidad, double angulo,int sentido){
        return false;
    }

    public int[] getPos(){
        return posTanque;
    }
    
    public void setBala(Bala bala){
        this.bala=bala;
    
    }

    public Bala getBala() {
        return bala;
    }
}

