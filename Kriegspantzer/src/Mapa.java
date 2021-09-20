import java.io.Serializable;
import java.util.ArrayList;

public class Mapa implements Serializable{
    private int mapId;
    private String[][] mapeo =new String[733][465];
    private ArrayList<int[]> campos = new ArrayList<int[]>();
    Mapa(int mapId){
        this.mapId = mapId;
    }
    public void fillAire() {
        for(int i=0; i<733;i++){
            for(int j=0; j<465;j++){
                mapeo[i][j] = "Aire";
            }
        }
    }
    public void setMapeado(int x, int y) {
        for(int i=y; i<465; i++){
            this.mapeo[x][i] = "solido";
        }
        for(int i=y; i>=0; i--){
            this.mapeo[x][i] = "aire";
        }
        int[] campo =  {x,y};
        campos.add(campo);
        this.mapeo[x][y] = "campo";
    }
    public String[][] getMapeo(){
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
