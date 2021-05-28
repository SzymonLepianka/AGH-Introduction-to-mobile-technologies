// Szymon Lepianka wtorek 14:00
// 1. zrobione
// 2. zrobione - ekran startowy
// 3. zrobione - obsługa obu tabel
// 4. Kursy walut: (zrobione)
//    a. zrobione - lista, każdy wiersz zawiera kod waluty i aktualny kurs (recylerView)
//    b. zrobione - w wierszach  znajdują się flagi państw (biblioteka worldCountryData)
//    c. zrobione - obsługa błędów w flagach (USD, GBP, EUR, HKD, CHF)
//    d. zrobione - przejście do nowego activity, kurs dzisiejszy i wczorajszy
//                  oraz wykresy zmiany kursu (7 oraz 30 ostatnich kursów).
//                  - biblioteka - MPAndroidChart
//                  - wygląd wykresów jest dostosowany
//    e. zrobione -  Lista walut zawiera czerwoną/zieloną strzałkę (res/drawable)
// 5. Złoto:
//    a. zrobione - pokazuje dzisiejszy kurs złota
//    b. zrobione - wykres ostatnich 30 kursów (MPAndroidChart)
// 6. Przelicznik walut: - pierwsze wyświetla się ten sam widok co w przypadku CurrenciesList, po kliknięciu na walutę przechodzi to przelicznika
//    a. zrobione - dynamicznie przelicza wartości podczas wpisywania w drugim EditText
//    b. zrobione - dynamicznie przelicza wartości podczas wpisywania w drugim EditText

package com.example.szymon_lepianka_wtorek14_nbpapi

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isInternetAvailable(this)) {
            Toast.makeText(this, "No network connection.", Toast.LENGTH_LONG).show()
        }

        // inicjalizacja kolejki:
        DataHolder.prepare(applicationContext)
    }

    fun currenciesRatesButtonClicked(view: View) {
        // przejście do innej activity wykonujemy z pomocą intenta
        val intent = Intent(this@MainActivity, CurrenciesListActivity::class.java).apply {
            //putExtra - przekazujemy rzeczy do CurrenciesListActivity
            putExtra("converter", false)
        }
        // musimy jeszcze uruchomić to activity:
        startActivity(intent)
    }

    fun goldButtonClicked(view: View) {
        val intent = Intent(this@MainActivity, GoldActivity::class.java).apply {}
        startActivity(intent)
    }

    fun converterButtonClicked(view: View) {
        val intent = Intent(this@MainActivity, CurrenciesListActivity::class.java).apply {
            putExtra("converter", true)
        }
        startActivity(intent)
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return result
    }
}