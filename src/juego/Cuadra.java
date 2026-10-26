package juego;
	import java.awt.Color;
	import java.util.Random;
	import entorno.Entorno;
	import java.awt.Image;
	import entorno.Herramientas;

	
	public class Cuadra{
	    private int x, y;
	    private int ancho, alto;
	    private boolean[] esNegocio; 

	    public Cuadra(int x, int y, int ancho, int alto, Random rand, boolean tieneNegocio) {
	        this.x = x;
	        this.y = y;
	        this.ancho = ancho;
	        this.alto = alto;
	       
	        this.esNegocio = new boolean[4];

	        // Elegimos una de las cuatro posiciones para el negocio
	        int posNegocio = rand.nextInt(4);

	        // Asignamos un negocio y tres casas
	        for (int i = 0; i < 4; i++) {
	            this.esNegocio[i] = (i == posNegocio);
	        }
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
	            	// Negocio en la tercera manzana de la primera fila
	                entorno.dibujarImagen(casa, cx, cy, 0, 0.5);
	            } else {
	                entorno.dibujarImagen(casa2, cx, cy, 0, 0.5);
	            }
	            
	           
	            
	        }
	    }
	    
	    public int getX() { return x; }
	    public int getY() { return y; }
	    public int getAncho() { return ancho; }
	    public int getAlto() { return alto; }

	    
	    private static Image casa =
	    	    Herramientas.cargarImagen("imagenes/casa.png");

	    	private static Image casa2 =
	    	    Herramientas.cargarImagen("imagenes/casa2.png");
	   

	    
	    
		public boolean[] getEsNegocio() {
			return esNegocio;
		}

		public void setEsNegocio(boolean[] esNegocio) {
			this.esNegocio = esNegocio;
		}
	    
	    
	  
	    
	}
	



