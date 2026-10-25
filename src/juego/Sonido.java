
package juego;

// Importamos las clases que necesitamos para reproducir audio
import java.net.URL;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;

public class Sonido {

    // Guarda el sonido que estamos reproduciendo
    private Clip sonido;

    // Guarda el nombre del sonido actual
    private String nombreActual = null;

    public void reproducir(String nombreArchivo, boolean repetir) {

        // Si ya está sonando el mismo audio, no lo reproducimos otra vez
        if (nombreArchivo.equals(nombreActual)
                && sonido != null && sonido.isRunning()) {
            return;
        }

        // Detenemos el sonido anterior antes de reproducir otro
        detener();

        try {
            // Buscamos el archivo dentro de la carpeta sonidos
            URL ruta = getClass().getResource("/sonidos/" + nombreArchivo);

            // Si no encontramos el archivo, mostramos un mensaje
            if (ruta == null) {
                System.out.println("No se encontro el sonido: " + nombreArchivo);
                return;
            }

            //Abrimos el archivo de audio
            AudioInputStream audio = AudioSystem.getAudioInputStream(ruta);

            //Creamos el reproductor de sonido
            sonido = AudioSystem.getClip();

            //Cargamos el audio en el reproductor
            sonido.open(audio);

            //Cerramos el archivo de entrada después de cargarlo
            audio.close();

            //Guardamos el nombre del sonido que estamos reproduciendo
            nombreActual = nombreArchivo;

            //Si repetir es true, el sonido se reproduce en bucle
            if (repetir) {
                sonido.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                //Si es false, se reproduce una sola vez
                sonido.start();
            }

        } catch (Exception e) {
            // Si ocurre un error, mostramos un mensaje en la consola
            System.out.println("Error al reproducir: " + nombreArchivo);
            e.printStackTrace();
        }
    }

    public void detener() {

        // Comprobamos que exista un reproductor de sonido
        if (sonido != null) {

            // Detenemos la reproducción
            sonido.stop();

            // Liberamos los recursos utilizados por el audio
            sonido.close();

            // Indicamos que ya no tenemos un reproductor activo
            sonido = null;
        }

        // Indicamos que no hay ningún sonido seleccionado
        nombreActual = null;
    }
}

