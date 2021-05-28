package com.example.szymon_lepianka_wtorek14_calc

import java.lang.Error
import kotlin.math.sqrt

class Brain : BrainInterface {
    var numbers = mutableListOf<Double>()
    var operations = mutableListOf<String>()

    override fun addNumber(value: String) {
        numbers.add(0, value.toDouble())
    }

    override fun addOperation(value: String) {
        operations.add(0, value)
    }

    override fun operationsIsEmpty(): Boolean {
        return operations.isEmpty()
    }

    override fun lengthOfNumbers(): Int {
        return numbers.size
    }

    override fun clearNumbersAndOperations() {
        operations.clear()
        numbers.clear()
    }

    @ExperimentalStdlibApi
    override fun evaluateExpression(evaluatedWithRoot: Boolean): Double {
        println(numbers.toString())
        println(operations.toString())
        if (evaluatedWithRoot) {
            val removeLast = numbers.removeLast()
            clearNumbersAndOperations()
            numbers.add(sqrt(removeLast))
            return numbers.removeLast()
        }
        while (numbers.size > 1) {
            numbers.add(eval(numbers.removeLast(), numbers.removeLast(), operations.removeLast()))
        }
        return numbers.removeLast()
    }

    @ExperimentalStdlibApi
    override fun removeLastOperation() {
        operations.removeLast()
    }

    private fun eval(a: Double, b: Double, op: String): Double {
        when (op) {
            "+" -> return a + b
            "*" -> return a * b
            "-" -> return a - b
            "/" -> return a / b
        }
        throw Error()
    }
}