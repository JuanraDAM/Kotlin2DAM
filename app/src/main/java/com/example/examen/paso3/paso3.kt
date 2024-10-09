package com.example.examen.paso3

fun f1(a: Double, b: Double, c: Double, solucion: (Double, Double) -> Boolean, lambda: (Double, Double, Double) -> Double): DoubleArray {
    if (a == 0.0) {
        val x = -c / b
        if (solucion(x, x)) {
            val resultadoLambda = lambda(x, x, x)
            println("Resultado f1: [2.0, $x, 0.0]")
            println("Resultado lambda: $resultadoLambda")
            return doubleArrayOf(2.0, x, 0.0)
        } else {
            return doubleArrayOf(0.0, 0.0, 0.0)
        }
    } else {
        val discriminante = b * b - 4 * a * c
        if (discriminante > 0) {
            val x1 = (-b + Math.sqrt(discriminante)) / (2 * a)
            val x2 = (-b - Math.sqrt(discriminante)) / (2 * a)
            if (solucion(x1, x2)) {
                val resultadoLambda = lambda(x1, x2, 0.0)
                println("Resultado f1: [1.0, $x1, $x2]")
                println("Resultado lambda: $resultadoLambda")
                return doubleArrayOf(1.0, x1, x2)
            } else {
                return doubleArrayOf(0.0, 0.0, 0.0)
            }
        } else if (discriminante == 0.0) {
            val x = -b / (2 * a)
            if (solucion(x, x)) {
                val resultadoLambda = lambda(x, x, x)
                println("Resultado f1: [1.0, $x, $x]")
                println("Resultado lambda: $resultadoLambda")
                return doubleArrayOf(1.0, x, x)
            } else {
                return doubleArrayOf(0.0, 0.0, 0.0)
            }
        } else {
            return doubleArrayOf(0.0, 0.0, 0.0)
        }
    }
}

fun f2(a: Double, b: Double, c: Double, lambda: (Array<Double>) -> Double): Double {
    val array = Array(10) { it.toDouble() } // Crear un array de 10 elementos con valores desde 0.0 hasta 9.0
    val resultado = lambda(array) // Invocar la lambda con el array como parámetro
    println("Array: ${array.joinToString(", ")}")
    println("Resultado de la invocación a f2: $resultado")
    return resultado
}

fun main() {
    println("Resultado1")
    // Caso 1: Dos soluciones
    val resultado1 = f1(1.0, -3.0, 2.0, { x1, x2 -> x1 * x1 - 3 * x1 + 2.0 == 0.0 }, { x1, x2, x3 -> if (x1 == 0.0 && x2 == 0.0 && x3 == 0.0) 1.0 else x1 * x2 * x3 })
    println("\nResultado2")
    // Caso 2: Dos soluciones dobles
    val resultado2 = f1(1.0, -2.0, 1.0, { x1, x2 -> x1 * x1 - 2 * x1 + 1.0 == 0.0 }, { x1, x2, x3 -> if (x1 == 0.0 && x2 == 0.0 && x3 == 0.0) 1.0 else x1 * x2 * x3 })

    println("\nResultado3")
    // Caso 3: Una única solución
    val resultado3 = f1(0.0, 4.0, -8.0, { x1, x2 -> 4 * x1 - 8.0 == 0.0 }, { x1, x2, x3 -> if (x1 == 0.0 && x2 == 0.0 && x3 == 0.0) 1.0 else x1 * x2 * x3 })

    // Caso 4: No tiene solución
    val resultado4 = f1(1.0, 2.0, 3.0, { x1, x2 -> x1 * x1 + 2 * x1 + 3.0 == 0.0 }, { x1, x2, x3 -> if (x1 == 0.0 && x2 == 0.0 && x3 == 0.0) 1.0 else x1 * x2 * x3 })
    println("\nResultado 4 " + resultado4.toList())

    // Crear un array de 10 elementos e inicializarlos con una lambda
    val array = Array(10) { it.toDouble() }
    println("Array inicializado: ${array.joinToString(", ")}")

    // Invocar a f2 con una lambda que calcula el sumatorio del array
    val resultadoF2 = f2(1.0, 2.0, 3.0) { array ->
        array.sum() // Lambda que calcula el sumatorio del array
    }
    println("Resultado de la invocación a f2 almacenado en resultadoF2: $resultadoF2")
}