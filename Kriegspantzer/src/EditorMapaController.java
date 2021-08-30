import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class EditorMapaController implements Initializable{
    @FXML private AnchorPane mapaPanel;
    public void setMap(int map){
        mapaPanel.getStylesheets().clear();
        mapaPanel.getStylesheets().add("Estilos.css");
        mapaPanel.getStyleClass().add("map"+map);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
}
