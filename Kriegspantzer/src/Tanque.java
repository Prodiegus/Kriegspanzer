public class Tanque {
    private String color;
    private int [] posTanque;
    private Bala bala;
        
    public Tanque (String color, int[] posTanque, Bala bala){
        this.color = color;
        this.posTanque = posTanque;
        this.bala=bala;
    }

    public boolean disparo(double velocidad, double angulo, Mapa mapa){
        /*
            Setea la posicion de la bala, antiguamente era (x,0)
            Luego calcular el lanzamiento con la bala del tanque
        */
        this.bala.setPosBala(posTanque);
        return (bala.calcularLanzamiento(bala, velocidad, angulo, mapa)==1);
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

