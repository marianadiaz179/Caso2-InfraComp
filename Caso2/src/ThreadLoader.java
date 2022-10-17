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
        
        // Se repite mientras todavia existan  referencias por cargar
        while (this.referencias.size() != 0) {
    
            //Referencia a analizar
            Integer referencia = referencias.get(0);
            //carga cada referencia que entra en el archivo
            ram.cargarReferencia(referencia);
            //Elimina la referencia de la lista porque ya fue realizada
            referencias.remove(0);

            //Tiempo que espera para volver a ejecutarse
            try {
                Thread.sleep(2);
            } catch (Exception e) {}
        }

        ram.setNuevaReferencia(-1);

        System.out.println("\n");
        System.out.println("************************************************************************");
        System.out.println(" ------- El tiempo de traducción de direcciones es " + ram.getDirecciones() + " ns ------- ");
        System.out.println(" ------- El tiempo de carga de datos es : " + ram.getDatos()+ " ns ------- ");
        System.out.println(" ------- Num Fallas: " + ram.getNumFallas()+ " ------- ");
        System.out.println("************************************************************************");

    }

    

}
