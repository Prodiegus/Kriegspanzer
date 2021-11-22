
public class Jugador{
    private String nombre;
    private boolean estado;
    private Tanque tanque;
    private boolean IA;
    private int kills;
    private int guardaAng;
    private int guardaVel;
     
    public Jugador(String nombre, boolean IA){
        this.nombre = nombre;
        this.estado = true;
        this.IA=IA;
        this.kills=0;
        this.guardaAng=0;
        this.guardaVel=0;
    }

    public int getKills() {
        return kills;
    }

    public void masKill() {
        this.kills++;
    }
    public void quitarKills(){
        this.kills=0;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    //metodo que hará el lanzamiento de la bala en la clase Bala
    public boolean lanzamiento(double velocidad, double angulo, Mapa mapa, double gravedad, int viento){
        if ( (velocidad>=0 && velocidad<150) && (angulo>=0 && angulo<=180) ){
            //se trabaja con el tanque del jugador
            return(tanque.disparo(velocidad, angulo,mapa, gravedad, viento));
        }
        return false;
    }

    public boolean isIA() {
        return IA;
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
    public void setAng(int angulo){
        this.guardaAng=angulo;
    }
    public void setVel(int velocidad){
        this.guardaVel=velocidad;
    }
    public int getAng(){
        return this.guardaAng;
    }
    public int getVel(){
        return this.guardaVel;
    }
}

