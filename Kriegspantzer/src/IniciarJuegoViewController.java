import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
public class IniciarJuegoViewController implements Initializable {
    
    @FXML private ComboBox<String> cJugador;
    @FXML private AnchorPane mapaPanel;
    @FXML private TextField nJugador;
    @FXML private TextField nMap;
    @FXML private CheckBox IA;

    private int map;
    private Mapa mapa;
    private int ancho = 800;
    private int alto = 800;
    private int cantJug=2;
    
    int[] municiones = {3,3,10}; 
    double gravedad=9.81;
    int viento=0;
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    String[] colors = {"Azul", "Verde", "Amarillo", "Rojo", "Morado", "Naranja", "Negro"};//colores disponibles
    
    @FXML
    private boolean handleIngresar(ActionEvent event) {
        //por ahora se generan las posiciones aleatorias de los tanques
        if (jugadores.size()<cantJug){//mientras la cantidad de jugadores ingresados sea menor a la maxima puede ingresar más
            int valorDado = (int) Math.floor((Math.random()*700));
            int[] pos={valorDado,10};
            String color=cJugador.getValue();
            Jugador jActual =  new Jugador(nJugador.getText().trim(),IA.isSelected());
            Bala bActual = new Bala(pos);
            Tanque tActual =  new Tanque(color, pos,bActual);
            jActual.setTanque(tActual);
            jugadores.add(jActual);

            this.cJugador.getItems().removeAll(this.cJugador.getItems());
            this.cJugador.getItems().addAll(setColors(color));
            cJugador.setPromptText("Color Tanque "+(jugadores.size()+1)); 
            nJugador.setText(null);
            nJugador.setPromptText("- Nombre jugador "+(jugadores.size()+1)+" - ");
            IA.setSelected(false);
           
            return true;
        }
        else{
            JOptionPane.showMessageDialog(null, "No puede agregar más jugadores, aprete jugar o reinicie configuraciones.");
            return false;
        }
        
    }
    
