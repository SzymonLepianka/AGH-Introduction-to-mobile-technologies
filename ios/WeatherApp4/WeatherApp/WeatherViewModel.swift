//
//  WeatherViewModel.swift
//  WeatherApp
//
//  Created by Użytkownik Gość on 04/05/2021.
//

import Foundation

class WeatherViewModel: ObservableObject{
    
    @Published private(set) var model: WeatherModel = WeatherModel(cities: ["Venice","Warsaw","Cracow","Berlin","Lodon","Paris"])
    
    var records: Array<WeatherModel.WeatherRecord>{
        model.records
    }
    
    func refresh(record: WeatherModel.WeatherRecord, currentParamIndex: Int){
        model.refresh(record: record, currentParamIndex: currentParamIndex)
    }
	    
    func refreshDescription(record: WeatherModel.WeatherRecord, currentParamIndex: Int){
        model.refreshDescription(record: record, currentParamIndex: currentParamIndex)
    }
}
