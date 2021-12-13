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
    @FXML private CheckBox checkGravedad;
    @FXML private CheckBox checkViento;
    String[] colors;
    
    //definiendo los default y las variables a usar
    int panAncho = 800;
    int panLargo = 800;
    int mun60 = 10;
    int munPe = 10;
    int mun105 = 10;
    int cantJ = 2;
    int wind;
    String dir;
    double gravity;
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    
    @FXML
    private void aceptar(ActionEvent event) {
        if(!pAncho.getText().trim().equals(""))
            panAncho = Integer.parseInt(pAncho.getText().trim());
        if(!pLargo.getText().trim().equals(""))
            panLargo = Integer.parseInt(pLargo.getText().trim());
        if(!balas60.getText().trim().equals(""))
            mun60 = Integer.parseInt(balas60.getText().trim());
        if(!balasPe.getText().trim().equals(""))
            munPe = Integer.parseInt(balasPe.getText().trim());
        if(!balas105.getText().trim().equals(""))
            mun105 = Integer.parseInt(balas105.getText().trim());
        if(!cantJug.getText().trim().equals(""))
            cantJ = Integer.parseInt(cantJug.getText().trim());

        if( (panAncho  <=1600 ) && (panLargo <=1600 ) && (panAncho >= 800) && (panLargo >= 800 ) && (cantJ>=2 && cantJ<=6) ){
            if ( (mun60>= 0) && (mun60 <= 30) && (mun105>= 0) && (mun105 <= 30) && (munPe>= 0) && (munPe <= 100) ){
                try {
                    FXMLLoader loader =new FXMLLoader(getClass().getResource("IniciarJuegoView.fxml"));

                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();

                    IniciarJuegoViewController controller = loader.getController();
                
                    
                    if(checkGravedad.isSelected()){//si acepta los efectos de entorno se setean los cambios
                        gravity=Double.parseDouble(gravedad.getText().trim());
                        controller.setGravity(gravity);
                    }
                    if(checkViento.isSelected()){
                        wind=Integer.parseInt(viento.getText().trim());
                        if (wind>=1 && wind<=10){
                            controller.setWind(wind);   
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Rango de viento mal");
                        }
                    }
                    
                    controller.setBoxes(colors);
                    controller.setAnchoAlto(panAncho, panLargo);
                    controller.setMap();
                    controller.setCantBalasIni(mun60,munPe,mun105);
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
    public void guardaColores(String[] colores){
        this.colors=colores;
    }
    //closer
    @FXML private void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
