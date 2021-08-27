/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author basti
 */
public class Kriegspantzer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader =new FXMLLoader(getClass().getResource("IniciarJuegoView.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);

        IniciarJuegoViewController controller = loader.getController();

        String[] colors = {"Azul", "Verde", "Amarillo", "Rojo", "Morado", "Naranja"};

        controller.setBoxes(colors);
        stage.setResizable(false);
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
