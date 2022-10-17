import java.util.HashMap;

public class TP {
    private HashMap<Integer, Integer> referencias;
    private TLB tlb;

    public TP(TLB tlb) {
        this.referencias = new HashMap<Integer, Integer>();
        this.tlb = tlb;
    }

    //Bsuca si la referencia está en la TP y si está retorna el marco en el que está
    public synchronized Integer buscarReferencia(int referencia) {
        Integer respuesta = -1;

        if (referencias.containsKey(referencia)) {
            respuesta = referencias.get(referencia);
        }

        return respuesta;
    }

    //Se agrega una nueva referencia a la RAM
    public synchronized void agregarReferencia(int referencia, int marco, boolean condicion) {
    {   
            //Es true si la RAM ya está llena y debe eliminar una referencia, entra el marco que se debe reemplazar
            // correspondería al marco menos usado según el algoritmo de envejecimiento
            if (condicion == true)
            {
                int paginaEliminar = -1;
                //Se recorre hasta que se encuentra la pagina que está en el mapa
                for (Integer pagina : referencias.keySet()) {
                    Integer marcoPagina = referencias.get(pagina);
                    if (marcoPagina == marco) {
                        paginaEliminar = pagina;
                        break;
                    }
                }

                // Eliminar la pagina del marco menos usado
                referencias.remove(paginaEliminar);
                //System.out.println("Se ha eliminado la referencia de la página " + paginaEliminar + " en el marco " + marco + " de la TP");

                //Eliminar la pagina de la TLB
                tlb.eliminarReferencia(paginaEliminar);
                //System.out.println("Se ha eliminado la referencia de la página " + paginaEliminar + " en el marco " + marco +  " de la TLB");

            }
            // Agregar la nueva referencia al marco que se ha eliminado de la TLB
            referencias.put(referencia, marco);
            //System.out.println("Se ha agregado la referencia de la página " + referencia + " en el marco " +marco);

            // Agregar a la TLB
            tlb.agregarReferencia(referencia, marco);

        }
        
    }
}
