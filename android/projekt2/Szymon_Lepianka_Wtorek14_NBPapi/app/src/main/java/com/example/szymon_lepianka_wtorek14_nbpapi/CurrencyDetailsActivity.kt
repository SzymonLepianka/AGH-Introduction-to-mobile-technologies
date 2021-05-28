package com.example.szymon_lepianka_wtorek14_nbpapi

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONObject

class CurrencyDetailsActivity : AppCompatActivity() {
    internal lateinit var todayRate: TextView
    internal lateinit var yesterdayRate: TextView
    internal lateinit var lineChart7Days: LineChart
    internal lateinit var lineChart30Days: LineChart
    internal lateinit var currencyCode: String
    internal lateinit var table: String
    internal lateinit var historicRates: Array<Pair<String, Double>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency_details)
        currencyCode = intent.getStringExtra("currencyCode")!!
        table = intent.getStringExtra("table")!!
        lineChart7Days = findViewById(R.id.historic7RatesChart)
        lineChart30Days = findViewById(R.id.historic30RatesChart)
        todayRate = findViewById(R.id.todayRateTextView)
        yesterdayRate = findViewById(R.id.yesterdayRateTextView)
        getData(table)
    }

    fun getData(table: String) {
        val queue = DataHolder.queue
        val url = "http://api.nbp.pl/api/exchangerates/rates/%s/%s/last/30?format=json".format(
            table,
            currencyCode
        )
        val historicRatesRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                println("Success")
                loadDetails(response)
                setData()
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        )
        queue.add(historicRatesRequest)
    }

    private fun setData() {
        todayRate.text = getString(R.string.todayRateTextView, historicRates.last().second)
        yesterdayRate.text =
            getString(R.string.yesterdayRateTextView, historicRates[historicRates.size - 2].second)

        val entries30Days = ArrayList<Entry>()
        for ((index, element) in historicRates.withIndex()) {
            entries30Days.add(Entry(index.toFloat(), element.second.toFloat()))
        }
        val entries7Days = entries30Days.subList(historicRates.size - 7, historicRates.size)

        val entries30DaysDataSet = LineDataSet(entries30Days, "Kursy")
        entries30DaysDataSet.color = Color.RED
        entries30DaysDataSet.setCircleColors(Color.RED)
        entries30DaysDataSet.valueTextSize = 9F
        var lineData = LineData(entries30DaysDataSet)
        lineChart30Days.data = lineData
        var description = Description()
        description.text = "Kurs %s z ostatnich 30 dni".format(currencyCode)
        description.textSize = 12F
        lineChart30Days.description = description
        lineChart30Days.xAxis.valueFormatter =
            IndexAxisValueFormatter(historicRates.map { it.first }.toTypedArray())
        lineChart30Days.invalidate()

        val entries7DaysDataSet = LineDataSet(entries7Days, "Kursy")
        entries7DaysDataSet.color = Color.BLUE
        entries7DaysDataSet.setCircleColors(Color.BLUE)
        entries7DaysDataSet.valueTextSize = 12F
        lineData = LineData(entries7DaysDataSet)
        lineChart7Days.data = lineData
        description = Description()
        description.text = "Kurs %s z ostatnich 7 dni".format(currencyCode)
        description.textSize = 12F
        lineChart7Days.description = description
        lineChart7Days.xAxis.valueFormatter =
            IndexAxisValueFormatter(historicRates.map { it.first }.toTypedArray())
        lineChart7Days.invalidate()
    }

    private fun loadDetails(response: JSONObject?) {
        response?.let {
            val rates = response.getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpData = arrayOfNulls<Pair<String, Double>>(ratesCount)
            for (i in 0 until ratesCount) {
                val date = rates.getJSONObject(i).getString("effectiveDate")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")
                tmpData[i] = Pair(date, currencyRate)
            }
            this.historicRates = tmpData as Array<Pair<String, Double>>
        }
    }
}