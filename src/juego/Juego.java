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
    
    private int totalPedidosEntregados = 0;  //incrementa el contador cuando se entrega un pedido
    private int tiempoEsperaNuevoPedido = 0;  //el tiempo de espera para el proximo pedido
    private int totalPedidosPerdidos = 0;  //incrementa el contador cuando se pierde un pedido
    
    
    // Control de estado para el menú de inicio
    private boolean enMenu;
    
    // Listas para manejar múltiples enemigos y bombas
    private Enemigos [] enemigos;
    private Bomba[] bombas;

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
        this.rapi = new Repartidor(750, 400, 20, 20, 3.5, 10);
      
        // Inicializa el Proyectil cuando sea llamado en la función
        this.proyectiles = new Proyectil [1];
        
        // Inicializar clases del juego
        this.mapa = new MapaCiudad();
        this.hud = new HUD();
        this.ticks = 0;
        
        // Inicializar listas de entidades
        this.enemigos = new Enemigos[4];
        this.bombas = new Bomba[100]; // Capacidad máxima simultánea de bombas

        // Inicializa enemigos en distintas posiciones
        this.enemigos[0] = new Enemigos(200, 220, 1.5, 90);
        this.enemigos[1] = new Enemigos(360, 380, 1.5, 140);
        this.enemigos[2] = new Enemigos(520, 540, 1.5, 190);
        this.enemigos[3] = new Enemigos(360, 220, 1.5, 240);

        
        //Creamos un pedido para el juego
        generarNuevoPedido();
        
       
        // Iniciar el juego
        this.entorno.iniciar();
    }
    
    
    
    //Metodos Auxiliares
    
    private void agregarBomba(Bomba nuevaBomba) {
        for (int i = 0; i < this.bombas.length; i++) {
        	if (this.bombas[i] == null || (!this.bombas[i].isActiva() && !this.bombas[i].isExplotada())) {
        	    this.bombas[i] = nuevaBomba;
        	    return;
        	}
        }
    }
    
    
    
    private void generarNuevoPedido() {
        Random rand = new Random(); 
        Cuadra[] cuadras = this.mapa.getCuadras(); 

        // Elige dos cuadras al azar de la lista
        Cuadra cuadraNegocio = cuadras[rand.nextInt(cuadras.length)]; 
        Cuadra cuadraCasa = cuadras[rand.nextInt(cuadras.length)]; 

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
    
    
    //colisión por rectángulos
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
        
        
        // Condicion de victoria y derrota
        if (enVictoria) {
        	
        if (!sonidoFinalReproducido) {
                sonido.reproducir("sonido_de_victoria.wav", false);
                sonidoFinalReproducido = true;
            }

            dibujarPantallaVictoria();
            if (entorno.sePresiono('J') || entorno.sePresiono('j')) {
                reiniciarJuego();
            }
            return;
        }
         
        // Musica de la partida
            sonido.reproducir("sonido_de_partida.wav", true);
        
            
        //condicion derrota
        if (this.rapi.getVida() <= 0 || this.totalPedidosPerdidos >= 3) {
            enGameOver = true;
            return;
        }
        
        //condicion victoria
        if (this.totalPedidosEntregados >= 3) {
            enVictoria = true;
            return;
        }
        
        
        if (cooldownDaño > 0) {
            cooldownDaño--;
        }
        
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------        
        //movimiento del repartidor
        
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


//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Estado del pedido 
        
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
            for (Enemigos e : enemigos) {
                if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), e.getX(), e.getY(), e.getAncho(), e.getAlto()) && cooldownDaño == 0 && this.pedidos.camino == true) {
                	
                    this.pedidos.estaPerdido();
                    this.totalPedidosPerdidos += 1;
                }    

            }
            
            //Para agarrar el pedido selecciona con el clik derecho del mouse
            if (entorno.sePresionoBoton(entorno.BOTON_DERECHO) && !this.pedidos.entregado && !this.pedidos.perdido) {
                
                //el estado del pedido esta en reposo hasta que el jugador interactue con el negocio con click derecho
                if (this.pedidos.esperando) {
                	if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), pedidos.xNegocio, pedidos.yNegocio, 80, 80)) {
                        
                    	this.pedidos.agarrarPedido();
                    }
                }
           
                // El pedido ya está en camino y hace clic sobre la casa de destino
                else 
                	if (this.pedidos.camino) {
                		if (hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(), pedidos.xCasa, pedidos.yCasa, 80, 80)) {
                       
                			this.pedidos.entregarPedido(); 
                			this.totalPedidosEntregados ++;
                    }
                }
            }
        } 
        
        
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------  
       // Movimiento de los enemigos y activacion de la bomba
        
        for (Enemigos e : enemigos) {
            if (e != null) {
            	
                if (e.getDireccion() == 0) {
                    e.moverIzquierda();
                } else if (e.getDireccion() == 1) {
                    e.moverDerecha();
                } else if (e.getDireccion() == 2) {
                    e.moverArriba();
                } else if (e.getDireccion() == 3) {
                    e.moverAbajo();
                }

                boolean choco = false;

                // Movimiento dentro del mapa
                if (e.getX() < e.getAncho() / 2 || e.getX() >= 800 - e.getAncho() / 2 ||
                    e.getY() <= 50 + e.getAlto() / 2 || e.getY() >= 700 - e.getAlto() / 2) {
                    choco = true;
                }

                // Colision con las cuadras del mapa
                for (Cuadra c : mapa.getCuadras()) {
                    if (c != null && hayColision(e.getX(), e.getY(), e.getAncho(), e.getAlto(),
                                                 c.getX(), c.getY(), c.getAncho(), c.getAlto())) {
                        choco = true;
                    }
                }

                // Si choca contra el borde o una cuadra, deshace el paso y cambia de dirección
                if (choco) {
                    if (e.getDireccion() == 0) e.moverDerecha();
                    else if (e.getDireccion() == 1) e.moverIzquierda();
                    else if (e.getDireccion() == 2) e.moverAbajo();
                    else if (e.getDireccion() == 3) e.moverArriba();

                    e.elegirNuevaDireccion();
                    e.setContadorGiro(0);
                    
                } else {
                    // Si avanza libre, cada 90 ticks cambia de dirección al azar
                    e.setContadorGiro(e.getContadorGiro() + 1);
                    if (e.getContadorGiro() >= 90) {
                        e.elegirNuevaDireccion();
                        e.setContadorGiro(0);
                    }
                }

                // Coloca bombas
                if (e.ColocarBombas()) {
                    agregarBomba(new Bomba(e.getX(), e.getY()));
                }
            }
        }
            
        
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------           
        // Colision de repartidor contra el enemigo
        
        for (Enemigos e : enemigos) {
            if (cooldownDaño == 0 && hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(),
                                                  e.getX(), e.getY(), e.getAncho(), e.getAlto())) {
            	
            	rapi.setVida(rapi.getVida() - 1);
            	cooldownDaño = 30; // Espera 30 ticks para volver a recibir daño
            	System.out.println("¡Colisión con enemigo! Vidas restantes: " + rapi.getVida());
            	
            }
            
        }
        
        
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------           
        // Actualizar el estado de las bombas y su colision con el repartidor
  
        for (Bomba b : bombas) {
        	if (b != null) {
        		b.actualizar(mapa);
        	
            
            // Colision del repartidor con las bombas
            if (b.isActiva() && cooldownDaño == 0 && hayColision(rapi.getX(), rapi.getY(), rapi.getAncho(), rapi.getAlto(),
                                                                b.getX(), b.getY(), b.getAncho(), b.getAlto())) {
                rapi.setVida(rapi.getVida() - 1);
                b.desactivar(); 
                cooldownDaño = 45;
                System.out.println("¡Chocaste una bomba! Vidas restantes: " + rapi.getVida());
            }
            
            // Colision del repartidor con la expansion de las bombas
            if (cooldownDaño == 0 &&
                b.alcanzaRepartidor(rapi.getX(), rapi.getY(),
                                    rapi.getAncho(), rapi.getAlto())) {

                rapi.setVida(rapi.getVida() - 1);
                cooldownDaño = 45;

                System.out.println("¡Te alcanzó una explosión! Vidas restantes: " + rapi.getVida());
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
        for (Enemigos e : enemigos) {
            e.dibujar(this.entorno);
        }

        // 3. Dibujar bombas
        for (Bomba b : bombas) {
        	if (b != null) {        
        		b.dibujar(this.entorno);
        	}
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
            if (entorno.estaPresionada('a')|| entorno.estaPresionada('A'))

                direccion = "IZQUIERDA";
            else if (entorno.estaPresionada('w') || entorno.estaPresionada('W'))
                direccion = "ARRIBA";
            else if (entorno.estaPresionada('s') || entorno.estaPresionada('S'))
                direccion = "ABAJO";

         
            // PARAMETROS PARA GENERAR EL PROYECTIL
            this.proyectiles[0] = new Proyectil(this.rapi.getX(), this.rapi.getY(), 10, 4, direccion);
            
            }
        
            
        	if (this.proyectiles[0] != null) {

                this.proyectiles[0].mover();

                boolean colision = false;
                
                // Recorremos los enemigos para comprobar si hubo un impacto
                for (Enemigos e : enemigos) {

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
        
        
        entorno.cambiarFont("arial", 15, Color.BLACK, entorno.NEGRITA);
        entorno.escribirTexto("Moverse con la teclas W A S D", 580 , 698);
        
        //8. Dibuja el pedido en pantalla
        if (this.pedidos != null) {
        	this.pedidos.dibujarEncargo(entorno);
        	this.pedidos.dibujarPedido(entorno);
        	
        	
        	entorno.cambiarFont("OCR A Extended", 15, Color.CYAN, entorno.NEGRITA);
        	entorno.escribirTexto("Pedidos Entregados: " + this.totalPedidosEntregados, 135, 25);
        	entorno.escribirTexto("Pedidos Perdidos: "+ this.totalPedidosPerdidos, 135, 45);
        	
        	
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
        entorno.escribirTexto("Perdiste, suerte la proxima", 270, 330);

        entorno.cambiarFont("OCR A Extended", 20, Color.YELLOW, entorno.NEGRITA);
        entorno.escribirTexto("Presioná 'R' para volver a intentar", 210, 430);
    
    }
    
    // Pantalla de Victoria
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
        this.totalPedidosEntregados = 0;
        this.totalPedidosPerdidos = 0;
        this.enGameOver = false;
        this.enVictoria = false;
        sonidoFinalReproducido = false;
        tiempoInicio = entorno.tiempo();
    }
    
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}