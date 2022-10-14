import java.util.ArrayList;

public class ThreadLoader extends Thread {
    private RAM ram;
    private ArrayList<Integer> referencias;


    public ThreadLoader(RAM r, ArrayList<Integer> lista) {
        this.ram = r;
        this.referencias = lista;
    }

    @Override
    public void run() {
        
        while (this.referencias.size() != 0) {
            System.out.println(ram.getNuevaReferencia());
            //Referencia a analizar
            Integer referencia = referencias.get(0);
            //System.out.println("Buscando la página " + referencia + " en la TLB");

            ram.cargarReferencia(referencia);
            referencias.remove(0);

            try {
                Thread.sleep(2);
            } catch (Exception e) {}
        }

        ram.setNuevaReferencia(-1);

        System.out.println("El tiempo de traducción de direcciones es " + ram.getDirecciones());
        System.out.println("El tiempo de carga de datos es : " + ram.getDatos());
        System.out.println("Num Fallas: " + ram.getNumFallas());
    }

    

}
