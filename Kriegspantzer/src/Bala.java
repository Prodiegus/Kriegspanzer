public class Bala{
    private int [] posBala;
    private boolean estado_bala;
    private double velocidad;
    private double angulo;
    public Bala(int [] posBala){
        this.posBala=posBala;
    }
    public int calcularLanzamiento(Bala bala,double velocidad, double angulo){
        double posX=posBala[0];
        double tFinal;
        double posY=0;
        if (angulo>90){
            tFinal=(-posX)/(velocidad*Math.cos(Math.toRadians(180-angulo)));
            posY=posBala[1]-(velocidad*Math.sin(Math.toRadians(180-angulo))*tFinal)-( 0.5*9.81*(tFinal)*(tFinal)) ;
            return (posY<(433-300))?1:0;//si la altura pasa del limite retorna 0
        }
        else{
            tFinal=(733-posX)/(velocidad*Math.cos(Math.toRadians(angulo)));
            posY=posBala[1]+(velocidad*Math.sin(Math.toRadians(angulo))*tFinal)-( 0.5*9.81*(tFinal)*(tFinal)) ;
            return (posY<(433-265)? 1:0);//si la altura pasa del limite retorna 0
        }
    }
    
    public void setPosBala(int[] posBala) {
        this.posBala = posBala;
    }

    public int[] getPosBala() {
        return posBala;
    }
    
    
}


