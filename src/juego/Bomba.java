package juego;

public class Bomba {

	private double x;
	private double y;
	private int tiempo;
	private boolean activa;
	
	//Funcion de bomba
	public Bomba(double x, double y) {
		
		//Constructor de bomba
		this.x = x;
		this.y = y;
		this.tiempo = 4;
		this.activa = true;
	}
	//Funcion de actualizacion de la bomba enemiga
	public void actualizar() {
		
		//Condicion si el tiempo es mayor a 0, la bomba esta activa 
		if(tiempo > 0 && activa == true) {
			this.tiempo -= 1; //resta un segundo a la bomba antes de explotar
			
		//Condicion si el tiempo es igual a 0 y la bomba sigue activa	
		}if(tiempo == 0 && activa == true) {
			explotar(); //invocamos explotar
			this.activa = false; //Una vez que exploto pasa a false
			}
	}
	//Funcion explotar
	public void explotar() {
		System.out.println("La bomba exploto");
	
	}

}