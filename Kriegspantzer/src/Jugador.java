public class Jugador extends Juego {
    private String nombre;
    private boolean estado;
    private Tanque tanque;
     
    public Jugador(String nombre){
        super(null);
        this.nombre = nombre;
    }

    public boolean Lanzamiento(double velocidad, double angulo){
        return false;
    }

    public boolean cheekTanque(){
        return false;
    }

}

