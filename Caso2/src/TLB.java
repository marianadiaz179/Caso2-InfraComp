import java.util.ArrayList;
import java.util.HashMap;

public class TLB {
    private int tamTLB;
    private HashMap<Integer, Integer> referencias;
    private ArrayList<Integer> colaOrden;

    public TLB(int tam) {
        this.tamTLB = tam;
        this.referencias = new HashMap<Integer, Integer>();
        this.colaOrden = new ArrayList<Integer>();
    }

    public synchronized boolean buscarReferencia(int referencia) {
        boolean respuesta = false;

        if (referencias.containsKey(referencia)) {
            respuesta = true;
        }

        return respuesta;
    }

    public synchronized void agregarReferencia(int referencia, int marcoRAM) {
        // si la TLB ya llegó a su máximo debe eliminar una referencia existente
        if (this.referencias.size() == tamTLB) {
            // Hallar la página que entró primero para sacarla, de acuerdo a FIFO
            int paginaEliminar = colaOrden.get(0);
            // Eliminar la pagina que entró primero de las referencias
            referencias.remove(paginaEliminar);
            // Eliminar la página que entró primero de la cola
            colaOrden.remove(0);

        }

        referencias.put(referencia, marcoRAM);
        colaOrden.add(referencia);
    }

}
