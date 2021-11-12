import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class IA {
    private int velocidad;
    private int angulo;
    private double gravedad;
    private double rose;
    private int objetivo;
    private int intentos;
    private ArrayList<Jugador> jugadores;

    IA(ArrayList<Jugador> jugadores){
        this.jugadores = jugadores;
        this.gravedad = 9.81;
        this.rose = 1;
        this.intentos=0;
        calcularLanzamiento();
    }
    IA(ArrayList<Jugador> jugadores, double gravedad, double rose){
        this.jugadores = jugadores;
        this.gravedad = gravedad;
        this.rose = rose;
        this.intentos=0;
        calcularLanzamiento();
    }

    public void calcularLanzamiento(){
        Random random = new Random();
        this.objetivo = random.nextInt(jugadores.size()-1);
        //this.objetivo = 1;
        int xm = jugadores.get(objetivo).getTanque().getPos()[0];
        int ym = jugadores.get(objetivo).getTanque().getPos()[1];
        this.angulo = (int)Math.round(Math.toDegrees(Math.atan(4*((double)ym/xm))));
        this.velocidad = (int)Math.round(Math.abs(Math.sqrt(((double)xm*gravedad)/((double)Math.sin((double)2*angulo)))));
        this.intentos++;
        if(intentos>5){
            this.angulo=90;
            this.velocidad=60;
        }
    }
    /*
    public boolean calcularLanzamiento(Bala bala,double velocidad, double angulo,Mapa mapa){
        int i=0;
        double tiempo=0;
        double posX=posBala[0];
        double tFinal;
        double posY= (double)465 - posBala[1];
        double pActX=posX;
        double pActY=posY;
        double limIzq=0;
        double limDer=0;
        boolean flagI=true;
        boolean flagD=true;
        
        //    el primer while sirve para recorrer las columnas que están en la esquina y ver en que coordenada empieza la parte sólida 
        
        while(i<465 && (flagI || flagD)){
            if (mapa.comprobarCoordenadaSolido(2, i)){
                limIzq=i;
                flagI=false;
            }
            if (mapa.comprobarCoordenadaSolido(732, i)){
                limDer=i;
                flagD=false;
            }
            i++;
        }
        
          //  El segundo while sirve para hacer una simulación del disparo el cual me dirá si impacta al sólido antes de llegar al límite
        
        if(angulo == 90){ //ocurren problemas con el angulo 90, por lo que no se requiere hacer una validacion si se sale del cuadro
            return true;
        }
        while((pActX<732 && pActX>1)){
            pActX=(posX+velocidad*Math.cos(Math.toRadians(angulo))*tiempo);
            pActY=(posY+velocidad*Math.sin(Math.toRadians(angulo))*tiempo-(0.5*9.81*(tiempo*tiempo)));
            if (pActY<464 && pActX<732 && pActX>0 && pActY>0 && (angulo==90 || (posX!=pActX && posY!=pActY))){
                if (!mapa.comprobarCoordenadaAire( (int)Math.round(pActX), (int)Math.round(467-pActY) )){ //mientras el recorrido sea aire no entrará aquí
                    return true;   //si choca el suelo es un tiro válido
                }
            }
            tiempo=tiempo+0.1;
        }
       
        //    Llegará a esta parte en caso de que no haya tocado el suelo en su trayecto dentro del cuadro,
        //    sirve como una ayuda auxiliar para saber si es que se pasó de los otros límites
        
        if (angulo>90){
            tFinal=(posX)/(velocidad*Math.cos(Math.toRadians(180-angulo)));
            posY=465-posBala[1]+(velocidad*Math.sin(Math.toRadians(180-angulo))*tFinal)-( 0.5*9.81*(tFinal)*(tFinal));
            return (posY>(465-limIzq))?0:1;//si la altura pasa del limite retorna 0
        }
        else{
            tFinal=(733-posX)/(velocidad*Math.cos(Math.toRadians(angulo)));
            posY=465-posBala[1]+(velocidad*Math.sin(Math.toRadians(angulo))*tFinal)-( 0.5*9.81*(tFinal)*(tFinal)) ;
            return (posY>(465-limDer)? 0:1);//si la altura pasa del limite retorna 0
        }
    }
    */
    public int getVelocidad(){
        Random random = new Random();
        this.velocidad = random.nextInt(40)+40;
        return this.velocidad;
    }

    public int getAngulo() {
        Random random = new Random();
        this.angulo = random.nextInt(100)+30;
        return this.angulo;
    }

    public static void main(String[] args) throws InterruptedException{
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        int valorDado = (int) Math.floor(Math.random()*700);
        int[] pos1={valorDado,300};
        Jugador j1 =  new Jugador("juan",true);
        Bala b1 = new Bala(pos1);
        Tanque t1 =  new Tanque("rojo", pos1,b1);
        t1.setPos(pos1[0], pos1[1]);
        j1.setTanque(t1);
        jugadores.add(j1);
        valorDado = (int) Math.floor(Math.random()*700);
        int[] pos2={valorDado,433};
        Jugador j2 =  new Jugador("pedro",true);
        Bala b2 = new Bala(pos2);
        Tanque t2 =  new Tanque("azul", pos2,b2);
        t2.setPos(pos2[0], pos2[1]);
        j2.setTanque(t2);
        jugadores.add(j2);
        IA ia = new IA(jugadores);
        int[] disparo = jugadores.get(0).getTanque().getBala().getPosBala();
        int tiempo = 0;
        int xi = jugadores.get(0).getTanque().getPos()[0];
        int yi = jugadores.get(0).getTanque().getPos()[1];
        int velocidad = ia.getVelocidad();
        int angulo = ia.getAngulo();
        int[] objetivo = jugadores.get(ia.objetivo).getTanque().getPos();
        System.out.println("Velocidad: "+velocidad+" Angulo: "+angulo);
        TimeUnit.SECONDS.sleep(5 );
        while(disparo[1]>0 && disparo!=objetivo && disparo[0]<800){
            disparo[0] = (int)(xi+velocidad*Math.cos(Math.toRadians(angulo))*tiempo);
            disparo[1] = (int)(yi+velocidad*Math.sin(Math.toRadians(angulo))*tiempo-(0.5*ia.gravedad*(tiempo*tiempo)));
            
            System.out.println("("+disparo[0]+", "+disparo[1]+") to ("+objetivo[0]+", "+objetivo[1]+")");
            
            tiempo++;
        } 
    }
}
