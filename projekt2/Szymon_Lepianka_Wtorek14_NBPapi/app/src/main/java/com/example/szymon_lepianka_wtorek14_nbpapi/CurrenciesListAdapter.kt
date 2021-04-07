package com.example.szymon_lepianka_wtorek14_nbpapi

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// przyjmuje zestaw danych (tablica informacji):
class CurrenciesListAdapter(
    var dataSet: Array<Pair<CurrencyDetails, String>>,
    var yesterdayDataSet: Array<Boolean>,
    var converter: Boolean,
    private val context: Context
) :
    RecyclerView.Adapter<CurrenciesListAdapter.ViewHolder>() {

    // klasa reprezentująca wierz (flaga itp.):
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val currencyCodeTextView: TextView = view.findViewById(R.id.currencyCodeTextView)
        val rateTextView: TextView = view.findViewById(R.id.currencyRateTextView)
        val flagView: ImageView = view.findViewById(R.id.flagView)
        val arrowView: ImageView = view.findViewById(R.id.arrowView)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.currency_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val currency = dataSet[position].first
        val table = dataSet[position].second
        val arrowChoise = yesterdayDataSet[position]

        viewHolder.currencyCodeTextView.text = currency.currencyCode
        viewHolder.rateTextView.text = currency.rate.toString()
        viewHolder.flagView.setImageResource(currency.flag)
        if (arrowChoise) {
            viewHolder.arrowView.setImageResource(R.drawable.greenarrow)
        } else {
            viewHolder.arrowView.setImageResource(R.drawable.redarrow)
        }
        viewHolder.itemView.setOnClickListener {
            goToDetails(
                currency.currencyCode,
                currency.rate,
                table
            )
        }
    }

    // przejście do widoku z wykresami:
    private fun goToDetails(code: String, rate: Double, table: String) {
        if (!converter) {
            val intent = Intent(context, CurrencyDetailsActivity::class.java).apply {
                //przekazujemy currencyCode:
                putExtra("currencyCode", code)
                putExtra("table", table)
            }
            context.startActivity(intent)
        } else {
            val intent = Intent(context, ConverterActivity::class.java).apply {
                putExtra("currencyCode", code)
                putExtra("rate", rate)
                putExtra("converter", true)
            }
            context.startActivity(intent)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}