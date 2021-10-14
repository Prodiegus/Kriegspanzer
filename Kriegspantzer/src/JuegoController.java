import java.io.File;
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
import javafx.print.PrintColor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;


public class JuegoController implements Initializable {
    @FXML private AnchorPane mapaPanel;
    @FXML private Canvas board;
    @FXML private Label turnoPanel;
    @FXML private Label alturaPanel;
    @FXML private Label distanciaPanel;
    @FXML private ArrayList<ArrayList<ImageView> > arrayBalasImagen = new ArrayList<ArrayList<ImageView> >();
    @FXML private ArrayList<ImageView> balasPredImagen = new ArrayList<ImageView>();
    @FXML private ArrayList<ImageView> balasGImagen = new ArrayList<ImageView>();
    @FXML private ArrayList<ImageView> balasPImagen = new ArrayList<ImageView>();
    @FXML private ArrayList<ImageView> tanks = new ArrayList<ImageView>();
    @FXML private TextField ang;
    @FXML private TextField vel;
    @FXML private ComboBox<String> tBalas;
    @FXML private ProgressBar barraJ1=new ProgressBar();
    @FXML private ProgressBar barraJ2=new ProgressBar();
    @FXML private ArrayList<ProgressBar> barras = new ArrayList<ProgressBar>();
    @FXML private ArrayList<String[]> boxBalas = new ArrayList<String[]>();
    
    int turno=0;
    private Mapa mapa;
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    double altMax=0;
    double disMax=0;
    String[] tiposBalas1 = { "Proyectil 60mm: 3 balas","Proyectil 105mm: 3 balas", "Proyectil Perforador: 10 balas"};
    String[] tiposBalas2 = { "Proyectil 60mm: 3 balas","Proyectil 105mm: 3 balas", "Proyectil Perforador: 10 balas"};

    
    @FXML public void scale(KeyEvent event){
        if(event.getCode().equals(KeyCode.R)){
            posTank();
            //posBala();
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
        double alto = mapaPanel.getHeight();//se toma la cantidad de pixeles en alto que hay de la ventana original.
        double ancho = mapaPanel.getWidth();//se toma la cantidad de pixeles en ancho que hay de la ventana original.
        double altoI = mapaPanel.getPrefHeight();//se toma la cantidad de pixeles en alto que hay de la ventana actual (rescalada).
        double anchoI = mapaPanel.getPrefWidth();//se toma la cantidad de pixeles en ancho que hay de la ventana actual (rescalada).
        double altoScale = ancho/anchoI;//la division de ambos anchos de una proporcion de la ventana actual.
        double anchoScale = alto/altoI;//la division de ambas alturas de una proporcion de la ventana actual
        //musica de disparo
        String path = "audio/5.mp3";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.getClip();
        
        double tiempo=0;
        int tGanador=turno;
        if(flag){ //mientras el flag sea verdadero, es decir mientras no exista un ganador, sigue el juego
            
            if (jugadores.get(turno).lanzamiento(Integer.parseInt(vel.getText()), Integer.parseInt(ang.getText()), this.mapa) && flag){
                
                //las posiciones que se ingresan de "y" están al revés, entonces debemos modificarlas al momento de pasarlas al moverBala
                int [] posBala=jugadores.get(turno).getTanque().getBala().getPosBala();
                int valid=0;
                //hacemos visible la bala del jugador del turno actual
                if ("Proyectil 60mm".equals(tBalas.getValue()) ){ //60mm
                    valid++;
                    if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[0] != 0){
                        jugadores.get(turno).getTanque().getBala().setCantBalas(0);
                        arrayBalasImagen.get(0).get(turno).setVisible(true);
                        moverBala((posBala[0]+10)*altoScale,(470-posBala[1])*anchoScale,(posBala[0]+10)*altoScale,(470-posBala[1])*anchoScale,Integer.parseInt(ang.getText()),Integer.parseInt(vel.getText()),tiempo,turno,tGanador, event,0);
                        turnoPanel.setText("Turno: "+jugadores.get(turno).getName()); 
                     //actualizamos los turnos
                        if (turno==1){ 
                            turno--;
                            setJugadores(jugadores);
                        }
                        else{
                            turno++;
                            setJugadores(jugadores);
                        }
                    }
                    else{
                        turno++;
                        setJugadores(jugadores);
                    }
                    this.tBalas.getItems().removeAll(this.tBalas.getItems());
                    this.tBalas.getItems().addAll(boxBalas.get(turno));
                }
                if ("Proyectil 105mm".equals(tBalas.getValue()) ){
                    valid++;
                    if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[1] != 0){
                        jugadores.get(turno).getTanque().getBala().setCantBalas(1);
                        arrayBalasImagen.get(1).get(turno).setVisible(true);
                        moverBala((posBala[0]+10)*altoScale,(470-posBala[1])*anchoScale,(posBala[0]+10)*altoScale,(470-posBala[1])*anchoScale,Integer.parseInt(ang.getText()),Integer.parseInt(vel.getText()),tiempo,turno,tGanador, event,1);
                        turnoPanel.setText("Turno: "+jugadores.get(turno).getName()); 
                        //actualizamos los turnos
                        if (turno==1){ 
                            turno--;
                            setJugadores(jugadores);
                        }
                        else{
                            turno++;
                            setJugadores(jugadores);
                        }
                    }
                    else{
                        turno++;
                        setJugadores(jugadores);
                    }
                    this.tBalas.getItems().removeAll(this.tBalas.getItems());
                    this.tBalas.getItems().addAll(boxBalas.get(turno));
                }
                if ("Proyectil Perforador".equals(tBalas.getValue()) ){
                    valid++;
                    if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[2] != 0){
                        jugadores.get(turno).getTanque().getBala().setCantBalas(2);
                        arrayBalasImagen.get(2).get(turno).setVisible(true);
                        moverBala((posBala[0]+10)*altoScale,(470-posBala[1])*anchoScale,(posBala[0]+10)*altoScale,(470-posBala[1])*anchoScale,Integer.parseInt(ang.getText()),Integer.parseInt(vel.getText()),tiempo,turno,tGanador, event,2);
                        turnoPanel.setText("Turno: "+jugadores.get(turno).getName()); 

                        //actualizamos los turnos
                        if (turno==1){ 
                            turno--;
                            setJugadores(jugadores);
                        }
                        else{
                            turno++;
                            setJugadores(jugadores);
                        }
                    }
                    else{
                        turno++;
                        setJugadores(jugadores);
                    }
                    this.tBalas.getItems().removeAll(this.tBalas.getItems());
                    this.tBalas.getItems().addAll(boxBalas.get(turno));
                }
                else{
                   JOptionPane.showMessageDialog(null, "No queda de este tipo de munición"); 
                }
            }
            if(valid==0){
                JOptionPane.showMessageDialog(null, "Debe elegir un tipo de bala");
            }

