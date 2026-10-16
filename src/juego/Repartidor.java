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
	
	
	//con imagenes es angulo y escala
	/*
	double angulo;
	double escala;
	BufferedImage imagen;
	*/
	

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
	/*+
	public double getAngulo() {
		return angulo;
	}
	public double getEscala() {
		return escala;
	}
	*/
	
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
	
	
	
	public void disparar(double x, double y, Proyectil proyectiles) {
        proyectiles = (new Proyectil(x, y, 5, 0));

	} 
	
	/*
	public Repartidor(double x, double y, double angulo, double escala, double velocidad, double vida) {
		
		this.x = x;
		this.y = y;
		this.angulo = angulo;
		this.escala = escala;
		this.velocidad = velocidad;
		this.vida = vida;
		
		this.imagen = cargarImagen("/repartidor.png");
		
	}
	*/
	
	
	
	/*
	//cargar la imagen del jugador
	public BufferedImage cargarImagen(String imagen) {
		try {
			java.net.URL ruta= getClass().getResource(imagen);
			if (ruta == null) {
				System.out.println (imagen);
			return null;
		}
			return ImageIO.read(ruta);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	*/
	
	/*
	//dibujar la imagen cargada anteriormente
	public void dibujarRepartidor(Entorno entorno) {
		BufferedImage imagen = cargarImagen("repartidor.png");
		if (imagen != null) {
			entorno.dibujarImagen(imagen,this.x, this.y, this.angulo, this.escala);
		}
	}
	*/
	
	
	/*
	//obtener el ancho y el alto de la imagen
	public double getAnchoImagen() {
		if (this.imagen == null){
			return 0;
		}
		else {
			return imagen.getWidth()*this.escala;
		}
	}
	
	public double getAltoImagen() {
		if (this.imagen == null){
			return 0;
		}
		else {
			return imagen.getHeight()*this.escala;
		}
	}
	 */
	
	
	/*
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
	
	*/
	
	/*
	//moverse dentro del marco
	public void moverDentroMarco(){
		if (this.getX() < 10 + this.getAnchoImagen()/2) {
        	this.x = 10 + this.getAnchoImagen()/2;
        }
        
        else if(this.getX() > 785 + this.getAnchoImagen()/2) {
        	this.x = 785 - this.getAnchoImagen()/2;	
        }
        
        if (this.getY() < 65 + this.getAltoImagen()/2) {
        	this.y = 65 + this.getAltoImagen()/2;
        }
        
        else if(this.getY() > 690 + this.getAltoImagen()/2) {
        	this.y = 690 - this.getAltoImagen()/2;
        }
	}
	*/
	
	
	
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
	
	/*
	public void setAngulo(double angulo) {
		this.angulo = angulo;
	}
	public void setEscala(double escala) {
		this.escala = escala;
	}
	*/
	
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
