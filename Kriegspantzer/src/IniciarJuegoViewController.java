import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
public class IniciarJuegoViewController implements Initializable {
    
    @FXML private ComboBox<String> cJugador1;
    @FXML private ComboBox<String> cJugador2;
    @FXML private AnchorPane mapaPanel;
    
    private int map;
    @FXML
    private void handlePlay(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Has precionado para jugar :c\nLamentablemente esta funcionalidad aun no esta incorporada");
    }

    public void setBoxes(String[] colors){
        this.cJugador1.getItems().removeAll(this.cJugador1.getItems());
        this.cJugador2.getItems().removeAll(this.cJugador1.getItems());
        this.cJugador1.getItems().addAll(colors);
        this.cJugador2.getItems().addAll(colors);
    }

    // Esta funcion agrega un fondo al anchorPane del mapa
    public void setMap(){
        //se crea un random con la idea de generar un mapa random a futuro
        Random index = new Random();
        this.map = index.nextInt(2)+1;
        //ese valor dentro del nextint es la cantidad de mapas creados en existencia
        mapaPanel.getStylesheets().clear();
        mapaPanel.getStylesheets().add("Estilos.css");
        mapaPanel.getStyleClass().add("map"+map);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
