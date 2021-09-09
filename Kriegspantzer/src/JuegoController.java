import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class JuegoController implements Initializable{
    @FXML private AnchorPane mapaPanel;
    @FXML private Label turnoPanel;
    @FXML private ArrayList<ImageView> tanks = new ArrayList<ImageView>();
    @FXML private Spinner<Integer> ang = new Spinner<Integer>();
    @FXML private Spinner<Integer> vel = new Spinner<Integer>();
    @FXML private Spinner<String> dir = new Spinner<String>();
    
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
    
    @FXML private void dispara(ActionEvent event){
        //falta llamar el metodo de lanzamiento
        /*
        if ( "Izquierda".equals(dir.getValue())){
            this.jugadores.get(0).Lanzamiento(vel.getValue(), ang.getValue(),1);
        }
        else{
            this.jugadores.get(0).Lanzamiento(vel.getValue(), ang.getValue(),2);
        }*/
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
            tanks.add(new ImageView(new Image(getClass().getResourceAsStream("img/Tanque_Rojo.png"))));
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
        ObservableList<String> direcciones = FXCollections.observableArrayList("Izquierda","Derecha");
        SpinnerValueFactory<Integer> cajaSpinner1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,90,60); //(min,max,ejemplo)
        SpinnerValueFactory<Integer> cajaSpinner3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000,50); //(min,max,ejemplo)
        SpinnerValueFactory<String>  cajaSpinner5 = new SpinnerValueFactory.ListSpinnerValueFactory<>(direcciones);
        cajaSpinner5.setValue("Derecha");dir.setValueFactory(cajaSpinner5);
        
        ang.setValueFactory(cajaSpinner1);ang.setEditable(true);
        vel.setValueFactory(cajaSpinner3);vel.setEditable(true);
    }
    
}
