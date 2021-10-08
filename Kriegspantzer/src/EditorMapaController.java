import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EditorMapaController implements Initializable{

    @FXML private AnchorPane mapaPanel;
    @FXML private Canvas board;
    @FXML private Label mouseLb;  
    @FXML private Label mNum;  
    @FXML private Label tEd;

    private int map;
    private boolean editarCampos = false;

    private Mapa mapa;

    @FXML
    private void handleCampos(ActionEvent event){
        tEd.setText("Campos");
        this.editarCampos = true;
    }
    @FXML
    private void handleAreas(ActionEvent event){
        tEd.setText("Areas");
        this.editarCampos = false;
    }

    @FXML
    private void handleMouseMove(MouseEvent event){
        //mapaPanel.autosize();
        double alto = mapaPanel.getHeight();
        double ancho = mapaPanel.getWidth();
        double altoI = mapaPanel.getPrefHeight();
        double anchoI = mapaPanel.getPrefWidth();
        double altoScale = ancho/anchoI;
        double anchoScale = alto/altoI;
        int mouseX = (int)(event.getX()/altoScale);
        int mouseY = (int)(event.getY()/anchoScale);
        mouseLb.setText("Mouse pos: ("+mouseX+"<-->"+mouseY+
                        ")\nPanel size: "+ancho+"X"+alto+
                        "\nPanel Scale: "+altoScale+"X"+anchoScale);
        if(editarCampos){
            mapa.setCampos(mouseX, mouseY);
        }else{
            GraphicsContext gc = board.getGraphicsContext2D();
            gc.setStroke(Color.AQUAMARINE);
            gc.setFill(Color.valueOf("#008080"));
            gc.fillRect(mouseX, mouseY, 1, 1000);
            mapa.setAreas(mouseX, mouseY);
        }
    }
    public void iniciarMapa() {
        mapa.fillAire();
    }
    @FXML
    private void handleSerializar(ActionEvent event){
        Serializador sb = new Serializador();
        //mapa.verMapa();
        try {
            sb.ingresarABD(this.mapa);
            close(event);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: 003\nSerializado fallido\n"+e.getCause());
        }
        


    }

    public void setMap(int map){
        mapaPanel.getStylesheets().clear();
        mapaPanel.getStylesheets().add("Estilos.css");
        mapaPanel.getStyleClass().add("map"+(map));
        mapa = new Mapa(map);
        mNum.setText(Integer.toString(map));
        this.map = map;
        
    }
    //closer
    @FXML 
    private void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    } 
    
} 