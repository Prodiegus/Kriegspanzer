public class Jugador{
    private String nombre;
    private boolean estado;
    private Tanque tanque;
     
    public Jugador(String nombre){
        this.nombre = nombre;
        this.estado = true;
    }

    public boolean lanzamiento(double velocidad, double angulo){
        return false;
    }


    public void setTanque(Tanque tanque){
        this.tanque = tanque;
    }

    public Tanque getTanque(){
        return this.tanque;
    }
    public boolean cheekTanque(){
        return estado;
    }

}

