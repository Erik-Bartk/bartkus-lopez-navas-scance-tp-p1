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
        
        int [] columnaNegocio = {2, 0, 3, 1};
        
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                int x = xInicio + c * (anchoCuadra + anchoCalle) + (anchoCuadra / 2);
                int y = yInicio + f * (altoCuadra + anchoCalle) + (altoCuadra / 2);
                
                //Compara la columna actual con la columna elegida para esa fila
                boolean tieneNegocio = (c == columnaNegocio[f]);
                cuadras.add(new Cuadra(x, y, anchoCuadra, altoCuadra, rand, tieneNegocio));
            }
        }
    }

    public void dibujar(Entorno entorno) {
        // Fondo de calles (Gris oscuro en toda la pantalla)
        entorno.dibujarRectangulo(400, 350, 800, 700, 0, Color.DARK_GRAY);
        

        // Dibujar las 16 cuadras y las veredas
        for (Cuadra cuadra : cuadras) {
        	entorno.dibujarRectangulo( cuadra.getX(), cuadra.getY(), anchoCuadra + 8, altoCuadra + 8, 0, new Color(190, 190, 180));
        }
        
        //Dibuja las lineas por la callle
        dibujarLineasCalle(entorno);
        
        //Dibuja los cruces peatonales
        dibujarCrucesPeatonales(entorno);
        
        //Dibuja las cuadras por arriba de las veredas
        for(Cuadra cuadra: cuadras) {
        	cuadra.dibujar(entorno);
        }
    }
    
    private void dibujarLineasCalle(Entorno entorno) {

        Color blanco = new Color(225, 225, 215);

        // Líneas discontinuas de las calles horizontales
        for (int y = 220; y <= 540; y += 160) {

            for (int x = 10; x < 790; x += 30) {

            	boolean cercaCruce =
            	        (x > 165 && x < 235)
            	        || (x > 325 && x < 395)
            	        || (x > 485 && x < 555);

                if (!cercaCruce) {
                    entorno.dibujarRectangulo(
                            x, y, 16, 2, 0, blanco);
                }
            }
        }

        // Líneas discontinuas de las calles verticales
        for (int x = 200; x <= 520; x += 160) {

            for (int y = 65; y < 690; y += 30) {

            	boolean cercaCruce =
            	        (y > 185 && y < 255)
            	        || (y > 345 && y < 415)
            	        || (y > 505 && y < 575);

                if (!cercaCruce) {
                    entorno.dibujarRectangulo(
                            x, y, 2, 16, 0, blanco);
                }
            }
        }
    }

    private void dibujarCrucesPeatonales(Entorno entorno) {

        Color blanco = new Color(240, 240, 235);

        // Intersecciones entre calles
        for (int x = 200; x <= 520; x += 160) {

            for (int y = 220; y <= 540; y += 160) {

                // Franjas horizontales
                for (int i = -12; i <= 12; i += 8) {

                    entorno.dibujarRectangulo(
                            x + i, y - 12,
                            4, 3, 0, blanco);

                    entorno.dibujarRectangulo(
                            x + i, y + 12,
                            4, 3, 0, blanco);
                }

                // Franjas verticales
                for (int i = -12; i <= 12; i += 8) {

                    entorno.dibujarRectangulo(
                            x - 12, y + i,
                            3, 4, 0, blanco);

                    entorno.dibujarRectangulo(
                            x + 12, y + i,
                            3, 4, 0, blanco);
                }
            }
        }
    }

    
    
    public List<Cuadra> getCuadras(){
    	return cuadras;
    }
    
    
}