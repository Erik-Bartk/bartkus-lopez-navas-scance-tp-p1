package juego;

import java.awt.Color;

import entorno.Entorno;

public class Pedido {

	double xNegocio;
	double yNegocio;
	double xCasa;
	double yCasa;
	boolean realizado;
	boolean esperando;
	boolean	camino;
	boolean	entregado;
	boolean perdido;
	double contadorPedidos;
	
	
	public Pedido(double xNegocio, double yNegocio,double xCasa ,double yCasa ,double contadorPedidos) {
		this.xNegocio = xNegocio;
		this.yNegocio= yNegocio;
		this.xCasa = xCasa;
		this.yCasa = yCasa;
		this.realizado = true;
		this.esperando = true;
		this.camino = false;
		this.entregado = false;
		this.perdido = false;
		this.contadorPedidos = 0;
	}



	public void dibujarEncargo(Entorno entorno) {
		entorno.dibujarRectangulo(xCasa, yCasa -55, 10, 40, 0, Color.WHITE);
		entorno.dibujarTriangulo(xCasa - 5, yCasa - 40, 30, 30, 500, Color.WHITE);
		entorno.dibujarRectangulo(xCasa, yCasa, 25, 25, 0, Color.WHITE);
	}
	
	
	public void dibujarPedido(Entorno entorno) {
		entorno.dibujarRectangulo(xNegocio, yNegocio -55, 10, 40, 0, Color.BLACK);
		entorno.dibujarTriangulo(xNegocio - 5, yNegocio - 40, 30, 30, 500, Color.BLACK);
		entorno.dibujarRectangulo(xNegocio, yNegocio, 25, 25, 0, Color.BLACK);
	}
	
	

	public void agarrarPedido() {
		if (this.esperando == true  && this.perdido == false) {
			this.esperando = false;
			this.camino = true;
		}
	}
	
	
	public void entregarPedido () {
		if (this.camino == true && this.perdido == false ){
			this.camino = false;
			this.entregado = true;
			this.contadorPedidos = this.contadorPedidos + 1;
		}
	}
	
	
	public void estaPerdido() {
		this.esperando = false;
		this.camino = false;
		this.perdido = true;
	}
	
	
	public void incrementarContador() {
		contadorPedidos++;
	}
	
	public double getContadorPedidos() {
    	return contadorPedidos;
    }
		
	public boolean Encamino() {
		return	this.camino;
	}
	
	
	public boolean estaEsperando() {
        return this.esperando;
    }

    public boolean esEntregado() {
        return this.entregado;
    }

    
    public boolean esPerdido() {
        return this.perdido;
    }

	
	public double getXNegocio() {
		return xNegocio;
	}


	public void setXNegocio(double xNegocio) {
		this.xNegocio = xNegocio;
	}



	public double getYNegocio() {
		return yNegocio;
	}



	public void setYNegocio(double yNegocio) {
		this.yNegocio = yNegocio;
	}



	public boolean isRealizado() {
		return realizado;
	}



	public double getxCasa() {
		return xCasa;
	}



	public void setxCasa(double xCasa) {
		this.xCasa = xCasa;
	}



	public double getyCasa() {
		return yCasa;
	}



	public void setyCasa(double yCasa) {
		this.yCasa = yCasa;
	}



	public void setRealizado(boolean realizado) {
		this.realizado = realizado;
	}



	public void setEsperando(boolean esperando) {
		this.esperando = esperando;
	}




	public void setCamino(boolean camino) {
		this.camino = camino;
	}




	public void setEntregado(boolean entregado) {
		this.entregado = entregado;
	}




	public void setPerdido(boolean perdido) {
		this.perdido = perdido;
	}
	
	
	
	
	
	
}
