
public class ThreadAging extends Thread {
    private RAM ram;

    public ThreadAging(RAM ram) {
        this.ram = ram;

    }

    @Override   
    public void run() {

        //Se repite mientras que sea diferente a -1 (Se cambia al finalizar las referencias)
        while(ram.getNuevaReferencia() != -1)
        {   
            //Se ejecuta el algoritmo de envejecimiento
            ram.algoritmoEnvejecimiento();

            //Tiempo que espera para volver a repetirse
            try {
                Thread.sleep(1);
            } catch (Exception e) {}
        }

    }


}