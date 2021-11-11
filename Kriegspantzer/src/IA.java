import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class IA {
    private int velocidad;
    private int angulo;
    private double gravedad;
    private double rose;
    private int objetivo;
    private ArrayList<Jugador> jugadores;

    IA(ArrayList<Jugador> jugadores){
        this.jugadores = jugadores;
        this.gravedad = 9.81;
        this.rose = 1;
        calcularLanzamiento();
    }
    IA(ArrayList<Jugador> jugadores, double gravedad, double rose){
        this.jugadores = jugadores;
        this.gravedad = gravedad;
        this.rose = rose;
        calcularLanzamiento();
    }

    public void calcularLanzamiento(){
        //Random random = new Random();
        //this.objetivo = random.nextInt(jugadores.size()-1);
        this.objetivo = 1;
        int xm = jugadores.get(objetivo).getTanque().getPos()[0];
        int ym = jugadores.get(objetivo).getTanque().getPos()[1];
        this.angulo = (int)Math.round(Math.toDegrees(Math.atan(4*((double)ym/xm))));
        this.velocidad = (int)Math.round(Math.abs(Math.sqrt(((double)xm*gravedad)/((double)Math.sin((double)2*angulo)))));

    }

    public int getVelocidad(){
        return this.velocidad;
    }

    public int getAngulo() {
        return this.angulo;
    }

    public static void main(String[] args) throws InterruptedException{
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        int valorDado = (int) Math.floor(Math.random()*700);
        int[] pos1={valorDado,10};
        Jugador j1 =  new Jugador("juan",true);
        Bala b1 = new Bala(pos1);
        Tanque t1 =  new Tanque("rojo", pos1,b1);
        t1.setPos(pos1[0], pos1[1]);
        j1.setTanque(t1);
        jugadores.add(j1);
        valorDado = (int) Math.floor(Math.random()*700);
        int[] pos2={valorDado,10};
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
        while(disparo!=objetivo){
            disparo[0] = (int)(xi+velocidad*Math.cos(Math.toRadians(angulo))*tiempo);
            disparo[1] = (int)(yi+velocidad*Math.sin(Math.toRadians(angulo))*tiempo-(0.5*ia.gravedad*(tiempo*tiempo)));
            
            System.out.println("("+disparo[0]+", "+disparo[1]+") to ("+objetivo[0]+", "+objetivo[1]+")");
            TimeUnit.SECONDS.sleep(1);
            tiempo++;
        } 
    }
}
