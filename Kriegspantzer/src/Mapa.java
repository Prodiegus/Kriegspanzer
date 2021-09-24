import java.io.Serializable;
import java.util.ArrayList;

public class Mapa implements Serializable{
    private int mapId;
    public enum Area{AIRE, SOLIDO, TANQUE}
    private Area[][] mapeo =new Area[733][465];
    private ArrayList<int[]> campos = new ArrayList<int[]>();

    //la id del mapa se define al crear el objeto
    Mapa(int mapId){
        this.mapId = mapId;
    }
    
    //rellena el mapa con aire
    public void fillAire() {
        for(int i=0; i<733;i++){
            for(int j=0; j<465;j++){
                mapeo[i][j] = Area.AIRE;
            }
        }
    }
    /**@return mapeo[x][y] == Area.SOLIDO si la coordenada actual es de tipo sólido*/
    public boolean comprobarCoordenadaSolido(int x, int y) {
        return mapeo[x][y] == Area.SOLIDO;
    }
    /**@return return mapeo[x][y] == Area.TANQUE comprueba si la coordenada actual es de tipo tanque*/
    public boolean comprobarCoordenadaTanque(int x, int y) {
        return mapeo[x][y] == Area.TANQUE;
    }
    /**@return mapeo[x][y] == Area.AIRE si la coordenada actual es de tipo aire*/
    public boolean comprobarCoordenadaAire(int x, int y) {
        return mapeo[x][y] == Area.AIRE;
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
                System.out.println(mapeo[i][j]);
            }
        }
        
    }
}
