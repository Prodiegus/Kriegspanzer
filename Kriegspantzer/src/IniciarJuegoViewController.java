import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class IniciarJuegoViewController implements Initializable {
    
    @FXML private ComboBox<String> cJugador1;
    @FXML private ComboBox<String> cJugador2;
    @FXML private AnchorPane mapaPanel;

    //label de testeo
    @FXML private Label mouseLb;
    
    private int map;

    @FXML
    private void handlePlay(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Has precionado para jugar :c\nLamentablemente esta funcionalidad aun no esta incorporada");
    }

    @FXML
    private void handleEdMap(ActionEvent event){
        //la instruccion esta dentro de un try catch debido a que se podrian presentar errores en la ejecucion 
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditorMapa.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            EditorMapaController controller = loader.getController();


            controller.setMap(map);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Kriegspanzer Map Editor");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            //en caso de que algo salga mal mostraremos el siguiente mensaje
            JOptionPane.showMessageDialog(null, "Error 001:\nNo ha sido posible cargar el editor\n"+e.getCause());
        }
        
    }
    //metodo de testeo
    @FXML
    private void handleMouseMove(MouseEvent event){
        //mapaPanel.autosize();
        double alto = mapaPanel.getHeight();
        double ancho = mapaPanel.getWidth();
        double altoI = mapaPanel.getPrefHeight();
        double anchoI = mapaPanel.getPrefWidth();
        double altoScale = ancho/anchoI;
        double anchoScale = alto/altoI;
        mouseLb.setText("Mouse pos: ("+(int)(event.getX()/altoScale)+"<-->"+(int)(event.getY()/anchoScale)+
                        ")\nPanel size: "+ancho+"X"+alto+
                        "\nPanel Scale: "+altoScale+"X"+anchoScale);
    }
     //closer
     @FXML private void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
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
        this.map = index.nextInt(1)+1;
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
