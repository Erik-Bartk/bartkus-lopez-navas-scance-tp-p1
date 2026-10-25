package juego;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    private Entorno entorno;
    private MapaCiudad mapa;
    private HUD hud;
    private Repartidor rapi;
    private boolean enGameOver;
    private boolean enVictoria;
    private int contadorBombas = 0;
    private int tiempoInicio;
    
    // contador de pedidios
    private int pedidosEntregados;
    
    // Control de estado para el menú de inicio
    private boolean enMenu;
    
    // Listas para manejar múltiples enemigos y bombas
    private List<Enemigos> listaEnemigos;
    private List<Bomba> listaBombas;
    
    private Proyectil[] proyectiles;
    
    private int ticks;
    private int cooldownDaño = 0; // Control para no restar vidas de golpe

    int horas;
    int minutos;
    int segundos;
    String miFont= "Arial";
    
    public Juego() {
        this.entorno = new Entorno(this, "Repartidor - TP P1", 800, 700);
        
        // El juego arranca en el menú de inicio
        this.enMenu = true;
        this.enVictoria=false;
        this.pedidosEntregados = 0;
        this.enGameOver = false;
       
        // Inicializa la clase jugador
        this.rapi = new Repartidor(750, 400, 20, 20, 3.5, 5);
      
        // Inicializa el Proyectil cuando sea llamado en la función
        this.proyectiles = new Proyectil [1];
        
        // Inicializar clases del juego
        this.mapa = new MapaCiudad();
        this.hud = new HUD();
        this.ticks = 0;

        // Inicializar listas de entidades
        this.listaEnemigos = new ArrayList<>();
        this.listaBombas = new ArrayList<>();

        // Creamos algunos enemigos de prueba en distintas posiciones
        this.listaEnemigos.add(new Enemigos(200, 220, 1.5, 90));
        this.listaEnemigos.add(new Enemigos(360, 380, 1.5, 140));
        this.listaEnemigos.add(new Enemigos(520, 540, 1.5, 190));
        this.listaEnemigos.add(new Enemigos(360, 220, 1.5, 240));
        // Creamos una bomba de prueba
        this.listaBombas.add(new Bomba(300, 300));

        // Iniciar el juego
        this.entorno.iniciar();
    }

    @Override
    public void tick() {
        // --- ESTADO_ MENÚ DE INICIO ---
        if (enMenu) {
            dibujarPantallaInicio();
            
            // Al presionar ENTER, salimos del menú y arranca el juego
            if (entorno.sePresiono(entorno.TECLA_ENTER)) {
                enMenu = false;
                tiempoInicio = entorno.tiempo();
            }
            return; // Cortamos el tick para que no ejecute la lógica del juego de fondo
        }

        if (enGameOver) {
            dibujarPantallaGameOver();
            
            
            if (entorno.sePresiono('r') || entorno.sePresiono('R')) {
                reiniciarJuego();
            }
            return;
        }
        // condicion de victoria
        if (enVictoria) {
            dibujarPantallaVictoria();
            if (entorno.sePresiono('s') || entorno.sePresiono('S')) {
                reiniciarJuego();
            }
            return;
        }
       
        if (this.rapi.getVida() <= 0) {
            enGameOver = true;
            return;
        }
       // condicion 
        if (this.pedidosEntregados >= 3) {
            enVictoria = true;
            return;
        }
        
       
        if (cooldownDaño > 0) {
            cooldownDaño--;
        }

        // Movimiento del jugador con el teclado
        // Movimiento superior del repartidor
        if (entorno.estaPresionada('w')|| entorno.estaPresionada('W')) { 
            this.rapi.MoverseArriba();
            
            for (Cuadra cuadras : mapa.getCuadras()) {
                if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), cuadras.getX(), cuadras.getY(), cuadras.getAncho(), cuadras.getAlto())) {
                    rapi.MoverseAbajo(); // Deshace yendo abajo
                }
            }    
        }
        
        //Movimiento inferior del repartidor
        if (entorno.estaPresionada('s') || entorno.estaPresionada('s')) {
            this.rapi.MoverseAbajo();
            
            for (Cuadra cuadras : mapa.getCuadras()) {
                if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), cuadras.getX(), cuadras.getY(), cuadras.getAncho(), cuadras.getAlto())) {
                    rapi.MoverseArriba(); // Deshace yendo arriba
                }
            }
        }
              
        //Movimiento derecho del repartidor
        if (entorno.estaPresionada('d') || entorno.estaPresionada('d')) {
            this.rapi.MoverseDerecha();
            
            for (Cuadra cuadras : mapa.getCuadras()) {
                if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), cuadras.getX(), cuadras.getY(), cuadras.getAncho(), cuadras.getAlto())) {
                    rapi.MoverseIzquierda(); // Deshace yendo a la izquierda
                }
            }
        }
        
        //Movimiento izquierdo del repartidor
        if (entorno.estaPresionada('a') || entorno.estaPresionada('a')) {
            this.rapi.MoverseIzquierda();
            
            for (Cuadra cuadras : mapa.getCuadras()) {
                if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), cuadras.getX(), cuadras.getY(), cuadras.getAncho(), cuadras.getAlto())) {
                    rapi.MoverseDerecha(); // Deshace yendo a la derecha
                }
            }
        }
        
        // Movimiento dentro del mapa
        if (this.rapi.getX() < 0 + this.rapi.getAncho()/2) {
            this.rapi.x = 0 + this.rapi.getAncho()/2;
        }
        else if(this.rapi.getX() >= 800 - this.rapi.getAncho() /2) {
            this.rapi.x = 800 - this.rapi.getAncho() /2;    
        }
        
        if (this.rapi.getY() <= 50 + this.rapi.getAlto() /2 ) {
            this.rapi.y = 50 + this.rapi.getAlto() /2;
        }
        else if(this.rapi.getY() >= 700 - this.rapi.getAlto() /2) {
            this.rapi.y = 700 - this.rapi.getAlto() /2;
        }

     // Actualizamos el movimiento de los enemigos
        for (Enemigos e : listaEnemigos) {
            e.mover();
      
            // Comprobamos si este enemigo debe colocar una bomba
            if (e.ColocarBombas()) {
                listaBombas.add(new Bomba(e.getX(), e.getY()));
            }
        }
            
            // --- COLISIÓN REPARTIDOR vs ENEMIGO ---
            for (Enemigos e : listaEnemigos) {
            if (cooldownDaño == 0 && hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(),
                                                  e.getX(), e.getY(), e.getAncho(), e.getAlto())) {
                rapi.setVida(rapi.getVida() - 1);
                cooldownDaño = 45; // Espera 45 ticks para volver a recibir daño
                System.out.println("¡Colisión con enemigo! Vidas restantes: " + rapi.getVida());
            }
            
        	
        // Actualizar el estado de las bombas
        for (Bomba b : listaBombas) {
            b.actualizar(mapa);
            b.dibujar(entorno);
            
            // --- COLISIÓN REPARTIDOR vs BOMBA ---
            if (b.isActiva() && cooldownDaño == 0 && hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(),
                                                                b.getX(), b.getY(), b.getAncho(), b.getAlto())) {
                rapi.setVida(rapi.getVida() - 1);
                b.desactivar(); 
                cooldownDaño = 45;
                System.out.println("¡Chocaste una bomba! Vidas restantes: " + rapi.getVida());
            }
         // COLISIÓN REPARTIDOR vs EXPLOSIÓN
            if (cooldownDaño == 0 &&
                b.alcanzaRepartidor(rapi.getX(), rapi.getY(),
                                    rapi.getAncho(), rapi.getAlto())) {

                rapi.setVida(rapi.getVida() - 1);
                cooldownDaño = 45;

                System.out.println(
                    "¡Te alcanzó una explosión! Vidas restantes: " + rapi.getVida()
                );
            }
        
        }
        
        	}
        // Llamar al dibujado general
        this.dibujar();
    }

    // Método auxiliar de colisión por rectángulos
    public static boolean hayColision(double x1, double y1, double ancho1, double alto1,
                                      double x2, double y2, double ancho2, double alto2) {
        boolean colisionX = Math.abs(x1 - x2) < (ancho1 + ancho2) / 2;
        boolean colisionY = Math.abs(y1 - y2) < (alto1 + alto2) / 2;
        return colisionX && colisionY;
    }

    public void dibujar() {
        this.ticks++;
        
        // 1. Dibujar el mapa de la ciudad
        if (this.mapa != null) {
            this.mapa.dibujar(this.entorno);
        }
        
        // 2. Dibujar enemigos
        for (Enemigos e : listaEnemigos) {
            e.dibujar(this.entorno);
        }

        // 3. Dibujar bombas
        for (Bomba b : listaBombas) {
            b.dibujar(this.entorno);
        }

        // 4. Dibujar al repartidor
        if (this.rapi != null) {
            this.rapi.dibujarRepartidor(entorno);
        }

        // 5. Dibujar la barra superior (HUD)
        if (this.hud != null) {
        	this.hud.actualizar(this.ticks, (int)this.rapi.getVida(), this.pedidosEntregados, 0, "Camino al negocio");
            this.hud.dibujar(this.entorno);
        }
        
        // 6. Dibuja el proyectil cuando se lo invoca
        if (this.proyectiles[0] == null && entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            String direccion = "DERECHA"; 
            
            if (entorno.estaPresionada('a') || entorno.estaPresionada('a'))
                direccion = "IZQUIERDA";
            else if (entorno.estaPresionada('w') || entorno.estaPresionada('w'))
                direccion = "ARRIBA";
            else if (entorno.estaPresionada('s') || entorno.estaPresionada('s'))
                direccion = "ABAJO";
         

            this.proyectiles[0] = new Proyectil(this.rapi.getX(), this.rapi.getY(), 10, 4, direccion);
        }
        
        	if (this.proyectiles[0] != null) {

                this.proyectiles[0].mover();

                boolean colision = false;
                
                // Recorremos los enemigos para comprobar si hubo un impacto
                for (Enemigos e : listaEnemigos) {

                	// Comprueba si hay impacto con el enemigo y recibe las coordeanadas
                    if (colision == false && hayColision(
                            this.proyectiles[0].getX(), this.proyectiles[0].getY(),  this.proyectiles[0].getRadio(), this.proyectiles[0].getRadio(),
                            e.getX(), e.getY(), e.getAncho(), e.getAlto())) {
                    	
                    	// El enemigo respawnea si comprueba la colision del proyectil y el enemigo
                        e.respawn();
                        this.proyectiles[0] = null;
                        colision = true;
                    }
                }
                
                // Si todavía existe el proyectil, lo dibujamos y controlamos sus límites
                if (this.proyectiles[0] != null) {
                	
                	
                    this.proyectiles[0].dibujarProyectil(entorno);
            
            // Control de límites de pantalla
            if (this.proyectiles[0].getX() > 800 || this.proyectiles[0].getX() < 0 || this.proyectiles[0].getY() > 700 || this.proyectiles[0].getY() < 50) {  
                this.proyectiles[0] = null; 
            }
        }
        	}
        
        // 7. Dibuja el TIEMPO DEL JUEGO en el HUD
        int tiempoInicial = 300000;
        int tiempoRestante = tiempoInicial - (entorno.tiempo() - tiempoInicio);
        
        // Condicion de game over en caso de finalizar el tiempo
        if (tiempoRestante < 0) {
            tiempoRestante = 0;
            enGameOver = true;
            
        }
        
        int sec = tiempoRestante / 1000;
        segundos = sec % 60;
        
        int min = sec / 60;
        minutos = min % 60;
        
        Color miColor = new Color (100, 100, 100);
        
        entorno.cambiarFont("OCR A Extended", 25, miColor, entorno.NEGRITA);
        entorno.escribirTexto("TIEMPO: "+ minutos +":"+ segundos, 5, 35);
    
        entorno.cambiarFont("OCR A Extended", 20, Color.RED, entorno.NEGRITA);
        entorno.escribirTexto("Vidas: " + rapi.getVida(), 650, 35);
    }

    // Método exclusivo para dibujar la pantalla de inicio
    private void dibujarPantallaInicio() {
        entorno.cambiarFont("OCR A Extended", 32, Color.WHITE, entorno.NEGRITA);
        entorno.escribirTexto("REPARTIDOR - TP P1", 200, 180);

        entorno.cambiarFont("OCR A Extended", 20, Color.LIGHT_GRAY, entorno.NEGRITA);
        entorno.escribirTexto("Integrantes del grupo:", 260, 260);
        entorno.escribirTexto("- Bartkus", 280, 300);
        entorno.escribirTexto("- López", 280, 330);
        entorno.escribirTexto("- Navas", 280, 360);
        entorno.escribirTexto("- Scance", 280, 390);

        entorno.cambiarFont("OCR A Extended", 22, Color.YELLOW, entorno.NEGRITA);
        entorno.escribirTexto("Presioná ENTER para comenzar", 200, 500);
    }
    private void dibujarPantallaGameOver() {
        entorno.cambiarFont("OCR A Extended", 40, Color.RED, entorno.NEGRITA);
        entorno.escribirTexto("GAME OVER", 280, 250);

        entorno.cambiarFont("OCR A Extended", 22, Color.WHITE, entorno.NEGRITA);
        entorno.escribirTexto("Te quedaste sin vidas", 270, 330);

        entorno.cambiarFont("OCR A Extended", 20, Color.YELLOW, entorno.NEGRITA);
        entorno.escribirTexto("Presioná 'R' para volver a intentar", 210, 430);
    }
    //PANTALLA DE VICTORIA
 // Pantalla de Victoria nueva
    private void dibujarPantallaVictoria() {
        entorno.cambiarFont("OCR A Extended", 40, Color.GREEN, entorno.NEGRITA);
        entorno.escribirTexto("¡VICTORIA!", 290, 250);

        entorno.cambiarFont("OCR A Extended", 22, Color.WHITE, entorno.NEGRITA);
        entorno.escribirTexto("¡Entregaste 3 pedidos a tiempo!", 220, 330);

        entorno.cambiarFont("OCR A Extended", 20, Color.YELLOW, entorno.NEGRITA);
        entorno.escribirTexto("Presioná 'J' para jugar de nuevo", 220, 430);
    }
    private void reiniciarJuego() {
        this.rapi = new Repartidor(750, 400, 20, 20, 3.5, 5);
        this.ticks = 0;
        this.pedidosEntregados = 0;
        this.enGameOver = false;
        this.enVictoria = false;
        tiempoInicio = entorno.tiempo();
    }
    
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}