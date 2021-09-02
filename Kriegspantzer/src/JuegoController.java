import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.RootPaneContainer;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class JuegoController implements Initializable{
    @FXML private AnchorPane mapaPanel;
    @FXML private Label turnoPanel;
    @FXML private ArrayList<ImageView> tanks;
    private Mapa mapa;
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();

    
    //closer
    @FXML private void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    public void setMap(int map, ActionEvent event){
        mapaPanel.getStylesheets().clear();
        mapaPanel.getStylesheets().add("Estilos.css");
        mapaPanel.getStyleClass().add("map"+(map+1));
        Serializador serializador = new Serializador();
        try {
            this.mapa = serializador.cargarDataBase(map);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR: 004\nno se a podido cargar el mapa");
        }
    }
    
    public void setJugadores(ArrayList<Jugador> jugadores){
        this.jugadores = jugadores;
    }
    public void addViews(){
        for (int i = 0; i<jugadores.size(); i++) {
            tanks.add(new ImageView(new Image(getClass().getResourceAsStream("img/TanqueRojo"))));
        }
    }
    public void posTank(){
        for (int i = 0; i<jugadores.size(); i++) {
            tanks.get(i).setX(jugadores.get(i).getTanque().getPos()[0]);
            tanks.get(i).setY(jugadores.get(i).getTanque().getPos()[0]);
            
        }

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
