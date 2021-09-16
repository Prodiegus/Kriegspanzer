public class Bala implements Monitor {
    private int [] posBala;
    private boolean estado_bala;
    private double velocidad;
    private double angulo;
    public Bala(int [] posBala){
        this.posBala=posBala;
    }
    public int calcularLanzamiento(Bala bala,double velocidad, double angulo){
        double posX=posBala[0];double tiempo=0;double tFinal;double posY=0;
        if (angulo>90){
            tFinal=(-posX)/(velocidad*Math.cos(Math.toRadians(180-angulo)));
            posY=posBala[1]-(velocidad*Math.sin(Math.toRadians(180-angulo))*tFinal)-( 0.5*9.81*(tFinal)*(tFinal)) ;
            //System.out.println("tiempo en 0:"+tFinal);System.out.println("altura en 0:"+posY);
            //System.out.println(posY>265 ? "la bala sale del rango de x con posicion:"+posY: "la bala está dentro del rango con posicion:"+posY);
            return (posY<(433-300))?1:0;//si la altura pasa del limite retorna 0
        }
        else{
            tFinal=(733-posX)/(velocidad*Math.cos(Math.toRadians(angulo)));
            posY=posBala[1]+(velocidad*Math.sin(Math.toRadians(angulo))*tFinal)-( 0.5*9.81*(tFinal)*(tFinal)) ;
            //System.out.println("tiempo en 733:"+tFinal);System.out.println("altura en 733:"+posY);
            //System.out.println(posY>265 ? "la bala sale del rango de x con posicion:"+posY: "la bala está dentro del rango con posicion:"+posY);
            return (posY<(433-265)? 1:0);//si la altura pasa del limite retorna 0
        }
        /*
        while(posX<733 && posY>0){
            posX=posBala[0]+velocidad*Math.cos(Math.toRadians(angulo))*tiempo;
            posY=posBala[1]+(velocidad*Math.sin(Math.toRadians(angulo))*tiempo)-( 0.5*9.81*(tiempo)*(tiempo)) ;
            System.out.println("posX:"+posX+", posY:"+posY);
            tiempo+=0.1;
        }*/
    }
    
    public void setPosBala(int[] posBala) {
        this.posBala = posBala;
    }

    public int[] getPosBala() {
        return posBala;
    }
    
    
}


