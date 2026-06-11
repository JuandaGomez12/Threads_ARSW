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
| 2                 | 168.234     |
| 5                 | 171.562     |
| 10                | 169.847     |
| 50                | 173.291     |
| 100               | 176.388     |
| 200               | 164.405     |
| 300               | 164.114     |
| 500               | 188.734     |
| 700               | 176.413    |
| 1000              | 193.856     |
| 2000              | 181.461    |
| 5000              | 174.137      |

### Go

| Cantidad de hilos (goroutines) | Tiempo (ms) |
|:------------------------------:|:-----------:|
| 1                              | 147.001     |
| 2                              | 149.234     |
| 5                              | 152.871     |
| 10                             | 156.123     |
| 50                             | 161.445     |
| 100                            | 164.484     |
| 200                            | 170.482     |
| 300                            | 163.455     |
| 500                            | 164.892     |
| 700                            | 163.552     |
| 1000                           | 164.550     |
| 2000                           | 171.892     |
| 5000                           | 179.469     |

---

## Análisis de Resultados

### Comportamiento en Java

Entre 1 y 2000 hilos, los tiempos en Java se mantienen relativamente estables en el rango de **164 ms a 193 ms**. Esto se debe al **overhead** de administrar hilos del sistema operativo: crearlos, sincronizarlos y decidir cuál corre en cada momento. En ese rango ese costo existe pero es manejable, y la ejecución se mantiene predecible.

Sin embargo, al escalar a **5000 hilos** el comportamiento cambia drásticamente: el tiempo sube a **174.137 ms** (aproximadamente 174 segundos), representando un colapso de rendimiento de más de **1.000x** respecto a configuraciones menores. Esto evidencia el límite real de los hilos del sistema operativo: crear y gestionar 5000 hilos bajo contención genera un overhead de cambio de contexto tan alto que el sistema deja de avanzar de forma útil.

### Comportamiento en Go

Con **1 goroutine** Go logra el mejor tiempo de toda la prueba: 147.001 ms, lo que muestra que su runtime es muy eficiente cuando no hay concurrencia de por medio.

A diferencia de Java, Go mantiene tiempos muy consistentes en la mayoría de configuraciones, moviéndose entre 147.001 ms y 179.469 ms. Esto se debe a que el scheduler de Go multiplexa las goroutines sobre un número reducido de hilos del sistema operativo. A escalas altas, el sistema operativo nunca llega a gestionar miles de hilos reales: Go los abstrae internamente, evitando el colapso que sí ocurre en Java.

A partir de **100 goroutines** los tiempos se estabilizan cerca de los 164 ms. Sin embargo, a partir de **2000 goroutines** se observa un leve incremento (171 ms → 179 ms), lo que indica que incluso el scheduler de Go empieza a sentir algo de presión a escalas muy altas, aunque sin comparación con el colapso de Java.

---

## Conclusiones

1. **Más hilos no significa más velocidad.** En ambos lenguajes, agregar hilos no redujo el tiempo de ejecución en el rango moderado. La tarea de imprimir en consola es secuencial por naturaleza, lo que limita el beneficio real de la concurrencia.

2. **Java colapsa a escalas extremas de hilos.** Entre 1 y 2000 hilos Java se mantiene en ~164–193 ms, pero con 5000 hilos el tiempo escala a ~174 segundos. Esto refleja el costo real de crear y gestionar miles de hilos del sistema operativo bajo contención masiva.

3. **Go escala sin degradarse significativamente.** Con 5000 goroutines Go sube a 179 ms frente a los ~164 ms del rango estable, un incremento leve pero manejable. Esto contrasta radicalmente con el colapso de Java a ~174 segundos, demostrando la ventaja del modelo de goroutines frente a los hilos del sistema operativo.

4. **Go es más eficiente en ejecución secuencial.** Con un solo hilo/goroutine, Go superó a Java por cerca de 23 ms, lo que refleja la eficiencia de su runtime para este tipo de tareas.

5. **La concurrencia funciona mejor cuando las tareas son independientes.** Cuando todos los hilos compiten por el mismo recurso, como la consola, el overhead de sincronización reduce el beneficio del paralelismo en ambos lenguajes.