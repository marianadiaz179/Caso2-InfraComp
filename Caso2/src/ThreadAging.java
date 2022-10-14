
public class ThreadAging extends Thread {
    private RAM ram;

    public ThreadAging(RAM ram) {
        this.ram = ram;

    }

    @Override   
    public void run() {

        while(ram.getNuevaReferencia() != -1)
        {   
            System.out.println("Aging");
            ram.algoritmoEnvejecimiento();
            try {
                Thread.sleep(1);
            } catch (Exception e) {}
        }

    }


}