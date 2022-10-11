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
            System.out.println("Buscando la página " + referencia + " en la TLB");

            //Verificar si está en la TP
            if (!(tlb.buscarReferencia(referencia)))
            {
                System.out.println("No se ha encontrado la página en la TLB");
                System.out.println("Buscando la página en la TP");
                //Si no está en la TP, se agrega a la TP
                if((tp.buscarReferencia(referencia))==-1)
                {
                    System.out.println("No se ha encontrado la página en la TP");
                    System.out.println("Agregando la página a la TP");
                    try {
                        tp.agregarReferencia(referencia);
                        System.out.println("La página " + referencia + " se ha agregado correctamente a la TP ");
                    } catch (Exception e) {
                        System.out.println("La página " + referencia + "  no se ha agregado correctamente a la TP ");
                    }
                    
                }
                //Si está en la TP, se agrega a la TLB
                else{

                    Integer marco = tp.buscarReferencia(referencia);
                    System.out.println("Se ha encontrado la pagina " + referencia + " en el marco de RAM " + marco);
                    try {
                        tlb.agregarReferencia(referencia, marco);
                        System.out.println("Se ha agregado la pagina " + referencia + " a la TLB");
                    } catch (Exception e) {
                        System.out.println("No se ha agregado la pagina " + referencia + " a la TLB");
                    }
                }
            
            }
            else{
                System.out.println("Se ha encontrado la página en la TLB");
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
