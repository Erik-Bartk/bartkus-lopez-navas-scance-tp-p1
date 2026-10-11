package juego;

import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
    private Entorno entorno;
    private MapaCiudad mapa;
    private HUD hud;
    private Repartidor rapi;
    private int ticks;

    public Juego() {
        // Inicializar el Entorno de la UNGS
        this.entorno = new Entorno(this, "Repartidor - TP P1", 800, 700);
        
        
      //Inicializa la clase jugador
        this.rapi = new Repartidor(750, 400, 0, 0.08, 3.5, 5);
        
        
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
        
        
        //movimiento del jugador
        if (entorno.estaPresionada(entorno.TECLA_ARRIBA)){
        	this.rapi.MoverseArriba();
        }
        
        if (entorno.estaPresionada(entorno.TECLA_ABAJO)){
        	this.rapi.MoverseAbajo();
        }
        
        if (entorno.estaPresionada(entorno.TECLA_DERECHA)){
        	this.rapi.MoverseDerecha();
        }
        
        if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)){
        	this.rapi.MoverseIzquierda();
        }
        
        
        
        if(this.rapi != null) {
        	this.rapi.moverDentroMarco();
        }
        
        
 
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
        
        
        if (this.rapi != null) {
        	this.rapi.dibujarRepartidor(entorno);
        }
        
    }

    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}