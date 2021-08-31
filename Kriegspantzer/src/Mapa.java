public class Mapa {
    int mapId;
    private String[][] mapeo =new String[733][465];

    Mapa(int mapId){
        this.mapId = mapId;
    }

    public void setMapeado(int x, int y) {
        for(int i=y; i<465; i++){
            mapeo[x][i] = "solido";
        }
        for(int i=y; i>=0; i--){
            mapeo[x][i] = "aire";
        }
        mapeo[x][y] = "campo";
    }
}
