import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Kriegspantzer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader =new FXMLLoader(getClass().getResource("IniciarJuegoView.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);

        IniciarJuegoViewController controller = loader.getController();

        String[] colors = {"Azul", "Verde", "Amarillo", "Rojo", "Morado", "Naranja", "Negro"};

        controller.setBoxes(colors);
        controller.setMap();
        stage.setResizable(true);
        stage.setTitle("Kriegspanzer Game");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
}
