package juego;

import java.awt.Color;
import entorno.Entorno;

public class Bomba {
	
//<<<<<<< Updated upstream
    private double x;
    private double y;
    private int tiempo;
    private boolean activa;
    private double ancho = 20;
    private double alto = 20;
    private double radioExplosion = 20;
    private boolean explotada;
    private int tiempoExplosion = 100;
    private double alcanceExplosion = 50;
    private double alcanceArriba;
    private double alcanceAbajo;
    private double alcanceIzquierda;
    private double alcanceDerecha;
    private int contadorBombas = 0;
    

     
    // Función de bomba
    public Bomba(double x, double y) {
        this.x = x;
        this.y = y;
        this.tiempo = 550;
        this.activa = true;
        this.explotada = false;
    }

    // Función de actualización de la bomba enemiga
    public void actualizar(MapaCiudad mapa) {
        // Condición si el tiempo es mayor a 0, la bomba está activa 
        if (tiempo > 0 && activa == true) {
            this.tiempo -= 1; // resta un segundo a la bomba antes de explotar
        }
        
        // Condición si el tiempo es igual a 0 y la bomba sigue activa    
        if (tiempo == 0 && activa == true) {
            explotar(mapa); // invocamos explotar
            this.activa = false; // Una vez que explotó pasa a false
        }
        
        else if (explotada) {
            tiempoExplosion--;

            if (tiempoExplosion <= 0) {
                explotada = false;
            }
        }
    }
    
    public boolean ColocarBombas() {
    	contadorBombas++;
    	
    	if(contadorBombas < 180) {
    		contadorBombas = 0;
    		return true;
    	}
    		return false;
    }

    // Función explotar
    public void explotar(MapaCiudad mapa) {
    	calcularAlcances(mapa);
    	
    	this.activa = false;
    	this.explotada = true;
        System.out.println("La bomba explotó");
    }

    public void dibujar(Entorno entorno) {
        if (this.activa) {
            entorno.dibujarCirculo(this.x, this.y, this.ancho, Color.MAGENTA);
        }
    
      //Si la bomba ya exploto 
        if(explotada) {
        	
        	//Explosion hacia arriba
        	entorno.dibujarRectangulo(this.x, this.y - alcanceArriba / 2, 15, alcanceArriba, 0, Color.ORANGE);
        
        	//Explosion hacia abajo
        	entorno.dibujarRectangulo(this.x, this.y + alcanceAbajo / 2, 15, alcanceAbajo, 0, Color.ORANGE);
        	
        	//Explosion hacia la izquierda
        	entorno.dibujarRectangulo(this.x - alcanceIzquierda / 2, y, alcanceIzquierda, 15, 0, Color.ORANGE);
        	
        	//Explosion hacia la derecha
        	entorno.dibujarRectangulo(this.x + alcanceDerecha / 2, y, alcanceDerecha, 15, 0, Color.ORANGE);
        
        	}
      }
    
    public boolean estaEnRango(double enemigoX, double enemigoY) {
    	double distanciaX = enemigoX - this.x;
    	double distanciaY = enemigoY - this.y;
    	
    	double distancia = Math.sqrt(
    			distanciaX * distanciaX + distanciaY * distanciaY);
    	
    	return explotada && distancia <= radioExplosion;
   }
    
public boolean alcanzaRepartidor(double jugadorX, double jugadorY, double jugadorAncho, double jugadorAlto){
    	
    	if(explotada == false) {
    		return false;
    	}
    	
    	//Rectangulo superior de la bomba en explosion 
    	boolean arriba =
    			jugadorX > x - (jugadorAncho + 12) / 2 && jugadorX < x + (jugadorAncho + 12) / 2 &&
    			jugadorY > y - alcanceArriba - jugadorAlto / 2 && jugadorY < y + jugadorAlto / 2;
    			
    	
    	//Rectangulo inferior de la bomba en explosion 	
    	boolean abajo =
    		    jugadorX > x - (jugadorAncho + 12) / 2 && jugadorX < x + (jugadorAncho + 12) / 2 &&
    		    jugadorY > y - jugadorAlto / 2 && jugadorY < y + alcanceAbajo + jugadorAlto / 2;
    		    
    
    	//Rectangulo izquierdo de la bomba en explosion    
    	boolean izquierda =
    		    jugadorX > x - alcanceIzquierda - jugadorAncho / 2 && jugadorX < x + jugadorAncho / 2 &&
    		    jugadorY > y - (jugadorAlto + 12) / 2 && jugadorY < y + (jugadorAlto + 12) / 2;
    		    
    	
    	//Rectangulo derecho de la bomba en explosion 
    	boolean derecha =
    		    jugadorX > x - jugadorAncho / 2 && jugadorX < x + alcanceDerecha + jugadorAncho / 2 &&
    		    jugadorY > y - (jugadorAlto + 12) / 2 && jugadorY < y + (jugadorAlto + 12) / 2;

    		        return arriba || abajo || izquierda || derecha;
    		    }
    

public void calcularAlcances(MapaCiudad mapa) {
    alcanceArriba = alcanceExplosion;
    alcanceAbajo = alcanceExplosion;
    alcanceIzquierda = alcanceExplosion;
    alcanceDerecha = alcanceExplosion;

    for (Cuadra c : mapa.getCuadras()) {
        double izquierda = c.getX() - c.getAncho() / 2.0;
        double derecha = c.getX() + c.getAncho() / 2.0;
        double arriba = c.getY() - c.getAlto() / 2.0;
        double abajo = c.getY() + c.getAlto() / 2.0;

        // Explosión hacia la derecha
        if (y >= arriba && y <= abajo && izquierda >= x) {
            double distancia = izquierda - x;

            if (distancia < alcanceDerecha) {
                alcanceDerecha = distancia;
            }
        }

        // Explosión hacia la izquierda
        if (y >= arriba && y <= abajo && derecha <= x) {
            double distancia = x - derecha;

            if (distancia < alcanceIzquierda) {
                alcanceIzquierda = distancia;
            }
        }

        // Explosión hacia abajo
        if (x >= izquierda && x <= derecha && arriba >= y) {
            double distancia = arriba - y;

            if (distancia < alcanceAbajo) {
                alcanceAbajo = distancia;
            }
        }

        // Explosión hacia arriba
        if (x >= izquierda && x <= derecha && abajo <= y) {
            double distancia = y - abajo;

            if (distancia < alcanceArriba) {
                alcanceArriba = distancia;
            }
        }
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
        this.explotada = false;
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