package juego;

public class Bomba {

	private double x;
	private double y;
	private int tiempo;
	private boolean activa;
	
	public Bomba(double x, double y) {
		this.x = x;
		this.y = y;
		this.tiempo = 4;
		this.activa = true ;
	}
	
	public void actualizar() {
		if(tiempo > 0 && activa == true) {
			this.tiempo -= 1;
			
		}if(tiempo == 0 && activa == true) {
			explotar();
			this.activa = false;
			}
	}
	public void explotar() {
		System.out.println("La bomba exploto");
	
	}

}