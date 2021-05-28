package com.example.szymon_lepianka_wtorek14_nbpapi

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley.newRequestQueue
import com.blongho.country_data.Country
import com.blongho.country_data.World

// tutaj znajdzie się nasza kolejka
object DataHolder {
    lateinit var queue: RequestQueue
    private lateinit var countriesData: List<Country>

    fun prepare(context: Context) {

        // inicjalizacja kolejki:
        queue = newRequestQueue(context)

        //inicjalizacja biblioteki od flag:
        World.init(context)

        // listra krajów, unikalna względem kodu waluty
        countriesData = World.getAllCountries().distinctBy { it.currency.code }
    }

    fun getFlagForCountry(countryCode: String): Int {
        return when (countryCode) {
            "USD" -> R.drawable.us
            "GBP" -> R.drawable.gb
            "EUR" -> R.drawable.eu
            "HKD" -> R.drawable.hk
            "CHF" -> R.drawable.ch
            else -> {
                countriesData.find { it.currency.code == countryCode }?.flagResource
                    ?: World.getWorldFlag()
            }
        }
    }
}