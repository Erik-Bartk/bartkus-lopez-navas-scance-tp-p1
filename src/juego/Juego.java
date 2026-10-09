package juego;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    private Entorno entorno;
    private MapaCiudad mapa;
    private HUD hud;
    private int ticks;

    public Juego() {
        // Inicializar el Entorno de la UNGS
        this.entorno = new Entorno(this, "Repartidor - TP P1", 800, 700);
        
        // Inicializar clases del juego
        this.mapa = new MapaCiudad();
        this.hud = new HUD();
        this.ticks = 0;

        // Iniciar el juego
        this.entorno.iniciar();
    }

    @Override
    public void tick() {
        this.dibujar();
    }

    public void dibujar() {
        this.ticks++;
        
        // 1. Dibujar el mapa de la ciudad
        if (this.mapa != null) {
            this.mapa.dibujar(this.entorno);
        }
        
        // 2. Dibujar la barra superior (HUD)
        if (this.hud != null) {
            this.hud.actualizar(this.ticks, 3, 0, 0, "Camino al negocio");
            this.hud.dibujar(this.entorno);
        }
    }

    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}