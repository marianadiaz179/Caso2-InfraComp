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

    //Busca si la referencia está en la TLB y su está retorna el marco en el que está
    public synchronized int buscarReferencia(int referencia) {
        int respuesta = -1;

        if (referencias.containsKey(referencia)) {
            respuesta = referencias.get(referencia);
        }
        return respuesta;
    }

    // Se agrega la referencia a la TLB
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
        //Agregar la nueva referencia en el marco determinado
        referencias.put(referencia, marcoRAM);
        //Agregar a la cola la referencia al final de la cola
        colaOrden.add(referencia);
    }

    //Elimina la referencia de la TLB si sale tambien de la RAM
    public synchronized void eliminarReferencia(int referencia)
    {
        if (referencias.containsKey(referencia)) {
            referencias.remove(referencia);
        }
    }
}
