package com.example.szymon_lepianka_wtorek14_nbpapi

data class CurrencyDetails(var currencyCode: String, var rate: Double, var flag: Int = 0) {
    private var historicRates: List<Pair<String, Double>>? = null
}
