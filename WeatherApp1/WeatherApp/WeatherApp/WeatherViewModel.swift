//
//  WeatherViewModel.swift
//  WeatherApp
//
//  Created by Użytkownik Gość on 04/05/2021.
//

import Foundation

class WeatherViewModel: ObservableObject {
    
    @Published
    private(set) var model: WeatherModel = WeatherModel(cities: ["Venice", "Paris", "Warsaw", "Berlin", "Barcelona", "London", "Prague"])
    
    var records: Array<WeatherModel.WeatherRecord>{
        model.records
    }
    
    func refresh(record: WeatherModel.WeatherRecord){
        objectWillChange.send()
        model.refresh(record: record)
    }
    
}
