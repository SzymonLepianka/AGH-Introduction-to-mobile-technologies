//
//  ContentView.swift
//  WeatherApp
//
//  Created by Użytkownik Gość on 04/05/2021.
//

import SwiftUI

// Wszystkie stałe wartości używane w UI powinny być sparametryzowane
let roundedRectangleCornerRadius = 25.0
let adaptedFontSizeScaler = 0.9
let layoutPrior = 100.0
let cityRecordHeight = 100.0

struct ContentView: View {
    
    @ObservedObject var viewModel:WeatherViewModel
    
    var body: some View {
		// Użytkownik powinien mieć możliwość przewijania listy miast, jeżeli nie wszystkie mieszczą się na ekranie
        ScrollView(.vertical){
            VStack{
            ForEach(viewModel.records){ record in
                WeatherRecordView(record: record,viewModel:viewModel)
            }
        }.padding()
        }
	}
}

struct WeatherRecordView: View{
    var record: WeatherModel.WeatherRecord
    var viewModel: WeatherViewModel
    @State var currentParamIndex = 0
    var conditionDescriptions = ["Snow": "❄️", "Sleet": "❄️", "Hail": "🌨", "Thunderstorm": "⛈", "Heavy Rain": "🌧", "Light Rain": "🌧", "Showers": "🌦", "Heavy Cloud": "☁️", "Light Cloud": "🌥", "Clear":"☀️"]
    var body: some View{  
        ZStack{
            RoundedRectangle(cornerRadius: CGFloat(roundedRectangleCornerRadius)).stroke()
			
			// GeometryReader - widok, który odczytuje parametry geometryczne i udostępnia je swoim dzieciom
            GeometryReader { geometry in
                HStack(alignment: /*START_MENU_TOKEN@*/.center/*@END_MENU_TOKEN@*/, content:{
                    Text("\(conditionDescriptions[record.weatherState]!)")
						.font(.system(size: CGFloat(adaptedFontSizeScaler) * geometry.size.height)) // wielkość powinna być dostosowana do wielkości przestrzeni którą otrzymała
						.frame(alignment: .leading) // Ikonka pogody wyrównana do lewej strony komórek
					 VStack (alignment: .leading){ //Nazwa miasta i parametr wyrównane (względem siebie) do lewej strony
                        Text(record.cityName)
                        Text(record.recordDescrition)
							.font(.caption) // rozmiar czcionki
							.onTapGesture{
								self.currentParamIndex+=1
								if self.currentParamIndex > 2 {
									self.currentParamIndex=0
								}
								viewModel.refreshDescription(record: record, currentParamIndex: self.currentParamIndex)
							}
                    }.layoutPriority(layoutPrior) // żeby wyświetlały się całe teksty
                    Spacer()// Ikonka odświeżania wyrównana do prawej strony komórek.
                    Text("🔄")
						.font(.largeTitle) // rozmiar czcionki
                        .frame(alignment: .trailing) // wyrównanie do prawej strony
						.onTapGesture {
                            if self.currentParamIndex > 2 {
                                self.currentParamIndex = 0
                            }
							// Zmienianie cykliczne wyświetlanego parametru
                            viewModel.refresh(record: record, currentParamIndex: self.currentParamIndex)
                        }
                })
            }.padding()
        } .frame(height: CGFloat(cityRecordHeight)) //Wysokość komórek z miastami powinna być ustalona parametrem
        }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(viewModel: WeatherViewModel())
    }
}
