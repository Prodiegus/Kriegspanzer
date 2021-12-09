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
import javafx.scene.control.Button;
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
    @FXML private Label vientoPanel;
    @FXML private Label gravedadPanel;
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
    @FXML private ActionEvent eventGlobal;
    @FXML private ImageView tanqueActual;
    @FXML private Label vidaTanque;
    @FXML private Label killsTanque;
    @FXML private Button disparar;
    
    int contOrden=0;
    int []arrayOrden;
    int []opcionesViento={-1,1};
    private Mapa mapa;
    private ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
    private int destruccionMapa = 3;// valores del 1 al inifinito, mientras menor sea mayor sera la destruccion visible en el mapa default 3
    private int anchoMatrizMapa = 733;// largo de la matriz del mapa
    private int altoMatrizMapa = 465;// ancho de la matriz del mapa
    private int anchoTanquepx = 20;
    private int altoTanquepx = 10;//tamaño en pixeles del alto de un tanque
    double altoScale;//la division de ambos anchos de una proporcion de la ventana actual.
    double anchoScale;//la division de ambas alturas de una proporcion de la ventana actual.    
    double altMax=0;
    double disMax=0;
    double gravedad=9.81;   //gravedad por default
    int viento=0;
    int limSup=464;//limites de la matriz
    int limInf=2;
    int limIzq=2;
    int limDer=730;
    int dirViento=0;
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
    @FXML private void close(ActionEvent event){
        //Node source = (Node) event.getSource();
        Stage stage = (Stage) disparar.getScene().getWindow();
        stage.close();
    }
    public void setEventG(ActionEvent event){
        this.eventGlobal=event;
    }
    
    @FXML private void pressShoot(ActionEvent event) throws InterruptedException {
        /* Al presionar el botón de disparo lo primero que debemos hacer es verificar
        qué jugador es, para así poder hacer los lanzamientos por separados
        */
        setEventG(event);
        int turno=this.arrayOrden[contOrden];
        int angulo=Integer.parseInt(ang.getText());
        int velocidad=Integer.parseInt(vel.getText());
        double tiempo=0;
        if (jugadores.get(turno).lanzamiento(velocidad,angulo, this.mapa, gravedad, viento, dirViento)){
            //musica de disparo
            String path = "audio/5.mp3";
            Media media = new Media(new File(path).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.getClip();
            tBalas.setDisable(true);//no se puede modificar la vala durante un disparo
            //las posiciones que se ingresan de "y" están al revés, entonces debemos modificarlas al momento de pasarlas al moverBala
            int [] posBala=jugadores.get(turno).getTanque().getBala().getPosBala();
                
            if ( jugadores.get(turno).getTanque().getBalasDisp()[0].equals(tBalas.getValue()) ){ //60mm
                if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[0] != 0){
                    jugadores.get(turno).setAng(angulo);
                    jugadores.get(turno).setVel(velocidad);
                    jugadores.get(turno).getTanque().getBala().setCantBalas(0);
                    jugadores.get(turno).getTanque().setBalasDisp(("Proyectil 60mm: "+jugadores.get(turno).getTanque().getBala().getTipoBalas()[0]+ " balas"), 0);
                        
                    arrayBalasImagen.get(0).get(turno).setVisible(true);
                    //System.out.println("hace visible la bala del turno: "+jugadores.get(turno).getName());
                    //se le suman valores a las posiciones para que salga desde arriba y al medio del tanque y no desde una esquina
                    moverBala(posBala[0]+(double)altoTanquepx,(470-posBala[1]),posBala[0]+(double)altoTanquepx,(470-posBala[1]),angulo,velocidad,tiempo,contOrden,event,0);
                }
                else{
                    if (jugadores.get(arrayOrden[contOrden]).isIA()){
                        verIA(new ActionEvent(),contOrden,ia);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "No queda de este tipo de munición"); 
                    }
                }
            }
            else if( jugadores.get(turno).getTanque().getBalasDisp()[1].equals(tBalas.getValue()) ){
                if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[1] != 0){
                    jugadores.get(turno).setAng(angulo);
                    jugadores.get(turno).setVel(velocidad);
                    jugadores.get(turno).getTanque().getBala().setCantBalas(1);
                    jugadores.get(turno).getTanque().setBalasDisp(("Proyectil 105mm: "+jugadores.get(turno).getTanque().getBala().getTipoBalas()[1]+ " balas"), 1);
                        
                    arrayBalasImagen.get(1).get(turno).setVisible(true);
                    //System.out.println("hace visible la bala del turno: "+jugadores.get(turno).getName());
                    //se le suman valores a las posiciones para que salga desde arriba y al medio del tanque y no desde una esquina
                    moverBala(posBala[0]+(double)altoTanquepx,(470-posBala[1]),posBala[0]+(double)altoTanquepx,(470-posBala[1]),angulo,velocidad,tiempo,contOrden,event,1);
                }
                else{
                    if (jugadores.get(arrayOrden[contOrden]).isIA()){
                        verIA(new ActionEvent(), contOrden,ia);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "No queda de este tipo de munición"); 
                    }
                }
            }
            else if ( jugadores.get(turno).getTanque().getBalasDisp()[2].equals(tBalas.getValue())  ){
                if(jugadores.get(turno).getTanque().getBala().getTipoBalas()[2] != 0){
                    jugadores.get(turno).setAng(angulo);
                    jugadores.get(turno).setVel(velocidad);
                    jugadores.get(turno).getTanque().getBala().setCantBalas(2);
                    jugadores.get(turno).getTanque().setBalasDisp(("Proyectil Perforador: "+jugadores.get(turno).getTanque().getBala().getTipoBalas()[2]+ " balas"), 2);
                        
                    arrayBalasImagen.get(2).get(turno).setVisible(true);
                    //System.out.println("hace visible la bala del turno: "+jugadores.get(turno).getName());
                    //se le suman valores a las posiciones para que salga desde arriba y al medio del tanque y no desde una esquina
                    moverBala(posBala[0]+(double)altoTanquepx,(470-posBala[1]),posBala[0]+(double)altoTanquepx,(470-posBala[1]),angulo,velocidad,tiempo,contOrden,event,2);
                }
                else{
                    if (jugadores.get(arrayOrden[contOrden]).isIA()){
                        verIA(new ActionEvent(),contOrden,ia);
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "No queda de este tipo de munición"); 
                    }
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Debe elegir un tipo de bala");
                tBalas.setDisable(false);
            }
            /*if(jugadores.get(arrayOrden[cont_orden]).getTanque().getBala().verificaBalas() ){
                System.out.println("entra a cargarEmpate en esta verificacion1");
                cargarEmpate(event);
            }*/
        }
        else{ //en caso de que la IA no realize un calculo mal
            if (jugadores.get(arrayOrden[contOrden]).getTanque().getBala().verificaBalas() && jugadores.get(arrayOrden[contOrden]).cheekTanque()){
                boolean bandera=true;
                int maxbala=jugadores.get(0).getKills();
                int jugwin=0;
                for(int i=0;i<jugadores.size();i++){
                    if(jugadores.get(i).getKills()>maxbala){
                        maxbala=jugadores.get(i).getKills();
                        jugwin=i;
                    }
                }
                for(int a=0;a<jugadores.size();a++){
                    if(jugwin!=a){
                        if(jugadores.get(a).getKills()==maxbala){
                            bandera=false;
                            cargarEmpate(eventGlobal);
                        }
                    }
                }
                if(bandera){
                    cargarPantallaFinal(jugwin, event);
                }
            }
            else{
                if (jugadores.get(arrayOrden[contOrden]).isIA()){
                    verIA(new ActionEvent(),contOrden,ia);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Tiro fuera de límite, intente de nuevo.");
                }
            }
        }
    }
    
    @FXML
    public void verIA(ActionEvent event, int contOrden,IA ia) throws InterruptedException{
        if (jugadores.get(arrayOrden[contOrden]).isIA()){
            ia.calcularLanzamiento();
            int velocidad=ia.getVelocidad();
            int angulo=ia.getAngulo();
            vel.setText(""+velocidad);
            ang.setText(""+angulo);

            Random rn= new Random();
            String tipBala=jugadores.get(arrayOrden[contOrden]).getTanque().getBalasDisp()[rn.nextInt(3)];
            tBalas.setValue(tipBala);
            if(ia.calcularRango(jugadores.get(arrayOrden[contOrden]).getTanque().getBala().getPosBala(), velocidad, angulo, mapa, gravedad,viento, dirViento)){//tiro no se sale de los rangos
                try {
                    pressShoot(new ActionEvent());
                } catch (InterruptedException ex) {
                    Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                verIA(new ActionEvent(),this.contOrden,ia);
            }
        }
    }
    
    @FXML
    private void reset(ActionEvent event) {
        close(event);
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
           
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: 013\nNo se a podido cargar una nueva partida");
        }
    }
    @FXML
    private void cargarEmpate(ActionEvent event){
        close(event);
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
            
            
        } catch (IOException e) {
            //en caso de que algo salga mal mostraremos el siguiente mensaje
            JOptionPane.showMessageDialog(null, "Error 012:\nNo ha sido posible cargar el Empate del juego\n"+e.getCause());
        }
    }
    @FXML
    private void cargarPantallaFinal(int tGanador, ActionEvent event){
        close(event);
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
            
        } catch (IOException e) {
            //en caso de que algo salga mal mostraremos el siguiente mensaje
            JOptionPane.showMessageDialog(null, "Error 011:\nNo ha sido posible cargar el Fanal del juego\n"+e.getCause());
        }
    }
    @FXML
    private boolean moverBala(double xI,double yI,double x,double y,int angulo,double velocidad,double tiempo,int jug,ActionEvent event,int tipBala)throws InterruptedException {
        Platform.runLater(()->{
            int cont=0;//esta variable contador sirve para verificar cuantos tanque muertos hay.
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
            if ( (x>=limIzq &&  x<limDer) && (y>=limInf) && (y>limSup || mapa.comprobarCoordenadaAire((int)Math.round(x),(int)Math.round(limSup-y)) )){
                //se setean las imagenes en pantalla
                if (xI!=x){
                    arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setX(x*altoScale);
                }
                arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setY((limSup-y)*anchoScale);//el 465 significa la posicion real en la matriz, ya que esta es invertida
                try{//Se realiza la recursión hasta llegar al caso base 
                    moverBala(xI,yI,(xI+velocidad*Math.cos(Math.toRadians(angulo))*tiempo-0.5*viento*dirViento*tiempo*tiempo),(yI+velocidad*Math.sin(Math.toRadians(angulo))*tiempo-(0.5*gravedad*(tiempo*tiempo))),angulo,velocidad,(tiempo+0.05),jug,event,tipBala);      
                }
                catch(InterruptedException e2){
                    e2.printStackTrace();
                }
            }
            else if(mapa.comprobarCoordenadaTanque((int)Math.round(x),(int)Math.round(limSup-y))){ //entra al if si es que toca tanque
                int win=0;
                arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setVisible(false);
                arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setX(xI);
                arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setY(limSup-yI);
                for(int i=0;i<jugadores.size();i++){//revisa si el tanque por tanque si se encuentra en las coordenadas que cayó el misil
                    if (  ((int)Math.round(x)<=jugadores.get(i).getTanque().getPos()[0]+15) && ((int)Math.round(x)>=jugadores.get(i).getTanque().getPos()[0]-15)){ //+-15 representa el hitbox
                        //le quito vida al tanque que se encuentre en esa zona
                        jugadores.get(i).getTanque().setVida( jugadores.get(i).getTanque().getVida()-jugadores.get(i).getTanque().getDamageBala()[tipBala]);
                        barras.get(i).setProgress(jugadores.get(i).getTanque().getVida()/100);
                        if (jugadores.get(i).getTanque().getVida() <=0 ){// si el jugador al que le cae la bala pierde toda la vida. 
                            jugadores.get(arrayOrden[contOrden]).masKill();// se le suma la kill al tanque que lo elimino.
                            jugadores.get(i).quitarKills();// el tanque que muere pierde todas sus kills acumuladas.
                            jugadores.get(i).setEstado(false);// su estado cambia de vivo a muerto.
                            quitarTanque(i);// se eliminara el jugador muerto del sistema de turnos.
                            contOrden--;
                            Tanque tanque = jugadores.get(i).getTanque();
                            mapa.removeTank(tanque.getPos()[0], tanque.getPos()[1]);// se remueve el tanque en ambito de matriz del mapa.
                        }
                    }
                    if(!jugadores.get(i).cheekTanque()){//si el estado es falso es tanque destruido
                        cont++;// el contador de tanque destruidos aumentara. 
                    }
                    else{//si el estado es true es tanque vivo
                        win=i;// si encuentra un tanque vivo lo designara como ganador.
                    }
                    if(cont==(jugadores.size())-1){// al identificar que quede un tanque vivo "jugador.size()-1".
                        cargarPantallaFinal(win,eventGlobal);// identificara como al unico tanque vivo como el ganador
                    }
                }
                altMax=0;//se reinicia la altura máxima para el siguiente jugador.      
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                contOrden+=1;
                if (contOrden == arrayOrden.length){
                    contOrden=0;
                    nuevosTurnos();//desordena el orden nuevamente **ESTO DEBO HACERLO EN OTRA PARTE, PORQUE SE ESCONDE LA BALA 
                    setPanelUsuario();
                }
                setJugadores(jugadores);
                this.tBalas.getItems().removeAll(this.tBalas.getItems());
                this.tBalas.getItems().addAll(jugadores.get(arrayOrden[contOrden]).getTanque().getBalasDisp());
                tBalas.setDisable(false);
                 
                ang.setText( ""+jugadores.get(arrayOrden[contOrden]).getAng() );
                vel.setText( ""+jugadores.get(arrayOrden[contOrden]).getVel());
                if (jugadores.get(arrayOrden[contOrden]).getTanque().getBala().verificaBalas() && jugadores.get(arrayOrden[contOrden]).cheekTanque()){
                    boolean bandera=true;
                    int maxbala=jugadores.get(0).getKills();
                    int jugwin=0;
                    for(int i=0;i<jugadores.size();i++){
                        if(jugadores.get(i).getKills()>maxbala){
                            maxbala=jugadores.get(i).getKills();
                            jugwin=i;
                        }
                    }
                    for(int a=0;a<jugadores.size();a++){
                        if(jugwin!=a){
                            if(jugadores.get(a).getKills()==maxbala){
                                bandera=false;
                                cargarEmpate(eventGlobal);
                            }
                        }
                    }
                    if(bandera){
                        cargarPantallaFinal(jugwin, event);
                    }
            }
                else{
                    ia = new IA(jugadores);
                    try {
                        verIA(new ActionEvent(),contOrden,ia);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }else{
                altMax=0;//se reinicia la altura máxima para el siguiente jugador.
                arrayBalasImagen.get(tipBala).get(arrayOrden[jug]).setVisible(false);
                //System.out.println("hace invisible la bala del turno: "+jugadores.get(arrayOrden[jug]).getName());
                //se reinicia la posicion de la bala, en la del tanque
                mapa.destruir( (int)Math.round(x),(int)Math.round(464-y), (int)Math.round(jugadores.get(arrayOrden[jug]).getTanque().getDamageBala()[tipBala]/destruccionMapa) );
                ArrayList<Integer> impactados = impactados((int)Math.round(x),(int)Math.round(464-y), (int)Math.round(jugadores.get(arrayOrden[jug]).getTanque().getDamageBala()[tipBala]/destruccionMapa));
                for (Integer i : impactados) {
                    jugadores.get(i).getTanque().setVida(jugadores.get(i).getTanque().getVida()-10);//danio colateral por alcanxe de proyectil
                    barras.get(i).setProgress(jugadores.get(i).getTanque().getVida()/100);
                    Tanque tanque = jugadores.get(i).getTanque();
                    if(tanque.getVida()<=0){
                        tanks.remove((int)i);
                        jugadores.get(arrayOrden[contOrden]).masKill();
                        mapa.removeTank(tanque.getPos()[0], tanque.getPos()[1]);
                    }
                }
                posTank(true);
                setBoard();
                
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
                }
                contOrden+=1;
                if (contOrden == arrayOrden.length){
                    contOrden=0;
                    nuevosTurnos();//desordena el orden nuevamente **ESTO DEBO HACERLO EN OTRA PARTE, PORQUE SE ESCONDE LA BALA
                    setPanelUsuario();
                }
                setJugadores(jugadores);
                this.tBalas.getItems().removeAll(this.tBalas.getItems());
                this.tBalas.getItems().addAll(jugadores.get(arrayOrden[contOrden]).getTanque().getBalasDisp());
                tBalas.setDisable(false);
                 
                ang.setText( ""+jugadores.get(arrayOrden[contOrden]).getAng() );
                vel.setText( ""+jugadores.get(arrayOrden[contOrden]).getVel());
                if (jugadores.get(arrayOrden[contOrden]).getTanque().getBala().verificaBalas() && jugadores.get(arrayOrden[contOrden]).cheekTanque()){
                    boolean bandera=true;
                    int maxbala=jugadores.get(0).getKills();
                    int jugwin=0;
                    for(int i=0;i<jugadores.size();i++){
                        if(jugadores.get(i).getKills()>maxbala){
                            maxbala=jugadores.get(i).getKills();
                            jugwin=i;
                        }
                    }
                    for(int a=0;a<jugadores.size();a++){
                        if(jugwin!=a){
                            if(jugadores.get(a).getKills()==maxbala){
                                bandera=false;
                                cargarEmpate(eventGlobal);
                            }
                        }
                    }
                    if(bandera){
                        cargarPantallaFinal(jugwin, event);
                    }
            }
                else{
                    ia = new IA(jugadores);
                    try {
                        verIA(new ActionEvent(),contOrden,ia);
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
        this.altoScale = board.getWidth()/anchoMatrizMapa;
        this.anchoScale = board.getHeight()/altoMatrizMapa;
    }
    public void setGravedad(double gravity){
        this.gravedad=gravity;
    }
    public void setWind(int wind){
        this.viento=wind;
        gravedadPanel.setText("Gravedad: "+gravedad);
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
                }else if(mapeo[(int)Math.floor(x/altoScale)][(int)Math.floor(y/anchoScale)].equals(Mapa.Area.AIRE) || mapeo[(int)Math.floor(x/altoScale)][(int)Math.floor(y/anchoScale)].equals(Mapa.Area.TANQUE)){
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x, y, 1, 1);
                }
            }
        }  
    }
    //setea el label al principio del juego
    public void setJugadores(ArrayList<Jugador> jugadores){
        this.jugadores = jugadores;
        Random r=new Random();
        if (this.viento != 0){
            this.dirViento = opcionesViento[r.nextInt(2)];
            this.viento=1+r.nextInt(10);
            switch (dirViento) {
                case 1:
                    vientoPanel.setText("Viento: "+viento+" a la izquierda.");
                    break;
                case -1:
                    vientoPanel.setText("Viento: "+viento+" a la derecha.");
                    break;
                default:
                    vientoPanel.setText("Viento: "+viento+" desconocido");
                break;
            }
        }
        else{
            vientoPanel.setText("Sin viento.");
        }
        turnoPanel.setText("Turno: "+jugadores.get(arrayOrden[contOrden]).getName());
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
        int[] aux = new int[arrayOrden.length];
        for(int i=0;i<arrayOrden.length;i++){
            aux[i]=arrayOrden[i];
        }
        Random r=new Random();
        for (int i=0; i<aux.length; i++) {
            int posAleatoria = r.nextInt(aux.length);
            int temp = aux[i];
            aux[i] = aux[posAleatoria];
            aux[posAleatoria] = temp;
        }
        this.arrayOrden=aux;
    }
    public void quitarTanque(int jugMuerto){// este metodo servira para eliminar al tanque muerto del sistema de turnos.
        int[] aux = new int [this.arrayOrden.length-1];// se creara un arreglo auxiliar para guardar el nuevo arreglo de turnos.
        int cont=0;
        //System.out.println("el jugador muerto es: "+jugMuerto);
        for(int i=0;i<this.arrayOrden.length;i++){//se recorrera el arreglo de sistema de turnos.
            if(this.arrayOrden[i]!=jugMuerto){//al encontrar un tanque que no sea el eliminado actualmente.
                //System.out.println("compara jugadores: "+this.arrayOrden[i]+" y "+jugMuerto);
                aux[cont]=this.arrayOrden[i];// este se integrara al sistema de turnos.
                cont++;
            }
        }
        this.arrayOrden=aux;//finalmente reemplazando el arreglo de turnos viejos con el nuevo.
        mapaPanel.getChildren().remove(tanks.get(jugMuerto));     //se borra la imagen del tanque en pantalla
        mapaPanel.getChildren().remove(barras.get(jugMuerto));    //se borra la barra del tanque en pantalla
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
            tBalas.setDisable(false);
             
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

    @FXML
    public int setYTank(int x) {
        for (int y = 0; y < altoMatrizMapa; y++){//465 son las posibles y
            if(mapa.comprobarCoordenadaSolido(x+(int)Math.round(altoTanquepx/anchoScale), y)){
                return y-altoTanquepx;// se le resta el size en pixeles del tanque
            }
        }
        return 0;
    }

    public int pixelesY(int x, int y) {
        int i = 0;
        while(y < altoMatrizMapa){//si y sobrepasa este numero el tanque caeria fuera del mapa
            if(mapa.comprobarCoordenadaSolido(x+(int)Math.round(altoTanquepx/anchoScale), y)){
                return i;// se retornan los pixeles movidos
            }
            y++;
            i++;
        }
        return 400;//eso quiere decir que salio del mapa por lo que es destruido suelo es laba
    }

    /**
     * El objetivo de este metodo es retornar una lista con los tanques impactados por el area de una bala
     * @param x = pos x disparo
     * @param y = pos y disparo
     * @param d = es el daño a realizar al mapa
     * @return Una lista con los tanques impactados por el area del daño de la bala
     */
    public ArrayList<Integer> impactados(int x, int y, int d){
        d += 20;
        ArrayList<Integer> impactados = new ArrayList<Integer>();
        int i = 0;
        for (Jugador jugador : jugadores) { 
            Tanque tanque = jugador.getTanque();
            //System.out.println("La posicion de la bala es x="+x+" e y="+y+" el radio de explosion en x es de "+(x-d/2)+" hasta "+ (x+d/2)+" y la posicion del tanque en x es="+tanque.getPos()[0]+" el radio de explosion en y es de "+(y-d/2)+" hasta "+ (y+d/2)+" y la posicion del tanque en y es="+tanque.getPos()[1]);
            if(((tanque.getPos()[0]> x-d/2 && tanque.getPos()[0]<=x) || (tanque.getPos()[0]< x+d/2 && tanque.getPos()[0]>=x)) && ((tanque.getPos()[1]> y-d/2 && tanque.getPos()[1]<=y) || (tanque.getPos()[1]< y+d/2 && tanque.getPos()[1]>=y))){
                impactados.add(i);
            }
            i++;
        }
        return impactados;
    }

    public void posTank(){
        //ArrayList<int[]> campos = mapa.getCampos();
        for (int i = 0; i<jugadores.size(); i++) {
            Double x = jugadores.get(i).getTanque().getPos()[0]*altoScale;
            tanks.get(i).setX(x+altoScale*altoScale*altoScale+altoScale);
            int y = setYTank((int)Math.round(x/altoScale));
            tanks.get(i).setY((y+altoTanquepx)*anchoScale-(altoTanquepx-anchoScale-1)*anchoScale);//se le resta 1 para que este bien apegado
            tanks.get(i).setScaleX(altoScale);
            tanks.get(i).setScaleY(anchoScale);
            jugadores.get(i).getTanque().setPos((int)Math.round(x/altoScale), y);
            mapa.addTank((int)Math.round(x/altoScale), y);
            for (int k = (int)Math.floor(x/altoScale); k < anchoTanquepx+(int)Math.floor(x/altoScale); k++) {//ancho de un tanque en el mapa
                for (int j = y+altoTanquepx; j < altoMatrizMapa; j++) {//alto del mapa le ponemos suelo al tanque
                     mapa.setAreas(k, j);
                }
                for (int j = 0; j < y-altoTanquepx; j++){
                    mapa.fillAire(k, j);
                }
            }  
            mapaPanel.getChildren().add(tanks.get(i));
        }
        //posTank(true);
        setBoard();
    }

    @FXML
    public void posTank(boolean v){//El metodo "posTank" posicionara los tanques en "mapaPanel"
        //a los manejos con x se les sumara 1 por el redondeo de la funcion de pos anterior
        for (int i = 0; i<jugadores.size(); i++) {// se recorre el arraylist "jugadores", para proporcionarle cada tanque a su jugador.
            int x = (int)(jugadores.get(i).getTanque().getPos()[0]*altoScale);
            int caida = pixelesY((int)(x/altoScale), jugadores.get(i).getTanque().getPos()[1]+altoTanquepx);//posicion en y actual del tanque los 10 corresponden a los pixeles del tanque
            int y = setYTank((int)(jugadores.get(i).getTanque().getPos()[0]));
            tanks.get(i).setX(x+altoScale*altoScale*altoScale+altoScale+1);
            tanks.get(i).setY((y+altoTanquepx)*anchoScale-(altoTanquepx-anchoScale-1)*anchoScale);
            jugadores.get(i).getTanque().setPos((int)(x/altoScale)+1, y);
            barras.get(i).setTranslateX(x-(double)16); //reposiciono la barras con su respectiba escala
            barras.get(i).setTranslateY((jugadores.get(i).getTanque().getPos()[1])*anchoScale-altoTanquepx);//reposiciono la barras con su respectiba escala
            mapa.removeTank(jugadores.get(i).getTanque().getPos()[0], jugadores.get(i).getTanque().getPos()[1]);//antes de agregar un nuevo tanque eliminaremos el viejo del mapa
            mapa.addTank((int)Math.round(x/altoScale)+1, y);
            //se setean los tanques con el pocisionamiento respectivo y se multiplica con su reescalado.
            Tanque tanque = jugadores.get(i).getTanque();
            tanque.setVida(tanque.getVida()-(caida/(double)4));//danio por caida
            barras.get(i).setProgress(tanque.getVida()/100);//se actualiza la barra de vida
            if(tanque.getVida()<=0 && jugadores.get(i).cheekTanque() ){
                mapa.removeTank(tanque.getPos()[0], tanque.getPos()[1]);
                mapaPanel.getChildren().remove(tanks.get(i));     //se borra la imagen del tanque en pantalla
                mapaPanel.getChildren().remove(barras.get(i));    //se borra la barra del tanque en pantalla
                jugadores.get(i).quitarKills();// el tanque que muere pierde todas sus kills acumuladas.
                jugadores.get(i).setEstado(false);// su estado cambia de vivo a muerto.
                quitarTanque(i);// se eliminara el jugador muerto del sistema de turnos.
                contOrden--;
            }
        }
    }
    public void setBoardSize(double alto, double ancho){
        this.board.setWidth(alto);
        this.board.setHeight(ancho);
    }
    @FXML
    public void setPanelUsuario(ActionEvent event){
        Tanque tanque = jugadores.get(arrayOrden[contOrden]).getTanque();
        this.tanqueActual.setImage(new Image("img/Tanque_"+tanque.getColor()+".png"));
        this.vidaTanque.setText((int)Math.round(tanque.getVida())+"%");
        this.killsTanque.setText("Kills: "+jugadores.get(arrayOrden[contOrden]).getKills());
    }
    @FXML
    public void setPanelUsuario(){
        Tanque tanque = jugadores.get(arrayOrden[contOrden]).getTanque();
        this.tanqueActual.setImage(new Image("img/Tanque_"+tanque.getColor()+".png"));
        this.vidaTanque.setText((int)Math.round(tanque.getVida())+"%");
        this.killsTanque.setText("Kills: "+jugadores.get(arrayOrden[contOrden]).getKills());
        
    }
    //posiciona las balas incialmente arriba de los tanques
    public void  posBarras(){
        for(int i=0;i< jugadores.size();i++){
            //ingresar las progress bar por codigo y no fijas
            barras.add(new ProgressBar(1));
            barras.get(i).setStyle("-fx-accent:#5faf5f");
            barras.get(i).setVisible(true);
            barras.get(i).setTranslateX( (jugadores.get(i).getTanque().getPos()[0]-(double)15)*altoScale);
            barras.get(i).setTranslateY( (jugadores.get(i).getTanque().getPos()[1])*anchoScale-10);
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
