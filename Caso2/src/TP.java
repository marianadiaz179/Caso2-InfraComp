import java.util.HashMap;

public class TP {
    private HashMap<Integer, Integer> referencias;
    private RAM ram;
    private TLB tlb;

    public TP(RAM ram, TLB tlb) {
        this.referencias = new HashMap<Integer, Integer>();
        this.ram = ram;
        this.tlb = tlb;
    }

    public synchronized Integer buscarReferencia(int referencia) {
        Integer respuesta = -1;

        if (referencias.containsKey(referencia)) {
            respuesta = referencias.get(referencia);
        }

        return respuesta;
    }

    public synchronized void agregarReferencia(int referencia) {
        // si la RAM ya no tiene marcos disponibles
        if (this.ram.getMarcosDisponibles().size() == 0) {
            // Hallar la página que tiene el marco menos usado
            int paginaEliminar = -1;
            int marcoMenosUsado = this.ram.getMarcoMenosUsado();
            for (Integer pagina : referencias.keySet()) {
                Integer marcoPagina = referencias.get(pagina);
                if (marcoPagina == marcoMenosUsado) {
                    paginaEliminar = pagina;
                    break;
                }
            }

            // Eliminar la pagina del marco menos usado
            referencias.remove(paginaEliminar);
            System.out.println("Se ha eliminado la referencia de la página " + paginaEliminar + " en el marco " + marcoMenosUsado);

            //Eliminar la pagina de la TLB
            tlb.eliminarReferencia(paginaEliminar);

            // Agregar la nueva referencia al marco que se ha eliminado de la TLB
            referencias.put(referencia, marcoMenosUsado);

            // Agregar a la TLB
            tlb.agregarReferencia(referencia, marcoMenosUsado);

        }
        // si la RAM todavia tiene marcos disponibles
        else {
            // solicitar un marco a la RAM
            Integer marco = ram.darMarcoDisponible();
            // Agregar la nueva referencia al marco que se ha eliminado de la TLB
            referencias.put(referencia, marco);

            //Actualizar los marcos en la RAM
            ram.actualizarMarcos(marco);
            // Agregar a la TLB
            tlb.agregarReferencia(referencia, marco);

        }
    }
}
