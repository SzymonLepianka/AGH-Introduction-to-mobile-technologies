package com.example.szymon_lepianka_wtorek14_nbpapi

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONArray

class GoldActivity : AppCompatActivity() {

    private lateinit var todayGoldRate: TextView
    private lateinit var lineChartGold30Days: LineChart
    private lateinit var historicGoldRates: Array<Pair<String, Double>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold)
        todayGoldRate = findViewById(R.id.todayGoldRateTextView)
        lineChartGold30Days = findViewById(R.id.historic30GoldRatesChart)
        getData()
    }

    private fun getData() {

//        val queue = Volley.newRequestQueue(applicationContext)
        val queue = DataHolder.queue
        val url = "http://api.nbp.pl/api/cenyzlota/last/30?format=json"

//        val url = "http://api.nbp.pl/api/exchangerates/rates/%s/%s/last/30?format=json".format("A", "EUR")

        val historicGoldRatesRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                println("Success")
                loadDetails(response)
                setData()
            },
            Response.ErrorListener() {
                Toast.makeText(applicationContext,"ERROR", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        )
        queue.add(historicGoldRatesRequest)
    }

    private fun loadDetails(response: JSONArray?) {
        response?.let {
            val goldRatesCount = response.length()
            val tmpData = arrayOfNulls<Pair<String, Double>>(goldRatesCount)
            for (i in 0 until goldRatesCount) {
                val date = response.getJSONObject(i).getString("data")
                val goldRate = response.getJSONObject(i).getDouble("cena")
                tmpData[i] = Pair(date, goldRate)
            }
            this.historicGoldRates = tmpData as Array<Pair<String, Double>>
        }
    }

    private fun setData() {
        todayGoldRate.text =
            getString(R.string.todayGoldRateTextView, historicGoldRates.last().second)
        val entries30Days = ArrayList<Entry>()
        for ((index, element) in historicGoldRates.withIndex()) {
            entries30Days.add(Entry(index.toFloat(), element.second.toFloat()))
        }

        val entries30DaysDataSet = LineDataSet(entries30Days, "Kursy złota")
        entries30DaysDataSet.color = Color.RED
        entries30DaysDataSet.setCircleColors(Color.RED)
        entries30DaysDataSet.valueTextSize = 9F
        val lineData = LineData(entries30DaysDataSet)
        lineChartGold30Days.data = lineData
        val description = Description()
        description.text = "Kurs złota z ostatnich 30 dni"
        description.textSize = 12F
        lineChartGold30Days.description = description
        lineChartGold30Days.xAxis.valueFormatter =
            IndexAxisValueFormatter(historicGoldRates.map { it.first }.toTypedArray())
        lineChartGold30Days.invalidate()
    }
}