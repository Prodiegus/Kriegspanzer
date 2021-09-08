import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class IniciarJuegoViewController implements Initializable {
    
    @FXML private ComboBox<String> cJugador1;
    @FXML private ComboBox<String> cJugador2;
    @FXML private AnchorPane mapaPanel;
    @FXML private ProgressBar barraJ1=new ProgressBar();//@FXML private ProgressIndicator indJ1 = new ProgressIndicator(0.5);
    @FXML private ProgressBar barraJ2;
    @FXML private TextField nJugador1;
    @FXML private TextField nJugador2;
    @FXML private Spinner<Integer> angJ1 = new Spinner<Integer>();
    @FXML private Spinner<Integer> angJ2 = new Spinner<Integer>();
    @FXML private Spinner<Integer> velJ1 = new Spinner<Integer>();
    @FXML private Spinner<Integer> velJ2 = new Spinner<Integer>();
    @FXML private Spinner<String> dirJ1 = new Spinner<String>();
    @FXML private Spinner<String> dirJ2 = new Spinner<String>();

    //label de testeo
    @FXML private Label mouseLb;
    private int map;
    //prueba
    int turno=1;
    Jugador player1;
    Jugador player2;

    @FXML
    private void handlePlay(ActionEvent event) {
        
        //antes de cargar el juego necesitamos capturar algunos datos
        int[] pos1 = {75,194};
        int[] pos2 = {600,257};
        Jugador jugador1 = new Jugador(nJugador1.getText().trim());
        Jugador jugador2 = new Jugador(nJugador2.getText().trim());
        Tanque tanque1 = new Tanque(cJugador1.getValue(), pos1);
        Tanque tanque2 = new Tanque(cJugador2.getValue(), pos2);

        jugador1.setTanque(tanque1);
        jugador2.setTanque(tanque2);
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        jugadores.add(jugador1);
        jugadores.add(jugador2);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("JuegoView.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            JuegoController controller = loader.getController();
            
            controller.setMap(map, event);
            controller.setJugadores(jugadores);
            controller.addViews();
            controller.posTank();

            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(true);
            stage.setTitle("Kiegspanzer Game");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
            stage.setScene(scene);
            stage.show();
            close(event);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: 006\nno se a podido cargar el juego");
        }
    }
    
    @FXML
    private void handleSubirNom(ActionEvent event){
        String nom = nJugador1.getText(); player1= new Jugador(nom);
        nom = nJugador2.getText();player2 = new Jugador(nom);
        System.out.println( player1.getName() );System.out.println( player2.getName() ); //prueba de que se crean
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


            controller.setMap(map+1);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
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
        this.map = index.nextInt(1);
        this.map = 0;
        //ese valor dentro del nextint es la cantidad de mapas creados en existencia
        mapaPanel.getStylesheets().clear();
        mapaPanel.getStylesheets().add("Estilos.css");
        mapaPanel.getStyleClass().add("map"+(map+1));
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        barraJ1.setStyle("-fx-accent:#5faf5f");
        barraJ2.setStyle("-fx-accent:#5faf5f");
        barraJ1.setProgress(0.8);barraJ2.setProgress(0.3);
        ObservableList<String> direcciones = FXCollections.observableArrayList("Izquierda","Derecha");
        
        SpinnerValueFactory<Integer> cajaSpinner1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,90,60); //(min,max,ejemplo)
        SpinnerValueFactory<Integer> cajaSpinner2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,90,60); //(min,max,ejemplo)
        SpinnerValueFactory<Integer> cajaSpinner3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000,50); //(min,max,ejemplo)
        SpinnerValueFactory<Integer> cajaSpinner4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000,50); //(min,max,ejemplo)
        SpinnerValueFactory<String>  cajaSpinner5 = new SpinnerValueFactory.ListSpinnerValueFactory<>(direcciones);
        SpinnerValueFactory<String>  cajaSpinner6 = new SpinnerValueFactory.ListSpinnerValueFactory<>(direcciones);
        cajaSpinner5.setValue("Derecha");dirJ1.setValueFactory(cajaSpinner5);
        cajaSpinner5.setValue("Derecha");dirJ2.setValueFactory(cajaSpinner6);
        
        this.angJ1.setValueFactory(cajaSpinner1);angJ1.setEditable(true);
        this.angJ2.setValueFactory(cajaSpinner2);angJ2.setEditable(true);
        velJ1.setValueFactory(cajaSpinner3);velJ1.setEditable(true);
        velJ2.setValueFactory(cajaSpinner4);velJ2.setEditable(true);
        
    }    
    
}
