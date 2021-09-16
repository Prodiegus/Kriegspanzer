public class Jugador{
    private String nombre;
    private boolean estado;
    private Tanque tanque;
     
    public Jugador(String nombre){
        this.nombre = nombre;
        this.estado = true;
    }
    
    public boolean Lanzamiento(double velocidad, double angulo){
        if ( velocidad>=0 && (angulo>=0 && angulo<=360) ){
            return(tanque.disparo(velocidad, angulo));
            //System.out.println("funciona tanque de "+this.nombre+" :)");
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

