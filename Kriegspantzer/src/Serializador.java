import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JOptionPane;

import javafx.scene.canvas.Canvas;

public class Serializador implements Serializable{

    //para serializar el objeto 
    public Mapa ingresarABD(Mapa mapa) throws IOException{
        FileOutputStream file = new FileOutputStream("Mapas/Mapa"+mapa.getId());
        ObjectOutputStream output = new ObjectOutputStream(file);
        if(output != null){
            output.writeObject(mapa);

            output.close();

        }  
        file.close();
        return mapa;
    }

    /**(@return Mapa mapa) para recuperar objetos serializados*/
    public Mapa cargarDataBase(int id) throws IOException{
        FileInputStream file;
        ObjectInputStream input;
        Mapa mapa = null;
        file = new FileInputStream("Mapas/Mapa"+id);
        input = new ObjectInputStream(file);
        try {
            mapa = (Mapa)input.readObject();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error: 001\nMapa no encontrado");
        }
        input.close();
        file.close();
        return mapa;
    }

    //este metodo simplemente borra
    public void borrar(String id) {
        File cliente = new File("Mapas/mapa"+id);
        if(!cliente.exists() || !cliente.delete()){
            System.err.println("Error al borrar de DataBase");
        }
    }
}
