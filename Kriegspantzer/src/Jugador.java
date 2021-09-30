public class Jugador{
    private String nombre;
    private boolean estado;
    private Tanque tanque;
     
    public Jugador(String nombre){
        this.nombre = nombre;
        this.estado = true;
    }
    //metodo que hará el lanzamiento de la bala en la clase Bala
    public boolean lanzamiento(double velocidad, double angulo, Mapa mapa){
        if ( (velocidad>=0 && velocidad<150) && (angulo>=0 && angulo<=360) ){
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

