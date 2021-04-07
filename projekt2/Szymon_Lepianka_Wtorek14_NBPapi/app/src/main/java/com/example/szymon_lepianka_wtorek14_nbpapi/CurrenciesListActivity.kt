package com.example.szymon_lepianka_wtorek14_nbpapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray

class CurrenciesListActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CurrenciesListAdapter
    private lateinit var dataSet: Array<CurrencyDetails>
    private lateinit var yesterdayDataSet: Array<Boolean>
    var converter: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_curriencies_list)
//        // dostajemy się do przekazanych wartości:
//        val text = intent.getStringExtra("Message")
//        findViewById<TextView>(R.id.textView).text = text

        converter = intent.getBooleanExtra("converter", false)
        recycler = findViewById(R.id.CurrenciesListRecyclerView)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = CurrenciesListAdapter(emptyArray(), emptyArray(), converter, this)
        recycler.adapter = adapter
        makeRequest("A")
        makeRequest("B")
    }

    private fun makeRequest(table: String) {
        //globalna kolejka:
        val queue = DataHolder.queue

        val url = "http://api.nbp.pl/api/exchangerates/tables/%s/last/2?format=json".format(table)
        val currenciesRatesRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                print("Success")
                loadData(response)
                // przypisujemy pobrane dane do pola dataSet (CurrenciesListAdapter):

                val tmpData = arrayOfNulls<Pair<CurrencyDetails, String>>(dataSet.size)
                for (i in dataSet.indices) {
                    tmpData[i] = Pair(dataSet[i], table)
                }
                if (adapter.dataSet.isEmpty()) {
                    adapter.dataSet = tmpData as Array<Pair<CurrencyDetails, String>>
                    adapter.yesterdayDataSet = yesterdayDataSet
                } else {
                    adapter.dataSet += tmpData as Array<Pair<CurrencyDetails, String>>
                    adapter.yesterdayDataSet += yesterdayDataSet
                }
                // nie ma automatycznej aktualizacji zawartości, trzeba poinformować adapter o zmianie:
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        )
        queue.add(currenciesRatesRequest)
    }

    private fun loadData(response: JSONArray?) {
        response?.let {
            // response = tablica jednoelementowa, wydobywam z niej tablicę "rates"
            val rates = response.getJSONObject(1).getJSONArray("rates")
            var ratesCount = rates.length()
            val tmpData = arrayOfNulls<CurrencyDetails>(ratesCount)
            for (i in 0 until ratesCount) {
                val currencyCode = rates.getJSONObject(i).getString("code")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")
                val flag = DataHolder.getFlagForCountry(currencyCode)
                val currencyObject = CurrencyDetails(currencyCode, currencyRate, flag)
                tmpData[i] = currencyObject
            }
            this.dataSet = tmpData as Array<CurrencyDetails>

            val yesterdayRates = response.getJSONObject(0).getJSONArray("rates")
            ratesCount = yesterdayRates.length()
            val yesterdayTmpData = arrayOfNulls<Boolean>(ratesCount)
            for (i in 0 until ratesCount) {
                val currencyRate = rates.getJSONObject(i).getDouble("mid")
                val yesterdayCurrencyRate = yesterdayRates.getJSONObject(i).getDouble("mid")
                yesterdayTmpData[i] = currencyRate - yesterdayCurrencyRate >= 0
                println("$currencyRate $yesterdayCurrencyRate ${yesterdayTmpData[i]}")
            }
            this.yesterdayDataSet = yesterdayTmpData as Array<Boolean>
        }
    }
}