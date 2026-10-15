package juego;

import java.util.Random;
import java.awt.Color;
import entorno.Entorno;

public class Enemigos {

	
	private double x,y;
	private double velocidad;
	private int direccion;

	public void Movrandom() {
		Random random = new Random();
		direccion = random.nextInt(4);
		
	}
	
	//Funcion asignar valores segun su direccion 
	public void mover() {
		
		//Si es 0, su direccion es izquierda
		if(direccion == 0){
			this.x -= velocidad;}
		
		//Si es 1, su direccion es derecha
		if(direccion == 1){
			this.x += velocidad;}
		
		//Si es 2, su direccion es abajo
		if(direccion == 2) {
			this.y -= velocidad;}
		
		//Si es 3, su direccion es arriba
		if(direccion == 3) {
			this.y += velocidad;}
		
		
	} 		//Limite entre los laterales del movimiento del enemigo
	public void rebotarEnBordesLaterales(int limiteizquierdo, int limitederecho) {
		
		//Condicion que si choca con el borde izquierdo cambiara de direccion a la derecha
		if(this.x <= limiteizquierdo && direccion == 0){ 
			direccion = 1;
			}
		
		//Condicion que si choca con el borde derecho cambiara de direccion a la izquierda
		if(this.x >= limitederecho && direccion == 1){
			direccion = 0;
		}
		
	}		//Limite entre los bordes superior e inferior del movimiento del enemigo
	public void rebotarEnBordesSuperiorInferior(int limitesuperior, int limiteinferior){
		
		//Condicion que si choca con el borde inferior cambiara de direccion al borde superior
		if(this.y <= limiteinferior && direccion == 2) {
			direccion = 3;
			
		//Condicion que si choca con el borde superior cambiara de direccion al borde inferior
		if(this.y >= limitesuperior && direccion == 3) {
			direccion = 2;
		}
		
	}
	}
	
	public Enemigos(double x, double y, double velocidad) {
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		this.Movrandom();
	}
	public void dibujar(Entorno entorno) {
	    entorno.dibujarRectangulo(this.x, this.y, 30, 30, 0, Color.RED);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
