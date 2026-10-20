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
        this.listaEnemigos.add(new Enemigos(200, 200, 1.5));
        this.listaEnemigos.add(new Enemigos(400, 300, 2.0));

        // Creamos una bomba de prueba
        this.listaBombas.add(new Bomba(300, 300));

        // Iniciar el juego
        this.entorno.iniciar();
    }

    @Override
    public void tick() {
        // --- ESTADO 1: MENÚ DE INICIO ---
        if (enMenu) {
            dibujarPantallaInicio();
            
            // Al presionar ENTER, salimos del menú y arranca el juego
            if (entorno.sePresiono(entorno.TECLA_ENTER)) {
                enMenu = false;
            }
            return; // Cortamos el tick para que no ejecute la lógica del juego de fondo
        }

        // --- ESTADO 2: JUEGO EN CURSO ---
        
        // Reducir el cooldown de daño en cada tick si es mayor a 0
        if (cooldownDaño > 0) {
            cooldownDaño--;
        }

        // Movimiento del jugador con el teclado
        if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
            this.rapi.MoverseArriba();
            
            for (Cuadra cuadras : mapa.getCuadras()) {
                if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), cuadras.getX(), cuadras.getY(), cuadras.getAncho(), cuadras.getAlto())) {
                    rapi.MoverseAbajo(); // Deshace yendo abajo
                }
            }    
        }
        
        if (entorno.estaPresionada(entorno.TECLA_ABAJO)) {
            this.rapi.MoverseAbajo();
            
            for (Cuadra cuadras : mapa.getCuadras()) {
                if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), cuadras.getX(), cuadras.getY(), cuadras.getAncho(), cuadras.getAlto())) {
                    rapi.MoverseArriba(); // Deshace yendo arriba
                }
            }
        }
                
        if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
            this.rapi.MoverseDerecha();
            
            for (Cuadra cuadras : mapa.getCuadras()) {
                if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), cuadras.getX(), cuadras.getY(), cuadras.getAncho(), cuadras.getAlto())) {
                    rapi.MoverseIzquierda(); // Deshace yendo a la izquierda
                }
            }
        }
        
        if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
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

        // Actualizar movimiento y rebotes de los enemigos
        for (Enemigos e : listaEnemigos) {
            e.mover();
            e.rebotarEnBordesLaterales(50, 750);
            e.rebotarEnBordesSuperiorInferior(80, 650);
            
            // --- COLISIÓN REPARTIDOR vs ENEMIGO ---
            if (cooldownDaño == 0 && hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(),
                                                  e.getX(), e.getY(), e.getAncho(), e.getAlto())) {
                rapi.setVida(rapi.getVida() - 1);
                cooldownDaño = 45; // Espera 45 ticks para volver a recibir daño
                System.out.println("¡Colisión con enemigo! Vidas restantes: " + rapi.getVida());
            }
        }

        // Actualizar el estado de las bombas
        for (Bomba b : listaBombas) {
            b.actualizar();
            
            // --- COLISIÓN REPARTIDOR vs BOMBA ---
            if (b.isActiva() && cooldownDaño == 0 && hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(),
                                                                b.getX(), b.getY(), b.getAncho(), b.getAlto())) {
                rapi.setVida(rapi.getVida() - 1);
                b.desactivar(); 
                cooldownDaño = 45;
                System.out.println("¡Chocaste una bomba! Vidas restantes: " + rapi.getVida());
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
            this.hud.actualizar(this.ticks, (int)this.rapi.getVida(), 0, 0, "Camino al negocio");
            this.hud.dibujar(this.entorno);
        }
        
        // 6. Dibuja el proyectil cuando se lo invoca
        if (this.proyectiles[0] == null && entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            String direccion = "DERECHA"; 
            
            if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA))
                direccion = "IZQUIERDA";
            else if (entorno.estaPresionada(entorno.TECLA_ARRIBA))
                direccion = "ARRIBA";
            else if (entorno.estaPresionada(entorno.TECLA_ABAJO))
                direccion = "ABAJO";

            this.proyectiles[0] = new Proyectil(this.rapi.getX(), this.rapi.getY(), 10, 4, direccion);
        }
        
        if (this.proyectiles[0] != null) {
            this.proyectiles[0].mover();             
            this.proyectiles[0].dibujarProyectil(entorno);
            
            // Control de límites de pantalla
            if (this.proyectiles[0].getX() > 800 || this.proyectiles[0].getX() < 0 || this.proyectiles[0].getY() > 700 || this.proyectiles[0].getY() < 50) {  
                this.proyectiles[0] = null; 
            }
        }
        
        // 7. Dibuja el TIEMPO DEL JUEGO en el HUD
        int tiempoInicial = 90000;
        int tiempoRestante = tiempoInicial - entorno.tiempo();
        
        if (tiempoRestante < 0) {
            tiempoRestante = 0;
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
    
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}