package juego;

import java.awt.Image;
import java.util.Random;
import java.awt.Color;
import entorno.Entorno;
import entorno.Herramientas;

public class Enemigos {
    private double x, y;
    private double velocidad;
    private int direccion;
    private double ancho = 20;
    private double alto = 20;
    private int contadorBombas;
    private int tiempoInicial;
    private double XInicial;
    private double YInicial;

    //Imagenes del enemigo
    private static Image imagenFrente = Herramientas.cargarImagen("imagenes/Enemigo_angulo_Inferior.png");
    private static Image imagenAtras = Herramientas.cargarImagen("imagenes/Enemigo_angulo_superior.png");
    private static Image imagenIzquierda = Herramientas.cargarImagen("imagenes/Enemigo_angulo_izquierdo.png");
    private static Image imagenDerecha = Herramientas.cargarImagen("imagenes/Enemigo_angulo_derecho.png");
    private String direccion_enemiga = "FRENTE"; 
    
    
    private Random random = new Random();

    // Guarda la última intersección donde eligió dirección
    private double ultimaX = -1;
    private double ultimaY = -1;
     

    public Enemigos(double x, double y, double velocidad, int tiempoInicial) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.XInicial = x;
        this.YInicial = y;
        this.Movrandom();
        this.tiempoInicial = tiempoInicial;
        this.contadorBombas = 0;
        
    
        
    }

    // 0 izquierda, 1 derecha, 2 arriba, 3 abajo
    public void Movrandom() {
        direccion = random.nextInt(4);
    }

    public void mover() {

        // Movimiento según la dirección elegida
        if (direccion == 0) {
            x -= velocidad;
            direccion_enemiga = "IZQUIERDA";
            
        }

        if (direccion == 1) {
            x += velocidad;
            direccion_enemiga = "DERECHA";
        }

        if (direccion == 2) {
            y -= velocidad;
            direccion_enemiga = "ARRIBA";
        }

        if (direccion == 3) {
            y += velocidad;
            direccion_enemiga = "FRENTE";
        }
        
        controlarBordes();

        // Comprobar si llegó a una intersección
        elegirDireccionEnInterseccion();
    }

    private void elegirDireccionEnInterseccion() {
        double centroX = calleVerticalCercana();
        double centroY = calleHorizontalCercana();

        if (centroX == -1 || centroY == -1) {
            ultimaX = -1;
            ultimaY = -1;
            return;
        }

        if (centroX == ultimaX && centroY == ultimaY) {
            return;
        }

        // Ubicamos al enemigo en el centro de la intersección
        x = centroX;
        y = centroY;

        int direccionAnterior = direccion;

        // Elegimos una dirección aleatoria
        Movrandom();

        // Evitamos que retroceda inmediatamente
        while (direccion == direccionAnterior) {
            Movrandom();
        }

        ultimaX = centroX;
        ultimaY = centroY;
    }

    // Centros de las calles verticales interiores
    private double calleVerticalCercana() {

        if (Math.abs(x - 200) <= velocidad) {
            return 200;
        }

        if (Math.abs(x - 360) <= velocidad) {
            return 360;
        }

        if (Math.abs(x - 520) <= velocidad) {
            return 520;
        }

        return -1;
    }

    // Centros de las calles horizontales interiores
    private double calleHorizontalCercana() {

        if (Math.abs(y - 220) <= velocidad) {
            return 220;
        }

        if (Math.abs(y - 380) <= velocidad) {
            return 380;
        }

        if (Math.abs(y - 540) <= velocidad) {
            return 540;
        }

        return -1;
    }
    

public void controlarBordes() {

    // Borde izquierdo
    if (x <= 40 && direccion == 0) {
        direccion = 1;
    }

    // Borde derecho
    if (x >= 760 && direccion == 1) {
        direccion = 0;
    }

    // Borde superior
    if (y <= 100 && direccion == 2) {
        direccion = 3;
    }

    // Borde inferior
    if (y >= 660 && direccion == 3) {
        direccion = 2;
    }
}

	
    public void dibujar(Entorno entorno) {
    	//Aplicamos la imagen de los enemigos segun su direccion
    	Image imagen = imagenFrente;
    	
		if (direccion_enemiga.equals("ARRIBA")) {
			imagen = imagenAtras;
		}
		
		if (direccion_enemiga.equals("IZQUIERDA")) {
			imagen = imagenIzquierda;
		}
		
		if (direccion_enemiga.equals("DERECHA")) {
			imagen = imagenDerecha;
		}
		
		entorno.dibujarImagen(imagen, x, y, 0, 0.10);
    }

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
    
    public boolean ColocarBombas() {
    	contadorBombas++;
    	
    	if(contadorBombas >= tiempoInicial) {
    		contadorBombas = 0;
    		
    		tiempoInicial = 180;
    		return true;
    	}
    		return false;
    }
    
    public void respawn() {
    	x = XInicial;
    	y = YInicial;
    	
    	Movrandom();
    	
    	ultimaX = -2;
    	ultimaY = -2;
    }
}