//
//  ContentView.swift
//  WeatherApp
//
//  Created by Użytkownik Gość on 04/05/2021.
//

import SwiftUI

struct ContentView: View {
    
    @ObservedObject
    var viewModel: WeatherViewModel
    
    var body: some View {
        VStack{
            ForEach(viewModel.records) {record in
                WeatherRecordView(record: record, viewModel: viewModel)
            }
        }.padding()
    }
}

struct WeatherRecordView: View {
    var record: WeatherModel.WeatherRecord
    var viewModel: WeatherViewModel
    var body: some View {
        ZStack{
            RoundedRectangle(cornerRadius: /*@START_MENU_TOKEN@*/25.0/*@END_MENU_TOKEN@*/)
                .stroke()
            HStack {
                Text("☀️")
                    .font(.largeTitle)
                VStack {
                    Text(record.cityName)
                    Text("Temperature: \(record.temperature, specifier: "%.1f")℃")
                        .font(.caption)
                }
                Text("🔄")
                    .font(.largeTitle)
                    .onTapGesture {
                        viewModel.refresh(record: record)
                    }
            }
        }
        
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView(viewModel: WeatherViewModel())
    }
}
