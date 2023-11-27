import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//Clase de datos de las tiendas
class Datos {
    int cantidadTiendas;
    ArrayList<Integer> distanciasTiendas;
    int[][] matrizTiendas;

    Datos(int cantidadTiendas, ArrayList<Integer> distanciasTiendas, int[][] matrizTiendas) {
        this.cantidadTiendas = cantidadTiendas;
        this.distanciasTiendas = distanciasTiendas;
        this.matrizTiendas = matrizTiendas;
    }
}

public class simAn {
    //Constantes del codigo
    static final int MAX_ITERACIONES = 80; // Número máximo de iteraciones
    static double temperatura = 500; // Temperatura inicial
    static final double TASA_ENFRIAMIENTO = 0.90; // Tasa de enfriamiento
    static Random generadorAleatorio = new Random(); // Generador de números aleatorios
    static final String NOMBRE_ARCHIVO = "instancias\\QAP_sko56_04_n.txt"; // Nombre del archivo de entrada

    public static void main(String[] args) throws FileNotFoundException {
        //Leer los datos desde el archivo
        Datos datos = leerDatos();

        //Ejecutar el algoritmo de Recocido Simulado y obtener la mejor solución
        ArrayList<Integer> Solucion = simulatedAnnealing(datos.distanciasTiendas, datos.matrizTiendas, datos.cantidadTiendas);
        
        //Imprimir la mejor solución encontrada
        System.out.println("Mejor solución encontrada: " + Solucion );
    }

    //Función para abrir el archivo de datos y leerlo
    static Datos leerDatos() throws FileNotFoundException {
        //Abrir el archivo de entrada
        Scanner lectorArchivo = new Scanner(new File(NOMBRE_ARCHIVO));
        System.out.println("PASO 1");

        //Leer la cantidad de tiendas
        int cantidadTiendas = Integer.parseInt(lectorArchivo.nextLine());

        //Leer las distancias entre tiendas
        System.out.println("PASO 2");
        String[] distanciasTiendas = lectorArchivo.nextLine().split(",");
        ArrayList<Integer> distancias = new ArrayList<>();
        for (String distancia : distanciasTiendas) {
            distancias.add(Integer.parseInt(distancia));
        }

        //Leer la matriz de tiendas
        System.out.println("PASO 3");
        int[][] matriz = new int[cantidadTiendas][cantidadTiendas];
        for (int i = 0; i < cantidadTiendas; i++) {
            String[] linea = lectorArchivo.nextLine().split(",");
            for (int j = 0; j < linea.length; j++) {
                matriz[i][j] = Integer.parseInt(linea[j]);
            }
        }

        //Cerrar el archivo
        lectorArchivo.close();

        //Devolver los datos en un objeto de tipo Datos
        return new Datos(cantidadTiendas, distancias, matriz);
    }

    //Función para generar una solución vecina intercambiando dos elementos aleatorios
    static ArrayList<Integer> generarVecino(ArrayList<Integer> distTienda, ArrayList<Integer> listaIndiceAux) {
        // Generar dos índices aleatorios
        int indice1 = generadorAleatorio.nextInt(distTienda.size());
        int indice2 = generadorAleatorio.nextInt(distTienda.size());

        //Asegurarse de que los índices sean diferentes
        while (indice1 == indice2) {
            indice2 = generadorAleatorio.nextInt(distTienda.size());
        }

        //Intercambiar elementos en distTienda
        int temporal = distTienda.get(indice1);
        distTienda.set(indice1, distTienda.get(indice2));
        distTienda.set(indice2, temporal);

        //Intercambiar elementos en listaIndiceAux
        temporal = listaIndiceAux.get(indice1);
        listaIndiceAux.set(indice1, listaIndiceAux.get(indice2));
        listaIndiceAux.set(indice2, temporal);

        ArrayList<Integer> listaDef = new ArrayList<Integer>();
        listaDef.addAll(listaIndiceAux);
        listaDef.addAll(distTienda);

        return listaDef;
    }

    //Función para calcular la distancia entre dos puntos
    static double calcularDistanciaEntrePuntos(int i, int j, ArrayList<Integer> distTienda, int[][] matrizT) {
        double distanciaTotal = (distTienda.get(i) / 2.0) + (distTienda.get(j) / 2.0);
        if (Math.abs(i - j) > 1) {
            for (int k = i + 1; k < j - 1; k++) {
                distanciaTotal += distTienda.get(k);
            }
        }

        distanciaTotal = distanciaTotal * matrizT[i][j];
        return distanciaTotal;
    }

