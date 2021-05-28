//
//  WeatherModel.swift
//  WeatherApp
//
//  Created by Użytkownik Gość on 04/05/2021.
//

import Foundation

struct WeatherModel{
    
	// Wyświetlanie odpowiednich ikonkek stanu pogody
    var conditionDescriptions = ["Snow": "❄️", "Sleet": "❄️", "Hail": "🌨", "Thunderstorm": "⛈", "Heavy Rain": "🌧", "Light Rain": "🌧", "Showers": "🌦", "Heavy Cloud": "☁️", "Light Cloud": "🌥", "Clear":"☀️"]
    
	var records: Array<WeatherRecord> = []

    init(cities: Array<String>){
        records = Array<WeatherRecord>()
        for city in cities {
            records.append(WeatherRecord(cityName: city))
        }
    }
    
    struct WeatherRecord: Identifiable{
        var id: UUID = UUID()
        var cityName: String
        var weatherState: String = "Clear"
        var temperature: Float = round(Float.random(in: -10.0...30.0) * 100)/100
        var humidity: Float = round(Float.random(in: 0 ... 100)*100)/100
        var windSpeed: Float = round(Float.random(in: 0 ... 20)*100)/100
        var windDirection: Float = round(Float.random(in: 0 ..< 360)*100)/100
        var recordDescrition: String = "Temperature 20℃"
    }
    
    mutating func refresh(record: WeatherRecord, currentParamIndex: Int){
        
        let index = records.firstIndex(where: {$0.id==record.id})
        records[index!].weatherState = conditionDescriptions.randomElement()!.key
        records[index!].temperature = round(Float.random(in: -10.0...30.0) * 100)/100
        records[index!].humidity = round(Float.random(in: 0 ... 100)*100)/100
        records[index!].windSpeed = round(Float.random(in: 0 ... 20)*100)/100
        records[index!].windDirection = round(Float.random(in: 0 ..< 360)*100)/100
        
        let weatherStatesDescriptions = ["Temperature: \(records[index!].temperature)℃", "Humidity: \(records[index!].humidity)%", "WindSpeed: \(records[index!].windSpeed) km/h"]
        records[index!].recordDescrition = weatherStatesDescriptions[currentParamIndex]
        
        print("Refreshing record: \(record)")
    }
    
    mutating func refreshDescription(record: WeatherRecord, currentParamIndex: Int){
        
        let index = records.firstIndex(where: {$0.id==record.id})
        
        let weatherStatesDescriptions = ["Temperature: \(records[index!].temperature)℃", "Humidity: \(records[index!].humidity)%", "WindSpeed: \(records[index!].windSpeed) km/h"]
        records[index!].recordDescrition = weatherStatesDescriptions[currentParamIndex]
        
        print("Refreshing record: \(record)")
    }

}
