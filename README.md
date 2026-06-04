#  Evaluación de Hilos en Java y Go
 
**Tema:** Concurrencia y Programación Multihilo  

---

## Objetivo

En esta actividad se requiere que la temática vista en clase sobre los hilos sea evaluada midiendo cuánto tiempo se demora en imprimir hasta el número 5.000.000, incrementando progresivamente la cantidad de hilos para obtener conclusiones sobre el comportamiento de la concurrencia en dos lenguajes distintos: Java y Go.

---

## Marco Teórico

La concurrencia es la capacidad de un programa de ejecutar varias tareas de forma simultánea o aparentemente simultánea. En programación, esto se logra a través del uso de **hilos** (threads), que son unidades de ejecución independientes dentro de un mismo proceso.

Existen dos modelos principales de concurrencia relevantes para esta actividad:

- Hilos: Son los que gestionan directamente por el sistema operativo. Java los utiliza por defecto a través de su clase y su extension Thread. 

- Go: Go implementa su propio modelo de concurrencia mediante goroutines, que son hilos ligeros gestionados por el runtime de Go, no directamente por el sistema operativo. En Go multiplexa muchas goroutines sobre un número reducido de hilos del sistema operativo, lo que en teoría reduce el costo por unidad de concurrencia.

Un aspecto clave a entender es que más hilos no siempre significan más velocidad. La creación, sincronización y cambio de contexto entre hilos genera un overhead que puede degradar el rendimiento si no se usa de forma adecuada.

---

## Metodología

Se implementó un programa en Java y otro en **Go** que imprimen los números del 1 al 5.000.000 distribuyendo la carga entre una cantidad variable de hilos. Se midió el tiempo total de ejecución en milisegundos para cada configuración de hilos: 1, 100, 200, 300, 700 y 1000.

---

## Resultados

### Java

| Cantidad de hilos | Tiempo (ms) |
|:-----------------:|:-----------:|
| 1                 | 170.418     |
| 100               | 176.388     |
| 200               | 164.405     |
| 300               | 207.300     |
| 700               | 162.032     |
| 1000              | 193.856     |

### Go

| Cantidad de hilos (goroutines) | Tiempo (ms) |
|:------------------------------:|:-----------:|
| 1                              | 147.001     |
| 100                            | 164.484     |
| 200                            | 170.482     |
| 300                            | 163.455     |
| 700                            | 163.552     |
| 1000                           | 164.550     |

---

## Análisis de Resultados

### Comportamiento en Java

Los tiempos en Java se mantienen estables entre 162.032 ms y 207.300 ms sin importar cuántos hilos se usen. Esto se debe al **overhead**, que es el trabajo extra que el sistema debe hacer solo para administrar los hilos: crearlos, sincronizarlos y decidir cuál corre en cada momento. En Java ese costo existe siempre, pero es predecible.

El pico de 207.300 ms con 300 hilos ocurre por **contención**: todos los hilos quieren escribir en la consola al mismo tiempo, pero solo uno puede hacerlo a la vez, entonces los demás esperan. Esa espera acumulada sube el tiempo total.

### Comportamiento en Go

Con **1 goroutine** Go logra el mejor tiempo de toda la prueba: 147.001 ms, lo que muestra que su runtime es muy eficiente cuando no hay concurrencia de por medio.

A diferencia de Java, Go mantiene tiempos muy consistentes en todas las configuraciones, moviéndose entre 147.001 ms y 170.482 ms. Esto se debe a que el scheduler de Go gestiona el overhead de forma más eficiente: en lugar de dejar que todas las goroutines compitan directamente por los recursos, él mismo decide cuándo y cómo repartir el trabajo, evitando los picos de contención que sí aparecen en Java.

A partir de **100 goroutines** los tiempos se estabilizan cerca de los 164 ms, lo que indica que el scheduler encuentra rápidamente un equilibrio en la distribución de la carga.

---

## Conclusiones

1. **Más hilos no significa más velocidad.** En ambos lenguajes, agregar hilos no redujo el tiempo de forma considerable. La tarea de imprimir en consola es secuencial por naturaleza, lo que limita el beneficio real de la concurrencia.

2. **Go es más rápido en ejecución secuencial.** Con un solo hilo Go superó a Java por cerca de 23.000 ms, lo que refleja la eficiencia de su runtime para este tipo de tareas.

3. **El overhead en Go está mejor controlado.** El scheduler de Go administra las goroutines de forma más eficiente que el sistema operativo administra los hilos de Java, lo que se traduce en menos variación entre configuraciones.

4. **Java es menos predecible bajo contención.** Aunque no fue el más lento, Java presentó más variación entre configuraciones, especialmente el pico con 300 hilos.

5. **La concurrencia funciona mejor cuando las tareas son independientes.** Cuando todos los hilos compiten por el mismo recurso, como la consola, el overhead de sincronización reduce el beneficio del paralelismo en ambos lenguajes.