public class Tanque {
    private final String color;
    private int [] posTanque;
    private Bala bala;
    private double vida=100;
    private final double [] damageBala={30,50,40};   //(0)Proyectil 60mm;(1)Proyectil 105mm; (2)Proyectil perforante; 
    String[] balasDisp = { "Proyectil 60mm: 3 balas","Proyectil 105mm: 3 balas", "Proyectil Perforador: 10 balas"};
    
        
    public Tanque (String color, int[] posTanque, Bala bala){
        this.color = color;
        this.posTanque = posTanque;
        this.bala=bala;
    }

    public boolean disparo(double velocidad, double angulo, Mapa mapa,  double gravedad, int viento){
        /*
            Setea la posicion de la bala, antiguamente era (x,0)
            Luego calcular el lanzamiento con la bala del tanque
        */
        this.bala.setPosBala(posTanque);
        return (bala.calcularLanzamiento(bala, velocidad, angulo, mapa, gravedad, viento)==1);
    }
    

    public int[] getPos(){
        return posTanque;
    }
    public String getColor() {
        return color;
    }
    public void setPos(int x, int y){
        int [] pos = {x, y};
        posTanque = pos;
    }
    public void setBala(Bala bala){
        this.bala=bala;
    }
    public Bala getBala() {
        return bala;
    }
    public void setVida(double vida) {
        this.vida = vida;
    }
    public double getVida() {
        return vida;
    }
    public double[] getDamageBala() {
        return damageBala;
    }
    public void setBalasDisp(String nombre, int tipBala) {
        this.balasDisp[tipBala] = nombre;
    }
    public void actMunicionesString(int[] balas){
        this.balasDisp[0]="Proyectil 60mm: "+balas[0]+" balas";
        this.balasDisp[1]="Proyectil 105mm: "+balas[1]+" balas";
        this.balasDisp[2]="Proyectil Perforador: "+balas[2]+" balas";
    }
    public String[] getBalasDisp() {
        return balasDisp;
    }
   
}

