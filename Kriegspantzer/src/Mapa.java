import java.io.Serializable;
import java.util.ArrayList;

public class Mapa implements Serializable{
    private int mapId;
    public enum Area{AIRE, SOLIDO, TANQUE}
    private Area[][] mapeo =new Area[733][465];//Area[LargoMapa][AnchoMapa]
    private ArrayList<int[]> campos = new ArrayList<int[]>();

    //la id del mapa se define al crear el objeto
    Mapa(int mapId){
        this.mapId = mapId;
    }
    
    //rellena el mapa con aire
    public void fillAire() {
        for(int i=0; i<733;i++){// i<LargoMapa
            for(int j=0; j<465;j++){// j<AnchoMapa
                mapeo[i][j] = Area.AIRE;
            }
        }
    }
    //le pone aire al mapa
    public void fillAire(int x, int y) {
        mapeo[x][y] = Area.AIRE;
    }
    /**@return Si la coordenada actual es de tipo sólido retorna verdadero*/
    public boolean comprobarCoordenadaSolido(int x, int y) {
        return mapeo[x][y] == Area.SOLIDO;
    }
    /**@return Si la coordenada actual es de tipo tanque retorna verdadero*/
    public boolean comprobarCoordenadaTanque(int x, int y) {
        return mapeo[x][y] == Area.TANQUE;
    }
    /**@return Si la coordenada actual es de tipo aire retorna verdadero*/
    public boolean comprobarCoordenadaAire(int x, int y) {
        return mapeo[x][y] == Area.AIRE;
    }

    /* x = pos x disparo
     * y = pos y disparo
     * d = es el daño a realizar al mapa
     * En este metodo se hace un damange cuadrado al mapa
     * Se comprueban cordenadas en caso de que el daño este en un borde fuera del mapa*/
    public void destruir(int x, int y, int d) {
        for (int i = x-d/2; i <= d/2+x; i++) {
           for (int j = y-d/2; j <= d/2+y; j++) {
                if(i>=0 && j>=0 && i<mapeo.length && j<mapeo[i].length){
                    mapeo[i][j] = Area.AIRE;
                }
           }
        }
    }

    //ubica las posciones de los solidos
    public void setAreas(int x, int y) {
        for(int i=y; i<465; i++){
            this.mapeo[x][i] = Area.SOLIDO;
        }
    }

    //Posiciones admisibles para tanques
    public void setCampos(int x, int y) {
        int[] campo =  {x,y};
        campos.add(campo);
    }
    //le da el valor de TANQUE a la posicion entregada
    public void addTank(int x, int y){
        for(int i=x; i<x+20;i++){
            for(int j=y; j<y+10;j++){
                mapeo[i][j] = Area.TANQUE;
            }
        }
    }

    //le da el valor de Aire a la posicion entregada
    public void removeTank(int x, int y){
        for(int i=x; i<x+20;i++){
            for(int j=y; j<y+10;j++){
                mapeo[i][j] = Area.AIRE;
            }
        }
    }
    /**@return mapeo el mapa con los tipos con el que se está trabajando*/
    public Area[][] getMapeo(){
        return mapeo;
    }
    /**@return campos entrega el array con los distintos tipos de campos*/
    public ArrayList<int[]> getCampos(){
        return campos;
    }

    /**@return mapId */
    public int getId() {
        return mapId;
    }

    //printea los datos del mapa por pantalla
    public void verMapa(){
        for(int i=0; i<733;i++){
            for(int j=0; j<465;j++){
                System.out.print(mapeo[i][j]);
            }
        }
        
    }
     
}
