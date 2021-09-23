public class Jugador{
    private String nombre;
    private boolean estado;
    private Tanque tanque;
     
    public Jugador(String nombre){
        this.nombre = nombre;
        this.estado = true;
    }
    
    public boolean Lanzamiento(double velocidad, double angulo, Mapa mapa){
        if ( velocidad>=0 && (angulo>=0 && angulo<=360) ){
            //se trabaja con el tanque del jugador
            return(tanque.disparo(velocidad, angulo,mapa));
        }
        return false;
    }


    public void setTanque(Tanque tanque){
        this.tanque = tanque;
    }

    public String getName(){
        return nombre;
    }

    public Tanque getTanque(){
        return this.tanque;
    }
    
    public boolean cheekTanque(){
        return estado;
    }
    
}

