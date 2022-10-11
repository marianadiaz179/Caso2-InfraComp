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
    private HashMap<Integer, Integer[]> paginasBits = new HashMap<Integer, Integer[]>();

    public RAM(int num, ArrayList<Integer> lista) {
        this.numMarcos = num;
        this.listaReferencias = lista;
        this.marcosDisponibles = new ArrayList<>();
        for (int i = 0; i < numMarcos; i++) {
            marcosDisponibles.add(i);
        }
        this.marcosOcupados = new ArrayList<>();
        this.ajustarBits();
        this.nuevaReferencia = -1;

    }

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

    public Integer darMarcoDisponible() {
        Integer marcoDisponible = -1;
        marcoDisponible = marcosDisponibles.get(0);
        marcosDisponibles.remove(0);
        return marcoDisponible;
    }

    public void actualizarMarcos(Integer marco) {
        for (int i = 0; i < marcosDisponibles.size(); i++) {
            if (marcosDisponibles.get(i) == marco) {
                marcosOcupados.add(i);
                marcosDisponibles.remove(i);
            }
        }
    }

    // Algpritmo que ejecuta el aging
    public synchronized void algoritmoEnvejecimiento(Integer referencia) {

        Integer maximo = -1;

        for (Integer pagina : paginasBits.keySet()) {
            if (pagina != referencia) {
                envejecerBitsNoReferenciados(pagina);
            } else {
                envejecerBitsReferenciados(referencia);
            }
        }

        for (Integer pagina : paginasBits.keySet()) {
            Integer maximoLocal = posicionReferencia(referencia);

            if (maximoLocal > maximo) {
                maximo = maximoLocal;
                marcoMenosUsado = pagina;
            }

        }

    }

    // Ajustar inicio de los bits
    public void ajustarBits() {
        Integer[] bits0 = new Integer[32];
        for (int k = 0; k < 32; k++) {
            bits0[k] = 0;
        }
        for (int i = 0; i < numMarcos; i++) {
            paginasBits.put(i, bits0);
        }
    }

    // Envejece los bits que no están siendo referenciados
    public void envejecerBitsNoReferenciados(Integer referencia) {
        Integer[] resultado = new Integer[32];
        Integer[] bitsActuales = paginasBits.get(referencia);

        for (int i = 0; i < 32; i++) {
            resultado[i + 1] = bitsActuales[i];
        }

        resultado[0] = 0;

        paginasBits.replace(referencia, resultado);
    }

    // Envejece los bits que fueron referenciados
    public void envejecerBitsReferenciados(Integer referencia) {
        Integer[] resultado = new Integer[32];
        Integer[] bitsActuales = paginasBits.get(referencia);

        for (int i = 0; i < 32; i++) {
            resultado[i + 1] = bitsActuales[i];
        }

        resultado[0] = 1;

        paginasBits.replace(referencia, resultado);
    }

    // Determina la posición en la que aparece el primer uno (ultima referencia)
    public Integer posicionReferencia(Integer referencia) {
        Integer[] bitsReferencia = paginasBits.get(referencia);
        Integer posicion = 0;

        for (int i = 0; i < 32; i++) {
            if (bitsReferencia[i] == 1) {
                posicion = i;
                break;
            }
        }

        return posicion;
    }

}
