public class Bala{
    private int [] posBala;
    public Bala(int [] posBala){
        this.posBala=posBala;
    }
    public int calcularLanzamiento(Bala bala,double velocidad, double angulo,Mapa mapa){
        int i=0;
        double tiempo=0;
        double posX=posBala[0];
        double tFinal;
        double posY= 465 - posBala[1];
        double pActX=posX;
        double pActY=posY;
        double limIzq=0;
        double limDer=0;
        boolean flagI=true;
        boolean flagD=true;
        
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
        while((pActX<732 && pActX>1)){
            pActX=(posX+velocidad*Math.cos(Math.toRadians(angulo))*tiempo);
            pActY=(posY+velocidad*Math.sin(Math.toRadians(angulo))*tiempo-(0.5*9.81*(tiempo*tiempo)));
            if (pActY<464 && pActX<732 && pActX>0 && pActY>0 && (posX!=pActX && posY!=pActY)){
                if (!mapa.comprobarCoordenadaAire( (int)Math.round(pActX), (int)Math.round(467-pActY) )){
                    //System.out.println("solido en la coordenada: "+(int)Math.round(pActX)+","+(int)Math.round(467-pActY));
                    return 1;
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
            return (posY>(433-limDer)? 0:1);//si la altura pasa del limite retorna 0
        }
        
    }
    
    public void setPosBala(int[] posBala) {
        this.posBala = posBala;
    }

    public int[] getPosBala() {
        return posBala;
    }
    
    
}


