package juego;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import entorno.Entorno;

public class MapaCiudad {
    private int filas = 4;
    private int columnas = 4;
    
    private int anchoCuadra = 120;
    private int altoCuadra = 120;
    private int anchoCalle = 40;
    
    // Margen superior para no tapar con el HUD
    private int xInicio = 60;
    private int yInicio = 80;

    private List<Cuadra> cuadras;

    public MapaCiudad() {
        this.cuadras = new ArrayList<>();
        this.generarCuadras();
    }

    private void generarCuadras() {
        Random rand = new Random();
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                int x = xInicio + c * (anchoCuadra + anchoCalle) + (anchoCuadra / 2);
                int y = yInicio + f * (altoCuadra + anchoCalle) + (altoCuadra / 2);
                
                cuadras.add(new Cuadra(x, y, anchoCuadra, altoCuadra, rand));
            }
        }
    }

    public void dibujar(Entorno entorno) {
        // Fondo de calles (Gris oscuro en toda la pantalla)
        entorno.dibujarRectangulo(400, 350, 800, 700, 0, Color.DARK_GRAY);

        // Dibujar las 16 cuadras
        for (Cuadra cuadra : cuadras) {
            cuadra.dibujar(entorno);
        }
    }
}