package com.example.solitario_celta;

public class JuegoCelta {
    static final int TAMANIO = 7;
    private static final int NUM_MOVIMIENTOS = 4;
    private int[][] tablero;

    protected String tableroInicialSerializado;
    private static final int[][] TABLERO_INICIAL = { // Posiciones válidas del tablero
            {0, 0, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 0},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1},
            {0, 0, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 0}
    };
    private static final int[][] desplazamientos = {
            {0, 2},   // Dcha
            {0, -2},   // Izda
            {2, 0},   // Abajo
            {-2, 0}    // Arriba
    };
    private int iSeleccionada, jSeleccionada;   // coordenadas origen ficha
    private int iSaltada, jSaltada;             // coordenadas ficha sobre la que se hace el movimiento

    private enum Estado {
        ESTADO_SELECCION_FICHA, ESTADO_SELECCION_DESTINO, ESTADO_TERMINADO
    }

    private Estado estadoJuego;

    /**
     * Constructor
     * Inicializa el tablero y el estado del miJuego
     */
    public JuegoCelta() {
        tablero = new int[TAMANIO][TAMANIO];
        for (int i = 0; i < TAMANIO; i++)
            System.arraycopy(TABLERO_INICIAL[i], 0, tablero[i], 0, TAMANIO);
        tablero[TAMANIO / 2][TAMANIO / 2] = 0;   // posición central

        estadoJuego = Estado.ESTADO_SELECCION_FICHA;
        tableroInicialSerializado = serializaTablero();
    }

    /**
     * Devuelve el contenido de una posición del tablero
     *
     * @param i fila del tablero
     * @param j columna del tablero
     * @return contenido
     */
    protected int obtenerFicha(int i, int j) {
        return tablero[i][j];
    }

    /**
     * Determina si el movimiento (i1, j1) a (i2, j2) es aceptable
     *
     * @param i1 fila origen
     * @param j1 columna origen
     * @param i2 fila destino
     * @param j2 columna destino
     * @return valor lógico
     */
    private boolean movimientoAceptable(int i1, int j1, int i2, int j2) {

        if (tablero[i1][j1] == 0 || tablero[i2][j2] == 1)
            return false;

        if ((j1 == j2 && Math.abs(i2 - i1) == 2)
                || (i1 == i2 && Math.abs(j2 - j1) == 2)) {
            iSaltada = (i1 + i2) / 2;
            jSaltada = (j1 + j2) / 2;
            if (tablero[iSaltada][jSaltada] == 1)
                return true;
        }

        return false;
    }

    /**
     * Recibe las coordenadas de la posición pulsada y dependiendo del estado, realiza la acción
     *
     * @param iPulsada coordenada fila
     * @param jPulsada coordenada columna
     */
    public void jugar(int iPulsada, int jPulsada) {
        if (estadoJuego == Estado.ESTADO_SELECCION_FICHA) {
            iSeleccionada = iPulsada;
            jSeleccionada = jPulsada;
            estadoJuego = Estado.ESTADO_SELECCION_DESTINO;
        } else if (estadoJuego == Estado.ESTADO_SELECCION_DESTINO) {
            if (movimientoAceptable(iSeleccionada, jSeleccionada, iPulsada, jPulsada)) {
                estadoJuego = Estado.ESTADO_SELECCION_FICHA;

                // Actualizar tablero
                tablero[iSeleccionada][jSeleccionada] = 0;
                tablero[iSaltada][jSaltada] = 0;
                tablero[iPulsada][jPulsada] = 1;

                if (juegoTerminado())
                    estadoJuego = Estado.ESTADO_TERMINADO;
            } else { // El movimiento no es aceptable, la última ficha pasa a ser la seleccionada
                iSeleccionada = iPulsada;
                jSeleccionada = jPulsada;
            }
        }
    }

    /**
     * Determina si el miJuego ha terminado (no se puede realizar ningún movimiento)
     *
     * @return valor lógico
     */
    public boolean juegoTerminado() {

        for (int i = 0; i < TAMANIO; i++)
            for (int j = 0; j < TAMANIO; j++)
                if (tablero[i][j] == 1) {
                    for (int k = 0; k < NUM_MOVIMIENTOS; k++) {
                        int p = i + desplazamientos[k][0];
                        int q = j + desplazamientos[k][1];
                        if (p >= 0 && p < TAMANIO && q >= 0 && q < TAMANIO && tablero[p][q] == 0 && TABLERO_INICIAL[p][q] == 1)
                            if (movimientoAceptable(i, j, p, q))
                                return false;
                    }
                }

        return true;
    }

    /**
     * Serializa el tablero, devolviendo una cadena de 7x7 caracteres (dígitos 0 o 1)
     *
     * @return tablero serializado
     */
    public String serializaTablero() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < TAMANIO; i++)
            for (int j = 0; j < TAMANIO; j++)
                str.append(tablero[i][j]);
        return str.toString();
    }

    /**
     * Recupera el estado del tablero a partir de su representación serializada
     *
     * @param str representación del tablero
     */
    public void deserializaTablero(String str) {
        for (int i = 0, cont = 0; i < TAMANIO; i++)
            for (int j = 0; j < TAMANIO; j++)
                tablero[i][j] = str.charAt(cont++) - '0';
    }

    /**
     * Recupera el miJuego a su estado inicial
     */
    public void reiniciar() {
        for (int i = 0; i < TAMANIO; i++)
            System.arraycopy(TABLERO_INICIAL[i], 0, tablero[i], 0, TAMANIO);
        tablero[TAMANIO / 2][TAMANIO / 2] = 0;   // posición central

        estadoJuego = Estado.ESTADO_SELECCION_FICHA;
    }

    public int missingPieces() {
        int contador = 0;
        for (int i = 0; i < TAMANIO; i++) {
            for (int j = 0; j < TAMANIO; j++) {
                if (tablero[i][j] == 1) {
                    contador++;
                }
            }
        }
        return contador;
    }
}
