import java.net.URL;
import java.util.ResourceBundle;


import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class EditorMapaController implements Initializable{

    @FXML private AnchorPane mapaPanel;
    @FXML private Label mouseLb;

    private int map = 0;

    private Mapa mapa = new Mapa(map);

    @FXML
    private void handleMouseMove(MouseEvent event){
        //mapaPanel.autosize();
        double alto = mapaPanel.getHeight();
        double ancho = mapaPanel.getWidth();
        double altoI = mapaPanel.getPrefHeight();
        double anchoI = mapaPanel.getPrefWidth();
        double altoScale = ancho/anchoI;
        double anchoScale = alto/altoI;
        int mouseX = (int)(event.getX()/altoScale);
        int mouseY = (int)(event.getY()/anchoScale);
        mouseLb.setText("Mouse pos: ("+mouseX+"<-->"+mouseY+
                        ")\nPanel size: "+ancho+"X"+alto+
                        "\nPanel Scale: "+altoScale+"X"+anchoScale);
        mapa.setMapeado(mouseX, mouseY);
    }

    public void setMap(int map){
        mapaPanel.getStylesheets().clear();
        mapaPanel.getStylesheets().add("Estilos.css");
        mapaPanel.getStyleClass().add("map"+map);
        this.map = map;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
}
