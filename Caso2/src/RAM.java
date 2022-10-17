import java.util.ArrayList;
import java.util.HashMap;

public class RAM {
    private int numMarcos;
    private int marcoMenosUsado;
    private int nuevaReferencia;
    private ArrayList<Integer> marcosDisponibles;
    private ArrayList<Integer> marcosOcupados;
    private TLB tlb;
    private TP tp;

    // Estructuras para el aging
    private HashMap<Integer, Long> bitsMarcos = new HashMap<>();

    // variables para el tiempo
    private long direcciones;
    private long datos;
    private long numFallas;

    // constructor
    public RAM(int num, TLB tlb, TP tp) {
        this.numMarcos = num;
        this.tlb = tlb;
        this.tp = tp;
        this.marcosDisponibles = new ArrayList<>();
        for (int i = 0; i < numMarcos; i++) {
            marcosDisponibles.add(i);
        }
        this.marcosOcupados = new ArrayList<>();
        this.ajustarBits();
        this.nuevaReferencia = -2;

    }

    // getters y setters
    public int getMarcoMenosUsado() {
        return marcoMenosUsado;
    }

    public ArrayList<Integer> getMarcosDisponibles() {
        return marcosDisponibles;
    }

    public int getNuevaReferencia() {
        return nuevaReferencia;
    }

    public void setNuevaReferencia(int nuevaReferencia) {
        this.nuevaReferencia = nuevaReferencia;
    }

    public long getDirecciones() {
        return direcciones;
    }

    public long getDatos() {
        return datos;
    }

    public long getNumFallas() {
        return numFallas;
    }

    public void setMarcoMenosUsado(int marcoMenosUsado) {
        this.marcoMenosUsado = marcoMenosUsado;
    }

    // retorna un marco que esté disponible para la creación de una referencia en la TP
    public Integer darMarcoDisponible() {
        Integer marcoDisponible = -1;
        marcoDisponible = marcosDisponibles.get(0);
        marcosDisponibles.remove(0);
        return marcoDisponible;
    }

    // actualiza los marcos cuando se realiza una referencia
    public void actualizarMarcos(Integer marco) {
        for (int i = 0; i < marcosDisponibles.size(); i++) {
            if (marcosDisponibles.get(i) == marco) {
                marcosOcupados.add(i);
                marcosDisponibles.remove(i);
            }
        }
    }

    // algoritmo de aging
    public synchronized void algoritmoEnvejecimiento() {
        
        // se inicializa un valor grande para ser reemplazado por el mínimo
        long minimo = (long) Math.pow(2, 33);
        //Itera para envejecer todos los bits de los marcos
        for (Integer marco : bitsMarcos.keySet()) {
            //Recupera la información actual y la cambia por el corrimiento de bits
            long bitsReferenciados = bitsMarcos.get(marco);
            bitsMarcos.replace(marco, bitsReferenciados >> 1);
            //Si hay un marco que acaba de ser referenciado se corre con un 1
            if (marco == nuevaReferencia) {
                long nuevos = (long) Math.pow(2, 32);
                long bitsReferencia = bitsMarcos.get(marco);
                bitsMarcos.replace(marco, bitsReferencia | nuevos);
            }
            long minimoLocal = bitsMarcos.get(marco);
            
            //Busca el menor de acuerdo al que ha sido menos referenciado
            if (minimoLocal < minimo) {
                minimo = minimoLocal;
                //Actualiza el marco menos usado que será el próximo reemplazado al entrar una referencia nueva
                setMarcoMenosUsado(marco);
            }
        }
        //System.out.println("Marco menos usado:  " + marcoMenosUsado);
    }

    // Ajustar inicio de los bits
    public void ajustarBits() {
        long bits0 = 0;
        for (int i = 0; i < numMarcos; i++) {
            bitsMarcos.put(i, bits0);
        }
    }

    // algoritmo de carga de datos
    public synchronized void cargarReferencia(Integer referencia) {        

        // Verificar si está en la TP
        if ((this.tlb.buscarReferencia(referencia)) == -1) {
            //System.out.println("No se ha encontrado la página " + referencia + " en la TLB");
            //System.out.println("Buscando la página " + referencia + " en la TP");
            //Si no está en la TP, se agrega a la TP
            if ((tp.buscarReferencia(referencia)) == -1) {
                //System.out.println("No se ha encontrado la página en la TP" + " Fallo de  pagina " + referencia);
                this.direcciones += 60;
                this.datos += 10000000;
                this.numFallas++;
                // System.out.println("Agregando la página a la TP");
                try {
                    // si la RAM ya no tiene marcos disponibles
                    if (this.getMarcosDisponibles().size() == 0) {
                        
                        int marcoMenosUsado = this.getMarcoMenosUsado();

                        tp.agregarReferencia(referencia, marcoMenosUsado, true);
                        setNuevaReferencia(tp.buscarReferencia(referencia));
                    }
                    // si la RAM todavia tiene marcos disponibles
                    else {
                        // solicitar un marco a la RAM
                        Integer marco = this.darMarcoDisponible();
                        // Actualizar los marcos en la RAM
                        this.actualizarMarcos(marco);
                        //Agregar la referencia a la TP
                        tp.agregarReferencia(referencia, marco, false);
                        setNuevaReferencia(tp.buscarReferencia(referencia));
                    }

                } catch (Exception e) {
                    // System.out.println("La página " + referencia + " no se ha agregado correctamente a la TP ");
                }

            }
            // Si está en la TP, se agrega a la TLB
            else {

                Integer marco = tp.buscarReferencia(referencia);
                setNuevaReferencia(tp.buscarReferencia(referencia));
                //System.out.println("Se ha encontrado la pagina " + referencia + " en el marco de RAM " + marco);
                this.direcciones += 30;
                this.datos += 30;
                try {
                    tlb.agregarReferencia(referencia, marco);
                    //System.out.println("Se ha agregado la pagina " + referencia + " a la TLB");
                } catch (Exception e) {
                    //System.out.println("No se ha agregado la pagina " + referencia + " a la TLB");
                }
            }
        // Si está en la TLB
        } else {
            this.direcciones += 2;
            this.datos += 30;
            //System.out.println("Se ha encontrado la página " + referencia + " en la TLB");
            setNuevaReferencia(tlb.buscarReferencia(referencia));

        }

    }

}
