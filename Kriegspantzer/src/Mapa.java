public class Mapa {
    private String[][] mapeo =new String[733][465];

    public void setMapeado(int x, int y) {
        mapeo[x][y] = "campo";
        for(int i=y; i<465; i++){
            mapeo[x][i] = "solido";
        }
        for(int i=y; i>=0; i--){
            mapeo[x][i] = "aire";
        }
    }
}
