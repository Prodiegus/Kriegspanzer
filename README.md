Proyecto: Kriegspanzer

Desarrolladores: Bastian Nuñez, Carlos Jara, Diego Fernández, Omar Muñoz.

Lenguaje: Java (JavaFXML)

Version: JMV-11


Acerca del programa:

    Este proyecto es un juego el cual simula una batalla entre
    tanques, la idea del juego es que, cada jugador tenga su tanque y
    pueda establecer parametros de lanzamiento tales como, velocidad
    de disparo y angulo de tiro, con estos datos el programa al
    recibir la orden de disparar simulara un disparo en movimiento
    parabolico, el objetivo del juego, es que, este disparo impacte en
    el tanque enemigo para asi darle la victoria al jugador que logro
    dar en el blanco.

Mapa:
    
    Para crear el mapa se uso una clase la cual no formara parte del
    proyecto final ya que su objetivo es ayudar a definir los espacios
    del mapa para serializar un objeto de tipo mapa, el cual si sera
    usado en el programa, pero, al jugador no se le dara la habilidad
    de editar el mapa debido que eso puede generar problemas en la
    ejecucion del juego, por lo que, al momento de generar versiones
    finales del proyecto estas estaran sin la opcion de editar el mapa
    y vendran con un mapa ya serializado por defecto.

Mensaje de Errores:

    - Error 011:

        Se produce por la complicacion al cargar la ventana del final
        del juego una causa mas comun a este error es que el archivo
        fxml este dañado o no exista, de todas maneras tambien se 
        puede deber a que el icono del juego fuera borrado o que algun
        dato multimedia se extravie.
    
    - Error 013:

        Se produce por la complicacion al cargar la ventana del juego
        una causa mas comun a este error es que el archivo fxml este
        dañado o no exista, de todas maneras tambien se puede deber a que
        el icono del juego fuera borrado o que algun dato multimedia se
        extravie.

    - Error 001:

        Se produce al momento de leer un mapa desde la memoria del 
        computador este error se puede producir debido a que la carpeta
        con mapa esta en una ubicacion erronea o a que el mapa no exista.

Compilacion:

    - Desde un ide:
    La manera mas facil de ejecutar el programa es con una jdk-11+ desde un IDE
    IDE testeados son Eclipse y Netbeans aunque tambien se puede usar vs-code
    Clase Main: 'Kriegspantzer.java'

     -Ejecutable .jar
	A la  hora de crear el .jar del programa el cual se guardara en 
	la direccion "Kriegspanzer\Kriegspantzer\dist", se deben introducir 
	la carpeta de "audio" que se encuentra (Kriegspanzer\Kriegspantzer) 
	y la carpeta de "Mapas"que se encuentra en (Kriegspanzer\Kriegspantzer).

     - Error "no se ha podido encontrar el mapa":
	Si a la hora de ejecutar el programa aparece este error se debe copiar 
	la carpeta "Mapas" encontrada en (Kriegspanzer) y se debe pegar en la capeta
	(Kriegspanzer\Kriegspantzer).

Ruta codigo fuente:

    /Kriegpantzer/src/
