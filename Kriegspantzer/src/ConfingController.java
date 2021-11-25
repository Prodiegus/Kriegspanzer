import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ConfingController implements Initializable {
    @FXML private TextField pAncho;
    @FXML private TextField pLargo;
    @FXML private TextField balas60;
    @FXML private TextField balasPe;
    @FXML private TextField balas105;
    @FXML private TextField cantJug;
    @FXML private TextField gravedad;
    @FXML private TextField viento;
    @FXML private CheckBox checkEfectos;
    @FXML private ComboBox<String> direccion;
    
    int panAncho;
    int panLargo;
    int mun60;
    int munPe;
    int mun105;
    int cantJ;
    int wind;
    String dir;
    double gravity;
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    String[] direc={"Izquierda","Derecha"};
    
    @FXML
    private void aceptar(ActionEvent event) {
        panAncho = Integer.parseInt(pAncho.getText().trim());
        panLargo = Integer.parseInt(pLargo.getText().trim());
        mun60 = Integer.parseInt(balas60.getText().trim());
        munPe = Integer.parseInt(balasPe.getText().trim());
        mun105 = Integer.parseInt(balas105.getText().trim());
        cantJ = Integer.parseInt(cantJug.getText().trim());
        dir = direccion.getValue();
        if( (panAncho  <=1600 ) && (panLargo <=1600 ) && (cantJ>=2 && cantJ<=6) ){
            if ( (mun60>= 0) && (mun60 <= 30) && (mun105>= 0) && (mun105 <= 30) && (munPe>= 0) && (munPe <= 100) ){
                try {
                    FXMLLoader loader =new FXMLLoader(getClass().getResource("IniciarJuegoView.fxml"));

                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();

                    IniciarJuegoViewController controller = loader.getController();
                    
                    String[] colors = {"Azul", "Verde", "Amarillo", "Rojo", "Morado", "Naranja", "Negro"};
                    
                    if(checkEfectos.isSelected()){//si acepta los efectos de entorno se setean los cambios
                        wind=Integer.parseInt(viento.getText().trim());
                        gravity=Double.parseDouble(gravedad.getText().trim());
                        if (wind>=1 && wind<=10){
                            controller.setGravity(gravity);
                            if("Izquierda" == dir){ //si el viento que se eligió va a la izquierda nuestra formula no varía
                                controller.setWind(wind,1);
                            }
                            else if ("Derecha" == dir){//si se elige derecha nuestra formula si varía
                                controller.setWind(wind,-1);
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "Datos inválidos, seleccione una dirección");
                            }
                        }
                    }
                    controller.setBoxes(colors);
                    controller.setAnchoAlto(panAncho, panLargo); //falta ver donde validar
                    controller.setMap();
                    controller.setCantBalasIni(mun60,munPe,mun105); //falta ver donde validar
                    controller.setCantJug(cantJ);
                    controller.recuperaJugadores(jugadores);
                    stage.setResizable(true);
                    stage.setTitle("Kriegspanzer Game");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
                    stage.setScene(scene);
                    stage.show();
                    close(event);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error: 018\nNo se a podido volver a inicio");
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Datos inválidos, cantidad municiones.");
            }
        }
        else{
                JOptionPane.showMessageDialog(null, "Datos inválidos, tamaño panel/cantidad jugadores");
            }
    }
    
    public void guardaJugadores(ArrayList<Jugador> jug){
        this.jugadores=jug;
    }
    //closer
    @FXML private void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.direccion.getItems().removeAll(this.direccion.getItems());
        this.direccion.getItems().addAll(direc);
    }    
    
}
