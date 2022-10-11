
public class ThreadAging extends Thread {
    private RAM ram;

    public ThreadAging(RAM ram) {
        this.ram = ram;

    }

    @Override   
    public void run() {

        while(ram.getNuevaReferencia() != -1)
        {
            Integer referencia = ram.getNuevaReferencia();
            ram.algoritmoEnvejecimiento(referencia);
            ram.setNuevaReferencia(-1);
            try {
                Thread.sleep(1);
            } catch (Exception e) {}
        }

    }


}