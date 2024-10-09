package com.example.examen.paso4

fun f1(a: Double, b: Double, c: Double,
       solucion: (Double, Double) -> Boolean,
       lambda1: (Double, Double, Double) -> Double,
       lambda2: (Array<Double>) -> Double,
       lambda3: (Array<Double>) -> Double): Double {
    var resultadoLambda1 = 0.0
    if (a == 0.0) {
        val x = -c / b
        if (solucion(x, x)) {
            resultadoLambda1 = lambda1(x, x, x)
            println("Resultado lambda1: $resultadoLambda1")
        }
    } else {
        val discriminante = b * b - 4 * a * c
        if (discriminante > 0) {
            val x1 = (-b + Math.sqrt(discriminante)) / (2 * a)
            val x2 = (-b - Math.sqrt(discriminante)) / (2 * a)
            if (solucion(x1, x2)) {
                resultadoLambda1 = lambda1(x1, x2, 0.0)
                println("Resultado lambda1: $resultadoLambda1")
            }
        } else if (discriminante == 0.0) {
            val x = -b / (2 * a)
            if (solucion(x, x)) {
                resultadoLambda1 = lambda1(x, x, x)
                println("Resultado lambda1: $resultadoLambda1")
            }
        }
    }

    val array = Array(10) { it.toDouble() }
    println("Array: ${array.joinToString(", ")}")
    val resultadoLambda2 = lambda2(array)
    println("Resultado lambda2: $resultadoLambda2")
    val resultadoLambda3 = lambda3(array)
    println("Resultado lambda3: $resultadoLambda3")

    return resultadoLambda1 + resultadoLambda2 + resultadoLambda3
}

fun main() {
    val resultado = f1(1.0, -3.0, 2.0,
        { x1, x2 -> x1 * x1 - 3 * x1 + 2.0 == 0.0 },
        { x1, x2, x3 -> if (x1 == 0.0 && x2 == 0.0 && x3 == 0.0) 1.0 else x1 * x2 * x3 },
        { array -> array.sum() },
        { array -> array.sum() })
    println("Resultado final: $resultado")
}