public class Jugador{
    private String nombre;
    private boolean estado;
    private Tanque tanque;
     
    public Jugador(String nombre){
        this.nombre = nombre;
    }
    
    public boolean Lanzamiento(double velocidad, double angulo, int sentido){
        if ( velocidad>=0 && (angulo>=0 && angulo<=360) ){
            //tanque.Disparo(velocidad, angulo,sentido);
            System.out.println("funciona tanque de "+this.nombre+" :)");
        }
        return false;
    }
    
    public boolean cheekTanque(){
        return estado;
    }

    public String getNombre() {
        return nombre;
    }
    
}

