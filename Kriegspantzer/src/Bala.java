public class Bala{
    private int [] posBala;
    private int [] cantBalas={3,3,10};      //(0)Proyectil 60mm;(1)Proyectil 105mm; (2)Proyectil perforante; 
    public Bala(int [] posBala){
        this.posBala=posBala;
    }
    public int calcularLanzamiento(Bala bala,double velocidad, double angulo,Mapa mapa){
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
        /*
            el primer while sirve para recorrer las columnas que están en la esquina y ver en que coordenada empieza la parte sólida 
        */
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
        /*
            El segundo while sirve para hacer una simulación del disparo el cual me dirá si impacta al sólido antes de llegar al límite
        */
        if(angulo == 90){ //ocurren problemas con el angulo 90, por lo que no se requiere hacer una validacion si se sale del cuadro
            return 1;
        }
        while((pActX<732 && pActX>1)){
            pActX=(posX+velocidad*Math.cos(Math.toRadians(angulo))*tiempo);
            pActY=(posY+velocidad*Math.sin(Math.toRadians(angulo))*tiempo-(0.5*9.81*(tiempo*tiempo)));
            if (pActY<464 && pActX<732 && pActX>0 && pActY>0 && (angulo==90 || (posX!=pActX && posY!=pActY))){
                if (!mapa.comprobarCoordenadaAire( (int)Math.round(pActX), (int)Math.round(467-pActY) )){ //mientras el recorrido sea aire no entrará aquí
                    return 1;   //si choca el suelo es un tiro válido
                }
            }
            tiempo=tiempo+0.1;
        }
        /*
            Llegará a esta parte en caso de que no haya tocado el suelo en su trayecto dentro del cuadro,
            sirve como una ayuda auxiliar para saber si es que se pasó de los otros límites
        */
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
    
    public void setPosBala(int[] posBala) {
        this.posBala = posBala;
    }
    public int[] getPosBala() {
        return posBala;
    }
    public void setCantBalas(int posicion) {
        //System.out.println("antes: "+this.cantBalas[0]+","+this.cantBalas[1]+","+this.cantBalas[2]);
        this.cantBalas[posicion] = cantBalas[posicion]-1;
        //System.out.println("nuevas municiones para este: "+this.cantBalas[0]+","+this.cantBalas[1]+","+this.cantBalas[2]);
    }
    public int[] getTipoBalas() {
        return this.cantBalas;
    }
    public boolean verificaBalas(){ //metodo que retorna true si no existen balas
        return (this.cantBalas[0]==0 && this.cantBalas[1]==0 && this.cantBalas[2]==0);
    }
    
    public void actualizaMuniciones(int[] balas){
        this.cantBalas[0]=balas[0];
        this.cantBalas[1]=balas[1];
        this.cantBalas[2]=balas[2];
    }
    
}


    
    



