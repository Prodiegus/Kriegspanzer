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
import javafx.scene.layout.Border;


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
    @FXML private ArrayList<ProgressBar> barras = new ArrayList<ProgressBar>();
    
    int turno=0;
    private Mapa mapa;
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    double altMax=0;
    double disMax=0;
    String[] balasDisp = { "Proyectil 60mm: 3 balas","Proyectil 105mm: 3 balas", "Proyectil Perforador: 10 balas"};
    
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

        //musica de disparo
        String path = "audio/5.mp3";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.getClip();
        
        double tiempo=0;
        int tGanador=turno;
        if (jugadores.get(turno).lanzamiento(Integer.parseInt(vel.getText()), Integer.parseInt(ang.getText()), this.mapa)){
            //las posiciones que se ingresan de "y" están al revés, entonces debemos modificarlas al momento de pasarlas al moverBala
            int [] posBala=jugadores.get(turno).getTanque().getBala().getPosBala();
            //int [] posBala=jugadores.get(turno).getTanques().get(id del tanque).getBala().getPosBala();
            int valid=0;
            //hacemos visible la bala del jugador del turno actual
            if ( jugadores.get(turno).getTanque().getBalasDisp()[0].equals(tBalas.getValue()) ){ //60mm
                //jugadores.get(turno).getTanques().get(id del tanque).getBalasDisp()[0].equals(tBalas.getValue())
                valid++;
                if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[0] != 0){
                    //jugadores.get(turno).getTanques().get(id del tanque).getBala().getTipoBalas()[0]
                    
                    jugadores.get(turno).getTanque().getBala().setCantBalas(0);
                    //jugadores.get(turno).getTanques().get(id del tanque).getBala().setCantBalas(0);
                    
                    jugadores.get(turno).getTanque().setBalasDisp( ("Proyectil 60mm: "+jugadores.get(turno).getTanque().getBala().getTipoBalas()[0]+ "  balas"), 0);
                    //jugadores.get(turno).getTanques().get(id del tanque).setBalasDisp( ("Proyectil 60mm: "+jugadores.get(turno).getTanque().getBala().getTipoBalas()[0]+ "  balas"), 0);
                    
                    arrayBalasImagen.get(0).get(turno).setVisible(true);
                    //arrayBalasImagen.get(0).get(turno).setVisible(true);
                    
                    //se le suman valores a las posiciones para que salga desde arriba y al medio del tanque y no desde una esquina
                    moverBala(posBala[0]+10,(470-posBala[1]),posBala[0]+10,(470-posBala[1]),Integer.parseInt(ang.getText()),Integer.parseInt(vel.getText()),tiempo,turno,tGanador, event,0);
                    turnoPanel.setText("Turno: "+jugadores.get(turno).getName()); 
                 //actualizamos los turnos
                    if (turno==1){ 
                        turno--;
                    }
                    else{
                        turno++;
                    }
                    setJugadores(jugadores);
                    this.tBalas.getItems().removeAll(this.tBalas.getItems());
                    this.tBalas.getItems().addAll(jugadores.get(turno).getTanque().getBalasDisp());
                }
                else{
                    JOptionPane.showMessageDialog(null, "No queda de este tipo de munición");
                }
            }
            if ( jugadores.get(turno).getTanque().getBalasDisp()[1].equals(tBalas.getValue()) ){
                valid++;
                if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[1] != 0){
                    jugadores.get(turno).getTanque().getBala().setCantBalas(1);
                    
                    jugadores.get(turno).getTanque().setBalasDisp( ("Proyectil 105mm: "+jugadores.get(turno).getTanque().getBala().getTipoBalas()[1]+ "  balas"), 1);
                    
                    arrayBalasImagen.get(1).get(turno).setVisible(true);
                    //se le suman valores a las posiciones para que salga desde arriba y al medio del tanque y no desde una esquina
                    moverBala(posBala[0]+10,(470-posBala[1]),posBala[0]+10,(470-posBala[1]),Integer.parseInt(ang.getText()),Integer.parseInt(vel.getText()),tiempo,turno,tGanador, event,1);
                    turnoPanel.setText("Turno: "+jugadores.get(turno).getName()); 
                    //actualizamos los turnos
                    if (turno==1){ 
                        turno--;
                    }
                    else{
                        turno++;
                    }
                    setJugadores(jugadores);
                    this.tBalas.getItems().removeAll(this.tBalas.getItems());
                    this.tBalas.getItems().addAll(jugadores.get(turno).getTanque().getBalasDisp());
                }
                else{
                    JOptionPane.showMessageDialog(null, "No queda de este tipo de munición");
                }
            }
            if ( jugadores.get(turno).getTanque().getBalasDisp()[2].equals(tBalas.getValue())  ){
                valid++;
                if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[2] != 0){
                    jugadores.get(turno).getTanque().getBala().setCantBalas(2);
                    
                    jugadores.get(turno).getTanque().setBalasDisp( ("Proyectil Perforador: "+jugadores.get(turno).getTanque().getBala().getTipoBalas()[2]+ "  balas"), 2);
                    
                    arrayBalasImagen.get(2).get(turno).setVisible(true);
                    //se le suman valores a las posiciones para que salga desde arriba y al medio del tanque y no desde una esquina
                    moverBala(posBala[0]+10,(470-posBala[1]),posBala[0]+10,(470-posBala[1]),Integer.parseInt(ang.getText()),Integer.parseInt(vel.getText()),tiempo,turno,tGanador, event,2);
                    turnoPanel.setText("Turno: "+jugadores.get(turno).getName()); 
                    //actualizamos los turnos
                    if (turno==1){ 
                        turno--;
                    }
                    else{
                        turno++;
                    }
                    setJugadores(jugadores);
                    this.tBalas.getItems().removeAll(this.tBalas.getItems());
                    this.tBalas.getItems().addAll(jugadores.get(turno).getTanque().getBalasDisp());
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
                arrayBalasImagen.get(tipBala).get(jug).setY(465-y);//el 465 significa la posicion real en la matriz, ya que esta es invertida
                try{//Se realiza la recursión hasta llegar al caso base 
                    moverBala(xI,yI,(xI+velocidad*Math.cos(Math.toRadians(angulo))*tiempo),(yI+velocidad*Math.sin(Math.toRadians(angulo))*tiempo-(0.5*9.81*(tiempo*tiempo))),angulo,velocidad,(tiempo+0.05),jug,tGanador, event,tipBala);      
                }
                catch(InterruptedException e2){
                    e2.printStackTrace();
                }
            }
            else if(mapa.comprobarCoordenadaTanque((int)Math.round(x),(int)Math.round(464-y))){ //entra al if si es que toca tanque
                
                // se verifica que si el lanzamiento de la bala llega a la zona del tanque se pega a sí mismo (posx-10,posx+10)
                if( ( (int)Math.round(x)<=jugadores.get(jug).getTanque().getPos()[0] + 10 ) && ((int)Math.round(x)>=jugadores.get(jug).getTanque().getPos()[0] - 10) ){
                    jugadores.get(jug).getTanque().setVida(jugadores.get(jug).getTanque().getVida()-jugadores.get(jug).getTanque().getBala().getDamageBala()[tipBala] );
                    barras.get(jug).setProgress(jugadores.get(jug).getTanque().getVida()/100);
                    if (jugadores.get(jug).getTanque().getVida() <=0 ){ //corresponderia al turno del otro tanque
                        cargarPantallaFinal(turno,event);
                    }
                } 
                else{ //si no se pega así mismo le pega al otro tanque
                    /*falta mejorar esta parte por si se presentan futuros tanques, 
                        Lo óptimo sería recorrer todos los tanques de cada jugador, primero se parte con un jugador, obtenemos sus tanques y verificamos uno por uno si su tanque
                        se encuentra en la posición de la bala
                    
                    for(int i=0;i<jugadores.size();i++){
                       Jugador jugActual=jugadores.get(i); //tenemos el jugador actual
                       for(int j=0;j<jugActual.getTanques().size();j++) //debemos tener el array de tanques que posee
                        Tanque tankActual= jugActual.getTanques().get(j)
                        if (  ((int)Math.round(x)<=tankActual.getPos()[0]+10) && ((int)Math.round(x)>=tankActual.getPos()[0]+10)  ){ //
                            jugadores.get(i).getTanques().get(j).setVida( jugadores.get(i).getTanques().get(j).getVida()-jugadores.get(i).getTanques().get(j).getBala().getDamageBala[tipBala])
                        }
                    }
                    */
                    jugadores.get(turno).getTanque().setVida(jugadores.get(turno).getTanque().getVida()-jugadores.get(turno).getTanque().getDamageBala()[tipBala] );
                    barras.get(turno).setProgress(jugadores.get(turno).getTanque().getVida()/100);
                    if (jugadores.get(turno).getTanque().getVida() <=0 ){ //si la vida es menor, gana el turno del otro tanque
                        cargarPantallaFinal(tGanador,event);
                    }
                }           
                
                altMax=0;//se reinicia la altura máxima para el siguiente jugador
                arrayBalasImagen.get(tipBala).get(jug).setVisible(false);
            }else{
                altMax=0;//se reinicia la altura máxima para el siguiente jugador
                arrayBalasImagen.get(tipBala).get(jug).setVisible(false);
                mapa.destruir((int)Math.round(x),(int)Math.round(464-y), (int)Math.round(jugadores.get(turno).getTanque().getBala().getDamageBala()[tipBala]/10));
                arrayBalasImagen.get(tipBala).get(jug).setX(xI);
                arrayBalasImagen.get(tipBala).get(jug).setY(465-yI);//el 465 significa la posicion real en la matriz, ya que esta es invertida
                posTank();
                //se reinicia la posicion de la bala, en la del tanque
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
                }else if(j.equals(Mapa.Area.AIRE)||j.equals(Mapa.Area.TANQUE)){
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
        double ancho = 733;//ancho panel con un pequeño padding
        double anchoI = mapaPanel.getPrefWidth();
        double altoScale = ancho/anchoI;
        //ArrayList<int[]> campos = mapa.getCampos();
        for (int i = 0; i<jugadores.size(); i++) {
            Double x = jugadores.get(i).getTanque().getPos()[0]*altoScale;
            tanks.get(i).setX(x);
            for (int[] campo : campos) {
                if(campo[0]==x){
                    int y = setYTank((int) Math.round(x));
                    tanks.get(i).setY(y);
                    jugadores.get(i).getTanque().setPos((int)Math.round(x), y);
                    mapa.addTank((int)Math.round(x), y);
                    for (int k = (int)Math.round(x); k < 20+(int)Math.round(x); k++) {//ancho de un tanque en el mapa
                        for (int j = y+10; j < 465; j++) {//alto del mapa le ponemos suelo al tanque
                            mapa.setAreas(k, j);
                        }
                        for (int j = 0; j < y; j++){
                            mapa.fillAire(k, j);
                        }
                    }
                }
            }
            mapaPanel.getChildren().add(tanks.get(i));
        }
        setBoard();

    }
    public int setYTank(int x) {
        for (int y = 0; y < 465; y++){//465 son las posibles y
            if(mapa.comprobarCoordenadaSolido(x, y)){
                return y-10;// se le resta el size en pixeles del tanque
            }
        }
        return 0;
    }
    public void posTank(){//El metodo "posTank" posicionara los tanques en "mapaPanel"
        double alto = mapaPanel.getHeight();//se toma la cantidad de pixeles en alto que hay de la ventana original.
        double ancho = mapaPanel.getWidth();//se toma la cantidad de pixeles en ancho que hay de la ventana original.
        double altoI = mapaPanel.getPrefHeight();//se toma la cantidad de pixeles en alto que hay de la ventana actual (rescalada).
        double anchoI = mapaPanel.getPrefWidth();//se toma la cantidad de pixeles en ancho que hay de la ventana actual (rescalada).
        double altoScale = ancho/anchoI;//la division de ambos anchos de una proporcion de la ventana actual.
        double anchoScale = alto/altoI;//la division de ambas alturas de una proporcion de la ventana actual.
        for (int i = 0; i<jugadores.size(); i++) {// se recorre el arraylist "jugadores", para proporcionarle cada tanque a su jugador.
            int x = jugadores.get(i).getTanque().getPos()[0];
            int y = setYTank(x);
            mapa.removeTank(jugadores.get(i).getTanque().getPos()[0], jugadores.get(i).getTanque().getPos()[1]);
            tanks.get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale);
            tanks.get(i).setY(y*anchoScale);
            jugadores.get(i).getTanque().setPos(x, y);
            mapa.addTank(x, y);
            //se setean los tanques con el pocisionamiento respectivo y se multiplica con su reescalado.
            for (int k = x; k < 20+x; k++) {//ancho de un tanque en el mapa
                for (int j = y+10; j < 465; j++) {//alto del mapa le ponemos suelo al tanque
                    mapa.setAreas(k, j);
                }
                for (int j = 0; j < y; j++){
                    mapa.fillAire(k, j);
                }
            }
        }
        for (int i=0; i<jugadores.size();i++){//Se utiliza el mismo metodo anterior para posicionar las balas.
            for (int j=0 ; j<arrayBalasImagen.size();j++){
                    arrayBalasImagen.get(j).get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale);
                    arrayBalasImagen.get(j).get(i).setY(jugadores.get(i).getTanque().getPos()[1]*anchoScale);
                
            }
        }
        setBoard();

    }
    public void  posBarras(){
        for(int i=0;i< jugadores.size();i++){
            //ingresar las progress bar por codigo y no fijas
            barras.add(new ProgressBar(1));
            barras.get(i).setStyle("-fx-accent:#5faf5f");
            barras.get(i).setVisible(true);
            barras.get(i).setTranslateX(  jugadores.get(i).getTanque().getPos()[0]-15);
            barras.get(i).setTranslateY( (jugadores.get(i).getTanque().getPos()[1])-25);
            barras.get(i).setProgress(1);
            barras.get(i).setPrefSize(60, 4);
            mapaPanel.getChildren().add(barras.get(i));
        }
        
    }
    

    public void posBala(){//El metodo "posBala" posicionara las balas en "mapaPanel"
        double alto =465;
        double ancho =733;
        double altoI = mapaPanel.getPrefHeight();
        double anchoI = mapaPanel.getPrefWidth();
        double altoScale = ancho/anchoI;
        double anchoScale = alto/altoI;
        /*
        barraJ1.setTranslateX(  jugadores.get(0).getTanque().getPos()[0] -15);    //se ubica la progressbar arriba del tanque
        barraJ1.setTranslateY( (jugadores.get(0).getTanque().getPos()[1]) -25);    //se ubica la progressbar arriba del tanque
        barraJ2.setTranslateX(  jugadores.get(1).getTanque().getPos()[0] -20);    //se ubica la progressbar arriba del tanque
        barraJ2.setTranslateY( (jugadores.get(1).getTanque().getPos()[1]) -25 );    //se ubica la progressbar arriba del tanque
        */
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
        this.tBalas.getItems().addAll(balasDisp);
        //barraJ1.setStyle("-fx-accent:#5faf5f");barraJ1.setProgress(1);
        //barraJ2.setStyle("-fx-accent:#5faf5f");barraJ1.setProgress(1);
        //barras.add(barraJ1);
        //barras.add(barraJ2);
        
    }
    
}
