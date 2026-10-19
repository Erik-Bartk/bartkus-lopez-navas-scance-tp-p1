package juego;


import java.awt.Color;

//import java.awt.image.BufferedImage;

//import javax.imageio.ImageIO;

import entorno.Entorno;



public class Repartidor {
	
	//segmento de posicion de jugador y velocidad
	double x;
	double y;
	
	//con figura es alto y ancho
	double ancho;
	double alto;
	double velocidad;
	

	//segmento de vidas
	double vida;
	boolean segundos_respawn;
	
	
	
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
	
	
	public Repartidor(double x, double y, double ancho, double alto, double velocidad, double vida) {
		
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = velocidad;
		this.vida = vida;
	}	
	
	
	//dibujar el repartidor dentro de la pantalla
	public void dibujarRepartidor(Entorno entorno) {
		entorno.dibujarRectangulo(x, y, ancho, alto, 0, Color.BLUE);
	}
	
	
	
	//movimiento
	public void MoverseDerecha() {
		x += this.velocidad;
	}
	
	public void MoverseIzquierda() {
		x -= this.velocidad;
	}
	
	public void MoverseArriba() {
		y -= this.velocidad;
	}
	
	public void MoverseAbajo() {
		y += this.velocidad;
	}
	
	
	
	//segmento de vidas
	public Repartidor(double vidasIniciales) {
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

	
	public double getVida() {
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
	public void setVida(double vida) {
		this.vida = vida;
	}
	public void setAncho(double ancho) {
		this.ancho = ancho;
	}
	public void setAlto(double alto) {
		this.alto = alto;
	}
	
	
}
