//
//  ContentView.swift
//  WeatherApp
//
//  Created by Użytkownik Gość on 04/05/2021.
//

import SwiftUI
import MapKit
import CoreLocation

// Wszystkie stałe wartości używane w UI powinny być sparametryzowane
let roundedRectangleCornerRadius = 25.0
let adaptedFontSizeScaler = 0.9
let layoutPrior = 100.0
let cityRecordHeight = 100.0

struct ContentView: View {
    
    @ObservedObject var viewModel:WeatherViewModel
    
    var body: some View {
		// Użytkownik powinien mieć możliwość przewijania listy miast, jeżeli nie wszystkie mieszczą się na ekranie
        
            
        NavigationView{
        ScrollView(.vertical){
            
            VStack{
                
                    
                
            ForEach(viewModel.records){ record in
                //List(viewModel.records, id: \.id){record in
                               WeatherRecordView(
                    record: record,
                    viewModel:viewModel
                    
                    
                    )
                
                
                
            }
            }.padding()
        }.navigationBarTitle(Text("WeatherApp"))
       
        }
	}
}
struct Place: Identifiable{
    let id = UUID()
    let coordinate: CLLocationCoordinate2D
}
struct WeatherRecordView: View{
    
    
    var record: WeatherModel.WeatherRecord
    var viewModel: WeatherViewModel
    @State var currentParamIndex = 0
    @State var region = MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: 50.0, longitude: 20.0), span: MKCoordinateSpan(latitudeDelta: 1.0, longitudeDelta:1.0))
    @State private var trackingMode = MapUserTrackingMode.none
    @State private var places: [Place] = [Place(coordinate: .init(latitude: 30.064528, longitude: 19.923556))]
    var conditionDescriptions = ["Snow": "❄️", "Sleet": "❄️", "Hail": "🌨", "Thunderstorm": "⛈", "Heavy Rain": "🌧", "Light Rain": "🌧", "Showers": "🌦", "Heavy Cloud": "☁️", "Light Cloud": "🌥", "Clear":"☀️"]
    @State private var showingSheet = false
   
    private func setRegio(){
        region = MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: record.lattitude, longitude: record.longitude), span: MKCoordinateSpan(latitudeDelta: 1.0, longitudeDelta:1.0))    }
    
    
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
                    Text("🗺").onTapGesture{
                        setRegio()
                        
                        showingSheet = true
                        
                                           }.font(.largeTitle)
                        
                        // rozmiar czcionki
                    .frame(alignment: .trailing) // wyrównanie do prawej strony
                    .sheet(isPresented: $showingSheet, content:{
                    
                        VStack{
                            Text("Map")    }
                        // odpowiednia mapa , pokaże się za drugim razem, za pierwszym domyślna - bug
                        Map(coordinateRegion: $region
                            , annotationItems: [Place(coordinate: .init(latitude: record.lattitude, longitude: record.longitude))]
                        )
                        {
                          place in MapPin(coordinate: place.coordinate)}
                        
                        .onAppear{self.setRegio()}
                        
                    })
                    NavigationLink(destination:  WeatherDetails(record: record)){                     Text(">")
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
