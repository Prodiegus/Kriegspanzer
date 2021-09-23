import java.io.Serializable;
import java.util.ArrayList;

public class Mapa implements Serializable{
    private int mapId;
    public enum Area{AIRE, SOLIDO, TANQUE}
    private Area[][] mapeo =new Area[733][465];
    private ArrayList<int[]> campos = new ArrayList<int[]>();
    Mapa(int mapId){
        this.mapId = mapId;
    }

    public void fillAire() {
        for(int i=0; i<733;i++){
            for(int j=0; j<465;j++){
                mapeo[i][j] = Area.AIRE;
            }
        }
    }
    //comprueba si la coordenada actual es de tipo sólido
    public boolean comprobarCoordenadaSolido(int x, int y) {
        return mapeo[x][y] == Area.SOLIDO;
    }
    //comprueba si la coordenada actual es de tipo tanque
    public boolean comprobarCoordenadaTanque(int x, int y) {
        return mapeo[x][y] == Area.TANQUE;
    }
    //comprueba si la coordenada actual es de tipo aire
    public boolean comprobarCoordenadaAire(int x, int y) {
        return mapeo[x][y] == Area.AIRE;
    }

    public void setAreas(int x, int y) {
        for(int i=y; i<465; i++){
            this.mapeo[x][i] = Area.SOLIDO;
        }
    }
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
    //entrega el mapa con los tipos con el que se está trabajando
    public Area[][] getMapeo(){
        return mapeo;
    }
    //entrega el array con los distintos tipos de campos
    public ArrayList<int[]> getCampos(){
        return campos;
    }
    public int getId() {
        return mapId;
    }
    public void verMapa(){
        for(int i=0; i<733;i++){
            for(int j=0; j<465;j++){
                System.out.println(mapeo[i][j]);
            }
        }
        
    }
}
