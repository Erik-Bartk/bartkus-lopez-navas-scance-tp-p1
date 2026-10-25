package juego;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entorno.Entorno;
import entorno.InterfaceJuego;
import jdk.internal.org.jline.terminal.TerminalBuilder.SystemOutput;

public class Juego extends InterfaceJuego {
    private Entorno entorno;
    private MapaCiudad mapa;
    private HUD hud;
    private Repartidor rapi;
    private boolean enGameOver;
    private boolean enVictoria;
    private int contadorBombas = 0;

    private int tiempoInicio;

    private Pedido pedidos;
    private Cuadra cuadras;

    
    // contador de pedidios
    
    private int totalPedidosEntregados = 0; //incrementa el contador cuando se entrega un pedido
    private int tiempoEsperaNuevoPedido = 0; //el tiempo de espera para el proximo pedido
    
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
    
    private Sonido sonido = new Sonido();
    private boolean sonidoFinalReproducido = false;
    
    public Juego() {
        this.entorno = new Entorno(this, "Repartidor - TP P1", 800, 700);
        
        // El juego arranca en el menú de inicio
        this.enMenu = true;
        this.enVictoria=false;
//       this.totalPedidosEntregados = 0;
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

        //Creamos un pedido para el juego
        generarNuevoPedido();
        
        
        
        // Iniciar el juego
        this.entorno.iniciar();
    }
    
    
    //Metodos Auxiliares
    private void generarNuevoPedido() {
        Random rand = new Random(); 
        List<Cuadra> listaCuadras = this.mapa.getCuadras(); 

        // Elige dos cuadras al azar de la lista
        Cuadra cuadraNegocio = listaCuadras.get(rand.nextInt(listaCuadras.size())); 
        Cuadra cuadraCasa = listaCuadras.get(rand.nextInt(listaCuadras.size())); 

        // Variables para guardar las posiciones finales del pedido
        double xNegocio = 0;
        double yNegocio = 0;
        double xCasa = 0;
        double yCasa = 0;

        // Busca cual de las 4 casa es el negocio
        boolean[] posicionesNegocio = cuadraNegocio.getEsNegocio();
        for (int i = 0; i < 4; i++) {
            if (posicionesNegocio[i]) {
                
                // va preguntado si el negocio es la posicion 0,1,2,3 para guardar los valores de su alto/ancho para generar el pedido en el negocio
            	// se divide sobre 4 porque se calcula sobre toda la cuadra y la divide en 4 secciones para obtener los valores de las casas
                if (i == 0) { 
                	xNegocio = cuadraNegocio.getX() - cuadraNegocio.getAncho() / 4; 
                	yNegocio = cuadraNegocio.getY() - cuadraNegocio.getAlto() / 4; 
                }
                
                if (i == 1) { 
                	xNegocio = cuadraNegocio.getX() + cuadraNegocio.getAncho() / 4; 
                	yNegocio = cuadraNegocio.getY() - cuadraNegocio.getAlto() / 4; 
                }
                
                if (i == 2) { 
                	xNegocio = cuadraNegocio.getX() - cuadraNegocio.getAncho() / 4;
                	yNegocio = cuadraNegocio.getY() + cuadraNegocio.getAlto() / 4; 
                }
                
                if (i == 3) { 
                	xNegocio = cuadraNegocio.getX() + cuadraNegocio.getAncho() / 4; 
                	yNegocio = cuadraNegocio.getY() + cuadraNegocio.getAlto() / 4; 
                }
                
            }
        }
        
        //Busca una casa al azar en la cuadra elegida para la entrega
        boolean[] posicionesCasa = cuadraCasa.getEsNegocio();
        int indiceCasa = rand.nextInt(4); // Elegimos una de las 4 esquinas al azar
        
        // Si la esquina elegida resulta ser el negocio de esa cuadra, seguimos buscando otra
        while (posicionesCasa[indiceCasa] == true) {
            indiceCasa = rand.nextInt(4);  //asigna una esquina de una casa a otra cuadra al azar 
        }

        // Calculamos el centro de la casa elegida
        if (indiceCasa == 0) { 
        	xCasa = cuadraCasa.getX() - cuadraCasa.getAncho() / 4; 
        	yCasa = cuadraCasa.getY() - cuadraCasa.getAlto() / 4; 
        }
        
        if (indiceCasa == 1) { 
        	xCasa = cuadraCasa.getX() + cuadraCasa.getAncho() / 4; 
        	yCasa = cuadraCasa.getY() - cuadraCasa.getAlto() / 4; 
        }
        
        if (indiceCasa == 2) { 
        	xCasa = cuadraCasa.getX() - cuadraCasa.getAncho() / 4; 
        	yCasa = cuadraCasa.getY() + cuadraCasa.getAlto() / 4; 
        }
        
        if (indiceCasa == 3) { 
        	xCasa = cuadraCasa.getX() + cuadraCasa.getAncho() / 4; 
        	yCasa = cuadraCasa.getY() + cuadraCasa.getAlto() / 4; 
        }

        // Se crea el pedido con los puntos exactos obtenidos
        this.pedidos = new Pedido(xNegocio, yNegocio, xCasa, yCasa, 0); 
    } 
    
    
    // Método auxiliar de colisión por rectángulos
    public static boolean hayColision(double x1, double y1, double ancho1, double alto1,
                                      double x2, double y2, double ancho2, double alto2) {
        boolean colisionX = Math.abs(x1 - x2) < (ancho1 + ancho2) / 2;
        boolean colisionY = Math.abs(y1 - y2) < (alto1 + alto2) / 2;
        return colisionX && colisionY;
    }
    
 
    
    
    