    //Función para calcular la distancia total de una solución
    static double calcularDistanciaTotal(int cantidadTiendas, int[][] matriz, ArrayList<Integer> distTiendas) {
        double longitudTotal = 0;

        for (int i = 0; i < cantidadTiendas - 1; i++) {
            for (int j = i + 1; j < cantidadTiendas; j++) {
                longitudTotal += calcularDistanciaEntrePuntos(i, j, distTiendas, matriz);
            }
        }

        return longitudTotal;
    }

    //Función para ejecutar el Simulated Annealing.
    static ArrayList<Integer> simulatedAnnealing(ArrayList<Integer> distanciasTiendas, int[][] matriz, int cantidadTiendas) {
        //Datos del simulated annealing.
        ArrayList<Integer> mejorSolucion = new ArrayList<Integer>();
        ArrayList<Integer> listaCostos = new ArrayList<Integer>();
        ArrayList<Integer> listaIndice = new ArrayList<Integer>();
        int mejorCosto = 0;
        int evaluaciones = 0;
        int cantidad = 0;
        double costoActual = 0;

        for (int k = 0; k < distanciasTiendas.size(); k++) {
            listaIndice.add(k + 1);
        }


        ArrayList<Integer> solucionInicial = new ArrayList<Integer>(listaIndice);
        ArrayList<Integer> sActual = new ArrayList<Integer>(listaIndice);
        ArrayList<Integer> longTiendas = new ArrayList<Integer>(distanciasTiendas);



        while (temperatura >= 100 && evaluaciones < MAX_ITERACIONES) {
            //Datos para generar un vecino, siguiente vecino y sacar las distancias entre tiendas.
            ArrayList<Integer> listasAux = generarVecino(distanciasTiendas, listaIndice);
            ArrayList<Integer> sVecino = new ArrayList<Integer>(listasAux.subList(0, listasAux.size() / 2));
            ArrayList<Integer> auxDistTiendas = new ArrayList<Integer>(listasAux.subList(listasAux.size() / 2, listasAux.size()));

            //Costo de la primera solución.
            costoActual = calcularDistanciaTotal(matriz.length, matriz, longTiendas);
            double costoVecino = calcularDistanciaTotal(matriz.length, matriz, auxDistTiendas);
            double deltaS = costoVecino - costoActual;


            //Se verifica si el vecino tiene mejor solución que la actual.
            if (deltaS < 0) {
                cantidad++;
                sActual = new ArrayList<Integer>(sVecino);
                mejorCosto = (int) costoVecino;
            } else {
                double probabilidad = Math.exp((deltaS * -1) / temperatura);
                if (generadorAleatorio.nextDouble() < probabilidad) {
                    cantidad++;
                    sActual = new ArrayList<Integer>(sVecino);
                    mejorCosto = (int) costoVecino;
                }
            }
            //Calculo de la mejor solución con el mejor vecino.
            mejorSolucion = new ArrayList<Integer>(sActual);
            temperatura *= TASA_ENFRIAMIENTO;
            evaluaciones++;
            listaCostos.add(mejorCosto);
        }


        int media = (int) listaCostos.stream().mapToInt(val -> val).average().orElse(0.0);
        double desviacionEstandar = calcularDesviacionEstandar(listaCostos, media);

        System.out.println();
        System.out.println("-Resultados-");
        System.out.println("Solución inicial: " + solucionInicial);
        System.out.println("Costo inicial: " + costoActual);
        System.out.println("Cantidad de iteraciones: " + MAX_ITERACIONES);
        System.out.println("Total de tiendas: " + cantidadTiendas);
        System.out.println("Cantidad de soluciones encontradas: " + cantidad);
        System.out.println("Cantidad de evaluaciones encontradas: " + evaluaciones);
        System.out.println("Costo de la mejor evaluación: " + mejorCosto);
        System.out.println("Media: " + media);
        System.out.println("Desviación estandar: " + desviacionEstandar);

        return mejorSolucion;
    }

//Función para calcular la desviación estándar de los costos
static double calcularDesviacionEstandar(ArrayList<Integer> listaCostos, double media) {
    double sum = 0.0;
    for (int i : listaCostos) {
        sum += Math.pow((i - media), 2);
    }
    return Math.sqrt(1.0 * sum / listaCostos.size());
}
}