package juego;

import java.awt.Image;
import java.util.Random;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class Enemigos {
    private double x, y;
    private double velocidad;
    private int direccion;
    private double ancho = 20;
    private double alto = 20;
    private int contadorBombas;
    private int tiempoInicial;
    private double XInicial;
    private double YInicial;
    private int contadorGiro = 0;
    

    //Imagenes del enemigo
    private static Image imagenFrente = Herramientas.cargarImagen("imagenes/Enemigo_angulo_Inferior.png");
    private static Image imagenAtras = Herramientas.cargarImagen("imagenes/Enemigo_angulo_superior.png");
    private static Image imagenIzquierda = Herramientas.cargarImagen("imagenes/Enemigo_angulo_izquierdo.png");
    private static Image imagenDerecha = Herramientas.cargarImagen("imagenes/Enemigo_angulo_derecho.png");
    private String direccion_enemiga = "FRENTE"; 
    
    
    private Random random = new Random();

     

    public Enemigos(double x, double y, double velocidad, int tiempoInicial) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.XInicial = x;
        this.YInicial = y;
        this.Movrandom();
        this.tiempoInicial = tiempoInicial;
        this.contadorBombas = 0;
        
    
        
    }
 
    

    // 0 izquierda, 1 derecha, 2 arriba, 3 abajo
    public void Movrandom() {
        direccion = random.nextInt(4);
    }
    

    // Elige una dirección distinta a la que llevaba
    public void elegirNuevaDireccion() {
        int direccionAnterior = direccion;
        while (direccion == direccionAnterior) {
            Movrandom();
        }
    }

    // Movimientos de los enemigos
    public void moverIzquierda() {
        x -= velocidad;
        direccion_enemiga = "IZQUIERDA";
    }

    public void moverDerecha() {
        x += velocidad;
        direccion_enemiga = "DERECHA";
    }

    public void moverArriba() {
        y -= velocidad;
        direccion_enemiga = "ARRIBA";
    }

    public void moverAbajo() {
        y += velocidad;
        direccion_enemiga = "FRENTE";
    }


	//Aplicamos la imagen de los enemigos segun su direccion
    public void dibujar(Entorno entorno) {
    	Image imagen = imagenFrente;
    	
		if (direccion_enemiga.equals("ARRIBA")) {
			imagen = imagenAtras;
		}
		
		if (direccion_enemiga.equals("IZQUIERDA")) {
			imagen = imagenIzquierda;
		}
		
		if (direccion_enemiga.equals("DERECHA")) {
			imagen = imagenDerecha;
		}
		
		entorno.dibujarImagen(imagen, x, y, 0, 0.10);
    }

    
    public boolean ColocarBombas() {
    	contadorBombas++;
    	
    	if(contadorBombas >= tiempoInicial) {
    		contadorBombas = 0;
    		
    		tiempoInicial = 300;
    		return true;
    	}
    		return false;
    }
    
    
    public void respawn() {
    	x = XInicial;
    	y = YInicial;
    	
    	Movrandom();
    	
    	contadorGiro = 0;
    }
    
    
    
      public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }
    
    public int getDireccion() {
        return direccion;
    }

    public void setDireccion(int direccion) {
        this.direccion = direccion;
    }

    public int getContadorGiro() {
        return contadorGiro;
    }

    public void setContadorGiro(int contadorGiro) {
        this.contadorGiro = contadorGiro;
    }
    
    
}