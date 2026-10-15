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
    
    // Listas para manejar múltiples enemigos y bombas
    private List<Enemigos> listaEnemigos;
    private List<Bomba> listaBombas;
    
    private int ticks;
    private int cooldownDaño = 0; // Control para no restar vidas de golpe

    public Juego() {
        // Inicializar el Entorno de la UNGS
        this.entorno = new Entorno(this, "Repartidor - TP P1", 800, 700);
        
        // Inicializa la clase jugador
        this.rapi = new Repartidor(750, 400, 0, 0.08, 3.5, 5);
        
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
        // Reducir el cooldown de daño en cada tick si es mayor a 0
        if (cooldownDaño > 0) {
            cooldownDaño--;
        }

        // Movimiento del jugador con el teclado
        if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
            this.rapi.MoverseArriba();
        }
        if (entorno.estaPresionada(entorno.TECLA_ABAJO)) {
            this.rapi.MoverseAbajo();
        }
        if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
            this.rapi.MoverseDerecha();
        }
        if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
            this.rapi.MoverseIzquierda();
        }
        
        if (this.rapi != null) {
            this.rapi.moverDentroMarco();
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
                cooldownDaño = 45; // Espera 45 ticks (aprox. 3/4 de segundo) para volver a recibir daño
                System.out.println("¡Colisión con enemigo! Vidas restantes: " + rapi.getVida());
            }
        }

        // Actualizar el estado de las bombas
        for (Bomba b : listaBombas) {
            b.actualizar();
            
            // --- COLISIÓN REPARTIDOR vs BOMBA (si está activa) ---
            if (b.isActiva() && cooldownDaño == 0 && hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(),
                                                                b.getX(), b.getY(), b.getAncho(), b.getAlto())) {
                rapi.setVida(rapi.getVida() - 1);
                b.desactivar(); // Desactiva la bomba para que no baje vida infinitamente
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

        // 3. Dibujar bombas (si siguen activas)
        for (Bomba b : listaBombas) {
            b.dibujar(this.entorno);
        }

        // 4. Dibujar al repartidor
        if (this.rapi != null) {
            this.rapi.dibujarRepartidor(entorno);
        }

        // 5. Dibujar la barra superior (HUD) al final para que quede por encima de todo
        if (this.hud != null) {
            this.hud.actualizar(this.ticks, (int)this.rapi.getVida(), 0, 0, "Camino al negocio");
            this.hud.dibujar(this.entorno);
        }
    }

    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}