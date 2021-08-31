import java.io.Serializable;

public class Mapa implements Serializable{
    private int mapId;
    private String[][] mapeo =new String[733][465];

    Mapa(int mapId){
        this.mapId = mapId;
    }

    public void setMapeado(int x, int y) {
        for(int i=0; i<733;i++){
            for(int j=0; j<465;j++){
                mapeo[i][j] = "Aire";
            }
        }
        for(int i=y; i<465; i++){
            this.mapeo[x][i] = "solido";
        }
        for(int i=y; i>=0; i--){
            this.mapeo[x][i] = "aire";
        }
        this.mapeo[x][y] = "campo";
    }
    public String[][] getMapeo(){
        return mapeo;
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
