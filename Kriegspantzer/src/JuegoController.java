import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class JuegoController implements Initializable{
    @FXML private AnchorPane mapaPanel;
    @FXML private Label turnoPanel;
    @FXML private ArrayList<ImageView> tanks = new ArrayList<ImageView>();
    private Mapa mapa;
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();

    @FXML public void scale(KeyEvent event){
        if(event.getCode().equals(KeyCode.R)){
            posTank(1);
        }

    }
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
        turnoPanel.setText("Turno: "+jugadores.get(0).getName());
    }
    public void addViews(){
        for (int i = 0; i<jugadores.size(); i++) {
            tanks.add(new ImageView(new Image(getClass().getResourceAsStream("img/TanqueRojo"))));
        }
    }
    public void posTank(){
        double alto = 465;
        double ancho = 733;
        double altoI = mapaPanel.getPrefHeight();
        double anchoI = mapaPanel.getPrefWidth();
        double altoScale = ancho/anchoI;
        double anchoScale = alto/altoI;
        for (int i = 0; i<jugadores.size(); i++) {
            tanks.get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale);
            tanks.get(i).setY(jugadores.get(i).getTanque().getPos()[1]*anchoScale);
            mapaPanel.getChildren().add(tanks.get(i));
        }

    }
    public void posTank(int a){
        double alto = mapaPanel.getHeight();
        double ancho = mapaPanel.getWidth();
        double altoI = mapaPanel.getPrefHeight();
        double anchoI = mapaPanel.getPrefWidth();
        double altoScale = ancho/anchoI;
        double anchoScale = alto/altoI;
        for (int i = 0; i<jugadores.size(); i++) {
            tanks.get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale);
            tanks.get(i).setY(jugadores.get(i).getTanque().getPos()[1]*anchoScale);
            
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
