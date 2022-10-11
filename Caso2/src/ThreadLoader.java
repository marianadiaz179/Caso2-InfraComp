import java.util.ArrayList;

public class ThreadLoader extends Thread {
    private RAM ram;
    private TLB tlb;
    private TP tp;
    private ArrayList<Integer> referencias;

    public ThreadLoader(RAM r, TLB t, TP tp) {
        this.ram = r;
        this.tlb = t;
        this.tp = tp;
        this.referencias = this.ram.getListaReferencias();

    }

    @Override
    public void run() {
        while (this.referencias.size() != 0) {
            //Referencia a analizar
            Integer referencia = referencias.get(0);

            //Verificar si está en la TP
            if (!(tlb.buscarReferencia(referencia)))
            {
                //Si no está en la TP, se agrega a la TP
                if((tp.buscarReferencia(referencia))==-1)
                {
                    tp.agregarReferencia(referencia);
                }
                //Si está en la TP, se agrega a la TLB
                else{
                    Integer marco = tp.buscarReferencia(referencia);
                    tlb.agregarReferencia(referencia, marco);
                }
            }
            ram.setNuevaReferencia(referencia);

            // eliminar referencia recien visitada
            referencias.remove(0);

            //actualizar la lista de referencias
            this.ram.setListaReferencias(referencias);

            try {
                Thread.sleep(2);
            } catch (Exception e) {}
        }
    }

}
