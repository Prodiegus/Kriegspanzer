public class Tanque extends Jugador implements Monitor {
    private String color;
    private int [] pos_tanque;
    private boolean estado_tanque;
        
    public Tanque (String color, int[] pos_tanque){
        super("");
        this.color = color;
        this.pos_tanque = pos_tanque;
    }

    public boolean Disparo(double velocidad, double angulo){
        return false;
    }

}

