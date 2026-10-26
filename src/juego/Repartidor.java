package juego;

//import para las imagenes
import java.awt.Image;
import java.awt.Color;

//import java.awt.image.BufferedImage;

//import javax.imageio.ImageIO;

import entorno.Entorno;
import entorno.Herramientas;



public class Repartidor {
	
	//segmento de posicion de jugador y velocidad
	double x;
	double y;
	
	//con figura es alto y ancho
	double ancho;
	double alto;
	double velocidad;
	

	//segmento de vidas
	int vida;
	boolean segundos_respawn;
	
	//Imagenes del repartidor segun sus angulos
	private static Image imagenFrente = Herramientas.cargarImagen("imagenes/Repartidor_inferior_sin_mochila sin fondo.png");
	private static Image imagenAtras = Herramientas.cargarImagen("imagenes/Repartidor_angulo_superior sin fondo.png");
	private static Image imagenIzquierda = Herramientas.cargarImagen("imagenes/Repartidor_angulo_izquierdo sin fondo.png");
	private static Image imagenDerecha = Herramientas.cargarImagen("imagenes/Repartidor_angulo_derecho sin fondo.png");
	private String direccion = "FRENTE";
	
	public double getAncho() {
		return ancho;
	}
	public double getAlto() {
		return alto;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public double getVelocidad() {
		return velocidad;
	}
	
	
	public Repartidor(double x, double y, double ancho, double alto, double velocidad, int vida) {
		
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.vida = vida;
	}	
	
	
	//dibujar el repartidor dentro de la pantalla
	public void dibujarRepartidor(Entorno entorno) {
		Image imagen = imagenFrente;
	
		if (direccion.equals("ARRIBA")) {
			imagen = imagenAtras;
		}
		
		if (direccion.equals("IZQUIERDA")) {
			imagen = imagenIzquierda;
		}
		
		if (direccion.equals("DERECHA")) {
			imagen = imagenDerecha;
		}
		
		entorno.dibujarImagen(imagen, x, y, 0, 0.17);
		
				
				
		//entorno.dibujarRectangulo(x, y, ancho, alto, 0, Color.BLUE);
	}
	
	
	
	//movimiento
	public void MoverseDerecha() {
		x += this.velocidad;
		//Muestra la imagen mirando hacia la derecha
		direccion = "DERECHA";
	}
	
	public void MoverseIzquierda() {
		x -= this.velocidad;
		//Muestra la imagen mirando hacia la izquierda
		direccion = "IZQUIERDA";
	}
	
	public void MoverseArriba() {
		y -= this.velocidad;
		//Muestra la imagen mirando hacia arriba
		direccion = "ARRIBA";
	}
	
	public void MoverseAbajo() {
		y += this.velocidad;
		//Muestra la imagen mirando hacia abajo
		direccion = "FRENTE";
	}
	
	
	
	//segmento de vidas
	public Repartidor(int vidasIniciales) {
		this.vida = vidasIniciales;
		this.segundos_respawn = false;
	}
	
	
	public void restarVidas() {
		if (this.vida > 0 && segundos_respawn == true) {
			this.vida --;
			this.segundos_respawn = false;
		}
	}
	
	
	public boolean estaMuerto() {
		return this.vida <= 0;
	}

	
	public int getVida() {
		return vida;
	}
	
	
	
	
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	

	
	public void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}
	public void setVida(int vida) {
		this.vida = vida;
	}
	public void setAncho(double ancho) {
		this.ancho = ancho;
	}
	public void setAlto(double alto) {
		this.alto = alto;
	}
	
	
}