            //les pasamos las coordenadas verdaderas al método, que representan en el plano XY
        }
        else{
            JOptionPane.showMessageDialog(null, "Tiro fuera de límite, intente de nuevo.");
        }
        
    }

    @FXML
    private void reset(ActionEvent event) {
        try {
            FXMLLoader loader =new FXMLLoader(getClass().getResource("IniciarJuegoView.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            IniciarJuegoViewController controller = loader.getController();

            String[] colors = {"Azul", "Verde", "Amarillo", "Rojo", "Morado", "Naranja", "Negro"};

            controller.setBoxes(colors);
            controller.setMap();
            stage.setResizable(true);
            stage.setTitle("Kriegspanzer Game");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
            stage.setScene(scene);
            stage.show();
            close(event);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: 013\nNo se a podido cargar una nueva partida");
        }
    }

    @FXML
    private void cargarPantallaFinal(int tGanador,ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("seguirJugandoView.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            SeguirJugandoViewController controller = loader.getController();

            controller.setGanador(jugadores.get(tGanador).getName());
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
    @FXML
    private boolean moverBala(double xI,double yI,double x,double y,int angulo,double velocidad,double tiempo,int jug,int tGanador, ActionEvent event,int tipBala)throws InterruptedException {
        Platform.runLater( ()->{
            try{
                TimeUnit.MILLISECONDS.sleep(20);    
            }
            catch(InterruptedException el){
                el.printStackTrace();
            }
            /*
                Se setea los labels que van mostrando la distancia y altura máxima en cada instante
            */
            if (x-xI >=0 ){
                distanciaPanel.setText("Distancia máxima: "+ Math.round( (x-xI)*100.0)/100.0);
            }
            else{
                distanciaPanel.setText("Distancia máxima: "+ Math.round( (xI-x)*100.0)/100.0);
            }
            if (altMax<y){
                altMax=y;
                alturaPanel.setText("Altura máxima: "+Math.round( altMax*100.0)/100.0);
            } 
            /*
                Antes de inicar el proceso de recursión debemos verificar si las coordenadas que nos están entregando
                son correctas, es decir que no sobre pasen los límites laterales, y que no pasen más abajo del solido.
            
            */   
            if ( (x>=0 &&  x<=733) && (y>=0) && (y>464 || mapa.comprobarCoordenadaAire((int)Math.round(x),(int)Math.round(464-y)) )){
                //se setean las imagenes en pantalla
                if (xI!=x){
                    arrayBalasImagen.get(tipBala).get(jug).setX(x);
                }
                 arrayBalasImagen.get(tipBala).get(jug).setY(465-y);
                try{//Se realiza la recursión hasta llegar al caso base
                    moverBala(xI,yI,(xI+velocidad*Math.cos(Math.toRadians(angulo))*tiempo),(yI+velocidad*Math.sin(Math.toRadians(angulo))*tiempo-(0.5*9.81*(tiempo*tiempo))),angulo,velocidad,(tiempo+0.05),jug,tGanador, event,tipBala);      
                }
                catch(InterruptedException e2){
                    e2.printStackTrace();
                }
            }
            else if(mapa.comprobarCoordenadaTanque((int)Math.round(x),(int)Math.round(464-y))){
                //entra al if si es que toca tanque
                if (mapa.comprobarCoordenadaTanque( (int)Math.round(x),(int)Math.round(464-y)) ){
                    //debo ver a cuál tanque es el que le pega
                    
                    if( ( (int)Math.round(x)<=jugadores.get(jug).getTanque().getPos()[0] + 10 ) && ((int)Math.round(x)>=jugadores.get(jug).getTanque().getPos()[0] - 10) ){
                        jugadores.get(jug).getTanque().setVida(jugadores.get(jug).getTanque().getVida()-jugadores.get(jug).getTanque().getBala().getDamageBala()[tipBala] );
                        barras.get(jug).setProgress(jugadores.get(jug).getTanque().getVida()/100);
                        if (jugadores.get(jug).getTanque().getVida() <=0 ){ //corresponderia al turno del otro tanque
                            cargarPantallaFinal(turno,event);
                        }
                    } 
                    else{
                        //if (mapa.comprobarCoordenadaTanque(jugadores.get(jug).getTanque().getPos()[0],jugadores.get(jug).getTanque().getPos()[1]) ){ 
                            jugadores.get(turno).getTanque().setVida(jugadores.get(turno).getTanque().getVida()-jugadores.get(turno).getTanque().getBala().getDamageBala()[tipBala] );
                            barras.get(turno).setProgress(jugadores.get(turno).getTanque().getVida()/100);
                            if (jugadores.get(turno).getTanque().getVida() <=0 ){ //corresponderia al turno del otro tanque
                                cargarPantallaFinal(tGanador,event);
                            }
                        //}
                    }           
                }
                altMax=0;//se reinicia la altura máxima para el siguiente jugador
                arrayBalasImagen.get(tipBala).get(jug).setVisible(false);
            }else{
                altMax=0;//se reinicia la altura máxima para el siguiente jugador
                arrayBalasImagen.get(tipBala).get(jug).setVisible(false);
                mapa.destruir((int)Math.round(x),(int)Math.round(464-y), (int)Math.round(jugadores.get(turno).getTanque().getBala().getDamageBala()[tipBala]/10));
                setBoard();
            }
            
        });
        return false;
    }

    //le asigna la hoja de estilos al fondo del panel y la da la clase con la imagen del mapa
    public void setMap(Mapa mapa){
        //mapaPanel.getStylesheets().clear();
        //mapaPanel.getStylesheets().add("Estilos.css");
       // mapaPanel.getStyleClass().add("map"+(mapa.getId()));
        this.mapa = mapa;
        setBoard();
    }
    public void setBoard(){
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setFill(Color.valueOf("#008080"));
        int x = 0;
        for (Mapa.Area[] i : mapa.getMapeo()) {
            int y = 0;
            for (Mapa.Area j : i) {
                if(j.equals(Mapa.Area.SOLIDO)){
                    gc.setFill(Color.valueOf("#008080"));
                    gc.fillRect(x, y, 1, 1);
                }else if(j.equals(Mapa.Area.AIRE)){
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x, y, 1, 1);
                }
                y++;
            }
            x++;
            
        }  
    }
    //setea el label al principio del juego
    public void setJugadores(ArrayList<Jugador> jugadores){
        this.jugadores = jugadores;
        turnoPanel.setText("Turno: "+jugadores.get(turno).getName());
    }
    //se añade las imagenes a nuestro cuadro
    public void addViews(){
        for (int i = 0; i<jugadores.size(); i++) {
            tanks.add(new ImageView(
                new Image(getClass()
                .getResourceAsStream("img/Tanque_"+jugadores.get(i).getTanque().getColor()+".png"))));
            balasPredImagen.add(new ImageView(new Image(getClass().getResourceAsStream("img/bala.png"))));
            balasGImagen.add(new ImageView(new Image(getClass().getResourceAsStream("img/balaG.png"))));
            balasPImagen.add(new ImageView(new Image(getClass().getResourceAsStream("img/balaP.png"))));
        }
        arrayBalasImagen.add(balasPredImagen);
        arrayBalasImagen.add(balasGImagen);
        arrayBalasImagen.add(balasPImagen);
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
    public void posTank(){//El metodo "posTank" posicionara los tanques en "mapaPanel"
        double alto = mapaPanel.getHeight();//se toma la cantidad de pixeles en alto que hay de la ventana original.
        double ancho = mapaPanel.getWidth();//se toma la cantidad de pixeles en ancho que hay de la ventana original.
        double altoI = mapaPanel.getPrefHeight();//se toma la cantidad de pixeles en alto que hay de la ventana actual (rescalada).
        double anchoI = mapaPanel.getPrefWidth();//se toma la cantidad de pixeles en ancho que hay de la ventana actual (rescalada).
        double altoScale = ancho/anchoI;//la division de ambos anchos de una proporcion de la ventana actual.
        double anchoScale = alto/altoI;//la division de ambas alturas de una proporcion de la ventana actual.
        for (int i = 0; i<jugadores.size(); i++) {// se recorre el arraylist "jugadores", para proporcionarle cada tanque a su jugador.
            tanks.get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale);
            tanks.get(i).setY(jugadores.get(i).getTanque().getPos()[1]*anchoScale);
            
            //se setean los tanques con el pocisionamiento respectivo y se multiplica con su reescalado.
            
        }
        for (int i=0; i<jugadores.size();i++){//Se utiliza el mismo metodo anterior para posicionar las balas.
            for (int j=0 ; j<arrayBalasImagen.size();j++){
                    arrayBalasImagen.get(j).get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale);
                    arrayBalasImagen.get(j).get(i).setY(jugadores.get(i).getTanque().getPos()[1]*anchoScale);
                
            }
        }

    }
    

    public void posBala(){//El metodo "posBala" posicionara las balas en "mapaPanel"
        double alto =465;
        double ancho =733;
        double altoI = mapaPanel.getPrefHeight();
        double anchoI = mapaPanel.getPrefWidth();
        double altoScale = ancho/anchoI;
        double anchoScale = alto/altoI;
        barraJ1.setTranslateX(  jugadores.get(0).getTanque().getPos()[0] -15);
        barraJ1.setTranslateY( (jugadores.get(0).getTanque().getPos()[1]) -25);
        barraJ2.setTranslateX(  jugadores.get(1).getTanque().getPos()[0] -20);
        barraJ2.setTranslateY( (jugadores.get(1).getTanque().getPos()[1]) -25 );
        for (int i=0; i<jugadores.size();i++){//Se utiliza el mismo metodo anterior para posicionar las balas.
            for (int j=0 ; j<arrayBalasImagen.size();j++){
                if (i==1){
                    arrayBalasImagen.get(j).get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale + 3);
                    arrayBalasImagen.get(j).get(i).setY(jugadores.get(i).getTanque().getPos()[1]*anchoScale);
                    arrayBalasImagen.get(j).get(i).setRotate(180);
                }
                else{
                    arrayBalasImagen.get(j).get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale + 3);
                    arrayBalasImagen.get(j).get(i).setY(jugadores.get(i).getTanque().getPos()[1]*anchoScale);
                }
                //System.out.println("posicion de la bala "+j+" del jugador "+ i + ": "+ jugadores.get(i).getTanque().getPos()[0]*altoScale+","+jugadores.get(i).getTanque().getPos()[1]*anchoScale);
                arrayBalasImagen.get(j).get(i).setVisible(false);//Se vuelve invisble la bala para que no se vea al estar en estado de reposo.
                mapaPanel.getChildren().add(arrayBalasImagen.get(j).get(i));//se agregan las balas al mapaPanel.
            }
        }
    }
 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tBalas.getItems().removeAll(this.tBalas.getItems());
        this.tBalas.getItems().addAll(tiposBalas1);
        boxBalas.add(tiposBalas1);
        boxBalas.add(tiposBalas2);
        barraJ1.setStyle("-fx-accent:#5faf5f");barraJ1.setProgress(1);
        barraJ2.setStyle("-fx-accent:#5faf5f");barraJ1.setProgress(1);
        barras.add(barraJ1);
        barras.add(barraJ2);
        
    }
    
}