    @Override
    public void tick() {
        // --- ESTADO_ MENÚ DE INICIO ---
        if (enMenu) {
            sonido.reproducir("sonido_de_menu.wav", true);
            dibujarPantallaInicio();
            
            // Al presionar ENTER, salimos del menú y arranca el juego
            if (entorno.sePresiono(entorno.TECLA_ENTER)) {
                enMenu = false;
                tiempoInicio = entorno.tiempo();
            }
            return; // Cortamos el tick para que no ejecute la lógica del juego de fondo
        }
 
        if (enGameOver) {
        
        	// Reproducimos la derrota una sola vez
        if (!sonidoFinalReproducido) {
                sonido.reproducir("sonido_de_derrota.wav", false);
                sonidoFinalReproducido = true;
            }
        
            dibujarPantallaGameOver();
            
            
            if (entorno.sePresiono('r') || entorno.sePresiono('R')) {
                reiniciarJuego();
            }
            return;
        }
        
        // Condicion de victoria
        if (enVictoria) {
        	
        if (!sonidoFinalReproducido) {
                sonido.reproducir("sonido_de_victoria.wav", false);
                sonidoFinalReproducido = true;
            }

            dibujarPantallaVictoria();
            if (entorno.sePresiono('s') || entorno.sePresiono('S')) {
                reiniciarJuego();
            }
            return;
        }
         
        // Musica de la partida
            sonido.reproducir("sonido_de_partida.wav", true);
        

        if (this.rapi.getVida() <= 0) {
            enGameOver = true;
            return;
        }
       // condicion 
        if (this.totalPedidosEntregados >= 3) {
            enVictoria = true;
            return;
        }
        
       
        if (cooldownDaño > 0) {
            cooldownDaño--;
        }

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
        if (this.pedidos != null) {

            // Si el pedido se entrego o se perdio se genera uno nuevo
            if (this.pedidos.entregado || this.pedidos.perdido) {
            		
            	this.tiempoEsperaNuevoPedido ++;	 // tiempo de esperar para el nuevo pedido
            	
            	
                if (this.tiempoEsperaNuevoPedido >= 90) {	// Cuando pasan 90 frames recién ahí generamos el nuevo
                    generarNuevoPedido(); 
                    this.tiempoEsperaNuevoPedido = 0; 	// Reiniciamos el tiempo para el próximo pedido
                }
              
            }
            
            // si el jugador choca con un enemigo o bomba se pierde y tiene que volver a generarse otro pedido
            for (Enemigos e : listaEnemigos) {
                if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), e.getX(), e.getY(), e.getAncho(), e.getAlto())) {
                    this.pedidos.estaPerdido();
                    System.out.println("Un motochorro te robo y te quedaste sin el pedido :(");
                }    

            }
            
            //Para agarrar el pedido selecciona con el clik derecho del mouse
            if (entorno.sePresionoBoton(entorno.BOTON_DERECHO) && !this.pedidos.entregado && !this.pedidos.perdido) {
                
                //el estado del pedido esta en reposo hasta que el jugador interactue con el negocio con click derecho
                if (this.pedidos.esperando) {
                	if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), pedidos.xNegocio, pedidos.yNegocio, 80, 80)) {
                        
                    	this.pedidos.agarrarPedido();
                        System.out.println("Agarraste el pedido, entregalo o comelo en el camino como quieras.");
                    }
                }
                
                // El pedido ya está en camino y hace clic sobre la casa de destino
                else 
                	if (this.pedidos.camino) {
                    if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), pedidos.xCasa, pedidos.yCasa, 80, 80)) {
                       
                    	this.pedidos.entregarPedido(); 
                    	System.out.println("Felicidades entregaste el pedido y no te lo robaron los hinchas de Nueva Chicago");
                    	
                        this.totalPedidosEntregados ++;
                    }
                }
            }
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
        	this.hud.actualizar(this.ticks, (int)this.rapi.getVida(), this.totalPedidosEntregados, 0, "Camino al negocio");
            this.hud.dibujar(this.entorno);
        }
        
        // 6. Dibuja el proyectil cuando se lo invoca/activa
        if (this.proyectiles[0] == null && entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
            
        	// AL HACER CLICK CON EL MOUSE REALIZA UN DISPARO HACIA LA DERECHA
        	String direccion = "DERECHA"; 
            
            // DEPENDIENDO DONDE SE ESTE MOVIENDO EL JUGADOR EL DISPARO VA HACIA SU DIRECCION
            if (entorno.estaPresionada('a')|| entorno.estaPresionada('a'))

                direccion = "IZQUIERDA";
            else if (entorno.estaPresionada('w') || entorno.estaPresionada('w'))
                direccion = "ARRIBA";
            else if (entorno.estaPresionada('s') || entorno.estaPresionada('s'))
                direccion = "ABAJO";

         
            // PARAMETROS PARA GENERAR EL PROYECTIL
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
        
        
        entorno.cambiarFont("OCR A Extended", 15, Color.WHITE, entorno.NEGRITA);
        entorno.escribirTexto("TIEMPO: "+ minutos +":"+ segundos, 5, 35);
    
        entorno.cambiarFont("OCR A Extended", 15, Color.RED, entorno.NEGRITA);
        entorno.escribirTexto("Vidas: " + rapi.getVida(), 700, 35);
        
        
        //8. Dibuja el pedido en pantalla
        if (this.pedidos != null) {
        	this.pedidos.dibujarEncargo(entorno);
        	this.pedidos.dibujarPedido(entorno);
        	
        	
        	entorno.cambiarFont("OCR A Extended", 15, Color.CYAN, entorno.NEGRITA);
        	entorno.escribirTexto("Pedidos Entregados: " + this.totalPedidosEntregados, 135, 35);
        	
        	
        	if (this.pedidos.perdido) {
        	        entorno.cambiarFont("OCR A Extended", 15, Color.RED, entorno.NEGRITA); 
        	        entorno.escribirTexto("Estado Pedido: PERDIDO", 350, 35);
        	}
        	
        	else if (this.pedidos.entregado) {
        	        entorno.cambiarFont("OCR A Extended", 15, Color.GREEN, entorno.NEGRITA); 
        	        entorno.escribirTexto("Estado Pedido: ENTREGADO", 350, 35);
        	} 
        	
        	else if (this.pedidos.camino) {
        	        entorno.cambiarFont("OCR A Extended", 15, Color.ORANGE, entorno.NEGRITA); 
        	        entorno.escribirTexto("Estado Pedido: EN CAMINO", 350, 35);
        	} 
        	
        	else if (this.pedidos.esperando) {
                entorno.cambiarFont("OCR A Extended", 15, Color.ORANGE, entorno.NEGRITA); 
                entorno.escribirTexto("Estado Pedido: ESPERANDO EN NEGOCIO", 350, 35);
            }
        	
        	}
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
        entorno.escribirTexto("Presioná 'S' para jugar de nuevo", 220, 430);
    }
    private void reiniciarJuego() {
        this.rapi = new Repartidor(750, 400, 20, 20, 3.5, 5);
        this.ticks = 0;
        this.totalPedidosEntregados = 0;
        this.enGameOver = false;
        this.enVictoria = false;
        sonidoFinalReproducido = false;
        tiempoInicio = entorno.tiempo();
    }
    
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}