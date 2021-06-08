//
//  WeatherDetails.swift
//  WeatherApp
//
//  Created by Użytkownik Gość on 08/06/2021.
//

import SwiftUI


struct WeatherDetails: View {
    var record: WeatherModel.WeatherRecord
    var body: some View {
        Text("Weather Details").font(.title)
        Text("City Name: \(record.cityName)")
        Text("Humidity: \(record.humidity)%")
        Text("Latitude: \(record.lattitude)")
        Text("Longitude: \(record.longitude)")
        Text("Temperature: \(record.temperature)℃")
        Text("Weather State: \(record.weatherState)")
        Text("Wind Speed: \(record.windSpeed)km/h")
        Text("woeId: \(record.woeId)")
        
    }
}

struct WeatherDetails_Previews: PreviewProvider {
    static var previews: some View {
        WeatherDetails(record: WeatherModel.WeatherRecord(woeId:"1"))
    }
}
