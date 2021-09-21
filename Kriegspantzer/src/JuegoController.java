import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Platform;


public class JuegoController implements Initializable {
    @FXML private AnchorPane mapaPanel;
    @FXML private Label turnoPanel;
    @FXML private ArrayList<ImageView> balasImagen = new ArrayList<ImageView>();
    @FXML private ArrayList<ImageView> tanks = new ArrayList<ImageView>();
    @FXML private Spinner<Integer> ang = new Spinner<Integer>();
    @FXML private Spinner<Integer> vel = new Spinner<Integer>();
    
    int turno=0;
    private Mapa mapa;
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    private boolean flag=true;
    
    
    SpinnerValueFactory<Integer> cajaSpinner1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,180,60); //(min,max,ejemplo)
    SpinnerValueFactory<Integer> cajaSpinner3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,300,68); //(min,max,ejemplo)

    
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
    @FXML private void pressShoot(ActionEvent event) throws InterruptedException {
        /* Al presionar el botón de disparo lo primero que debemos hacer es verificar
           qué jugador es, para así poder hacer los lanzamientos por separados
        */
        double tiempo=0;
        
        if(flag){ //mientras el flag sea verdadero, es decir mientras no exista un ganador, sigue el juego
            if (jugadores.get(turno).Lanzamiento(vel.getValue(), ang.getValue()) && flag){
                //las posiciones que se ingresan de "y" están al revés, entonces debemos modificarlas al momento de pasarlas al moverBala
                int [] posBala=jugadores.get(turno).getTanque().getBala().getPosBala();

                balasImagen.get(turno).setVisible(true);
                //les pasamos las coordenadas verdaderas al método, que representan en el plano XY
                moverBala(posBala[0],(465-posBala[1]),posBala[0],(465-posBala[1]),ang.getValue(),vel.getValue(),tiempo, turno, event);
                turno++;
                turnoPanel.setText("Turno: "+jugadores.get(turno).getName());
            }
            else{
                    JOptionPane.showMessageDialog(null, "Tiro fuera de límite, intente de nuevo.");
            }
        }
        else{
           cargarPantallaFinal(event); 
        }
        
    }
    @FXML
    private void cargarPantallaFinal(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("seguirJugandoView.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            SeguirJugandoViewController controller = loader.getController();

            controller.setGanador(jugadores.get(turno).getName());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Kriegspanzer End Game");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
            stage.setScene(scene);
            stage.show();
            close(event);
            
        } catch (IOException e) {
            //en caso de que algo salga mal mostraremos el siguiente mensaje
            JOptionPane.showMessageDialog(null, "Error 011:\nNo ha sido posible cargar el Fanal del juego\n"+e.getCause());
        }
    }
    private boolean moverBala(int xI,double yI,double x,double y,int angulo,double velocidad,double tiempo,int jug, ActionEvent event)throws InterruptedException {
        Platform.runLater( ()->{
            try{
                TimeUnit.MILLISECONDS.sleep(30);
            }
            catch(InterruptedException el){
                el.printStackTrace();
            }
            /*
                Antes de inicar el proceso de recursión debemos verificar si las coordenadas que nos están entregando
                son correctas, es decir que no sobre pasen los límites laterales, y que hasta el momento no pasen más abajo del cuadro.
                A futuro debemos verificar que llegue al suelo
            
            */
            if ( (x>=0 &&  x<=733) && (y>=0) && mapa.comprobarCoordenadaAire((int)Math.round(x),(int)Math.round(464-y))){
                balasImagen.get(jug).setX(x);
                balasImagen.get(jug).setY(465-y);
                //System.out.println("setea la posicion: ("+x+","+y+") - es ¿aire?: "+ mapa.comprobarCoordenadaAire((int)Math.round(x),(int)Math.round(465-y)));
                try{
                    if(angulo<=90){
                        moverBala(xI,yI,(xI+velocidad*Math.cos(Math.toRadians(angulo))*tiempo),(yI+velocidad*Math.sin(Math.toRadians(angulo))*tiempo-(0.5*9.81*(tiempo*tiempo))),angulo,velocidad,(tiempo+0.1),jug, event);
                    }
                    else{
                        moverBala(xI,yI,(xI+velocidad*Math.cos(Math.toRadians(angulo))*tiempo),(yI+velocidad*Math.sin(Math.toRadians(angulo))*tiempo-(0.5*9.81*(tiempo*tiempo))),angulo,velocidad,(tiempo+0.1),jug, event);
                    }
                    
                }
                catch(InterruptedException e2){
                    e2.printStackTrace();
                }
            }
            else{
                //toca tanque
                if (mapa.comprobarCoordenadaTanque((int)Math.round(x),(int)Math.round(464-y))){
                    this.flag=false;
                    cargarPantallaFinal(event);
                   
                }
                balasImagen.get(jug).setVisible(false);
            }
            
        });
        return false;
    }
    
    public void setMap(Mapa mapa){
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
                    //System.out.println(jugadores.get(i).getTanque().getPos()[0]+","+jugadores.get(i).getTanque().getPos()[1]);
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
        /*
            Se inicializan los spinner con sus respectivos rangos
        */
        ang.setValueFactory(cajaSpinner1);ang.setEditable(true);
        vel.setValueFactory(cajaSpinner3);vel.setEditable(true);
    }
    
}
