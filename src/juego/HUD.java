package juego;

import java.awt.Color;
import entorno.Entorno;

public class HUD {
    private int tiempoTranscurrido;
    private int vidas;
    private int pedidosEntregados;
    private int enemigosEliminados;
    private String estadoPedido;

    public HUD() {
        this.tiempoTranscurrido = 0;
        this.vidas = 3;
        this.pedidosEntregados = 0;
        this.enemigosEliminados = 0;
        this.estadoPedido = "Buscando pedido";
    }

    public void actualizar(int tiempo, int vidas, int entregados, int enemigos, String estado) {
        this.tiempoTranscurrido = tiempo;
        this.vidas = vidas;
        this.pedidosEntregados = entregados;
        this.enemigosEliminados = enemigos;
        this.estadoPedido = estado;
    }

    public void dibujar(Entorno entorno) {
        // Fondo negro del panel superior
        entorno.dibujarRectangulo(400, 25, 800, 50, 0, Color.BLACK);

        
        // En Entorno UNGS se escribe el texto en las coordenadas X, Y
        entorno.escribirTexto("Tiempo: " + (tiempoTranscurrido / 100) + "s", 15, 20);
        entorno.escribirTexto("Vidas: " + vidas, 130, 20);
        entorno.escribirTexto("Entregados: " + pedidosEntregados, 210, 20);
        entorno.escribirTexto("Enemigos: " + enemigosEliminados, 340, 20);
        entorno.escribirTexto("Pedido: " + estadoPedido, 500, 20);
    
    }
}