    @FXML
    private boolean handlePlay(ActionEvent event) {
        
        /* Antes de inicial el juego necesitamos capturar ciertos datos
         * Primero generamos un algoritmo rapido para encontrar posiciones x
         * Admisibles para los tanques
         * 
         * y luego creamos los objetos de los jugadores y tanques para poder 
         * manipularnos dentro del juego
         * */

        //primero cargamos el mapa para ver los campos
        
        if(jugadores.size() < 2){
            JOptionPane.showMessageDialog(null, "Error: 006\n deben existir mínimo 2 jugadores");
            return false;
        }
        else{
            Serializador serializador = new Serializador();
            try {
                mapa = serializador.cargarDataBase(map);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "ERROR: 004\nno se a podido cargar el mapa");
            }

            //cargamos los campos del mapa para encontrar posicion para los tanques
            ArrayList<int[]> campos = mapa.getCampos();

            //Creamos un Array de solamente el campo x de las coordenadas
            ArrayList<Integer> camposX = new ArrayList<Integer>();
            for (int[] campo : campos) {
                camposX.add(campo[0]);
            }
            //convertimos a Array
            Integer[] posiciones = camposX.stream().toArray(Integer[]::new);
            Arrays.sort(posiciones);
            //int x1 = foundX(posiciones);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("JuegoView.fxml"));

                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();

                JuegoController controller = loader.getController();



                stage.initModality(Modality.WINDOW_MODAL);
                stage.setHeight(alto);
                stage.setWidth(ancho);
                stage.setResizable(true);
                stage.setTitle("Kiegspanzer Game");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
                stage.setScene(scene);
                stage.show();
                close(event);

                controller.setBoardSize(this.ancho*0.915106117, this.alto*0.75732899);//escala de proporcionalidada canvas/ventana
                controller.setScale();
                controller.setMap(mapa);
                controller.ordenTurnos(jugadores);
                controller.setJugadores(jugadores);
                controller.actualizaCantBalas(municiones);
                controller.addViews();
                controller.posTank();
                controller.posBala();
                controller.posBarras();
                controller.setGravedad(gravedad);
                controller.setEventG(event);
                try {
                    IA ia=new IA(jugadores);
                    controller.verIA(event,0,ia);
                } catch (InterruptedException ex) {
                    Logger.getLogger(IniciarJuegoViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error: 006\nno se a podido cargar el juego");
            }
        }
        
        
        return true;
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

            Image img = new Image("img/Cursor32x32.png");
            scene.setCursor(new ImageCursor(img));
            controller.setMap(Integer.parseInt(nMap.getText().trim()));
            controller.iniciarMapa();
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
    @FXML
    private void handleConfings(ActionEvent event){
        //la instruccion esta dentro de un try catch debido a que se podrian presentar errores en la ejecucion 
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Confing.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            ConfingController controller = loader.getController();
            controller.guardaJugadores(jugadores); //para que no se pierdan los jugadores creados
            
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Kriegspanzer Confings");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
            stage.setScene(scene);
            stage.show();
            close(event);
        } catch (IOException e) {
            //en caso de que algo salga mal mostraremos el siguiente mensaje
            JOptionPane.showMessageDialog(null, "Error 081:\nNo ha sido posible cargar las configuraciones\n"+e.getCause());
        }
    }
    public void setCantBalasIni(int balas60, int balasPe, int balas105){
        municiones[0]=balas60;
        municiones[1]=balas105;
        municiones[2]=balasPe;
    }
    //closer
    @FXML private void close(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private int foundX(Integer[] posiciones){
        try{
            Random r = new Random();
            int x1;
            //verificamos que las x sean campos admisibles
            for (x1 = r.nextInt(733/2);true;x1 = r.nextInt(733/2)){//x1 = LargoMapa/2 Asi se consigue la mitad del mapa de distancia
                if(Arrays.binarySearch(posiciones, x1)>0 && Arrays.binarySearch(posiciones, (x1+(733/2)))>0){
                    return x1;  
                }
            }
        }catch(Exception e){
            foundX(posiciones);
        }
        return 200;//en caso de error se toma una distancia cualquiera
    }
    public void setAnchoAlto(int ancho, int alto) {
        this.alto = alto;
        this.ancho = ancho;
    }
    public void setGravity(double gravity){
        this.gravedad=gravity;
    }
    public void setWind(int wind){
        this.viento=wind;
    }
    public void setBoxes(String[] colors){
        this.cJugador.getItems().removeAll(this.cJugador.getItems());
        this.cJugador.getItems().addAll(colors);
    }
    public void recuperaJugadores(ArrayList<Jugador> jugList){
        this.jugadores=jugList;
    }
    public String[] setColors(String color){
        String[] aux = new String[colors.length-1];
        int cont=0;
        for (int i=0;i<colors.length;i++) {
            if (!colors[i].equals(color)) {//si es igual lo agrego
                aux[cont]=this.colors[i];
                cont++;
            } 
        }
        this.colors=aux;
        return colors;
    }
    // Esta funcion agrega un fondo al anchorPane del mapa
    public void setMap(){
        //Creamos un sb para saber cuantos mapas hay serializados
        Serializador sb = new Serializador();
        //se crea un random con la idea de generar un mapa random
        Random index = new Random();
        try {
            this.map = index.nextInt(sb.getAmountMaps());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Mapas no encontrados");
            this.map = 0;
        }
        this.map = 7;
        //System.out.println("Id de mapa: Mapa"+this.map);
        
        //ese valor dentro del nextint es la cantidad de mapas creados en existencia
        mapaPanel.getStylesheets().clear();
        mapaPanel.getStylesheets().add("Estilos.css");
        mapaPanel.getStyleClass().add("map"+(map));
    }
    
    //cambia la cantidad de jugadores que habrán
    public void setCantJug(int num){
        this.cantJug=num;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    
    
}
