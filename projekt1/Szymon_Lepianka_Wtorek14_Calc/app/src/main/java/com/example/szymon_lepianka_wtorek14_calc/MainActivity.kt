// Szymon Lepianka wtorek 14:00
// 1. a) zajmują, b) są zdefiniowane, c) zawiera
// 2. a) jest interfejs, b) własna implementacja - dwie listy
// 3. a) jest, b) nadpisuje, c) ciężko powiedzieć, mam nadzieję wszystkie :)
// 4. MainActivity.lastNumber.set()
// 5. jest
// 6. jest - ellipsize, singleLine
// 7. np. wpisanie 2+5+ spowoduje wyświetlenie 7, następnie klikając np. 20 i = wyświetli 27, można oczywiście kontynuować cząstowe wyniki np. 20+8-7*6
// 8. obsługa pierwiastków
// 9. poniekąd jest dobra kolejność bo zawsze jest tylko jedno działanie do wykonania (jak na analogowym kalkulatorze)

package com.example.szymon_lepianka_wtorek14_calc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    internal lateinit var buttonClear: Button
    internal lateinit var buttonEquals: Button
    internal lateinit var buttonDot: Button
    internal lateinit var buttonRoot: Button
    internal lateinit var digitButtons: Array<Button>

    internal lateinit var calcDisplay: TextView

    private var digitWasLastUsed = true
    private var dotUsed = false
    private var lastOperation = false
    private var lastNumber = ""
        set(value) {
            if (value == "") {
                setDisplay(0)
            } else {
                setDisplay(value)
            }
            field = value
        }

    internal val calcEngine: BrainInterface = Brain()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonClear = findViewById(R.id.buttonClear)
        buttonDot = findViewById(R.id.buttonDot)
        buttonEquals = findViewById(R.id.buttonEquals)
        buttonRoot = findViewById(R.id.buttonRoot)
        calcDisplay = findViewById(R.id.calcOutput)

        val digitButtonsIDs = arrayOf(
            R.id.button0,
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9
        )

        digitButtons = (digitButtonsIDs.map { id -> findViewById<Button>(id) }).toTypedArray()

        val operationButtons = mutableMapOf<Button, String>()
        operationButtons[findViewById(R.id.buttonAddition)] = "+"
        operationButtons[findViewById(R.id.buttonSubtraction)] = "-"
        operationButtons[findViewById(R.id.buttonMultiplication)] = "*"
        operationButtons[findViewById(R.id.buttonDivision)] = "/"

        buttonClear.setOnClickListener { clear() }
        buttonEquals.setOnClickListener { evaluateFormula(true, false) }
        buttonDot.setOnClickListener { dotButtonPressed() }
        buttonRoot.setOnClickListener { rootButtonPressed() }
        digitButtons.forEach { button -> button.setOnClickListener { i -> buttonPressed(i as Button) } }
        for (button in operationButtons.keys){
            button.setOnClickListener {
                val s = operationButtons[button]
                if (!s.isNullOrEmpty())
                    operationPressed(s)
            }
        }
    }

    private fun rootButtonPressed() {
        evaluateFormula(false, true)
    }

    private fun buttonPressed(digit: Button) {
        if (digitWasLastUsed) {
            if (lastNumber == "") {
                lastNumber = digit.text.toString()
            } else {
                var j = false;
                for (element in lastNumber) {
                    if (element != '0') j = true
                }
                if (!lastNumber.contains('.') && digit.text.toString() == "0" && !j) {
                    println(lastNumber)
                } else {
                    lastNumber += digit.text.toString()
                }
            }
        } else {
            calcEngine.addNumber(lastNumber)
            digitWasLastUsed = true
            lastNumber = digit.text.toString()
        }
        lastOperation = false
    }

    private fun evaluateFormula(evaluatedWithEquals: Boolean, evaluatedWithRoot: Boolean) {
        if (lastNumber.last() == '.') lastNumber += "0"
        calcEngine.addNumber(lastNumber)
        val result = calcEngine.evaluateExpression(evaluatedWithRoot)
        lastNumber = result.toString()
        if (!evaluatedWithEquals) {
            digitWasLastUsed = false
            lastOperation = true
        }
    }

    private fun clear() {
        digitWasLastUsed = true
        dotUsed = false
        lastNumber = ""
        lastOperation = false
        calcEngine.clearNumbersAndOperations()
    }

    private fun operationPressed(operation: String) {
        calcEngine.addOperation(operation)
        if (!lastOperation) {
            lastOperation = true
            println(calcEngine.lengthOfNumbers())
            println(calcEngine.operationsIsEmpty())
            if (calcEngine.lengthOfNumbers() == 1 && !calcEngine.operationsIsEmpty()) {
                evaluateFormula(false, false)
                return
            }
        } else {
            calcEngine.removeLastOperation()
        }
        digitWasLastUsed = false
    }

    private fun dotButtonPressed() {
        if (!dotUsed) {
            dotUsed = true
            digitWasLastUsed = true
            lastNumber += "."
        } else if (lastNumber == "") {
            dotUsed = true
            digitWasLastUsed = true
            lastNumber += "0."
        }
    }

    private fun setDisplay(text: String) {
        calcDisplay.text = text
    }

    private fun setDisplay(value: Int) {
        calcDisplay.text = value.toString()
    }
}