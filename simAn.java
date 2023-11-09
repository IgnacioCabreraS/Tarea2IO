import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
    // Definir las constantes para el algoritmo
    static final int MAX_ITERACIONES = 10; // Número máximo de iteraciones
    static double temperatura = 1000; // Temperatura inicial
    static final double TASA_ENFRIAMIENTO = 0.95; // Tasa de enfriamiento
    static Random generadorAleatorio = new Random(); // Generador de números aleatorios
    static final String NOMBRE_ARCHIVO = "S5.txt"; // Nombre del archivo de entrada


    /*----------------------------------MAIN--------------------------------*/
    public static void main(String[] args) throws FileNotFoundException {
        // Leer los datos desde el archivo
        Datos datos = leerDatos();

        // Ejecutar el algoritmo
        ArrayList<Integer> Solucion = funcionPrincipal(datos.distanciasTiendas, datos.matrizTiendas,datos.cantidadTiendas);

        // Imprimir la mejor solución encontrada
        System.out.println("Mejor solucion : (AUN NO FUNCIONA) " /*+ Solucion*/);
    }

    // Función para leer los datos desde el archivo
    static Datos leerDatos() throws FileNotFoundException {
        // Abrir el archivo de entrada
        Scanner lectorArchivo = new Scanner(new File(NOMBRE_ARCHIVO));
        System.out.println("Lectura archivo");

        // Leer la cantidad de tiendas desde el archivo (n)
        int cantidadTiendas = Integer.parseInt(lectorArchivo.nextLine());

        // Leer las distancias entre tiendas desde el archivo (l)
        String[] distanciasTiendas = lectorArchivo.nextLine().split(","); // Se separa la cadena de distancias y se guardan en "distanciasTiendas"
        ArrayList<Integer> distancias = new ArrayList<>();
        for (String d : distanciasTiendas) { //Recorre del primer string hasta el ultimo segun distanciasTiendas
            distancias.add(Integer.parseInt(d));
        }
        

        // Leer la matriz de tiendas desde el archivo
        int[][] matriz = new int[cantidadTiendas][cantidadTiendas];
        for (int i = 0; i < cantidadTiendas; i++) {
            String[] linea = lectorArchivo.nextLine().split(",");
            for (int j = 0; j < linea.length; j++) {
                matriz[i][j] = Integer.parseInt(linea[j]);
            }
        }

        // Cerrar el archivo de entrada
        lectorArchivo.close();
        // Devolver los datos en un objeto Datos
        return new Datos(cantidadTiendas, distancias, matriz);
    }

    // Función para generar una solución vecina intercambiando dos elementos
    // aleatorios
    static ArrayList<Integer> movSwap(ArrayList<Integer> distTienda, ArrayList<Integer> listaIndiceAux) {
        // Generar dos índices aleatorios
        int indice1 = generadorAleatorio.nextInt(distTienda.size());
        int indice2 = generadorAleatorio.nextInt(distTienda.size());

        // Asegurarse de que los índices sean diferentes
        while (indice1 == indice2) {
            indice2 = generadorAleatorio.nextInt(distTienda.size());
        }

        // Intercambiar elementos en distTienda
        int temporal = distTienda.get(indice1);
        distTienda.set(indice1, distTienda.get(indice2));
        distTienda.set(indice2, temporal);

        // Intercambiar elementos en listaIndiceAux
        temporal = listaIndiceAux.get(indice1);
        listaIndiceAux.set(indice1, listaIndiceAux.get(indice2));
        listaIndiceAux.set(indice2, temporal);

        ArrayList<Integer> listaFinal = new ArrayList<Integer>();
        listaFinal.addAll(listaIndiceAux);
        listaFinal.addAll(distTienda);

        return listaFinal;
    }

    // Función para calcular la distancia entre dos puntos (PDF)
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

    // Función para ejecutar el algoritmo de Recocido Simulado
    static ArrayList<Integer> funcionPrincipal(ArrayList<Integer> distanciasTiendas, int[][] matriz,
            int cantidadTiendas) {
        ArrayList<Integer> mejorSolucion = new ArrayList<Integer>();
        //ArrayList<Integer> listaCostos = new ArrayList<Integer>();
        ArrayList<Integer> listaIndice = new ArrayList<Integer>();
        int mejorCosto = 0;
        int evaluaciones = 0;
        //int cantidadSoluciones = 0;
        double costoActual = 0;

        for (int k = 0; k < distanciasTiendas.size(); k++) {
            listaIndice.add(k + 1);
        }

        ArrayList<Integer> solucionInicial = new ArrayList<Integer>(listaIndice);
        

        while (temperatura >= 100 && evaluaciones < MAX_ITERACIONES) {
            break;
            /* Hay que empezar a usar el movSwap, calcular las distancias totales y entre puntos, viendo entre los vecinos y las tiendas
            para luego empezar a calcular los costos y asi encontrar el mejor costo y las temperaturas que alcanza*/
        }

        System.out.println("Solucion inicial: " + solucionInicial);
        System.out.println("Costo inicial = (AUN NO FUNCIONA) " + costoActual);
        System.out.println("Costo mejor solución: (AUN NO FUNCIONA) " + mejorCosto);
        System.out.println("Max iteraciones: " + MAX_ITERACIONES);
        System.out.println("Cantidad de tiendas: " + cantidadTiendas);
        
        return mejorSolucion;
    }
}