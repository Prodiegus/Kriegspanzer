import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class IniciarJuegoViewController implements Initializable {
    
    @FXML private ComboBox<String> cJugador1;
    @FXML private ComboBox<String> cJugador2;
    @FXML private AnchorPane ventana;
    
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
    public void scale(Stage stage){
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
