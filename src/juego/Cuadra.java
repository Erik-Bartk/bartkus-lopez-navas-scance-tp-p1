package juego;
	import java.awt.Color;
	import java.util.Random;
	import entorno.Entorno;

	public class Cuadra{
	    private int x, y;
	    private int ancho, alto;
	    private boolean[] esNegocio; 

	    public Cuadra(int x, int y, int ancho, int alto, Random rand) {
	        this.x = x;
	        this.y = y;
	        this.ancho = ancho;
	        this.alto = alto;
	       
	        this.esNegocio = new boolean[4]; 
	        int posNegocio = rand.nextInt(4); 
	        this.esNegocio[posNegocio] = true;
	    }

	    public void dibujar(Entorno entorno) {
	        
	        entorno.dibujarRectangulo(x, y, ancho, alto, 0, Color.GREEN.darker());

	        
	        int subAncho = (ancho / 2) - 10;
	        int subAlto = (alto / 2) - 10;

	        
	        int[] offsetX = {-ancho/4, ancho/4, -ancho/4, ancho/4};
	        int[] offsetY = {-alto/4, -alto/4, alto/4, alto/4};

	        for (int i = 0; i < 4; i++) {
	            int cx = x + offsetX[i];
	            int cy = y + offsetY[i];

	            if (esNegocio[i]) {
	                entorno.dibujarRectangulo(cx, cy, subAncho, subAlto, 0, Color.BLUE); 
	            } else {
	                entorno.dibujarRectangulo(cx, cy, subAncho, subAlto, 0, Color.ORANGE); 
	            }
	        }
	    }

	    public int getX() { return x; }
	    public int getY() { return y; }
	    public int getAncho() { return ancho; }
	    public int getAlto() { return alto; }
	}


