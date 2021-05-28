package com.example.szymon_lepianka_wtorek14_calc

interface BrainInterface {
    public fun addNumber(value: String)
    public fun addOperation(value: String)
    public fun evaluateExpression(evaluatedWithRoot: Boolean): Double
    public fun removeLastOperation()
    public fun operationsIsEmpty(): Boolean
    public fun lengthOfNumbers(): Int
    public fun clearNumbersAndOperations()
}