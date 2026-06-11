import java.time.Duration;
import java.time.Instant;
public class Contador extends Thread {
   private long inicio;
   private long fin;
   public Contador(long inicio, long fin) {
       this.inicio = inicio;
       this.fin = fin;
   }
   public void run() {
       for (long i = inicio; i <= fin; i++) {
           System.out.println(i);
       }
   }
   public static void main(String[] args) throws InterruptedException {
       long limite = 5000000;  
       int numHilos = 300;         
       long rango = limite / numHilos;
       Contador[] hilos = new Contador[numHilos];
       for (int i = 0; i < numHilos; i++) {
           long inicio = i * rango + 1;
           long fin = (i == numHilos - 1) ? limite : (i + 1) * rango;
           hilos[i] = new Contador(inicio, fin);
       }
       Instant inicio = Instant.now();
       for (Contador h : hilos) h.start();
       for (Contador h : hilos) h.join();
       Instant fin = Instant.now();
       System.out.println("Tiempo: " + Duration.between(inicio, fin).toMillis() + " ms");
   }
}