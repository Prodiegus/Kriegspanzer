public class Tanque {
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

    public boolean disparo(double velocidad, double angulo){
        this.bala.setPosBala(posTanque);//setea la posicion de la bala, antiguamente era (x,0)
        return (bala.calcularLanzamiento(bala, velocidad, angulo)==1);
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
    
    public void setBala(Bala bala){
        this.bala=bala;
    
    }

    public Bala getBala() {
        return bala;
    }
}

