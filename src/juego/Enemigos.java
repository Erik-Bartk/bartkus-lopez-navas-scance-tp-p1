package juego;

public class Enemigos {

	
	private double x,y;
	private double velocidad;
	private boolean moviendoIzquierda_Derecha;
	private boolean moviendoArriba_Abajo;

	
	public void mover() {
		if(moviendoIzquierda_Derecha) {
			this.x -= velocidad;
			}
		else {
			this.x += velocidad;
		}
		
		if(moviendoArriba_Abajo) {
			this.y -= velocidad;
		}
		else {
			this.y += velocidad;
		}
	}
	public void rebotarEnBordesLaterales(int LimiteIzquierdo, int LimiteDerecho) {
		if(this.x <= LimiteIzquierdo || this.x >= LimiteDerecho){
			this.moviendoIzquierda_Derecha =! this.moviendoIzquierda_Derecha;
			}
	}
	public void rebotarEnBordesSuperiorInferior(int LimiteSuperior, int LimiteInferior){
		if(this.y <= LimiteInferior || this.y >= LimiteSuperior) {
			this.moviendoArriba_Abajo =! this.moviendoArriba_Abajo;
		
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
