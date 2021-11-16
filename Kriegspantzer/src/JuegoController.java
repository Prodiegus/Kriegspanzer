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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JuegoController implements Initializable {
    @FXML private AnchorPane mapaPanel;
    @FXML private Canvas board = new Canvas();
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
    
    int cont_orden=0;
    int []arrayOrden;
    //int cont_orden=0;
    private Mapa mapa;
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    double altoScale;//la division de ambos anchos de una proporcion de la ventana actual.
    double anchoScale;//la division de ambas alturas de una proporcion de la ventana actual.    
    double altMax=0;
    double disMax=0;
    IA ia;
    boolean disparo = false; //mientras la bala esta en aire no se puede disparar
    String[] balasDisp = { "Proyectil 60mm: 3 balas","Proyectil 105mm: 3 balas", "Proyectil Perforador: 10 balas"};
    
    
    @FXML public void scale(KeyEvent event){
        if(event.getCode().equals(KeyCode.R)){
            posTank(true);
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
        int turno=this.arrayOrden[cont_orden];
        double tiempo=0;
        int tGanador=cont_orden;
        if (jugadores.get(turno).lanzamiento(Integer.parseInt(vel.getText()), Integer.parseInt(ang.getText()), this.mapa)){
            //musica de disparo
            String path = "audio/5.mp3";
            Media media = new Media(new File(path).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.getClip();
            //las posiciones que se ingresan de "y" están al revés, entonces debemos modificarlas al momento de pasarlas al moverBala
            int [] posBala=jugadores.get(turno).getTanque().getBala().getPosBala();
                
            if ( jugadores.get(turno).getTanque().getBalasDisp()[0].equals(tBalas.getValue()) ){ //60mm
                if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[0] != 0){
                        
                    jugadores.get(turno).getTanque().getBala().setCantBalas(0);
                    jugadores.get(turno).getTanque().setBalasDisp(("Proyectil 60mm: "+jugadores.get(turno).getTanque().getBala().getTipoBalas()[0]+ " balas"), 0);
                        
                    arrayBalasImagen.get(0).get(turno).setVisible(true);
                    //System.out.println("hace visible la bala del turno: "+jugadores.get(turno).getName());
                    //se le suman valores a las posiciones para que salga desde arriba y al medio del tanque y no desde una esquina
                    moverBala(posBala[0]+10,(470-posBala[1]),posBala[0]+10,(470-posBala[1]),Integer.parseInt(ang.getText()),Integer.parseInt(vel.getText()),tiempo,cont_orden,tGanador, event,0);
                        
                    //actualizamos los turnos
                    /*
                    cont_orden+=1;
                    if (cont_orden == arrayOrden.length){
                        cont_orden=0;
                        nuevosTurnos();//desordena el orden nuevamente **ESTO DEBO HACERLO EN OTRA PARTE, PORQUE SE ESCONDE LA BALA 
                    }
                    turnoPanel.setText("Turno: "+jugadores.get(cont_orden).getName());
                    setJugadores(jugadores);
                    this.tBalas.getItems().removeAll(this.tBalas.getItems());
                    this.tBalas.getItems().addAll(jugadores.get(arrayOrden[cont_orden]).getTanque().getBalasDisp());
                    */
                    ang.setText(null);
                    vel.setText(null);
                    
                }
                else{
                    if (jugadores.get(arrayOrden[cont_orden]).isIA()){
                        verIA(event,cont_orden,ia);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "No queda de este tipo de munición"); 
                    }
                }
            }
            else if( jugadores.get(turno).getTanque().getBalasDisp()[1].equals(tBalas.getValue()) ){
                if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[1] != 0){
                    
                    jugadores.get(turno).getTanque().getBala().setCantBalas(1);
                    jugadores.get(turno).getTanque().setBalasDisp(("Proyectil 105mm: "+jugadores.get(turno).getTanque().getBala().getTipoBalas()[1]+ " balas"), 1);
                        
                    arrayBalasImagen.get(1).get(turno).setVisible(true);
                    //System.out.println("hace visible la bala del turno: "+jugadores.get(turno).getName());
                    //se le suman valores a las posiciones para que salga desde arriba y al medio del tanque y no desde una esquina
                    moverBala(posBala[0]+10,(470-posBala[1]),posBala[0]+10,(470-posBala[1]),Integer.parseInt(ang.getText()),Integer.parseInt(vel.getText()),tiempo,cont_orden,tGanador, event,1);
                        
                    //actualizamos los turnos
                    /*
                    cont_orden+=1;
                    if (cont_orden == arrayOrden.length){
                        cont_orden=0;
                        nuevosTurnos();//desordena el orden nuevamente **ESTO DEBO HACERLO EN OTRA PARTE, PORQUE SE ESCONDE LA BALA 
                    }
                    turnoPanel.setText("Turno: "+jugadores.get(cont_orden).getName());
                    setJugadores(jugadores);
                    this.tBalas.getItems().removeAll(this.tBalas.getItems());
                    this.tBalas.getItems().addAll(jugadores.get(arrayOrden[cont_orden]).getTanque().getBalasDisp());
                    */
                    ang.setText(null);
                    vel.setText(null);
                }
                else{
                    if (jugadores.get(arrayOrden[cont_orden]).isIA()){
                        verIA(event,cont_orden,ia);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "No queda de este tipo de munición"); 
                    }
                }
            }
            else if ( jugadores.get(turno).getTanque().getBalasDisp()[2].equals(tBalas.getValue())  ){
                if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[2] != 0){
                    
                    //System.out.println(jugadores.get(arrayOrden[cont_orden]).getName());
                    jugadores.get(turno).getTanque().getBala().setCantBalas(2);
                    jugadores.get(turno).getTanque().setBalasDisp(("Proyectil Perforador: "+jugadores.get(turno).getTanque().getBala().getTipoBalas()[2]+ " balas"), 2);
                        
                    arrayBalasImagen.get(2).get(turno).setVisible(true);
                    //System.out.println("hace visible la bala del turno: "+jugadores.get(turno).getName());
                    //se le suman valores a las posiciones para que salga desde arriba y al medio del tanque y no desde una esquina
                    moverBala(posBala[0]+10,(470-posBala[1]),posBala[0]+10,(470-posBala[1]),Integer.parseInt(ang.getText()),Integer.parseInt(vel.getText()),tiempo,cont_orden,tGanador, event,2);
                    
                    //actualizamos los turnos
                    /*
                    cont_orden+=1;
                    if (cont_orden == arrayOrden.length){
                        cont_orden=0;
                        nuevosTurnos();//desordena el orden nuevamente **ESTO DEBO HACERLO EN OTRA PARTE, PORQUE SE ESCONDE LA BALA 
                    }
                    turnoPanel.setText("Turno: "+jugadores.get(cont_orden).getName());
                    setJugadores(jugadores);
                    this.tBalas.getItems().removeAll(this.tBalas.getItems());
                    this.tBalas.getItems().addAll(jugadores.get(arrayOrden[cont_orden]).getTanque().getBalasDisp());
                    */
                    ang.setText(null);
                    vel.setText(null);
                }
                else{
                    if (jugadores.get(arrayOrden[cont_orden]).isIA()){
                        verIA(event,cont_orden,ia);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "No queda de este tipo de munición"); 
                    }
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Debe elegir un tipo de bala");
            }
            if(jugadores.get(arrayOrden[cont_orden]).getTanque().getBala().verificaBalas() ){
                System.out.println("entra a cargarEmpate en esta verificacion1");
                cargarEmpate(event);
            }
        }
        else{ //en caso de que la IA no realize un calculo mal
            if (jugadores.get(arrayOrden[cont_orden]).getTanque().getBala().verificaBalas() ){
                cargarEmpate(event);
            }
            else{
                if (jugadores.get(arrayOrden[cont_orden]).isIA()){
                    verIA(event,cont_orden,ia);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Tiro fuera de límite, intente de nuevo.");
                }
            }
        }
    }
    
    @FXML
    public void verIA(ActionEvent event, int cont_orden,IA ia) throws InterruptedException{
        if (jugadores.get(arrayOrden[cont_orden]).isIA()){
            if (jugadores.get(arrayOrden[cont_orden]).getTanque().getBala().verificaBalas() ){
                //close(event);
                cargarEmpate(event);
            }
            else{
                ia.calcularLanzamiento();
                int velocidad=ia.getVelocidad();
                int angulo=ia.getAngulo();
                vel.setText(""+velocidad);
                ang.setText(""+angulo);

                Random rn= new Random();
                String tipBala=jugadores.get(arrayOrden[cont_orden]).getTanque().getBalasDisp()[rn.nextInt(3)];
                tBalas.setValue(tipBala);
                if(ia.calcularRango(jugadores.get(arrayOrden[cont_orden]).getTanque().getBala().getPosBala(), velocidad, angulo, mapa)){//tiro no se sale de los rangos
                    try {
                        pressShoot(event);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    verIA(event,cont_orden,ia);
                }
                
            }
            
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
    private void cargarEmpate(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmpateView.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            //EmpateViewController controller = loader.getController();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Kriegspanzer End Game");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
            stage.setScene(scene);
            stage.show();
            close(event);
            
        } catch (IOException e) {
            //en caso de que algo salga mal mostraremos el siguiente mensaje
            JOptionPane.showMessageDialog(null, "Error 012:\nNo ha sido posible cargar el Empate del juego\n"+e.getCause());
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
        Platform.runLater(()->{
            int cont=0;
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
                distanciaPanel.setText("Distancia máxima: "+ Math.round( altoScale*(x-xI)*100.0)/100.0);
            }
            else{
                distanciaPanel.setText("Distancia máxima: "+ Math.round( altoScale*(xI-x)*100.0)/100.0);
            }
            if (altMax<y){
                altMax=y;
                alturaPanel.setText("Altura máxima: "+Math.round( anchoScale*altMax*100.0)/100.0);
            } 
            /*
                Antes de inicar el proceso de recursión debemos verificar si las coordenadas que nos están entregando
                son correctas, es decir que no sobre pasen los límites laterales, y que no pasen más abajo del solido.
            
            */   
            if ( (x>=0 &&  x<730) && (y>=0) && (y>464 || mapa.comprobarCoordenadaAire((int)Math.round(x),(int)Math.round(464-y)) )){
                //se setean las imagenes en pantalla
                if (xI!=x){
                    arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setX(x*altoScale);
                }
                arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setY((465-y)*anchoScale);//el 465 significa la posicion real en la matriz, ya que esta es invertida
                try{//Se realiza la recursión hasta llegar al caso base 
                    moverBala(xI,yI,(xI+velocidad*Math.cos(Math.toRadians(angulo))*tiempo),(yI+velocidad*Math.sin(Math.toRadians(angulo))*tiempo-(0.5*9.81*(tiempo*tiempo))),angulo,velocidad,(tiempo+0.05),jug,tGanador, event,tipBala);      
                }
                catch(InterruptedException e2){
                    e2.printStackTrace();
                }
            }
            else if(mapa.comprobarCoordenadaTanque((int)Math.round(x),(int)Math.round(464-y))){ //entra al if si es que toca tanque
                int win=0; 
                for(int i=0;i<jugadores.size();i++){//revisa si el tanque por tanque si se encuentra en las coordenadas que cayó el misil
                    if (  ((int)Math.round(x)<=jugadores.get(i).getTanque().getPos()[0]+20) && ((int)Math.round(x)>=jugadores.get(i).getTanque().getPos()[0]-20)  ){ //+-20 representa el hitbox
                        //le quito vida al tanque que se encuentre en esa zona
                        jugadores.get(i).getTanque().setVida( jugadores.get(i).getTanque().getVida()-jugadores.get(i).getTanque().getDamageBala()[tipBala]);
                        barras.get(i).setProgress(jugadores.get(i).getTanque().getVida()/100);
                        //***FALTA MODIFICAR QUIEN GANA***
                        if (jugadores.get(i).getTanque().getVida() <=0 ){ 
                            jugadores.get(i).setEstado(false);
                            //jugadores.get(jug).masKill();
                            //cargarPantallaFinal(cont_orden,event);
                        }
                    }
                    if(jugadores.get(i).cheekTanque()==false){
                        System.out.println("se encuentra uno tanque muerto");
                        cont++;
                    }
                    if(jugadores.get(i).cheekTanque()==true){
                        System.out.println("se encuentra un tanque vivo");
                        win=i;
                    }
                    if(cont==(jugadores.size())-1){
                    cargarPantallaFinal(win,event);
                    }
                }
                
                altMax=0;//se reinicia la altura máxima para el siguiente jugador.
                arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setVisible(false);
                //System.out.println("hace invisible la bala del turno: "+jugadores.get(arrayOrden[jug]).getName());
                
                
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                cont_orden+=1;
                if (cont_orden == arrayOrden.length){
                    cont_orden=0;
                    nuevosTurnos();//desordena el orden nuevamente **ESTO DEBO HACERLO EN OTRA PARTE, PORQUE SE ESCONDE LA BALA 
                }
                turnoPanel.setText("Turno: "+jugadores.get(cont_orden).getName());
                setJugadores(jugadores);
                this.tBalas.getItems().removeAll(this.tBalas.getItems());
                this.tBalas.getItems().addAll(jugadores.get(arrayOrden[cont_orden]).getTanque().getBalasDisp());
                if(jugadores.get(arrayOrden[cont_orden]).getTanque().getBala().verificaBalas() ){
                    System.out.println("entra a cargarEmpate en moverBala");
                    close(event);
                    cargarEmpate(event);
                }
                else{
                    ia = new IA(jugadores);
                    try {
                        verIA(event,cont_orden,ia);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }else{
                altMax=0;//se reinicia la altura máxima para el siguiente jugador.
                arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setVisible(false);
                //System.out.println("hace invisible la bala del turno: "+jugadores.get(arrayOrden[jug]).getName());
                //se reinicia la posicion de la bala, en la del tanque
                arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setX(xI);
                arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setY(465-yI);//el 465 significa la posicion real en la matriz, ya que esta es invertida
                mapa.destruir((int)Math.round(x),(int)Math.round(464-y), (int)Math.round(jugadores.get(arrayOrden[jug]).getTanque().getDamageBala()[tipBala]/3));
                
                posTank(true);
                setBoard();
                
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                cont_orden+=1;
                if (cont_orden == arrayOrden.length){
                    cont_orden=0;
                    nuevosTurnos();//desordena el orden nuevamente **ESTO DEBO HACERLO EN OTRA PARTE, PORQUE SE ESCONDE LA BALA 
                }
                turnoPanel.setText("Turno: "+jugadores.get(cont_orden).getName());
                setJugadores(jugadores);
                this.tBalas.getItems().removeAll(this.tBalas.getItems());
                this.tBalas.getItems().addAll(jugadores.get(arrayOrden[cont_orden]).getTanque().getBalasDisp());
                if(jugadores.get(arrayOrden[cont_orden]).getTanque().getBala().verificaBalas() ){
                    System.out.println("1 moverbala");
                    close(event);
                    cargarEmpate(event);
                }
                else{
                    ia = new IA(jugadores);
                    try {
                        verIA(event,cont_orden,ia);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
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
    public void setScale(){
        this.altoScale = board.getWidth()/mapaPanel.getPrefWidth();
        this.anchoScale = board.getHeight()/mapaPanel.getPrefHeight();
    }
    
    public void setBoard(){
        // a la hora de recorrer el ciclo se multiplica por sus escalas los valores
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setFill(Color.valueOf("#008080"));
        Mapa.Area[][] mapeo = mapa.getMapeo();
        for (int x = 0; x<mapeo.length*altoScale; x++) {
            for (int y = 0; y<mapeo[(int)Math.floor(x/altoScale)].length*anchoScale; y++) {
                if(mapeo[(int)Math.floor(x/altoScale)][(int)Math.floor(y/anchoScale)].equals(Mapa.Area.SOLIDO)){
                    gc.setFill(Color.valueOf("#008080"));
                    gc.fillRect(x, y, 1, 1);
                }else if(mapeo[(int)Math.floor(x/altoScale)][(int)Math.floor(y/anchoScale)].equals(Mapa.Area.AIRE) ||mapeo[(int)Math.floor(x/altoScale)][(int)Math.floor(y/anchoScale)].equals(Mapa.Area.TANQUE)  ){
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x, y, 1, 1);
                }
            }
        }  
    }
    //setea el label al principio del juego
    public void setJugadores(ArrayList<Jugador> jugadores){
        this.jugadores = jugadores;
        turnoPanel.setText("Turno: "+jugadores.get(arrayOrden[cont_orden]).getName());
    }
    //se randomiza el orden de los turnos
    public void ordenTurnos(ArrayList<Jugador> jugadores){
        this.arrayOrden= new int[jugadores.size()];
        for(int i=0;i<jugadores.size();i++){
            arrayOrden[i]=i;
        }
        Random r=new Random();
        for (int i=0; i<arrayOrden.length; i++) {
            int posAleatoria = r.nextInt(arrayOrden.length);
            int aux = this.arrayOrden[i];
            this.arrayOrden[i] = this.arrayOrden[posAleatoria];
            this.arrayOrden[posAleatoria] = aux;
        }
    }
    public void nuevosTurnos(){
        int[] aux = new int[jugadores.size()];
        for(int i=0;i<jugadores.size();i++){
            aux[i]=i;
        }
        Random r=new Random();
        for (int i=0; i<aux.length; i++) {
            int posAleatoria = r.nextInt(aux.length);
            int temp = aux[i];
            aux[i] = aux[posAleatoria];
            aux[posAleatoria] = temp;
        }
        this.arrayOrden=aux;
        System.out.print("nuevo orden: ");
        for(int j=0;j<arrayOrden.length;j++){
            System.out.print(arrayOrden[j]+",");
        }
    }
    
    //si se realiza un cambio en configuraciones, acá se aplican el el juego
    public void actualizaCantBalas(int[] balas){
        if (balas[0] != 0 || balas[1] != 0 || balas[2] != 0){   //si lo q recibio son solo ceros es porque se usará las municiones por default
            for(int i=0;i<jugadores.size();i++) {
                this.jugadores.get(i).getTanque().getBala().actualizaMuniciones(balas);
                this.jugadores.get(i).getTanque().actMunicionesString(balas);
            }
            this.tBalas.getItems().removeAll(this.tBalas.getItems());
            this.tBalas.getItems().addAll(jugadores.get(arrayOrden[0]).getTanque().getBalasDisp() );
            }
    }
    //se añade las imagenes a nuestro cuadro
    public void addViews(){
        //Aqui agregamos un track de musica para escuchar durante el juego
        String path = "audio/6.mp3";
        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.getClip();
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

    @FXML
    public int setYTank(int x) {
        for (int y = 0; y < 465; y++){//465 son las posibles y
            if(mapa.comprobarCoordenadaSolido(x+(int)Math.round(10/anchoScale), y)){
                return y-10;// se le resta el size en pixeles del tanque
            }
        }
        return 0;
    }

    public void posTank(){
        //ArrayList<int[]> campos = mapa.getCampos();
        for (int i = 0; i<jugadores.size(); i++) {
            Double x = jugadores.get(i).getTanque().getPos()[0]*altoScale;
            tanks.get(i).setX(x);
            int y = setYTank((int)Math.round(x));
            tanks.get(i).setY(y*anchoScale);
            jugadores.get(i).getTanque().setPos((int)Math.round(x/altoScale), y);
            mapa.addTank((int)Math.round(x), y);
            for (int k = (int)Math.round(x); k < 20+(int)Math.round(x); k++) {//ancho de un tanque en el mapa
                for (int j = y+10; j < 465; j++) {//alto del mapa le ponemos suelo al tanque
                     mapa.setAreas(k, j);
                }
                for (int j = 0; j < y; j++){
                     mapa.fillAire(k, j);
                }
            }  
            mapaPanel.getChildren().add(tanks.get(i));
        }
        setBoard();

    }
    @FXML
    public void posTank(boolean v){//El metodo "posTank" posicionara los tanques en "mapaPanel"
        for (int i = 0; i<jugadores.size(); i++) {// se recorre el arraylist "jugadores", para proporcionarle cada tanque a su jugador.
            int x = jugadores.get(i).getTanque().getPos()[0];
            int y = setYTank(x);
            mapa.removeTank(jugadores.get(i).getTanque().getPos()[0], jugadores.get(i).getTanque().getPos()[1]);
            tanks.get(i).setX(jugadores.get(i).getTanque().getPos()[0]*altoScale);
            tanks.get(i).setY(y*anchoScale);
            jugadores.get(i).getTanque().setPos(x, y);
            barras.get(i).setTranslateX( (jugadores.get(i).getTanque().getPos()[0]-15)*altoScale ); //reposiciono la barras con su respectiba escala
            barras.get(i).setTranslateY( (jugadores.get(i).getTanque().getPos()[1]-25)*anchoScale );//reposiciono la barras con su respectiba escala
            mapa.addTank(x, y);
            //se setean los tanques con el pocisionamiento respectivo y se multiplica con su reescalado.
        }
    }
    public void setBoardSize(double alto, double ancho){
        this.board.setWidth(alto);
        this.board.setHeight(ancho);
    }
    //posiciona las balas incialmente arriba de los tanques
    public void  posBarras(){
        for(int i=0;i< jugadores.size();i++){
            //ingresar las progress bar por codigo y no fijas
            barras.add(new ProgressBar(1));
            barras.get(i).setStyle("-fx-accent:#5faf5f");
            barras.get(i).setVisible(true);
            barras.get(i).setTranslateX( (jugadores.get(i).getTanque().getPos()[0]-15)*altoScale );
            barras.get(i).setTranslateY( (jugadores.get(i).getTanque().getPos()[1]-25)*anchoScale );
            barras.get(i).setProgress(1);
            barras.get(i).setPrefSize(60, 10);
            mapaPanel.getChildren().add(barras.get(i));
        }
    }

    public void posBala(){//El metodo "posBala" posicionara las balas en "mapaPanel"
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
    }
    
}
