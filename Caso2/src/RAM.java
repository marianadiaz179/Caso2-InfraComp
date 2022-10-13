import java.util.ArrayList;
import java.util.HashMap;

public class RAM {
    private int numMarcos;
    private int marcoMenosUsado;
    private int nuevaReferencia;
    private ArrayList<Integer> marcosDisponibles;
    private ArrayList<Integer> marcosOcupados;
    private ArrayList<Integer> listaReferencias;

    // Estructuras para el aging
    private HashMap<Integer, Long> bitsMarcos = new HashMap<>();

    //constructor
    public RAM(int num, ArrayList<Integer> lista) {
        this.numMarcos = num;
        this.listaReferencias = lista;
        this.marcosDisponibles = new ArrayList<>();
        for (int i = 0; i < numMarcos; i++) {
            marcosDisponibles.add(i);
        }
        this.marcosOcupados = new ArrayList<>();
        this.ajustarBits();
        this.nuevaReferencia = -2;

    }

    //getters y setters
    public int getMarcoMenosUsado() {
        return marcoMenosUsado;
    }

    public ArrayList<Integer> getMarcosDisponibles() {
        return marcosDisponibles;
    }

    public ArrayList<Integer> getListaReferencias() {
        return listaReferencias;
    }

    public void setListaReferencias(ArrayList<Integer> listaReferencias) {
        this.listaReferencias = listaReferencias;
    }

    public int getNuevaReferencia() {
        return nuevaReferencia;
    }

    public void setNuevaReferencia(int nuevaReferencia) {
        this.nuevaReferencia = nuevaReferencia;
    }

    // retorna un marco que esté disponible para la creación de una referenci aen la TP
    public Integer darMarcoDisponible() {
        Integer marcoDisponible = -1;
        marcoDisponible = marcosDisponibles.get(0);
        marcosDisponibles.remove(0);
        return marcoDisponible;
    }

    //actualiza los marcos cuando se realiza una referencia
    public void actualizarMarcos(Integer marco) {
        for (int i = 0; i < marcosDisponibles.size(); i++) {
            if (marcosDisponibles.get(i) == marco) {
                marcosOcupados.add(i);
                marcosDisponibles.remove(i);
            }
        }
    }

    //algoritmo de aging
    public synchronized void algoritmoEnvejecimiento(Integer referencia) {

        System.out.println("EL BIT REFERENCIADO ES " + referencia);
        System.out.println(referencia != -2);
        if (referencia != -2) {

            long maximo = -1;

            for (Integer marco : bitsMarcos.keySet()) {

                long bitsReferenciados = bitsMarcos.get(marco);
                bitsMarcos.replace(marco, bitsReferenciados >> 1);
                System.out.println("Se han envejecido el marco " + marco);
                if (marco == referencia) {
                    long nuevaReferencia = (long) Math.pow(2, 32);
                    long bitsReferencia = bitsMarcos.get(referencia);
                    bitsMarcos.replace(referencia, bitsReferencia | nuevaReferencia);
                    System.out.println("Se han envejcido el marco referenciado " + marco);
                }
                long maximoLocal = bitsMarcos.get(marco);

                if (maximoLocal > maximo) {
                    maximo = maximoLocal;
                    this.marcoMenosUsado = marco;
                }

            }
        } else if (referencia == -2) {
            long maximo = -1;

            for (Integer marco : bitsMarcos.keySet()) {

                long bitsReferenciados = bitsMarcos.get(marco);
                bitsMarcos.replace(marco, bitsReferenciados >> 1);
                System.out.println("Se han envejecido el marco " + marco);
                
                long maximoLocal = bitsMarcos.get(marco);

                if (maximoLocal > maximo) {
                    maximo = maximoLocal;
                    this.marcoMenosUsado = marco;
                }

            }
        }

    }

    
    // Ajustar inicio de los bits
    public void ajustarBits() {
        long bits0 = 0;
        for (int i = 0; i < numMarcos; i++) {
            bitsMarcos.put(i,bits0);
        }
    }

   
}
