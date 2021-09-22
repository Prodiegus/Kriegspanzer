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

    public boolean comprobarCoordenadaSolido(int x, int y) {
        return mapeo[x][y] == Area.SOLIDO;
    }

    public boolean comprobarCoordenadaTanque(int x, int y) {
        return mapeo[x][y] == Area.TANQUE;
    }

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
    public void addTank(int x, int y){
        for(int i=x; i<x+20;i++){
            for(int j=y; j<y+10;j++){
                mapeo[i][j] = Area.TANQUE;
            }
        }
    }
   
    public Area[][] getMapeo(){
        return mapeo;
    }
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
