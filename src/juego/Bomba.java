package juego;

import java.awt.Color;
import entorno.Entorno;

public class Bomba {
//<<<<<<< Updated upstream
    private double x;
    private double y;
    private int tiempo;
    private boolean activa;
    private double ancho = 30;
    private double alto = 30;

    // Función de bomba
    public Bomba(double x, double y) {
        this.x = x;
        this.y = y;
        this.tiempo = 4;
        this.activa = true;
    }

    // Función de actualización de la bomba enemiga
    public void actualizar() {
        // Condición si el tiempo es mayor a 0, la bomba está activa 
        if (tiempo > 0 && activa == true) {
            this.tiempo -= 1; // resta un segundo a la bomba antes de explotar
        }
        
        // Condición si el tiempo es igual a 0 y la bomba sigue activa    
        if (tiempo == 0 && activa == true) {
            explotar(); // invocamos explotar
            this.activa = false; // Una vez que explotó pasa a false
        }
    }

    // Función explotar
    public void explotar() {
        System.out.println("La bomba explotó");
    }

    public void dibujar(Entorno entorno) {
        if (this.activa) {
            entorno.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.MAGENTA);
        }
    }

    // Getters y Setters necesarios para el juego
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    public boolean isActiva() {
        return activa;
    }

    public void desactivar() {
        this.activa = false;
    }
    
/*
=======
	
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
	
>>>>>>> Stashed changes

*/
    
}