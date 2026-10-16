package juego;

import java.awt.Color;

import entorno.Entorno;

public class Proyectil {
	
	double x;
	double y;
	double radio;
	double velocidad;
	boolean activo = false;
	
	

	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public double getRadio() {
		return radio;
	}


	public void setRadio(double radio) {
		this.radio = radio;
		
	}

	
	public double getVelocidad() {
		return velocidad;
	}


	public void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}

	
	
	public Proyectil (double x, double y, double radio, double velocidad) {
		
		this.x = x;
		this.y= y;
		this.radio = radio;
		this.velocidad = velocidad;
		
	}
	
	

	public void dibujarProyectil(Entorno entorno) {
		if (activo = true) {
			entorno.dibujarCirculo(x, y, radio, Color.GRAY);
		}
	}
	
	
	/*
	public void movimientoProyectil(Entorno entorno) {
	
		if (entorno.sePresiono(entorno.TECLA_DERECHA)) {
				x+= this.velocidad;
				if (x >= 800) {
					activo = false;
				}
				else {
					activo = true;
				}
		}
		
		if (entorno.sePresiono(entorno.TECLA_IZQUIERDA)) {
				x-= this.velocidad;
				if (x <= 0) {
					activo = false;
				}
				else {
					activo = true;
				}
		}
		
		if (entorno.sePresiono(entorno.TECLA_ARRIBA)) {
				y-= this.velocidad;
				if (y <= 0) {
					activo = false;
				}
				else {
					activo = true;
				}
		}
		
		if (entorno.sePresiono(entorno.TECLA_ABAJO)) {
				y+= this.velocidad; 
				if (y >= 700) {
					activo = false;
				}
				else {
					activo = true;
				}
		}
	}
	*/
	
	
	public boolean estaActivado() {
		return activo;
	}
	
	
		
}


	
