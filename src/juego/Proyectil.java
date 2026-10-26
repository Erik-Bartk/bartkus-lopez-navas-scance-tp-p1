package juego;

import java.awt.Color;

import entorno.Entorno;

public class Proyectil {
	
	double x;
	double y;
	double radio;
	double velocidad;
	String direccion;
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


	public double setRadio() {
		return radio;
		
	}

	
	public double getVelocidad() {
		return velocidad;
	}


	public double setVelocidad() {
		return velocidad;
	}

	
	
	public Proyectil (double x, double y, double radio, double velocidad, String direccion) {
		
		this.x = x;
		this.y= y;
		this.radio = radio;
		this.velocidad = velocidad;
		this.direccion = direccion;
	}
	
	

	public void dibujarProyectil(Entorno entorno) {
		entorno.dibujarCirculo(x, y, radio, Color.GRAY);
	}

	
	
	public void mover() {
		 
	    if (this.direccion.equals("DERECHA")) {   
	    	this.x += velocidad;
	    }
	    if (this.direccion.equals("IZQUIERDA")) {
	    	this.x -= velocidad;
	    }
	    if (this.direccion.equals("ARRIBA")) {
	    	this.y -= velocidad;
	    }
	    if (this.direccion.equals("ABAJO")) {
	    	this.y += velocidad;
	    }
	}
	 
	 
	
	public boolean estaActivado() {
		return activo = true;
	}
	
	
		
}


	
