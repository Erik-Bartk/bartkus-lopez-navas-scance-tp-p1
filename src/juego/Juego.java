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
        }

        // Actualizar el estado de las bombas
        for (Bomba b : listaBombas) {
            b.actualizar();
        }

        // Llamar al dibujado general
        this.dibujar();
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