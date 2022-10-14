import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception 
    {   
        //variables
        int entradasTLB;
        int marcosPaginasRAM;
        String nombreArchivo;

        //Parametros por consola
        System.out.println("Escriba el número de entradas de la TLB: ");
        entradasTLB = Integer.parseInt(System.console().readLine());

        System.out.println("Escriba el número de marcos de página en memoria RAM que el sistema le asigna al proceso: ");
        marcosPaginasRAM = Integer.parseInt(System.console().readLine());

        System.out.println("Escriba el nombre del archivo con las referencias: ");
        nombreArchivo = (System.console().readLine());

        
        //Lee el archivo
        BufferedReader buffer = new BufferedReader(new FileReader("./Caso2/data/" + nombreArchivo));

        String linea = buffer.readLine();
        ArrayList<Integer> lista = new ArrayList<>();

        while (linea != null)
        {
            int referencia = Integer.parseInt(linea);
            lista.add(referencia);
            linea = buffer.readLine();
        }

        buffer.close();

        //Estructuras
        TLB TLB = new TLB(entradasTLB);
        TP TP = new TP(TLB);
        RAM RAM = new RAM(marcosPaginasRAM, TLB, TP);
        ThreadAging envejecimiento = new ThreadAging(RAM);
        envejecimiento.start();
        ThreadLoader cargador = new ThreadLoader(RAM, lista);
        cargador.start();




    }
}
