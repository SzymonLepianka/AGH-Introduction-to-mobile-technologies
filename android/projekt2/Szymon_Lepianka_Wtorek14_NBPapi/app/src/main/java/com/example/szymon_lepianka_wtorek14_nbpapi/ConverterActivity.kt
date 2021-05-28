package com.example.szymon_lepianka_wtorek14_nbpapi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ConverterActivity : AppCompatActivity() {
    internal var currencyRate: Double = 0.0
    internal lateinit var editText: EditText
    private lateinit var currencyCode: TextView
    internal lateinit var editText2: EditText
    private var machineChangedEditText = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)
        currencyCode = findViewById(R.id.CurrencyCodeConverterTextView)
        currencyCode.text = intent.getStringExtra("currencyCode").toString()
        currencyRate = intent.getDoubleExtra("rate", 1.0)
        editText = findViewById(R.id.editText)
        editText2 = findViewById(R.id.editText2)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!machineChangedEditText) {
                    machineChangedEditText = true;
                    if (s.toString() != "") {
                        var sFloat = s.toString().toDouble()
                        sFloat /= currencyRate
                        editText2.setText("" + sFloat)
                    } else {
                        editText2.setText("")
                    }
                    machineChangedEditText = false;
                }
            }
        })
        editText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!machineChangedEditText) {
                    machineChangedEditText = true;
                    if (s.toString() != "") {
                        var sFloat = s.toString().toDouble()
                        sFloat *= currencyRate
                        editText.setText("" + sFloat)
                    } else {
                        editText.setText("")
                    }
                    machineChangedEditText = false;
                }
            }
        })
    }
}