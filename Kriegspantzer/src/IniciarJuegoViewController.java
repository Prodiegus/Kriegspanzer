import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ProgressIndicator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
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
    @FXML private Spinner<Integer> angJ1 = new Spinner();
    @FXML private Spinner<Integer> angJ2 = new Spinner();
    @FXML private Spinner<Integer> velJ1 = new Spinner();
    @FXML private Spinner<Integer> velJ2 = new Spinner();
    @FXML private Spinner<String> dirJ1 = new Spinner();
    @FXML private Spinner<String> dirJ2 = new Spinner();

    //label de testeo
    @FXML private Label mouseLb;
    private int map;
    //prueba
    int turno=1;
    Jugador player1,player2;

    @FXML
    private void handlePlay(ActionEvent event) {
        //JOptionPane.showMessageDialog(null, "Has presionado para jugar :c\nLamentablemente esta funcionalidad aun no esta incorporada");
        if (turno==1){
            //System.out.print("la velocidad del jugador 1 es: "+velJ1.getValue()); 
            player1.Lanzamiento(velJ1.getValue(), angJ1.getValue(),1);
            turno--;
        }
        else{
            player2.Lanzamiento(velJ2.getValue(), angJ2.getValue(),1);
            turno++;
        }
        
    }
    
    @FXML
    private void handleSubirNom(ActionEvent event){
        String nom = nJugador1.getText(); player1= new Jugador(nom);
        nom = nJugador2.getText();player2 = new Jugador(nom);
        System.out.println( player1.getNombre() );System.out.println( player2.getNombre() ); //prueba de que se crean
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
        this.map = index.nextInt(1)+1;
        //ese valor dentro del nextint es la cantidad de mapas creados en existencia
        mapaPanel.getStylesheets().clear();
        mapaPanel.getStylesheets().add("Estilos.css");
        mapaPanel.getStyleClass().add("map"+map);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        barraJ1.setStyle("-fx-accent:#5faf5f");
        barraJ2.setStyle("-fx-accent:#5faf5f");
        barraJ1.setProgress(0.8);barraJ2.setProgress(0.3);
        ObservableList<String> direcciones = FXCollections.observableArrayList("Izquierda","Derecha");
        
        SpinnerValueFactory<Integer> caja_spinner1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,90,60); //(min,max,ejemplo)
        SpinnerValueFactory<Integer> caja_spinner2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,90,60); //(min,max,ejemplo)
        SpinnerValueFactory<Integer> caja_spinner3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000,50); //(min,max,ejemplo)
        SpinnerValueFactory<Integer> caja_spinner4 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000,50); //(min,max,ejemplo)
        SpinnerValueFactory<String>  caja_spinner5 = new SpinnerValueFactory.ListSpinnerValueFactory<>(direcciones);
        SpinnerValueFactory<String>  caja_spinner6 = new SpinnerValueFactory.ListSpinnerValueFactory<>(direcciones);
        caja_spinner5.setValue("Derecha");dirJ1.setValueFactory(caja_spinner5);
        caja_spinner5.setValue("Derecha");dirJ2.setValueFactory(caja_spinner6);
        
        this.angJ1.setValueFactory(caja_spinner1);angJ1.setEditable(true);
        this.angJ2.setValueFactory(caja_spinner2);angJ2.setEditable(true);
        velJ1.setValueFactory(caja_spinner3);velJ1.setEditable(true);
        velJ2.setValueFactory(caja_spinner4);velJ2.setEditable(true);
        
    }    
    
}
