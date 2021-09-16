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
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;


public class JuegoController implements Initializable {
    @FXML private AnchorPane mapaPanel;
    @FXML private Label turnoPanel;
    @FXML private ArrayList<ImageView> balasImagen = new ArrayList<ImageView>();
    @FXML private ArrayList<ImageView> tanks = new ArrayList<ImageView>();
    @FXML private Spinner<Integer> ang = new Spinner<Integer>();
    @FXML private Spinner<Integer> vel = new Spinner<Integer>();
    @FXML private Spinner<String> dir = new Spinner<String>();
    
    
    int turno=1;
    private Mapa mapa;
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    TranslateTransition mover=new TranslateTransition();
    RotateTransition rotar=new RotateTransition();
    
    @FXML public void scale(KeyEvent event){
        if(event.getCode().equals(KeyCode.R)){
            posTank();
        }

    }
    //closer
    @FXML private void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
<<<<<<< Updated upstream
    public void setMap(Mapa mapa){
=======
    @FXML private void pressShoot(ActionEvent event) {
        if (turno==1){ //turno jugador 1
            turnoPanel.setText("Turno: "+jugadores.get(0).getName());
            //this.jugadores.get(0).Lanzamiento(vel.getValue(), ang.getValue());
            System.out.println("se recibio "+jugadores.get(0).getName()+": vel="+vel.getValue()+", ang="+ang.getValue());
            turno++;
        }
        else{   //turno jugador 2
            turnoPanel.setText("Turno: "+jugadores.get(1).getName());
            //this.jugadores.get(1).Lanzamiento(vel.getValue(), ang.getValue());
            System.out.println("se recibio "+jugadores.get(1).getName()+": vel="+vel.getValue()+", ang="+ang.getValue());
            turno--;
        }
        /*
        for (ImageView bala : balasImagen) {
            bala.setVisible(true);
        }
        mover.setNode(balasImagen.get(0)); 
        mover.setDuration(Duration.millis(1500)); //cuanto se demora en trasladarse
        mover.setByX(200); //cuanto suma en coordenadas x|
        mover.setByY(-200);//cuanto suma en coordenadas y

        mover.play();
        mover.stop();
        */
      
        //balasImagen.get(0).setX((x)*altoScale);

      

        /*for (int i=0;i<200;i=i+20){
            try {
                
                TimeUnit.SECONDS.sleep(1);
            }
            catch (Exception e) {
                    System.out.println("Oops! Something went wrong!");
            }
            balasImagen.get(0).setX((jugadores.get(0).getTanque().getBala().getPosBala()[0]+i)*altoScale);


        }*/
            

        
    }
    public void setMap(int map, ActionEvent event){
>>>>>>> Stashed changes
        mapaPanel.getStylesheets().clear();
        mapaPanel.getStylesheets().add("Estilos.css");
        mapaPanel.getStyleClass().add("map"+(mapa.getId()+1));
        this.mapa = mapa;
    }
    
    public void setJugadores(ArrayList<Jugador> jugadores){
        this.jugadores = jugadores;
        turnoPanel.setText("Turno: "+jugadores.get(0).getName());
    }
    public void addViews(){
        for (int i = 0; i<jugadores.size(); i++) {
            tanks.add(new ImageView(
                new Image(getClass()
                .getResourceAsStream("img/Tanque_"+jugadores.get(i).getTanque().getColor()+".png"))));
            balasImagen.add(new ImageView(new Image(getClass().getResourceAsStream("img/bala.png"))));
        }
    }

    public void posTank(ArrayList<int[]> campos){
        double ancho = 733;
        double anchoI = mapaPanel.getPrefWidth();
        double altoScale = ancho/anchoI;
        //ArrayList<int[]> campos = mapa.getCampos();
        for (int i = 0; i<jugadores.size(); i++) {
            Double x = jugadores.get(i).getTanque().getPos()[0]*altoScale;
            tanks.get(i).setX(x);
            for (int[] campo : campos) {
                if(campo[0]==x){
                    tanks.get(i).setY(campo[1]);
                    jugadores.get(i).getTanque().setPos((int)Math.round(x), campo[1]);
                    mapa.addTank((int)Math.round(x), campo[1]);
                }
            }

            mapaPanel.getChildren().add(tanks.get(i));
        }

    }
    public void posTank(){
        double alto = mapaPanel.getHeight();
        double ancho = mapaPanel.getWidth();
        double altoI = mapaPanel.getPrefHeight();
        double anchoI = mapaPanel.getPrefWidth();
        double altoScale = ancho/anchoI;
        double anchoScale = alto/altoI;
        for (int i = 0; i<jugadores.size(); i++) {
            tanks.get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale);
            tanks.get(i).setY(jugadores.get(i).getTanque().getPos()[1]*anchoScale);
            balasImagen.get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale);
            balasImagen.get(i).setY(jugadores.get(i).getTanque().getPos()[1]*anchoScale);
            
        }

    }
    

    public void posBala(){
        double alto = 465;
        double ancho = 733;
        double altoI = mapaPanel.getPrefHeight();
        double anchoI = mapaPanel.getPrefWidth();
        double altoScale = ancho/anchoI;
        double anchoScale = alto/altoI;
        for (int i=0; i<jugadores.size();i++){
            if (i==1){
                balasImagen.get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale);
                balasImagen.get(i).setY(jugadores.get(i).getTanque().getPos()[1]*anchoScale);
                balasImagen.get(i).setRotate(180);

            }
            else{
            balasImagen.get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale);
            balasImagen.get(i).setY(jugadores.get(i).getTanque().getPos()[1]*anchoScale);
            }
            balasImagen.get(i).setVisible(false);
            mapaPanel.getChildren().add(balasImagen.get(i));
            
        
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
